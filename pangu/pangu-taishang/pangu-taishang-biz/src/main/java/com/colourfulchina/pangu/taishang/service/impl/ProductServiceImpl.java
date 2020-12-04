package com.colourfulchina.pangu.taishang.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.god.door.api.vo.KltSysUser;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.nuwa.api.feign.RemoteKlfEmailService;
import com.colourfulchina.nuwa.api.vo.SysEmailSendReqVo;
import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.api.vo.ExportProductAccom;
import com.colourfulchina.pangu.taishang.api.vo.ExportProductNoAccom;
import com.colourfulchina.pangu.taishang.api.vo.GroupProductVo;
import com.colourfulchina.pangu.taishang.api.vo.req.ProductPageReq;
import com.colourfulchina.pangu.taishang.api.vo.res.GroupProductExportVo;
import com.colourfulchina.pangu.taishang.api.vo.res.ProductPackPageRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ProductPageRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopItemNetPriceRuleQueryRes;
import com.colourfulchina.pangu.taishang.config.FileDownloadProperties;
import com.colourfulchina.pangu.taishang.mapper.ProductMapper;
import com.colourfulchina.pangu.taishang.service.*;
import com.colourfulchina.pangu.taishang.utils.DateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper,Product> implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private BlockRuleService blockRuleService;
    @Autowired
    private ShopItemService shopItemService;
    @Autowired
    private SysServiceService sysServiceService;
    @Autowired
    private ShopItemNetPriceRuleService shopItemNetPriceRuleService;
    @Autowired
    private ShopItemSettlePriceRuleService shopItemSettlePriceRuleService;
    @Autowired
    private ProductItemService productItemService;
    @Autowired
    private  ProductGroupProductService productGroupProductService;
    @Autowired
    private FileDownloadProperties fileDownloadProperties;
    @Autowired
    private RemoteKlfEmailService remoteKlfEmailService;

    /**
     * 根据产品组id查询产品列表
     * @param groupId
     * @return
     * @throws Exception
     */
    @Override
    public List<GroupProductVo> selectByGroupId(Integer groupId) throws Exception {
        List<GroupProductVo> productVoList = productMapper.selectByGroupId(groupId);
        //block规则翻译
        for (GroupProductVo groupProductVo : productVoList) {
            if (StringUtils.isNotBlank(groupProductVo.getBlockRule())){
                List<BlockRule> blockRules = blockRuleService.blockRuleStr2list(groupProductVo.getBlockRule());
                String blocks = blockRules.stream().map(blockRule -> blockRule.getNatural()).collect(Collectors.joining(" "));
                groupProductVo.setBlockNatural(blocks);
            }
        }
        return productVoList;
    }

    /**
     * 产品分页列表
     * @param pageReq
     * @return
     * @throws Exception
     */
    @Override
    public PageVo<ProductPageRes> selectPageList(PageVo<ProductPageReq> pageReq) throws Exception {
        PageVo<ProductPageRes> pageRes = new PageVo<>();
        Map params  = pageReq.getCondition();
        List<String> pIds = Lists.newLinkedList();
        if (params.containsKey("serviceList")){
            List<String> services = (List<String>) params.get("serviceList");
            if (!CollectionUtils.isEmpty(services)){
                List<String> singServices = Lists.newLinkedList();
                List<String> giftServices = Lists.newLinkedList();
                for (String service : services) {
                    Wrapper wrapper = new Wrapper() {
                        @Override
                        public String getSqlSegment() {
                            return "where del_flag = 0 and name ='"+service+"'";
                        }
                    };
                    SysService sysService = sysServiceService.selectOne(wrapper);
                    if (sysService.getProductType().compareTo(0) == 0 ){
                        singServices.add(service);
                    }else if (sysService.getProductType().compareTo(1) == 0){
                        giftServices.add(service);
                    }
                }
                params.put("singServices",singServices);
                params.put("giftServices",giftServices);
            }
        }
        if(params.containsKey("ids")){
            String temp  = params.get("ids")+"";
            List<String> tempList = Arrays.asList(temp.split(","));
            pIds.addAll(tempList);
        }
        if(params.containsKey("city")){
            final Object city = params.remove("city");
            if (city != null){
                List<String> cityList= (List<String>) city;
                cityList.remove("");
                params.put("city",cityList);
            }
        }
        if (params.containsKey("canOrderTime")){
            List<Date> dates = (List<Date>) params.get("canOrderTime");
            if (!CollectionUtils.isEmpty(dates)){
                Date startDate = DateUtil.parse((String) params.get("startDate"),"yyyy-MM-dd");
                Date endDate = DateUtil.parse((String) params.get("endDate"),"yyyy-MM-dd");
                if (!CollectionUtils.isEmpty(pIds)){
                    params.put("ids",pIds);
                }
                List<ProductPageRes> tempList = productMapper.selectPageList(params);
                if (!CollectionUtils.isEmpty(tempList)){
                    //获取日期范围内全部被block的产品id
                    List<String> productIds = getBlockProduct(tempList,startDate,endDate);
                    if (!CollectionUtils.isEmpty(productIds)){
                        pIds.addAll(productIds);
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(pIds)){
            params.put("ids",pIds);
        }
        List<ProductPageRes> list = productMapper.selectPageList(pageReq,params);
        BeanUtils.copyProperties(pageReq,pageRes);
        pageRes.setRecords(list);
        return pageRes;
    }

    /**
     * 产品子项分页列表
     * @param pageReq
     * @return
     * @throws Exception
     */
    public PageVo<ProductPackPageRes> selectItemPageList(PageVo<ProductPageReq> pageReq) throws Exception {
        PageVo<ProductPackPageRes> pageRes = new PageVo<>();
        Map params  = pageReq.getCondition();
        List<String> pIds = Lists.newLinkedList();
        if (params.containsKey("serviceList")){
            List<String> services = (List<String>) params.get("serviceList");
            if (!CollectionUtils.isEmpty(services)){
                List<String> singServices = Lists.newLinkedList();
                List<String> giftServices = Lists.newLinkedList();
                for (String service : services) {
                    Wrapper wrapper = new Wrapper() {
                        @Override
                        public String getSqlSegment() {
                            return "where del_flag = 0 and name ='"+service+"'";
                        }
                    };
                    SysService sysService = sysServiceService.selectOne(wrapper);
                    if (sysService.getProductType().compareTo(0) == 0 ){
                        singServices.add(service);
                    }else if (sysService.getProductType().compareTo(1) == 0){
                        giftServices.add(service);
                    }
                }
                params.put("singServices",singServices);
                params.put("giftServices",giftServices);
            }
        }
        if (params.containsKey("applyWeek")){
            List<String> weeks = (List<String>) params.get("applyWeek");
            if (!CollectionUtils.isEmpty(weeks)){
                for (String week : weeks) {
                    if (week.equals("1")){
                        params.put("monday",1);
                    }
                    if (week.equals("2")){
                        params.put("tuesday",1);
                    }
                    if (week.equals("3")){
                        params.put("wednesday",1);
                    }
                    if (week.equals("4")){
                        params.put("thursday",1);
                    }
                    if (week.equals("5")){
                        params.put("friday",1);
                    }
                    if (week.equals("6")){
                        params.put("saturday",1);
                    }
                    if (week.equals("7")){
                        params.put("sunday",1);
                    }
                }
            }
        }
        if(params.containsKey("ids")){
            String temp  = params.get("ids")+"";
            List<String> tempList = Arrays.asList(temp.split(","));
            pIds.addAll(tempList);
        }
        if(params.containsKey("city")){
            final Object city = params.remove("city");
            if (city != null){
                List<String> cityList= (List<String>) city;
                cityList.remove("");
                params.put("city",cityList);
            }
        }
        if (params.containsKey("canOrderTime")){
            List<Date> dates = (List<Date>) params.get("canOrderTime");
            if (!CollectionUtils.isEmpty(dates)){
                Date startDate = DateUtil.parse((String) params.get("startDate"),"yyyy-MM-dd");
                Date endDate = DateUtil.parse((String) params.get("endDate"),"yyyy-MM-dd");
                if (!CollectionUtils.isEmpty(pIds)){
                    params.put("ids",pIds);
                }
                List<ProductPackPageRes> tempList = productMapper.selectItemPageList(params);
                if (!CollectionUtils.isEmpty(tempList)){
                    //获取日期范围内全部被block的产品id
                    List<String> productIds = getBlockProduct2(tempList,startDate,endDate);
                    if (!CollectionUtils.isEmpty(productIds)){
                        pIds.addAll(productIds);
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(pIds)){
            params.put("ids",pIds);
        }
        List<ProductPackPageRes> list = productMapper.selectItemPageList(pageReq,params);
        BeanUtils.copyProperties(pageReq,pageRes);
        pageRes.setRecords(list);
        return pageRes;
    }

    /**
     * 产品导出
     * @param pageReq
     * @return
     */
    @Override
    @Async("taskExecutorPool")
    public String exportProduct(PageVo<ProductPageReq> pageReq,KltSysUser sysUser) throws Exception {
//        KltSysUser sysUser = SecurityUtils.getAttributeUser();
//        Assert.isTrue(sysUser != null && sysUser.getEmail() != null , "收件人不能为空");
        String url = null;
        Map params  = pageReq.getCondition();
        List<String> pIds = Lists.newLinkedList();
        //住宿和非住宿不能同时导出
        boolean noAccom = true;
        if (params.containsKey("service")){
            List<String> ser = (List<String>) params.get("service");
            if (!CollectionUtils.isEmpty(ser)){
                Assert.isTrue(!(ser.contains("住宿") && ser.size() > 1),"住宿和非住宿不能混合选择");
                if (ser.contains("住宿")){
                    noAccom = false;
                }
            }
        }

        if (params.containsKey("serviceList")){
            List<String> services = (List<String>) params.get("serviceList");
            if (!CollectionUtils.isEmpty(services)){
                List<String> singServices = Lists.newLinkedList();
                List<String> giftServices = Lists.newLinkedList();
                for (String service : services) {
                    Wrapper wrapper = new Wrapper() {
                        @Override
                        public String getSqlSegment() {
                            return "where del_flag = 0 and name ='"+service+"'";
                        }
                    };
                    SysService sysService = sysServiceService.selectOne(wrapper);
                    if (sysService.getProductType().compareTo(0) == 0 ){
                        singServices.add(service);
                    }else if (sysService.getProductType().compareTo(1) == 0){
                        giftServices.add(service);
                    }
                }
                params.put("singServices",singServices);
                params.put("giftServices",giftServices);
            }
        }
        if (params.containsKey("applyWeek")){
            List<String> weeks = (List<String>) params.get("applyWeek");
            if (!CollectionUtils.isEmpty(weeks)){
                for (String week : weeks) {
                    if (week.equals("1")){
                        params.put("monday",1);
                    }
                    if (week.equals("2")){
                        params.put("tuesday",1);
                    }
                    if (week.equals("3")){
                        params.put("wednesday",1);
                    }
                    if (week.equals("4")){
                        params.put("thursday",1);
                    }
                    if (week.equals("5")){
                        params.put("friday",1);
                    }
                    if (week.equals("6")){
                        params.put("saturday",1);
                    }
                    if (week.equals("7")){
                        params.put("sunday",1);
                    }
                }
            }
        }
        if(params.containsKey("ids")){
            String temp  = params.get("ids")+"";
            List<String> tempList = Arrays.asList(temp.split(","));
            pIds.addAll(tempList);
        }
        if(params.containsKey("city")){
            final Object city = params.remove("city");
            if (city != null){
                List<String> cityList= (List<String>) city;
                cityList.remove("");
                params.put("city",cityList);
            }
        }
        if (params.containsKey("canOrderTime")){
            List<Date> dates = (List<Date>) params.get("canOrderTime");
            if (!CollectionUtils.isEmpty(dates)){
                Date startDate = DateUtil.parse((String) params.get("startDate"),"yyyy-MM-dd");
                Date endDate = DateUtil.parse((String) params.get("endDate"),"yyyy-MM-dd");
                if (!CollectionUtils.isEmpty(pIds)){
                    params.put("ids",pIds);
                }
                List<ProductPageRes> tempList = productMapper.selectPageList(params);
                if (!CollectionUtils.isEmpty(tempList)){
                    //获取日期范围内全部被block的产品id
                    List<String> productIds = getBlockProduct(tempList,startDate,endDate);
                    if (!CollectionUtils.isEmpty(productIds)){
                        pIds.addAll(productIds);
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(pIds)){
            params.put("ids",pIds);
        }
        List<GroupProductExportVo> list = productMapper.selectExportList(params);
        if (!CollectionUtils.isEmpty(list)){
            String fileName = "产品信息"+"-"+DateUtil.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
            url = exportItem(noAccom,list,fileName);
            String[] receiveEmailAddress = sysUser.getEmail().split(",");
            // 发送邮件
            SysEmailSendReqVo sysEmailSendReqVo = new SysEmailSendReqVo();
            // 记录邮件发送记录
            sysEmailSendReqVo.setSubject("【产品列表-导出】"+fileName);
            sysEmailSendReqVo.setFrom(fileDownloadProperties.getSendEmailAddress());
            sysEmailSendReqVo.setTo(receiveEmailAddress);
            sysEmailSendReqVo.setContent("<html><a href='"+url+"'>"+fileName+"</a></html>");
            log.info("remoteKlfEmailService.send:{}",JSON.toJSONString(sysEmailSendReqVo));
            final CommonResultVo resultVo = remoteKlfEmailService.send(sysEmailSendReqVo);
            log.info("remoteKlfEmailService.send result:{}",JSON.toJSONString(resultVo));
        }
        return url;
    }

    @Override
    public Integer selectNewProductId(Integer productId) throws Exception {
        Integer newProductId = productMapper.selectNewProductId(productId);
        return newProductId;
    }

    public String exportItem(boolean noAccom,List<GroupProductExportVo> list,String fileName) throws Exception{
        String url = null;
        // 文件输出位置
        OutputStream out = new FileOutputStream(fileDownloadProperties.getPath()+"/"+fileName);
        ExcelWriter writer = EasyExcelFactory.getWriter(out);
        if (!CollectionUtils.isEmpty(list)){
            if (noAccom){
                List<ExportProductNoAccom> noAccoms = Lists.newLinkedList();
                for (GroupProductExportVo record : list) {
                    //block规则转换
                    List<String> blockList = Lists.newLinkedList();
                    String allBlock = null;
                    String targetBlock = null;
                    if (StringUtils.isNotBlank(record.getGroupProductBlock())){
                        blockList.add(record.getGroupProductBlock());
                    }
                    if (StringUtils.isNotBlank(record.getGoodsBlock())){
                        blockList.add(record.getGoodsBlock());
                    }
                    if (StringUtils.isNotBlank(record.getProductGroupBlock())){
                        blockList.add(record.getProductGroupBlock());
                    }
                    if (StringUtils.isNotBlank(record.getShopItemBlock())){
                        blockList.add(record.getShopItemBlock());
                    }
                    if (StringUtils.isNotBlank(record.getShopBlock())){
                        blockList.add(record.getShopBlock());
                    }
                    if (!CollectionUtils.isEmpty(blockList)){
                        allBlock = StringUtils.join(blockList,", ");
                    }
                    if (StringUtils.isNotBlank(allBlock)){
                        List<BlockRule> blockRules = blockRuleService.blockRuleStr2list(allBlock);
                        if (!CollectionUtils.isEmpty(blockRules)){
                            targetBlock = blockRules.stream().map(blockRule -> blockRule.getNatural()).collect(Collectors.joining(";"));
                        }
                    }
                    //价格组装
                    Wrapper priceWrapper = new Wrapper() {
                        @Override
                        public String getSqlSegment() {
                            return "where del_flag = 0 and shop_item_id ="+record.getShopItemId();
                        }
                    };
                    List<ShopItemNetPriceRule> prices = shopItemNetPriceRuleService.selectList(priceWrapper);

                    //市场参考价
                    String marketPrice = null;
                    //适用日期
                    String applyDate = null;
                    //适用星期
                    String applyWeek = null;
                    //净价
                    BigDecimal netPrice = null;
                    //服务税
                    String serviceRate = null;
                    //税费
                    String taxRate = null;
                    //单人总价
                    BigDecimal singlePrice = null;
                    //双人总价
                    BigDecimal doublePrice = null;
                    //成本
                    BigDecimal cost = null;
                    //折扣率
                    String itemRate = null;
                    if (record.getCost() != null){
                        applyDate = record.getApplyTime().split(":")[0];
                        applyWeek = record.getApplyTime().split(":")[1];
                        String week = genItemWeek(record);
                        if (StringUtils.isNotBlank(week)){
                            List<Date> dates = DateUtils.containDateByWeeks(record.getStartDate(),record.getEndDate(),week);
                            if (!CollectionUtils.isEmpty(dates) && !CollectionUtils.isEmpty(prices)){
                                //查找对应的价格
                                ShopItemNetPriceRule price = shopItemNetPriceRuleService.foundPriceByTime(dates.get(0),prices);
                                if (price != null){
                                    ShopItemNetPriceRuleQueryRes shopItemNetPriceRuleQueryRes = new ShopItemNetPriceRuleQueryRes();
                                    BeanUtils.copyProperties(price,shopItemNetPriceRuleQueryRes);
                                    BigDecimal tempPrice = ProductGroupProductServiceImpl.colPrice(shopItemNetPriceRuleQueryRes);
                                    marketPrice = tempPrice.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
                                    netPrice = price.getNetPrice().setScale(2,BigDecimal.ROUND_HALF_UP);
                                    serviceRate = price.getServiceRate().multiply(new BigDecimal(100)).setScale(0) + "%";
                                    taxRate = price.getTaxRate().multiply(new BigDecimal(100)).setScale(0) + "%";
                                    singlePrice = tempPrice.setScale(2,BigDecimal.ROUND_HALF_UP);
                                    doublePrice = tempPrice.multiply(new BigDecimal(2)).setScale(2,BigDecimal.ROUND_HALF_UP);
                                    cost = record.getCost();
                                    if (tempPrice != null && tempPrice.compareTo(new BigDecimal(0)) != 0){
                                        if ("F2".equals(record.getGiftCode())){
                                            tempPrice = tempPrice.multiply(new BigDecimal(2));
                                        }
                                        itemRate = record.getCost().divide(tempPrice,2,BigDecimal.ROUND_HALF_UP).toString();
                                    }
                                }
                            }
                        }
                    }
                    ExportProductNoAccom param = new ExportProductNoAccom();
                    param.setCity(record.getCityName());
                    param.setHotelChName(record.getHotelChName());
                    param.setHotelEnName(record.getHotelEnName());
                    param.setShopEnName(record.getShopEnName());
                    param.setShopChName(record.getShopChName());
                    param.setServiceName(record.getServiceName());
                    param.setGiftShortName(record.getGiftShortName());
                    param.setGiftName(record.getGiftName());
                    param.setMarketPrice(marketPrice);
                    param.setApplyDate(applyDate);
                    param.setApplyWeek(applyWeek);
                    param.setShopItemName(record.getShopItemName());
                    param.setNetPrice(netPrice);
                    param.setServiceRate(serviceRate);
                    param.setTaxRate(taxRate);
                    param.setSinglePrice(singlePrice);
                    param.setDoublePrice(doublePrice);
                    param.setCost(cost);
                    param.setItemRate(itemRate);
                    param.setOtherPrice(null);
                    param.setOpenTime((StringUtils.isNotBlank(record.getOpenTime()) && StringUtils.isNotBlank(record.getCloseTime())) ? record.getOpenTime() + "~" + record.getCloseTime() : null);
                    param.setBlock(targetBlock);
                    param.setAddress(record.getAddress());
                    param.setPhone(record.getPhone());
                    param.setParking(record.getParking());
                    param.setKid(record.getChildren());
                    param.setRemark(record.getRemark());
                    noAccoms.add(param);
                }
                // 写仅有一个 Sheet 的 Excel 文件, 此场景较为通用
                Sheet sheet1 = new Sheet(1, 0, ExportProductNoAccom.class);
                // 第一个 sheet 名称
                sheet1.setSheetName("sheet1");
                // 写数据到 Writer 上下文中
                // 入参1: 创建要写入的模型数据
                // 入参2: 要写入的目标 sheet
                writer.write(noAccoms, sheet1);
                // 将上下文中的最终 outputStream 写入到指定文件中
                writer.finish();
            }else {
                List<ExportProductAccom> accoms = Lists.newLinkedList();
                for (GroupProductExportVo record : list) {
                    //block规则转换
                    List<String> blockList = Lists.newLinkedList();
                    String allBlock = null;
                    String targetBlock = null;
                    if (StringUtils.isNotBlank(record.getGroupProductBlock())){
                        blockList.add(record.getGroupProductBlock());
                    }
                    if (StringUtils.isNotBlank(record.getGoodsBlock())){
                        blockList.add(record.getGoodsBlock());
                    }
                    if (StringUtils.isNotBlank(record.getProductGroupBlock())){
                        blockList.add(record.getProductGroupBlock());
                    }
                    if (StringUtils.isNotBlank(record.getShopItemBlock())){
                        blockList.add(record.getShopItemBlock());
                    }
                    if (StringUtils.isNotBlank(record.getShopBlock())){
                        blockList.add(record.getShopBlock());
                    }
                    if (!CollectionUtils.isEmpty(blockList)){
                        allBlock = StringUtils.join(blockList,", ");
                    }
                    if (StringUtils.isNotBlank(allBlock)){
                        List<BlockRule> blockRules = blockRuleService.blockRuleStr2list(allBlock);
                        if (!CollectionUtils.isEmpty(blockRules)){
                            targetBlock = blockRules.stream().map(blockRule -> blockRule.getNatural()).collect(Collectors.joining(";"));
                        }
                    }

                    //适用日期
                    String applyDate = null;
                    //适用星期
                    String applyWeek = null;
                    //成本
                    BigDecimal cost = null;
                    if (record.getCost() != null){
                        applyDate = record.getApplyTime().split(":")[0];
                        applyWeek = record.getApplyTime().split(":")[1];
                        cost = record.getCost();
                    }
                    ExportProductAccom param = new ExportProductAccom();
                    param.setCountryName(record.getCountryName());
                    param.setCityName(record.getCityName());
                    param.setHotelChName(record.getHotelChName());
                    param.setHotelEnName(record.getHotelEnName());
                    param.setShopItemName(record.getShopItemName());
                    param.setNeeds(record.getNeeds());
                    param.setAddon(record.getAddon());
                    param.setApplyDate(applyDate);
                    param.setApplyWeek(applyWeek);
                    param.setCost(cost);
                    param.setOtherPrice(null);
                    param.setAddress(record.getAddress());
                    param.setPhone(record.getPhone());
                    param.setBlock(targetBlock);
                    param.setRemark(record.getRemark());
                    accoms.add(param);
                }
                // 写仅有一个 Sheet 的 Excel 文件, 此场景较为通用
                Sheet sheet1 = new Sheet(1, 0, ExportProductAccom.class);
                // 第一个 sheet 名称
                sheet1.setSheetName("sheet1");
                // 写数据到 Writer 上下文中
                // 入参1: 创建要写入的模型数据
                // 入参2: 要写入的目标 sheet
                writer.write(accoms, sheet1);
                // 将上下文中的最终 outputStream 写入到指定文件中
                writer.finish();
            }
            url = fileDownloadProperties.getUrl()+fileName;
        }
        // 关闭流
        out.close();
        return url;
    }

    /**
     * 打包产品分页列表
     * @param pageReq
     * @return
     * @throws Exception
     */
    @Override
    public PageVo<ProductPackPageRes> selectPackPageList(PageVo<ProductPageReq> pageReq) throws Exception {
        PageVo<ProductPackPageRes> result = new PageVo<>();
        List<ProductPackPageRes> resList = Lists.newLinkedList();
        Map params  = pageReq.getCondition();
        //住宿和非住宿不能同时查询
        boolean noAccom = true;
        if (params.containsKey("serviceList")){
            List<String> ser = (List<String>) params.get("serviceList");
            if (!CollectionUtils.isEmpty(ser)){
                Assert.isTrue(!(ser.contains("住宿") && ser.size() > 1),"住宿和非住宿不能混合选择");
                if (ser.contains("住宿")){
                    noAccom = false;
                }
            }
        }
        if (params.containsKey("service")){
            List<String> ser = (List<String>) params.get("service");
            if (!CollectionUtils.isEmpty(ser)){
                Assert.isTrue(!(ser.contains("住宿") && ser.size() > 1),"住宿和非住宿不能混合选择");
                if (ser.contains("住宿")){
                    noAccom = false;
                }
            }
        }
        result = selectItemPageList(pageReq);
        if (result != null && !CollectionUtils.isEmpty(result.getRecords())){
            if (noAccom){
                for (ProductPackPageRes productPackPageRes : result.getRecords()) {
                    //block规则转换
                    List<String> blockList = Lists.newLinkedList();
                    String allBlock = null;
                    String targetBlock = null;
                    if (StringUtils.isNotBlank(productPackPageRes.getShopItemBlock())){
                        blockList.add(productPackPageRes.getShopItemBlock());
                    }
                    if (StringUtils.isNotBlank(productPackPageRes.getShopBlock())){
                        blockList.add(productPackPageRes.getShopBlock());
                    }
                    if (!CollectionUtils.isEmpty(blockList)){
                        allBlock = StringUtils.join(blockList,", ");
                    }
                    if (StringUtils.isNotBlank(allBlock)){
                        List<BlockRule> blockRules = blockRuleService.blockRuleStr2list(allBlock);
                        if (!CollectionUtils.isEmpty(blockRules)){
                            targetBlock = blockRules.stream().map(blockRule -> blockRule.getNatural()).collect(Collectors.joining(";"));
                        }
                    }
                    //价格组装
                    Wrapper priceWrapper = new Wrapper() {
                        @Override
                        public String getSqlSegment() {
                            return "where del_flag = 0 and shop_item_id ="+productPackPageRes.getShopItemId();
                        }
                    };
                    List<ShopItemNetPriceRule> prices = shopItemNetPriceRuleService.selectList(priceWrapper);
                    //市场参考价
                    BigDecimal marketPrice = null;
                    //适用日期
                    String applyDate = null;
                    //适用星期
                    String applyWeek = null;
                    //净价
                    BigDecimal netPrice = null;
                    //服务税
                    String serviceRate = null;
                    //税费
                    String taxRate = null;
                    //单人总价
                    BigDecimal singlePrice = null;
                    //双人总价
                    BigDecimal doublePrice = null;
                    //成本
                    BigDecimal cost = null;
                    //折扣率
                    String itemRate = null;
                    if (productPackPageRes.getCost() != null){
                        applyDate = productPackPageRes.getApplyTime().split(":")[0];
                        applyWeek = productPackPageRes.getApplyTime().split(":")[1];
                        String week = genItemWeek(productPackPageRes);
                        if (StringUtils.isNotBlank(week)){
                            List<Date> dates = DateUtils.containDateByWeeks(productPackPageRes.getStartDate(),productPackPageRes.getEndDate(),week);
                            if (!CollectionUtils.isEmpty(dates) && !CollectionUtils.isEmpty(prices)){
                                //查找对应的价格
                                ShopItemNetPriceRule price = shopItemNetPriceRuleService.foundPriceByTime(dates.get(0),prices);
                                if (price != null){
                                    ShopItemNetPriceRuleQueryRes shopItemNetPriceRuleQueryRes = new ShopItemNetPriceRuleQueryRes();
                                    BeanUtils.copyProperties(price,shopItemNetPriceRuleQueryRes);
                                    BigDecimal tempPrice = ProductGroupProductServiceImpl.colPrice(shopItemNetPriceRuleQueryRes);
                                    marketPrice = tempPrice.setScale(2,BigDecimal.ROUND_HALF_UP);
                                    netPrice = price.getNetPrice().setScale(2,BigDecimal.ROUND_HALF_UP);
                                    serviceRate = price.getServiceRate().multiply(new BigDecimal(100)).setScale(0) + "%";
                                    taxRate = price.getTaxRate().multiply(new BigDecimal(100)).setScale(0) + "%";
                                    singlePrice = tempPrice.setScale(2,BigDecimal.ROUND_HALF_UP);
                                    doublePrice = tempPrice.multiply(new BigDecimal(2)).setScale(2,BigDecimal.ROUND_HALF_UP);
                                    cost = productPackPageRes.getCost();
                                    if (tempPrice != null && tempPrice.compareTo(new BigDecimal(0)) != 0){
                                        if ("F2".equals(productPackPageRes.getGiftCode())){
                                            tempPrice = tempPrice.multiply(new BigDecimal(2));
                                        }
                                        itemRate = productPackPageRes.getCost().divide(tempPrice,2,BigDecimal.ROUND_HALF_UP).toString();
                                    }
                                }
                            }
                        }
                    }
                    productPackPageRes.setMarketPrice(marketPrice);
                    productPackPageRes.setApplyDate(applyDate);
                    productPackPageRes.setApplyWeek(applyWeek);
                    productPackPageRes.setNetPrice(netPrice);
                    productPackPageRes.setServiceRate(serviceRate);
                    productPackPageRes.setTaxRate(taxRate);
                    productPackPageRes.setSinglePrice(singlePrice);
                    productPackPageRes.setDoublePrice(doublePrice);
                    productPackPageRes.setItemRate(itemRate);
                    productPackPageRes.setBlockRule(targetBlock);

                    resList.add(productPackPageRes);
                }
            }else {
                for (ProductPackPageRes productPackPageRes : result.getRecords()) {
                    //block规则转换
                    List<String> blockList = Lists.newLinkedList();
                    String allBlock = null;
                    String targetBlock = null;
                    if (StringUtils.isNotBlank(productPackPageRes.getShopItemBlock())){
                        blockList.add(productPackPageRes.getShopItemBlock());
                    }
                    if (StringUtils.isNotBlank(productPackPageRes.getShopBlock())){
                        blockList.add(productPackPageRes.getShopBlock());
                    }
                    if (!CollectionUtils.isEmpty(blockList)){
                        allBlock = StringUtils.join(blockList,", ");
                    }
                    if (StringUtils.isNotBlank(allBlock)){
                        List<BlockRule> blockRules = blockRuleService.blockRuleStr2list(allBlock);
                        if (!CollectionUtils.isEmpty(blockRules)){
                            targetBlock = blockRules.stream().map(blockRule -> blockRule.getNatural()).collect(Collectors.joining(";"));
                        }
                    }
                    //适用日期
                    String applyDate = null;
                    //适用星期
                    String applyWeek = null;
                    //成本
                    BigDecimal cost = null;
                    if (productPackPageRes.getCost() != null){
                        applyDate = productPackPageRes.getApplyTime().split(":")[0];
                        applyWeek = productPackPageRes.getApplyTime().split(":")[1];
                        cost = productPackPageRes.getCost();
                    }
                    productPackPageRes.setApplyDate(applyDate);
                    productPackPageRes.setApplyWeek(applyWeek);
                    productPackPageRes.setBlockRule(targetBlock);

                    resList.add(productPackPageRes);
                }
            }
        }
        result.setRecords(resList);
        return result;
    }

    /**
     * 获取ProductPackPageRes中的week
     * @param item
     * @return
     */
    public static String genItemWeek(ProductPackPageRes item) {
        StringBuffer weeks = new StringBuffer();
        if (item.getMonday() == 1){
            weeks.append(2);
        }
        if (item.getTuesday() == 1){
            weeks.append(3);
        }
        if (item.getWednesday() == 1){
            weeks.append(4);
        }
        if (item.getThursday() == 1){
            weeks.append(5);
        }
        if (item.getFriday() == 1){
            weeks.append(6);
        }
        if (item.getSaturday() == 1){
            weeks.append(7);
        }
        if (item.getSunday() == 1){
            weeks.append(1);
        }
        return weeks.toString();
    }

    /**
     * 获取GroupProductExportVo中的week
     * @param item
     * @return
     */
    public static String genItemWeek(GroupProductExportVo item) {
        StringBuffer weeks = new StringBuffer();
        if (item.getMonday() == 1){
            weeks.append(2);
        }
        if (item.getTuesday() == 1){
            weeks.append(3);
        }
        if (item.getWednesday() == 1){
            weeks.append(4);
        }
        if (item.getThursday() == 1){
            weeks.append(5);
        }
        if (item.getFriday() == 1){
            weeks.append(6);
        }
        if (item.getSaturday() == 1){
            weeks.append(7);
        }
        if (item.getSunday() == 1){
            weeks.append(1);
        }
        return weeks.toString();
    }

    /**
     * 获取时间段内全部被block的产品id
     * @param list
     * @param startDate
     * @param endDate
     * @return
     */
    private List<String> getBlockProduct(List<ProductPageRes> list, Date startDate, Date endDate)throws Exception{
        List<String> result = Lists.newLinkedList();
        startDate = DateUtil.parse(DateUtil.format(startDate,"yyyy-MM-dd"),"yyyy-MM-dd");
        endDate = DateUtil.parse(DateUtil.format(endDate,"yyyy-MM-dd"),"yyyy-MM-dd");
        for (ProductPageRes productPageRes : list) {
            List<String> allBlock = Lists.newLinkedList();
            String block = null;
            if (StringUtils.isNotBlank(productPageRes.getShopBlock())){
                allBlock.add(productPageRes.getShopBlock());
            }
            if (StringUtils.isNotBlank(productPageRes.getShopItemBlock())){
                allBlock.add(productPageRes.getShopItemBlock());
            }
            if (!CollectionUtils.isEmpty(allBlock)){
                block = StringUtils.join(allBlock,", ");
            }
            if (StringUtils.isNotBlank(block)){
                List<Date> dates = blockRuleService.generateBookDate(startDate,endDate,block);
                if (CollectionUtils.isEmpty(dates)){
                    result.add(productPageRes.getId()+"");
                }
            }
        }
        return result;
    }

    /**
     * 获取时间段内全部被block的产品id
     * @param list
     * @param startDate
     * @param endDate
     * @return
     */
    private List<String> getBlockProduct2(List<ProductPackPageRes> list, Date startDate, Date endDate)throws Exception{
        List<String> result = Lists.newLinkedList();
        startDate = DateUtil.parse(DateUtil.format(startDate,"yyyy-MM-dd"),"yyyy-MM-dd");
        endDate = DateUtil.parse(DateUtil.format(endDate,"yyyy-MM-dd"),"yyyy-MM-dd");
        for (ProductPackPageRes productPageRes : list) {
            List<String> allBlock = Lists.newLinkedList();
            String block = null;
            if (StringUtils.isNotBlank(productPageRes.getShopBlock())){
                allBlock.add(productPageRes.getShopBlock());
            }
            if (StringUtils.isNotBlank(productPageRes.getShopItemBlock())){
                allBlock.add(productPageRes.getShopItemBlock());
            }
            if (!CollectionUtils.isEmpty(allBlock)){
                block = StringUtils.join(allBlock,", ");
            }
            if (StringUtils.isNotBlank(block)){
                List<Date> dates = blockRuleService.generateBookDate(startDate,endDate,block);
                if (CollectionUtils.isEmpty(dates)){
                    result.add(productPageRes.getId()+"");
                }
            }
        }
        return result;
    }

    /**
     * 规格生成产品
     * @param shopItemId
     * @return
     * @throws Exception
     */
    @Override
    @Async("taskExecutorPool")
    public List<Product> generateProduct(Integer shopItemId, HttpServletRequest request) throws Exception {
        log.info("生成规格id为{}的产品",shopItemId);
        ShopItem shopItem = shopItemService.selectById(shopItemId);
        if (shopItem.getDelFlag() == 0){
            //查询该规格生成的产品列表
            Wrapper<Product> productWrapper = new Wrapper<Product>() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0 and shop_item_id ="+shopItem.getId();
                }
            };
            List<Product> products = productMapper.selectList(productWrapper);
            SysService sysService = sysServiceService.selectById(shopItem.getServiceType());
            //规格类型为直接生成产品
            if (sysService.getProductType().compareTo(0) == 0){
                //检查是否已经生成过产品
                //不存在则生成产品
                if (CollectionUtils.isEmpty(products)){
                    Product product = new Product();
                    product.setShopId(shopItem.getShopId());
                    product.setShopItemId(shopItem.getId());
                    product.setDelFlag(DelFlagEnums.NORMAL.getCode());
                    product.setCreateUser(SecurityUtils.getLoginName(request));
                    product.setStatus(shopItem.getEnable());
                    productMapper.insert(product);
                    //生成产品子项
                    List<ProductItem> productItems = productItemService.generateProductItem(product.getId(),request);
                    //产品最高成本和最低成本计算
                    if (!CollectionUtils.isEmpty(productItems)){
                        product.setMinCost(minCost(productItems));
                        product.setMaxCost(maxCost(productItems));
                        productMapper.updateById(product);
                    }
                }
                //存在则更新成本最高最低价
                else {
                    for (Product product : products) {
                        //生成产品子项
                        List<ProductItem> productItems = productItemService.generateProductItem(product.getId(),request);
                        //产品最高成本和最低成本计算
                        if (!CollectionUtils.isEmpty(productItems)){
                            product.setMinCost(minCost(productItems));
                            product.setMaxCost(maxCost(productItems));
                            product.setStatus(shopItem.getEnable());
                            productMapper.updateById(product);
                        }
                    }
                }
            }
            //规格类型为按权益类型生成产品
            else if (sysService.getProductType().compareTo(1) == 0){
                //权益类型存在
                if (StringUtils.isNotBlank(shopItem.getGift())){
                    //规格中所有的权益类型
                    HashSet<String> ruleSet = new HashSet<>();
                    String[] gifts = shopItem.getGift().split(",");
                    for (String gift : gifts) {
                        ruleSet.add(gift);
                    }
                    //获取新增的权益类型
                    List<String> addGifts = giveAddGift(ruleSet,products);
                    //获取没有变动的产品,需要更新产品最高最低成本价
                    List<Product> updProducts = giveUpdGift(ruleSet,products);
                    //获取删除的产品
//                List<Product> delProducts = giveDelGift(ruleSet,products);
                    //产品新增
                    if (!CollectionUtils.isEmpty(addGifts)){
                        for (String addGift : addGifts) {
                            Product product = new Product();
                            product.setShopId(shopItem.getShopId());
                            product.setShopItemId(shopItem.getId());
                            product.setGift(addGift);
                            product.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            product.setCreateUser(SecurityUtils.getLoginName(request));
                            product.setStatus(shopItem.getEnable());
                            productMapper.insert(product);
                            //生成产品子项
                            List<ProductItem> productItems = productItemService.generateProductItem(product.getId(),request);
                            //产品最高成本和最低成本计算
                            if (!CollectionUtils.isEmpty(productItems)){
                                product.setMinCost(minCost(productItems));
                                product.setMaxCost(maxCost(productItems));
                                productMapper.updateById(product);
                            }
                        }
                    }
                    //产品最高最低成本修改
                    if (!CollectionUtils.isEmpty(updProducts)){
                        for (Product updProduct : updProducts) {
                            //生成产品子项
                            List<ProductItem> productItems = productItemService.generateProductItem(updProduct.getId(),request);
                            //产品最高成本和最低成本计算
                            if (!CollectionUtils.isEmpty(productItems)){
                                updProduct.setMinCost(minCost(productItems));
                                updProduct.setMaxCost(maxCost(productItems));
                                updProduct.setStatus(shopItem.getEnable());
                                productMapper.updateById(updProduct);
                            }
                        }
                    }
                    //产品删除
//                if (!CollectionUtils.isEmpty(delProducts)){
//                    for (Product delProduct : delProducts) {
//                        delProduct.setDelFlag(DelFlagEnums.DELETE.getCode());
//                        productMapper.updateById(delProduct);
//                    }
//                }
                }
            }
            return productMapper.selectList(productWrapper);
        }
        return null;
    }

    /**
     * 住宿只有固定贴现结算规则时产品子项 及成本显示问题
     * @return
     * @throws Exception
     */
    @Override
    public List<Integer> discountProduct() throws Exception {
        return productMapper.discountProduct();
    }

    /**
     * 停售规格删除的权益类型对应的产品
     *
     * @param listTemp
     * @param shopItem
     */
    @Override
    public void updateProductStatus(List<String> listTemp, ShopItem shopItem,Integer status) {
        Product params  = new Product();
        params.setShopItemId(shopItem.getId());
        params.setShopId(shopItem.getShopId());
        params.setStatus(status);
        SysService sysService = sysServiceService.selectById(shopItem.getServiceType());
        if(CollectionUtils.isEmpty(listTemp)){//产品的停售或者在售
            //跟新产品组下的产品为申请停售
            Wrapper<Product> local = new Wrapper<Product>() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0 and shop_id =" + params.getShopId() +" and shop_item_id =  "+params.getShopItemId() ;
                }
            };
            List<Product> productList =  productMapper.selectList(local);
            List<Integer> ids =  productList.stream().map(pro ->pro.getId()).collect(Collectors.toList());
            //状态改为停售时，产品、产品组产品、结算规则都改为停售
            if(status == 1){
                productMapper.updateProductStatus(params);
                updateProductGroupProduct(ids,1);
                updateShopItemSettlePriceRule(null,shopItem,status, params );
            }else{
                //状态改为在售时
                if(status == 0){
                    //产品生成规则为直接生成，例如 住宿 兑换券 类型的，产品、产品组产品、结算规则 改为在售，而产品生成规则为按权益类型生成，则不改为在售
                    if (sysService.getProductType().compareTo(0) == 0){
                        productMapper.updateProductStatus(params);
                        updateProductGroupProduct(ids,status);
                        updateShopItemSettlePriceRule(null,shopItem,status, params );
                    }
                }
            }
        }else{//产品组中权益类型的删除停售或者在售
            for(String str : listTemp){
                params.setGift(str);
                productMapper.updateProductStatus(params);
                //跟新产品组下的产品为申请停售
                Wrapper<Product> local = new Wrapper<Product>() {
                    @Override
                    public String getSqlSegment() {
                        return "where del_flag = 0 and shop_id =" + params.getShopId() +" and shop_item_id =  "+params.getShopItemId() + " and gift ='"+str+"'" ;
                    }
                };
                List<Product> productList =  productMapper.selectList(local);
                List<Integer> ids =  productList.stream().map(pro ->pro.getId()).collect(Collectors.toList());
                if(status == 1){
                    updateProductGroupProduct(ids,1);
                    updateShopItemSettlePriceRule(str,shopItem,status, params );
                }else{
                    if(status == 0){
                        updateProductGroupProduct(ids,status);
                        updateShopItemSettlePriceRule(str,shopItem,status,params);
                    }
                }

            }
        }

    }

    public void updateProduct(List<Product> productList,Integer status){
        for(Product product : productList){
            product.setStatus(status);
            productMapper.updateById(product);
        }
    }
    public void updateProductGroupProduct(List<Integer> productIds,Integer status){
//        for(ProductGroupProduct productGroupProduct : productGroupProducts){
//            productGroupProduct.setStatus(0);
//            productGroupProductService.updateById(productGroupProduct);
//        }
        Map params = Maps.newHashMap();
        params.put("status",status);
        params.put("productIds",productIds);
        if(productIds.size() > 0){
            productGroupProductService.updateByProductIds(params);
        }

    }

    public void updateShopItemSettlePriceRule(String gift , ShopItem item,Integer status, Product params){
//        EntityWrapper<ShopItemSettlePriceRule> wrapper = new EntityWrapper<ShopItemSettlePriceRule>();
//        wrapper.eq("gift",gift);
//        wrapper.eq("shop_item_id",item.getId());
//       List<ShopItemSettlePriceRule> result =  shopItemSettlePriceRuleService.selectList(wrapper);
//       if(!result.isEmpty()){
//           for(ShopItemSettlePriceRule rule :result){
//               rule.setStatus(status);
//               shopItemSettlePriceRuleService.updateById(rule);
//           }
//       }
        shopItemSettlePriceRuleService.updateShopItemSettlePriceRule(params);
    }

    /**
     * 获取结算规则比产品中新增的权益类型
     * @param set
     * @param products
     * @return
     */
    public List<String> giveAddGift(HashSet<String> set,List<Product> products){
        List<String> result = Lists.newLinkedList();
        if (!CollectionUtils.isEmpty(set)){
            if (CollectionUtils.isEmpty(products)){
                result.addAll(set);
            }else {
                for (String gift : set) {
                    boolean flag = true;
                    for (Product product : products) {
                        if (product.getGift().equals(gift)){
                            flag = false;
                            break;
                        }
                    }
                    if (flag){
                        result.add(gift);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 获取没有增删的权益类型
     * @param set
     * @param products
     * @return
     */
    public List<Product> giveUpdGift(HashSet<String> set, List<Product> products){
        List<Product> result = Lists.newLinkedList();
        if (!CollectionUtils.isEmpty(set) && !CollectionUtils.isEmpty(products)){
            for (String gift : set) {
                for (Product product : products) {
                    if (gift.equals(product.getGift())){
                        result.add(product);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 获取结算规则比产品中删除的权益类型
     * @param set
     * @param products
     * @return
     */
    public List<Product> giveDelGift(HashSet<String> set,List<Product> products){
        List<Product> result = Lists.newLinkedList();
        if (!CollectionUtils.isEmpty(products)){
            if (CollectionUtils.isEmpty(set)){
                for (Product product : products) {
                    result.add(product);
                }
            }else {
                for (Product product : products) {
                    boolean flag = true;
                    for (String gift : set) {
                        if (product.getGift().equals(gift)){
                            flag = false;
                            break;
                        }
                    }
                    if (flag){
                        result.add(product);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 获取产品子项中的最小成本
     * @param productItems
     * @return
     */
    public BigDecimal minCost(List<ProductItem> productItems){
        BigDecimal minCost = null;
        if (!CollectionUtils.isEmpty(productItems)){
            for (ProductItem productItem : productItems) {
                if (minCost == null){
                    minCost = productItem.getCost();
                }else {
                    if (productItem.getCost() != null && productItem.getCost().compareTo(minCost) < 0){
                        minCost = productItem.getCost();
                    }
                }
            }
        }
        return minCost;
    }

    /**
     * 获取产品子项中的最大成本
     * @param productItems
     * @return
     */
    public BigDecimal maxCost(List<ProductItem> productItems){
        BigDecimal maxCost = null;
        if (!CollectionUtils.isEmpty(productItems)){
            for (ProductItem productItem : productItems) {
                if (maxCost == null){
                    maxCost = productItem.getCost();
                }else {
                    if (productItem.getCost() != null && productItem.getCost().compareTo(maxCost) > 0){
                        maxCost = productItem.getCost();
                    }
                }
            }
        }
        return maxCost;
    }
}

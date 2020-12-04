package com.colourfulchina.pangu.taishang.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.god.door.api.vo.KltSysUser;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.nuwa.api.feign.RemoteKlfEmailService;
import com.colourfulchina.nuwa.api.vo.SysEmailSendReqVo;
import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.enums.ResourceTypeEnums;
import com.colourfulchina.pangu.taishang.api.vo.BlockBookDaysVo;
import com.colourfulchina.pangu.taishang.api.vo.BookProductVo;
import com.colourfulchina.pangu.taishang.api.vo.GroupProductVo;
import com.colourfulchina.pangu.taishang.api.vo.req.QueryGroupProductReq;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectBookShopItemReq;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopListPageQueryReq;
import com.colourfulchina.pangu.taishang.api.vo.res.GroupProductExportVo;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopItemNetPriceRuleQueryRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopListQueryRes;
import com.colourfulchina.pangu.taishang.config.FileDownloadProperties;
import com.colourfulchina.pangu.taishang.mapper.*;
import com.colourfulchina.pangu.taishang.service.*;
import com.colourfulchina.pangu.taishang.utils.DateUtils;
import com.colourfulchina.pangu.taishang.utils.ExportGroupProductUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductGroupProductServiceImpl extends ServiceImpl<ProductGroupProductMapper,ProductGroupProduct> implements ProductGroupProductService {
    @Autowired
    private ProductGroupProductMapper productGroupProductMapper;
    @Autowired
    private ProductGroupMapper productGroupMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ShopItemMapper shopItemMapper;
    @Autowired
    private ShopItemNetPriceRuleMapper shopItemNetPriceRuleMapper;
    @Autowired
    private ShopProtocolMapper shopProtocolMapper;
    @Autowired
    private BlockRuleService blockRuleService;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private ProductItemService productItemService;
    @Autowired
    private FileDownloadProperties fileDownloadProperties;
    @Autowired
    private ShopItemNetPriceRuleService shopItemNetPriceRuleService;
    @Autowired
    private ShopItemSettlePriceRuleService shopItemSettlePriceRuleService;
    @Autowired
    private RemoteKlfEmailService remoteKlfEmailService;

    /**
     * 产品组合产品之间的关系数据
     *
     * @param oldproductGroup
     * @return
     */
    @Override
    public List<ProductGroupProduct> selectByProductGroupId(Integer oldproductGroup) {
        Wrapper<ProductGroupProduct> localWrapper = new Wrapper<ProductGroupProduct>() {
            @Override
            public String getSqlSegment() {
                return " where product_group_id = " + oldproductGroup;
            }
        };
        return productGroupProductMapper.selectList(localWrapper);
    }

    /**
     * 产品组的所属产品条件查询
     * @param queryGroupProductReq
     * @return
     * @throws Exception
     */
    @Override
    public List<GroupProductVo> queryGroupProduct(QueryGroupProductReq queryGroupProductReq) throws Exception {
        List<GroupProductVo> list = productGroupProductMapper.queryGroupProduct(queryGroupProductReq);
        //block规则翻译
        for (GroupProductVo groupProductVo : list) {
            if (StringUtils.isNotBlank(groupProductVo.getBlockRule())){
                List<BlockRule> blockRules = blockRuleService.blockRuleStr2list(groupProductVo.getBlockRule());
                String blocks = blockRules.stream().map(blockRule -> blockRule.getNatural()).collect(Collectors.joining(" "));
                groupProductVo.setBlockNatural(blocks);
                groupProductVo.setBlockRuleList(blockRules);
            }
        }
        return list;
    }

    /**
     * 查询product_group_product最大sort
     * @return
     * @throws Exception
     */
    @Override
    public Integer queryMaxSort() throws Exception {
        Integer sort = productGroupProductMapper.queryMaxSort();
        return sort;
    }

    /**
     * 更新产品组下面的产品的成本区间
     * @param productGroupProductIds
     * @return
     * @throws Exception
     */
    @Override
    @Async("taskExecutorPool")
    public List<ProductGroupProduct> updCost(List<Integer> productGroupProductIds) throws Exception {
        List<ProductGroupProduct> res = Lists.newLinkedList();
        for (Integer productGroupProductId : productGroupProductIds) {
            //获取产品组下面的产品
            ProductGroupProduct productGroupProduct = productGroupProductMapper.selectById(productGroupProductId);
            //获取产品
            Product product = productMapper.selectById(productGroupProduct.getProductId());
            //获取产品组
            ProductGroup productGroup = productGroupMapper.selectById(productGroupProduct.getProductGroupId());
            //获取商品
            Goods goods = goodsMapper.selectById(productGroup.getGoodsId());
            //获取商户规格
            ShopItem shopItem = shopItemMapper.selectById(product.getShopItemId());
            //获取商户协议
            ShopProtocol shopProtocol = shopProtocolMapper.selectById(product.getShopId());
            List<String> allBlockList = Lists.newLinkedList();
            String allBlock = null;
            if (StringUtils.isNotBlank(productGroupProduct.getBlockRule())){
                allBlockList.add(productGroupProduct.getBlockRule());
            }
            if (StringUtils.isNotBlank(productGroup.getBlockRule())){
                allBlockList.add(productGroup.getBlockRule());
            }
            if (StringUtils.isNotBlank(goods.getBlock())){
                allBlockList.add(goods.getBlock());
            }
            if (StringUtils.isNotBlank(shopItem.getBlockRule())){
                allBlockList.add(shopItem.getBlockRule());
            }
            if (StringUtils.isNotBlank(shopProtocol.getBlockRule())){
                allBlockList.add(shopProtocol.getBlockRule());
            }
            if (!CollectionUtils.isEmpty(allBlockList)){
                allBlock = StringUtils.join(allBlockList,", ");
            }
            //查询产品子项的成本列表
            Wrapper productItemWrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0 and product_id ="+product.getId()+" order by cost desc";
                }
            };
            List<ProductItem> productItems = productItemService.selectList(productItemWrapper);
            if (!CollectionUtils.isEmpty(productItems)){
                List<BigDecimal> costs = Lists.newLinkedList();
                for (ProductItem productItem : productItems) {
                    if (productItem.getCost() != null){
                        //查询该成本对应时间列表
                        String week = "";
                        if (productItem.getMonday() == 1){
                            week = week + "2";
                        }
                        if (productItem.getTuesday() == 1){
                            week = week + "3";
                        }
                        if (productItem.getWednesday() == 1){
                            week = week + "4";
                        }
                        if (productItem.getThursday() == 1){
                            week = week + "5";
                        }
                        if (productItem.getFriday() == 1){
                            week = week + "6";
                        }
                        if (productItem.getSaturday() == 1){
                            week = week + "7";
                        }
                        if (productItem.getSunday() == 1){
                            week = week + "1";
                        }
                        List<Date> baseDates = DateUtils.containDateByWeeks(productItem.getStartDate(),productItem.getEndDate(),week);
                        if (!CollectionUtils.isEmpty(baseDates)){
                            //查询该成本区间被block的时间列表
                            HashSet<Date> bookDates = null;
                            if (StringUtils.isNotBlank(allBlock)){
                                bookDates = blockRuleService.generateBlockDate(productItem.getStartDate(),productItem.getEndDate(),allBlock);
                                if (!CollectionUtils.isEmpty(bookDates)){
                                    baseDates.removeAll(bookDates);
                                }
                            }
                            if (!CollectionUtils.isEmpty(baseDates)){
                                costs.add(productItem.getCost());
                            }
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(costs)){
                    Collections.sort(costs);
                    productGroupProduct.setMinCost(costs.get(0).setScale(2,BigDecimal.ROUND_HALF_UP));
                    productGroupProduct.setMaxCost(costs.get(costs.size()-1).setScale(2,BigDecimal.ROUND_HALF_UP));
                    productGroupProductMapper.updateById(productGroupProduct);
                }
            }
            res.add(productGroupProduct);
        }
        return res;
    }

    /**
     * 更新产品组下面的产品的成本区间(根据产品组id)
     * @param productGroupId
     * @return
     * @throws Exception
     */
    @Override
    @Async("taskExecutorPool")
    public List<ProductGroupProduct> updCostByGroupId(Integer productGroupId) throws Exception {
        List<ProductGroupProduct> result = Lists.newLinkedList();
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and product_group_id ="+productGroupId;
            }
        };
        List<ProductGroupProduct> list = productGroupProductMapper.selectList(wrapper);
        List<Integer> ids = Lists.newLinkedList();
        if (!CollectionUtils.isEmpty(list)){
            for (ProductGroupProduct productGroupProduct : list) {
                ids.add(productGroupProduct.getId());
            }
            result = updCost(ids);
        }
        return result;
    }

    /**
     * 查询
     * @param productGroupId
     * @return
     * @throws Exception
     */
    @Override
    public List<ProductGroupProduct> getCostByGroupId(Integer productGroupId ) throws Exception {
        List<ProductGroupProduct> result = Lists.newLinkedList();
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and product_group_id ="+productGroupId;
            }
        };
        List<ProductGroupProduct> list = productGroupProductMapper.selectList(wrapper);

        return list;
    }

    /**
     * 更新产品组下面的产品的成本区间(根据商品id)
     * @param goodsId
     * @return
     * @throws Exception
     */
    @Override
    @Async("taskExecutorPool")
    public List<ProductGroupProduct> updCostByGoodsId(Integer goodsId) throws Exception {
        List<ProductGroupProduct> result = Lists.newLinkedList();
        Wrapper productGroupWrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and goods_id ="+goodsId;
            }
        };
        List<ProductGroup> groups = productGroupMapper.selectList(productGroupWrapper);
        if (!CollectionUtils.isEmpty(groups)){
            for (ProductGroup group : groups) {
                List<ProductGroupProduct> res = updCostByGroupId(group.getId());
                result.addAll(res);
            }
        }
        return result;
    }

    /**
     * 更新产品组下面的产品的成本区间(根据商户规格id)
     * @param shopItemId
     * @return
     * @throws Exception
     */
    @Override
    @Async("taskExecutorPool")
    public List<ProductGroupProduct> updCostByShopItemId(Integer shopItemId) throws Exception {
        List<Integer> ids = Lists.newLinkedList();
        List<ProductGroupProduct> result = Lists.newLinkedList();
        Wrapper productWrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and shop_item_id ="+shopItemId;
            }
        };
        List<Product> products = productMapper.selectList(productWrapper);
        if (!CollectionUtils.isEmpty(products)){
            for (Product product : products) {
                Wrapper productGroupProductWrapper = new Wrapper() {
                    @Override
                    public String getSqlSegment() {
                        return "where del_flag = 0 and product_id ="+product.getId();
                    }
                };
                List<ProductGroupProduct> groupProducts = productGroupProductMapper.selectList(productGroupProductWrapper);
                if (!CollectionUtils.isEmpty(groupProducts)){
                    for (ProductGroupProduct productGroupProduct : groupProducts) {
                        ids.add(productGroupProduct.getId());
                    }
                }
            }
            result = updCost(ids);
        }
        return result;
    }

    /**
     * 更新产品组下面的产品的成本区间(根据商户id)
     * @param shopId
     * @return
     * @throws Exception
     */
    @Override
    @Async("taskExecutorPool")
    public List<ProductGroupProduct> updCostByShopId(Integer shopId) throws Exception {
        List<ProductGroupProduct> result = Lists.newLinkedList();
        Wrapper shopItemWrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and shop_id ="+shopId;
            }
        };
        List<ShopItem> shopItems = shopItemMapper.selectList(shopItemWrapper);
        if (!CollectionUtils.isEmpty(shopItems)){
            for (ShopItem shopItem : shopItems) {
                List<ProductGroupProduct> res = updCostByShopItemId(shopItem.getId());
                result.addAll(res);
            }
        }
        return result;
    }

    @Override
    public void saveSort(List<GroupProductVo> groupProductVoList) {
        for(GroupProductVo vo : groupProductVoList){
            ProductGroupProduct value = new ProductGroupProduct();
            value.setId(vo.getId());
            value.setSort(vo.getSort());
            productGroupProductMapper.updateById(value);
        }
    }

    /**
     * 查询产品组中的产品
     *
     * @param ids
     * @return
     */
    @Override
    public List<ProductGroupProduct> selectByProductIds(List<Integer> ids) {
        EntityWrapper<ProductGroupProduct> local = new EntityWrapper<ProductGroupProduct>() ;
        local.in("product_id",ids);
        return productGroupProductMapper.selectList(local);
    }

    @Override
    public void updateByProductIds(Map params) {
        productGroupProductMapper.updateByProductIds(params);
    }

    /**
     * 查询产品组下面的产品信息（商区）
     * @param pageVo
     * @return
     * @throws Exception
     */
    @Override
    public PageVo<ShopListQueryRes> selectGoodsListByGroupId(PageVo<ShopListPageQueryReq> pageVo) throws Exception {
        PageVo<ShopListQueryRes> pageRes = new PageVo<>();
        List<ShopListQueryRes> list = productGroupProductMapper.selectGoodsListByGroupId(pageVo,pageVo.getCondition());
        BeanUtils.copyProperties(pageVo,pageRes);
        pageRes.setRecords(list);
        return pageRes;
    }

    /**
     * 产品组产品导出
     * @param queryGroupProductReq
     * @return
     * @throws Exception
     */
    @Override
    @Async("taskExecutorPool")
    public String exportGroupProduct(QueryGroupProductReq queryGroupProductReq, KltSysUser sysUser) throws Exception {
        String url = null;
        List<GroupProductExportVo> list = productGroupProductMapper.groupProductExport(queryGroupProductReq);
        if (!CollectionUtils.isEmpty(list)){
            List<SysService> services = productGroupMapper.selectGroupService(queryGroupProductReq.getGroupId());
            if (!CollectionUtils.isEmpty(services)){
                boolean noAccom = true;
                for (SysService service : services) {
                    if (service.getCode().equals(ResourceTypeEnums.ACCOM.getCode())){
                        noAccom = false;
                    }
                }
                String fileName = "产品组产品信息"+"-"+DateUtil.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
                url = exportItem(noAccom,list,fileName);
                String[] receiveEmailAddress = sysUser.getEmail().split(",");
                // 发送邮件
                SysEmailSendReqVo sysEmailSendReqVo = new SysEmailSendReqVo();
                // 记录邮件发送记录
                sysEmailSendReqVo.setSubject("【产品组产品列表-导出】"+fileName);
                sysEmailSendReqVo.setFrom(fileDownloadProperties.getSendEmailAddress());
                sysEmailSendReqVo.setTo(receiveEmailAddress);
                sysEmailSendReqVo.setContent("<html><a href='"+url+"'>"+fileName+"</a></html>");
                log.info("remoteKlfEmailService.send:{}",JSON.toJSONString(sysEmailSendReqVo));
                final CommonResultVo resultVo = remoteKlfEmailService.send(sysEmailSendReqVo);
                log.info("remoteKlfEmailService.send result:{}",JSON.toJSONString(resultVo));
            }
        }
        return url;
    }

    /**
     * 在线预约产品列表
     * @param selectBookShopItemReq
     * @return
     * @throws Exception
     */
    @Override
    @Cacheable(value = "ProductGroupProduct",key = "'selectBookProduct_'+#selectBookShopItemReq.productGroupId+'_'+#selectBookShopItemReq.service",unless = "#result == null")
    public List<BookProductVo> selectBookProduct(SelectBookShopItemReq selectBookShopItemReq) throws Exception {
        List<BookProductVo> list = productGroupProductMapper.selectBookProduct(selectBookShopItemReq);
        return list;
    }

    @Override
    public BlockBookDaysVo queryBookDays(Integer productGroupProductId) {
        BlockBookDaysVo res = productGroupProductMapper.queryBookDays(productGroupProductId);
        return res;
    }

    @Override
    public String exportItem(boolean noAccom,List<GroupProductExportVo> list,String fileName)throws Exception{
        String url = null;
        ExcelWriter excelWriter=new ExcelWriter(true);
//        String fileName="产品信息"+"-"+DateUtil.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
        excelWriter.setDestFile(new File(fileDownloadProperties.getPath()+"/"+fileName));
        excelWriter.setOrCreateSheet("sheet1");
        excelWriter.writeHeadRow(ExportGroupProductUtils.headRow(noAccom));
        if (!CollectionUtils.isEmpty(list)){
            if (noAccom){
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
                    List<ShopItemNetPriceRule> prices = shopItemNetPriceRuleMapper.selectList(priceWrapper);

                    //新
                    Wrapper itemWrapper = new Wrapper() {
                        @Override
                        public String getSqlSegment() {
                            return "where del_flag = 0 and product_id ="+record.getProductId();
                        }
                    };
                    List<ProductItem> productItemList = productItemService.selectList(itemWrapper);
                    if (!CollectionUtils.isEmpty(productItemList)){
                        for (ProductItem item : productItemList) {
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
                            if (item.getCost() != null){
                                applyDate = item.getApplyTime().split(":")[0];
                                applyWeek = item.getApplyTime().split(":")[1];
                                String week = genItemWeek(item);
                                if (StringUtils.isNotBlank(week)){
                                    List<Date> dates = DateUtils.containDateByWeeks(item.getStartDate(),item.getEndDate(),week);
                                    if (!CollectionUtils.isEmpty(dates) && !CollectionUtils.isEmpty(prices)){
                                        //查找对应的价格
                                        ShopItemNetPriceRule price = shopItemNetPriceRuleService.foundPriceByTime(dates.get(0),prices);
                                        if (price != null){
                                            ShopItemNetPriceRuleQueryRes shopItemNetPriceRuleQueryRes = new ShopItemNetPriceRuleQueryRes();
                                            BeanUtils.copyProperties(price,shopItemNetPriceRuleQueryRes);
                                            BigDecimal tempPrice = colPrice(shopItemNetPriceRuleQueryRes);
                                            marketPrice = tempPrice.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
                                            netPrice = price.getNetPrice().setScale(2,BigDecimal.ROUND_HALF_UP);
                                            serviceRate = price.getServiceRate().multiply(new BigDecimal(100)).setScale(0) + "%";
                                            taxRate = price.getTaxRate().multiply(new BigDecimal(100)).setScale(0) + "%";
                                            singlePrice = tempPrice.setScale(2,BigDecimal.ROUND_HALF_UP);
                                            doublePrice = tempPrice.multiply(new BigDecimal(2)).setScale(2,BigDecimal.ROUND_HALF_UP);
                                            cost = item.getCost();
                                            if (tempPrice != null && tempPrice.compareTo(new BigDecimal(0)) != 0){
                                                if ("F2".equals(record.getGiftCode())){
                                                    tempPrice = tempPrice.multiply(new BigDecimal(2));
                                                }
                                                itemRate = item.getCost().divide(tempPrice,2,BigDecimal.ROUND_HALF_UP).toString();
                                            }
                                        }
                                    }
                                }
                            }
                            List<Object> rowData=Lists.newArrayList();
                            rowData.add(record.getCityName());
                            rowData.add(record.getHotelChName());
                            rowData.add(record.getHotelEnName());
                            rowData.add(record.getShopEnName());
                            rowData.add(record.getShopChName());
                            rowData.add(record.getServiceName());
                            rowData.add(record.getGiftShortName());
                            rowData.add(record.getGiftName());
                            rowData.add(marketPrice);
                            rowData.add(applyDate);
                            rowData.add(applyWeek);
                            rowData.add(record.getShopItemName());
                            rowData.add(netPrice);
                            rowData.add(serviceRate);
                            rowData.add(taxRate);
                            rowData.add(singlePrice);
                            rowData.add(doublePrice);
                            rowData.add(cost);
                            rowData.add(itemRate);
                            rowData.add(null);
                            rowData.add((StringUtils.isNotBlank(record.getOpenTime()) && StringUtils.isNotBlank(record.getCloseTime())) ? record.getOpenTime() + "~" + record.getCloseTime() : null);
                            rowData.add(targetBlock);
                            rowData.add(record.getAddress());
                            rowData.add(record.getPhone());
                            rowData.add(record.getParking());
                            rowData.add(record.getChildren());
                            rowData.add(record.getRemark());
                            excelWriter.writeRow(rowData);
                        }
                    }

                }
            }else {
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

                    //新
                    Wrapper itemWrapper = new Wrapper() {
                        @Override
                        public String getSqlSegment() {
                            return "where del_flag = 0 and product_id ="+record.getProductId();
                        }
                    };
                    List<ProductItem> productItemList = productItemService.selectList(itemWrapper);
                    if (!CollectionUtils.isEmpty(productItemList)){
                        for (ProductItem item : productItemList) {
                            //适用日期
                            String applyDate = null;
                            //适用星期
                            String applyWeek = null;
                            //成本
                            BigDecimal cost = null;
                            if (item.getCost() != null){
                                applyDate = item.getApplyTime().split(":")[0];
                                applyWeek = item.getApplyTime().split(":")[1];
                                cost = item.getCost();
                            }
                            List<Object> rowData=Lists.newArrayList();
                            rowData.add(record.getCountryName());
                            rowData.add(record.getCityName());
                            rowData.add(record.getHotelChName());
                            rowData.add(record.getHotelEnName());
                            rowData.add(record.getShopItemName());
                            rowData.add(record.getNeeds());
                            rowData.add(record.getAddon());
                            rowData.add(applyDate);
                            rowData.add(applyWeek);
                            rowData.add(cost);
                            rowData.add(null);
                            rowData.add(record.getAddress());
                            rowData.add(record.getPhone());
                            rowData.add(targetBlock);
                            rowData.add(record.getRemark());
                            excelWriter.writeRow(rowData);
                        }
                    }
                }
            }
            url = fileDownloadProperties.getUrl()+fileName;
        }
        excelWriter.flush();
        return url;
    }

    private String genSettlePriceRuleStr(ShopItemSettlePriceRule shopItemSettlePriceRule) {
        List<String> weeks = Lists.newLinkedList();
        StringBuffer timeStr = new StringBuffer();
        timeStr.append(DateUtil.format(shopItemSettlePriceRule.getStartDate(),"yyyy-MM-dd"));
        timeStr.append("~");
        timeStr.append(DateUtil.format(shopItemSettlePriceRule.getEndDate(),"yyyy-MM-dd"));
        timeStr.append("：");
        if (shopItemSettlePriceRule.getMonday()==1){
            weeks.add("周一");
        }
        if (shopItemSettlePriceRule.getTuesday()==1){
            weeks.add("周二");
        }
        if (shopItemSettlePriceRule.getWednesday()==1){
            weeks.add("周三");
        }
        if (shopItemSettlePriceRule.getThursday()==1){
            weeks.add("周四");
        }
        if (shopItemSettlePriceRule.getFriday()==1){
            weeks.add("周五");
        }
        if (shopItemSettlePriceRule.getSaturday()==1){
            weeks.add("周六");
        }
        if (shopItemSettlePriceRule.getSunday()==1){
            weeks.add("周日");
        }
        if (!CollectionUtils.isEmpty(weeks)){
            timeStr.append(StringUtils.join(weeks,"、"));
        }
        return timeStr.toString();
    }

    /**
     * 获取productItem中的week
     * @param item
     * @return
     */
    public static String genItemWeek(ProductItem item) {
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
     * 计算规格产品价格
     * @param req
     * @return
     */
    public static BigDecimal colPrice(ShopItemNetPriceRuleQueryRes req) {
        BigDecimal res = null;
        if (req.getNetPrice() != null){
            res = req.getNetPrice();
            BigDecimal taxBase = req.getNetPrice();
            if (req.getServiceRate() != null){
                taxBase = req.getNetPrice().add(req.getNetPrice().multiply(req.getServiceRate()));
                res = taxBase;
            }
            if (req.getTaxRate() != null){
                res = res.add(taxBase.multiply(req.getTaxRate()));
            }
        }
        return res;
    }
}

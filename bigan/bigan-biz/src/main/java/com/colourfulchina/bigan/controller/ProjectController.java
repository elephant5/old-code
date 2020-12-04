package com.colourfulchina.bigan.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.bigan.api.entity.*;
import com.colourfulchina.bigan.api.vo.*;
import com.colourfulchina.bigan.enums.GoodsEnums;
import com.colourfulchina.bigan.service.*;
import com.colourfulchina.bigan.utils.NumUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping(path = "/project")
public class ProjectController {

    private static final Integer HUNDRED = 100;
	private static final int ERROR_CODE = 200;
	private static final int SUCCESS_CODE = 100;
	private static final String SUCCESS_MESSAGE = "OK";

    @Autowired
    private ProjectService projectService;
    @Autowired
    private PrjGroupService prjGroupService;
    @Autowired
    private PrjGroupGoodsService prjGroupGoodsService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ShopitemsService shopitemsService;
    @Autowired
    private SysFileService sysFileService;
    @Autowired
    private ProjectChannelService projectChannelService;
    @Autowired
    private ProjectMetaService projectMetaService;
    @Autowired
    private PrjPackageService prjPackageService;
    @Autowired
    private PrjPackageGroupsService prjPackageGroupsService;

    @PostMapping("/list")
    public List<ProjectVo> list(@RequestBody ProjectListQueryVo projectListQueryVo){
        Wrapper<Project> wrapper=new Wrapper<Project>() {
            @Override
            public String getSqlSegment() {
                if (projectListQueryVo == null){
                    return null;
                }
                return projectListQueryVo.getBankId() == null ? null : "where bank_id = "+projectListQueryVo.getBankId();
            }
        };
        List<Project> projectList=projectService.selectList(wrapper);
        List<ProjectVo> projectVoList = Lists.newArrayList();
        for (Project project : projectList){
            ProjectVo projectVo=new ProjectVo();
            BeanUtils.copyProperties(project,projectVo);
            projectVoList.add(projectVo);
        }
        return projectVoList;
    }

    @ApiOperation(value = "生成项目详细信息")
    @GetMapping({"/cdnList","/cdnList/{projectIds}"})
    public CommonResultVo<List<ProjectCdnVo>> cdnList(@PathVariable(required = false) String projectIds){
        CommonResultVo<List<ProjectCdnVo>> result = new CommonResultVo<>();
        if (projectIds == null || "".equals(projectIds)){
            result.setCode(300);
            result.setMsg("项目id为空！");
            return result;
        }
        long start =System.currentTimeMillis();
        try {
            String[] strings = projectIds.split(",");
            List<ProjectCdnVo> projectCdnVoList = projectService.selectProjectList(strings);
            for (ProjectCdnVo projectCdnVo : projectCdnVoList){
                //销售渠道
                ProjectChannel projectChannel = new ProjectChannel();
                projectChannel.setProjectId(Integer.valueOf(projectCdnVo.getId()+""));
                List<ProjectChannel> projectChannelList = projectChannelService.selectByProjectId(projectChannel);
                projectCdnVo.setProjectChannelList(projectChannelList);
                //产品组
                PrjGroupVo pgvo = new PrjGroupVo();
                pgvo.setProjectId(projectCdnVo.getId());
                List<PrjGroupVo> prjGroupVoList = prjGroupService.selectPrjGroupList(pgvo);
                for (PrjGroupVo prjGroupVo : prjGroupVoList){
                    PrjGroupGoods prjGroupGoods = new PrjGroupGoods();
                    prjGroupGoods.setGroupId(prjGroupVo.getId());
                    prjGroupGoods.setProjectId(projectCdnVo.getId());
                    List<PrjGroupGoods> prjGroupGoodsList = prjGroupGoodsService.findListByProjectIdAndGroupId(prjGroupGoods);
                    List<GoodsDetailVo> goodsDetailVoList = goodsService.queryGoodsDetail(prjGroupGoodsList);
                    for (GoodsDetailVo gdv: goodsDetailVoList){
                        if(!CollectionUtils.isEmpty(prjGroupGoodsList)){
                            prjGroupGoodsList.forEach(item -> {
                                if(item.getGoodsId().equals(gdv.getGoodsId())){
                                    gdv.setSort(item.getSort());
                                }
                            });
                        }
                        gdv.setGroupId(prjGroupVo.getId());
                        //item列表和最低净价
                        if (gdv.getItems()!=null && !"".equals(gdv.getItems())){
                            List<ShopItem> itemList = shopitemsService.selectByItemIdList(getItemsId(gdv.getItems()));
                            gdv.setItemList(itemList);
                            gdv.setMinPrice(goodsLowNet(itemList));
                            gdv.setItemString(itemDetail(itemList));
                        }
                        List<SysFile> fileList = sysFileService.selectShopPic(gdv.getShopId());
                        List<String> stringList = Lists.newArrayList();
                        if (fileList == null || fileList.size() == 0){
                            stringList.add("https://cdn.colourfulchina.com/alipay/shop/imgs/shop-small.jpg");
                            stringList.add("https://cdn.colourfulchina.com/alipay/shop/imgs/shop-large.jpg");
                        }else {
                            for (SysFile sysFile : fileList){
                                stringList.add("https://cdn2.colourfulchina.com/upload/"+sysFile.getGuid()+"."+sysFile.getExt());
                            }
                        }
                        gdv.setImgList(stringList);
                        //住宿的根据权益code转换权益展现形式
                        if(GoodsEnums.service.ACCOM.getCode().equals(gdv.getTypeCode()) && StringUtils.isNotEmpty(gdv.getGiftCode())){
                            gdv.setGiftName(GoodsEnums.GiftTypeEnum.getGiftDesc(gdv.getGiftCode()));
                        }
                    }
                    prjGroupVo.setGoodsDetailVoList(goodsDetailVoList);
                }
                projectCdnVo.setPrjGroupVoList(prjGroupVoList);
            }
            result.setResult(projectCdnVoList);
        }catch (Exception e){
            log.error("生成项目详细信息失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        log.info("cdnList耗时:{}-{}",(System.currentTimeMillis()-start),projectIds);
        return result;
    }
    @ApiOperation(value = "生成项目列表简略信息")
    @GetMapping({"/prjList","/prjList/{projectIds}"})
    public CommonResultVo<List<ProjectCdnVo>> prjList(@PathVariable(required = false) String projectIds){
        CommonResultVo<List<ProjectCdnVo>> result = new CommonResultVo<>();
        if (projectIds == null || "".equals(projectIds)){
            result.setCode(300);
            result.setMsg("项目id为空！");
            return result;
        }
        long start =System.currentTimeMillis();
        try {
            String[] strings = projectIds.split(",");
            List<ProjectCdnVo> projectCdnVoList = projectService.selectProjectBriefList(strings);
            for (ProjectCdnVo projectCdnVo : projectCdnVoList){
                //产品组
                PrjGroupVo pgvo = new PrjGroupVo();
                pgvo.setProjectId(projectCdnVo.getId());
                List<PrjGroupVo> prjGroupVoList = prjGroupService.selectPrjGroupList(pgvo);
                projectCdnVo.setPrjGroupVoList(prjGroupVoList);
                //失效日期
                if (StringUtils.isEmpty(projectCdnVo.getExpiryDate())){
                    projectCdnVo.setExpiryDate(projectCdnVo.getUnitExpiry());
                }
            }
            result.setResult(projectCdnVoList);
        }catch (Exception e){
            log.error("生成项目列表简略信息失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        log.info("prjList耗时:{}-{}",(System.currentTimeMillis()-start),projectIds);
        return result;
    }

    @ApiOperation("根据goodsID查询商品详情")
    @GetMapping({"/getGoodsDetail","/getGoodsDetail/{goodsId}"})
    public CommonResultVo<GoodsDetailVo> getGoodsDetail(@PathVariable(required = false) String goodsId){
        CommonResultVo<GoodsDetailVo> result = new CommonResultVo<>();
        if (StringUtils.isBlank(goodsId)){
            result.setCode(300);
            result.setMsg("商品id为空！");
            return result;
        }
        GoodsDetailVo goodsDetailVo = goodsService.getGoodsDetail(goodsId);
        if (goodsDetailVo.getItems()!=null && !"".equals(goodsDetailVo.getItems())){
            List<ShopItem> itemList = shopitemsService.selectByItemIdList(getItemsId(goodsDetailVo.getItems()));
            goodsDetailVo.setItemList(itemList);
            goodsDetailVo.setMinPrice(goodsLowNet(itemList));
            goodsDetailVo.setItemString(itemDetail(itemList));
        }
        List<SysFile> fileList = sysFileService.selectShopPic(goodsDetailVo.getShopId());
        List<String> stringList = Lists.newArrayList();
        if (fileList == null || fileList.size() == 0){
            stringList.add("https://cdn.colourfulchina.com/alipay/shop/imgs/shop-small.jpg");
            stringList.add("https://cdn.colourfulchina.com/alipay/shop/imgs/shop-large.jpg");
        }else {
            for (SysFile sysFile : fileList){
                stringList.add("https://cdn2.colourfulchina.com/upload/"+sysFile.getGuid()+"."+sysFile.getExt());
            }
        }
        goodsDetailVo.setImgList(stringList);
        result.setResult(goodsDetailVo);
        return result;
    }

    /**
     * 组装item白话文
     * @param itemList
     * @return
     */
    private List<ItemNameVo> itemDetail(List<ShopItem> itemList)  {
        List<ItemNameVo> slist = Lists.newArrayList();
        for (ShopItem shopItem : itemList) {
            ItemNameVo itemNameVo = new ItemNameVo();
            List<String> stringList = Lists.newArrayList();
            String minNet = null;
            String minFee = "";
            String minVat = "";
            String[] nets = null;
            String result = "";
            Double netPrice = 0.0;
            Double servicePrice = 0.0;
            Double expensePrice = 0.0;
            itemNameVo.setName(shopItem.getName());
            final String itemPrice = shopItem.getPrice();
            if (StringUtils.isNotBlank(itemPrice)){
                Map<String,Object> itemPriceMap=JSON.parseObject(itemPrice,Map.class);
                final Object net = itemPriceMap.get("net");
                final Object fee = itemPriceMap.get("fee");
                final Object vat = itemPriceMap.get("vat");
                //得到一个shopItem里的净价
                if (net != null && StringUtils.isNotBlank(net.toString())){
                    String netStr=net.toString().trim();
                    if (NumberUtils.isNumber(netStr)){
                        minNet = netStr;
                    }else {
                        nets = netStr.split(";");
                    }
                }
                //组装服务费
                if (fee != null && StringUtils.isNotBlank(fee.toString())){
                    minFee=fee.toString().trim();
                }
                //组装税费
                if (vat != null && "true".equalsIgnoreCase(vat.toString().trim())){
                    minVat = "6%";
                }
            }
            if (minNet != null){
                //result = minNet +(minFee.equals("")?"":"+"+minFee) + (minVat.equals("")?"":"+"+minVat);
                //单人套餐:118+10%+6% 计算价格 118+118 * 0.1 +（118+118 * 0.1）*0.06
                //双人套餐:118/2+10%+6% 计算价格 118/2+118/2 * 0.1 +（118/2+118/2 * 0.1）*0.06
                if(shopItem.getName().contains("双人")){
                    netPrice = Double.valueOf(minNet)/2;
                }else {
                    netPrice = Double.valueOf(minNet);
                }
                servicePrice = minFee.equals("")?servicePrice: NumUtils.parseStringToDouble(minFee);
                expensePrice = minVat.equals("")?expensePrice:NumUtils.parseStringToDouble(minVat);
                double price = netPrice + netPrice * servicePrice + (netPrice + netPrice * servicePrice) * expensePrice;
                //保留两位小数,不四舍五入直接进一位
                BigDecimal bd = new BigDecimal(price).setScale(2, RoundingMode.UP);
                result = bd.doubleValue() + "";
                if (!result.equals("")){
                    result = " "+result+"元/位";
                    result = getResultString(result);
                    stringList.add(result);
                }
            }else if (nets != null && nets.length > 0){
                for (int i = 0;i<nets.length;i++){
                    /*if (i==0){
                        result = nets[i] + (minFee.equals("")?"":"+"+minFee) + (minVat.equals("")?"":"+"+minVat);
                        if (!result.equals("")){
                            result = getResultString(result);
                            stringList.add(result);
                        }
                    }else {
                        result = nets[i] + (minFee.equals("")?"":"+"+minFee) + (minVat.equals("")?"":"+"+minVat);
                        if (!result.equals("")){
                            result = getResultString(result);
                            stringList.add(result);
                        }
                    }*/
                    //result = nets[i] + (minFee.equals("")?"":"+"+minFee) + (minVat.equals("")?"":"+"+minVat);
                    if(nets[i].contains("W") && nets[i].split(":").length >= 2){
                        if(shopItem.getName().contains("双人")){
                            netPrice = Double.valueOf(nets[i].split(":")[1].trim())/2;
                        }else {
                            netPrice = Double.valueOf(nets[i].split(":")[1].trim());
                        }
                        servicePrice = minFee.equals("")?servicePrice: NumUtils.parseStringToDouble(minFee);
                        expensePrice = minVat.equals("")?expensePrice:NumUtils.parseStringToDouble(minVat);
                        double price = netPrice + netPrice * servicePrice + (netPrice + netPrice * servicePrice) * expensePrice;
                        BigDecimal bg = new BigDecimal(price).setScale(2, RoundingMode.UP);
                        result = nets[i].split(":")[0] +" "+ bg.doubleValue();
                    }
                    if (!result.equals("")){
                        result = " "+result+"元/位";
                        result = getResultString(result);
                        stringList.add(result);
                    }
                }
            }
            itemNameVo.setNameDetailList(stringList);
            slist.add(itemNameVo);
        }
        return slist;
    }

    private String getResultString(String result) {
        if (null != result && !"".equals(result)){
            result = result.replaceAll("W1","周一");
            result = result.replaceAll("W2","周二");
            result = result.replaceAll("W3","周三");
            result = result.replaceAll("W4","周四");
            result = result.replaceAll("W5","周五");
            result = result.replaceAll("W6","周六");
            result = result.replaceAll("W7","周日");
            result = result.replaceAll("-","至");
        }
        return result;
    }

    /**
     * 从shopItem列表中找到最低的净价（针对下午茶，单人套餐净价和双人套餐净价一半）
     * @param itemList
     * @return
     */
    private BigDecimal goodsLowNet(List<ShopItem> itemList){
        BigDecimal lowNet = null;
        for (ShopItem shopItem : itemList) {
            BigDecimal minNet = null;
            final String itemPrice = shopItem.getPrice();
            if (StringUtils.isNotBlank(itemPrice)){
                Map<String,Object> itemPriceMap=JSON.parseObject(itemPrice,Map.class);
                final Object net = itemPriceMap.get("net");
                //得到一个shopItem里最小的净价
                if (net != null && StringUtils.isNotBlank(net.toString())){
                    String netStr=net.toString().trim();
                    if (NumberUtils.isNumber(netStr)){
                        minNet = new BigDecimal(netStr);
                    }else {
                        final String[] nets = netStr.split(";");
                        for (String n : nets ){
                            final String[] ns = n.split(":");
                            if (minNet == null){
                                minNet = new BigDecimal(ns[1].trim());
                            }else {
                                if (minNet.compareTo(new BigDecimal(ns[1].trim())) == 1){
                                    minNet = new BigDecimal(ns[1].trim());
                                }
                            }
                        }
                    }
                    //下午茶双人套餐特殊
                    if (shopItem.getType().equals("tea") && shopItem.getName().contains("双人")){
                        minNet = minNet.divide(new BigDecimal(2));
                    }
                    //组装所有item中最小的净价下午茶单人套餐双人套餐立省规则
                    if (lowNet == null){
                        lowNet = minNet;
                    }else {
                        if (lowNet.compareTo(minNet) == 1){
                            lowNet = minNet;
                        }
                    }
                }
            }
        }
        return lowNet;
    }
    /**
     * 从shopItem列表中找到最低的预约价,用于首页多少金额起
     * @param itemList
     * @return
     */
    private BigDecimal goodsMinPrice(List<ShopItem> itemList){
        //组装goods预约的最低价格
        BigDecimal minPrice = null;
        for (ShopItem shopItem : itemList) {
            BigDecimal minNet = null;
            BigDecimal minFee = null;
            BigDecimal minVat = null;
            BigDecimal thisPrice = null;
            final String itemPrice = shopItem.getPrice();
            if (StringUtils.isNotBlank(itemPrice)){
                Map<String,Object> itemPriceMap=JSON.parseObject(itemPrice,Map.class);
                final Object net = itemPriceMap.get("net");
                final Object fee = itemPriceMap.get("fee");
                final Object vat = itemPriceMap.get("vat");
                //得到一个shopItem里最小的净价
                if (net != null && StringUtils.isNotBlank(net.toString())){
                    String netStr=net.toString().trim();
                    if (NumberUtils.isNumber(netStr)){
                        minNet = new BigDecimal(netStr);
                    }else {
                        final String[] nets = netStr.split(";");
                        for (String n : nets ){
                            final String[] ns = n.split(":");
                            if (minNet == null){
                                minNet = new BigDecimal(ns[1].trim());
                            }else {
                                if (minNet.compareTo(new BigDecimal(ns[1].trim())) == 1){
                                    minNet = new BigDecimal(ns[1].trim());
                                }
                            }
                        }
                    }
                }
                //组装服务费
                if (fee != null && StringUtils.isNotBlank(fee.toString())){
                    String feeStr=fee.toString().trim();
                    if (feeStr.endsWith("%")){
                        minFee = new BigDecimal(feeStr.replaceAll("%","")).divide(new BigDecimal(HUNDRED));
                    }else {
                        minFee = new BigDecimal(feeStr);
                    }
                }
                //组装税费
                if (vat != null && "true".equalsIgnoreCase(vat.toString().trim())){
                    minVat = new BigDecimal("0.06");
                }
            }
            if (minNet != null){
                BigDecimal rate = new BigDecimal(1);
                if (minFee != null){
                    rate = rate.add(minFee);
                }
                if (minVat != null){
                    rate = rate.add(minVat);
                }
                //计算此条item的最低价格
                thisPrice = minNet.multiply(rate);

                //计算所有item中的最低价格
                if (minPrice == null){
                    minPrice = thisPrice;
                }else {
                    if (minPrice.compareTo(thisPrice) == 1){
                        minPrice = thisPrice;
                    }
                }
            }
        }
        return minPrice;
    }

    /**
     * 解析goods里的items字段，得到itemId集合
     * @param xmlDoc
     * @return
     */
    public List getItemsId(String xmlDoc) {
        List result = Lists.newArrayList();
        if (xmlDoc != null && !"".equals(xmlDoc)){
            Pattern p = Pattern.compile("\r|\n");
            Matcher m = p.matcher(xmlDoc);
            xmlDoc = m.replaceAll("");
        }
        //创建一个新的字符串
        StringReader read = new StringReader(xmlDoc);
        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
        InputSource source = new InputSource(read);
        //创建一个新的SAXBuilder
        SAXBuilder sb = new SAXBuilder();
        try {
            //通过输入源构造一个Document
            Document doc = sb.build(source);
            //取的根元素
            Element root = doc.getRootElement();
            //得到根元素所有子元素的集合
            List jiedian = root.getChildren();
            //获得XML中的命名空间（XML中未定义可不写）
            Namespace ns = root.getNamespace();
            Element et = null;
            for(int i=0;i<jiedian.size();i++){
                et = (Element) jiedian.get(i);//循环依次得到子元素
                result.add(et.getAttribute("id").getValue());
            }
        } catch (JDOMException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        return result;
    }

    @ApiOperation(value = "获取项目列表")
    @PostMapping("/proList")
    public CommonResultVo<ProjectInfoResVo> projectList(@RequestBody ProjectReqVo reqVo) throws Exception {
    	CommonResultVo<ProjectInfoResVo> crv = new CommonResultVo<ProjectInfoResVo>();
		try {
			ProjectInfoResVo resVo = projectService.getProjectInfo(reqVo);
			crv.setCode(SUCCESS_CODE);
			crv.setMsg(SUCCESS_MESSAGE);
			crv.setResult(resVo);
		} catch (Exception e) {
			crv.setCode(ERROR_CODE);
			crv.setMsg(e.getMessage());
		}
		return crv;
    }
    @ApiOperation(value = "根据项目ID查询项目信息")
    @GetMapping("/getProjectById/{id}")
    public CommonResultVo<Project> getProjectById(@PathVariable Integer id) {
        CommonResultVo<Project> resultVo = new CommonResultVo<>();
        final Project project = projectService.selectById(id);
        resultVo.setResult(project);
        return resultVo;

    }

    @ApiOperation(value = "获取项目列表")
    @GetMapping("/get/id/{projectIds}")
    public CommonResultVo<List<ProjectInfoResVo>> projectById(@PathVariable String  projectIds) throws Exception {
        CommonResultVo crv = new CommonResultVo<ProjectInfoResVo>();
        try {
            List<ProjectInfoResVo> projectInfoResVos = projectService.getProjectInfoByOrderSys(projectIds);
            crv.setCode(SUCCESS_CODE);
            crv.setMsg(SUCCESS_MESSAGE);
            crv.setResult(projectInfoResVos);
        } catch (Exception e) {
            crv.setCode(ERROR_CODE);
            crv.setMsg(e.getMessage());
        }
        return crv;
    }
    @ApiOperation(value = "根据项目ID查询大项目信息")
    @GetMapping("/getBigProjectById/{id}")
    @Cacheable(value = "BigProject",key = "'get_'+#id",unless = "#result == null")
    public CommonResultVo<BigProjectVo> getBigProjectById(@PathVariable Integer id) {
        CommonResultVo<BigProjectVo> resultVo = new CommonResultVo<>();
        try {
            BigProjectVo bigProjectVo=new BigProjectVo();
            final Project project = projectService.selectById(id);
            if (project != null){
                bigProjectVo.setProject(project);

                //查询项目权益包
                Wrapper<PrjPackage> prjPackageWrapper=new Wrapper<PrjPackage>() {
                    @Override
                    public String getSqlSegment() {
                        return "where project_id = " + project.getId();
                    }
                };
                final List<PrjPackage> packageList = prjPackageService.selectList(prjPackageWrapper);
                if (!CollectionUtils.isEmpty(packageList)){
                    bigProjectVo.setPackageList(packageList);
                }

                PrjGroupVo group=new PrjGroupVo();
                group.setProjectId(project.getId());
                final List<PrjGroupVo> groupVoList = prjGroupService.selectPrjGroupList(group);
                if (!CollectionUtils.isEmpty(groupVoList)){
                    for (PrjGroupVo prjGroupVo : groupVoList){
                        PrjGroupGoods prjGroupGoods = new PrjGroupGoods();
                        prjGroupGoods.setGroupId(prjGroupVo.getId());
                        prjGroupGoods.setProjectId(project.getId());
                        List<PrjGroupGoods> prjGroupGoodsList = prjGroupGoodsService.findListByProjectIdAndGroupId(prjGroupGoods);
                        List<GoodsDetailVo> goodsDetailVoList = goodsService.queryGoodsDetail(prjGroupGoodsList);
                        for (GoodsDetailVo gdv: goodsDetailVoList){
                            if(!CollectionUtils.isEmpty(prjGroupGoodsList)){
                                prjGroupGoodsList.forEach(item -> {
                                    if(item.getGoodsId().equals(gdv.getGoodsId())){
                                        gdv.setSort(item.getSort());
                                        gdv.setWeight(item.getWeight());
                                    }
                                });
                            }
                            gdv.setGroupId(prjGroupVo.getId());
                            //item列表和最低净价
                            if (gdv.getItems()!=null && !"".equals(gdv.getItems())){
                                List<ShopItem> itemList = shopitemsService.selectByItemIdList(getItemsId(gdv.getItems()));
                                gdv.setItemList(itemList);
                            }
                        }
                        prjGroupVo.setGoodsDetailVoList(goodsDetailVoList);
                    }
                    bigProjectVo.setGroupVoList(groupVoList);
                }

                //查询项目销售渠道
                ProjectChannel channel=new ProjectChannel();
                channel.setProjectId(project.getId().intValue());
                final List<ProjectChannel> channelList = projectChannelService.selectByProjectId(channel);
//                ProjectChannel channel1=new ProjectChannel();
//                List<ProjectChannel> channelList1 = new ArrayList<ProjectChannel>();
//                //把newchannelid和oldchannelid都放到一起返回并排除根据projectid和channelid查询没有权益的渠道
//                Set<Integer> newChannel =new HashSet<Integer>();
//                    final ListIterator<ProjectChannel> channelListIterator = channelList.listIterator();
//                    while (channelListIterator.hasNext()){
//                        final ProjectChannel channelll = channelListIterator.next();
//                        newChannel.add(channelll.getOldId());
//                        newChannel.add(channelll.getNewId());
//                    }
//                if(!CollectionUtils.isEmpty(newChannel)){
//                        for(Integer i :newChannel){
//                            Map<String,Integer> map = new HashMap<>();
//                            map.put("channelId",i);
//                            map.put("projectId",project.getId().intValue());
//                            int i1 = projectChannelService.selectByProjectIdChannelId(map);
//                            if(i1 > 0 ) {
//                                channel1.setOldId(i);
//                                channel1.setProjectId(project.getId().intValue());
//                                channel1.setNewId(i);
//                                channelList1.add(channel1);
//                            }
//                        }
//                    bigProjectVo.setChannelList(channelList1);
//                }
                if(!CollectionUtils.isEmpty(channelList)){
                    bigProjectVo.setChannelList(channelList);
                }

                //查询项目设置
                Wrapper<ProjectMeta> wrapper=new Wrapper<ProjectMeta>() {
                    @Override
                    public String getSqlSegment() {
                        return "where project_id = "+project.getId();
                    }
                };
                final List<ProjectMeta> metaList = projectMetaService.selectList(wrapper);
                if (!CollectionUtils.isEmpty(metaList)){
                    bigProjectVo.setMetaList(metaList);
                }
            }
            resultVo.setResult(bigProjectVo);
        }catch (Exception e){
            log.error("根据项目ID:{}查询大项目信息",id,e);
        }
        return resultVo;

    }
}



package com.colourfulchina.pangu.taishang.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.bigan.api.entity.PrjPackage;
import com.colourfulchina.bigan.api.entity.Project;
import com.colourfulchina.bigan.api.entity.ProjectChannel;
import com.colourfulchina.bigan.api.feign.RemoteProjectService;
import com.colourfulchina.bigan.api.vo.BigProjectVo;
import com.colourfulchina.bigan.api.vo.GoodsDetailVo;
import com.colourfulchina.bigan.api.vo.PrjGroupVo;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.dto.MatcherGroupDto;
import com.colourfulchina.inf.base.utils.PatternMatcherUtils;
import com.colourfulchina.inf.base.utils.ShortUrlUtil;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.dto.GoodsPortalSettingDto;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.api.enums.ShopTypeEnums;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsChannelRes;
import com.colourfulchina.pangu.taishang.config.GoodsProperties;
import com.colourfulchina.pangu.taishang.mapper.ServiceGiftMapper;
import com.colourfulchina.pangu.taishang.mapper.ShopItemMapper;
import com.colourfulchina.pangu.taishang.service.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/goodsNewInit")
@Slf4j
@Api(tags = {"商品初始化"})
public class GoodsInitNewController {
    private final RemoteProjectService remoteProjectService;
    private final GoodsService goodsService;
    private final GoodsClauseService goodsClauseService;
    private final GoodsDetailService goodsDetailService;
    private final GoodsPortalSettingService goodsPortalSettingService;
    private final SysFileService sysFileService;
    private final ShopItemService shopItemService;
    private final ShopService shopService;
    private final ProductService productService;
    private final ProductGroupService productGroupService;
    private final GoodsProperties goodsProperties;
    private final GoodsChannelsService goodsChannelsService;
    private final CheckDataLogService checkDataLogService;
    private final ProductGroupGiftService productGroupGiftService;
    private  final ProductGroupProductService productGroupProductService;
    private static final Map<String,Integer> supportAuthTypeMap=Maps.newHashMap();
    private static final Map<String,String> oldNewLimitTypeMap=Maps.newHashMap();
    private static final List<String> oldIgnoreGiftList=Lists.newArrayList();
    private static final List<String> specialTypeList=Lists.newArrayList();
    private static final List<Integer> ignoreChannelList=Lists.newArrayList();
    private final ServiceGiftMapper serviceGiftMapper;
    private final ShopItemMapper shopItemMapper;
    static {

        supportAuthTypeMap.put("phone",0);
        supportAuthTypeMap.put("code",1);

        oldNewLimitTypeMap.put("none","none");
        oldNewLimitTypeMap.put("fixed","fixed_times");
        oldNewLimitTypeMap.put("fixed-weight","fixed_point");
        oldNewLimitTypeMap.put("fixed-night","fixed_times");

        oldIgnoreGiftList.add("N1");
        oldIgnoreGiftList.add("N2");
        oldIgnoreGiftList.add("N3");
        oldIgnoreGiftList.add("N4");
        oldIgnoreGiftList.add("NX");

        specialTypeList.add("gym");
        specialTypeList.add("lounge");
        specialTypeList.add("exchange");
        specialTypeList.add("car");
//        specialTypeList.add("car");
        ignoreChannelList.add(136);
        ignoreChannelList.add(197);

    }
    /**
     * 商品初始化
     * @param request
     * @return
     */
    @SysGodDoorLog("商品初始化")
    @GetMapping("/init")
    public CommonResultVo<String> init(String username,String password,Integer projectId,Integer newProjectId,HttpServletRequest request){
        CommonResultVo<String> resultVo=new CommonResultVo<>();
        try {
            Assert.hasText(username,"username不能为空");
            Assert.hasText(password,"password不能为空");
            Assert.isTrue("hyper.huang".equals(username),"username有误");
            Assert.isTrue("1qaz@WSX".equals(password),"password有误");
            Assert.notNull(projectId,"projectId不能为空");
            Assert.notNull(newProjectId,"newProjectId不能为空");
            log.info("GoodsInitController.init projectId {} start:{}",projectId,System.currentTimeMillis());
            final CommonResultVo<BigProjectVo> commonResultVo = remoteProjectService.getBigProjectById(projectId);
            Assert.notNull(commonResultVo,"remoteProjectService.getBigProjectById commonResultVo为空");
            final BigProjectVo bigProjectVo = commonResultVo.getResult();
            Assert.notNull(bigProjectVo,"remoteProjectService.getBigProjectById bigProjectVo为空");
            final Project project = bigProjectVo.getProject();
            Assert.notNull(project,"remoteProjectService.getBigProjectById bigProjectVo project为空");
            Assert.isTrue(supportAuthTypeMap.keySet().contains(project.getAuthType()),"验证方式暂不支持");
            final List<PrjPackage> packageList = bigProjectVo.getPackageList();
            Assert.notEmpty(packageList,"remoteProjectService.getBigProjectById bigProjectVo packageList为空");
            //权益包数量大于1  暂不处理
            //Assert.isTrue(packageList.size()==1,"remoteProjectService.getBigProjectById bigProjectVo packageList大于1");
            final List<ProjectChannel> channelList = bigProjectVo.getChannelList();
            Assert.notNull(channelList,"remoteProjectService.getBigProjectById bigProjectVo channelList为空");
            //排除掉测试渠道(136,197)后，渠道列表大于1  暂不处理
            final ListIterator<ProjectChannel> channelListIterator = channelList.listIterator();
            while (channelListIterator.hasNext()){
                final ProjectChannel channel = channelListIterator.next();
                if (ignoreChannelList.contains(channel.getOldId())){
                    channelListIterator.remove();
                }
            }
            //新项目ID
            Goods goods = goodsService.selectById(newProjectId);
            Assert.notEmpty(packageList,"goods为空");
//            Assert.isTrue(channelList.size()==1,"remoteProjectService.getBigProjectById bigProjectVo channelList不等于1");
            //为了不改动太大  针对多权益包 多销售渠道情况 项目ID为原ID后面补数字从1开始 最大为权益包数量*销售渠道数量 产品全部初始化 不分权益包 后续手工处理
            int copy=packageList.size()*channelList.size();
            for (int i=0;i<packageList.size();i++){
                for (int j=0;j<channelList.size();j++){
//                    int targetGoodsId=projectId;
                    String packageName=null;
                    if (copy>1){
                        int suffix=(i+1)*(j+1);
//                        targetGoodsId=Integer.valueOf(projectId+"50"+suffix);
                        packageName=packageList.get(i).getName();
                    }
                    String oldKey=projectId+"-"+packageList.get(i).getId()+"-"+channelList.get(j).getOldId();
                    log.info("oldKey:{}",oldKey);
                    //添加关联表
                    if(oldKey.equalsIgnoreCase(goods.getOldKey())){
                         initGoodsAllInfo(goods,newProjectId, bigProjectVo, project, channelList.get(j),packageName,oldKey);
                    }
                }
            }

            log.info("GoodsInitController.init projectId {} end:{}",projectId,System.currentTimeMillis());
        }catch (Exception e){
            log.error("商品初始化失败",e);
        }
        return resultVo;
    }

    /**
     * 初始化商品所有信息：商品基本信息 商品产品组、产品  商品 前端设置等(权益有效期、规则、商品图片需手动设置)
     * @param newProjectId
     * @param bigProjectVo
     * @param project
     * @param channel
     * @param packageName
     * @param oldKey 老系统项目id-权益包id-销售渠道id
     * @throws Exception
     */
    private void initGoodsAllInfo(Goods goods,Integer newProjectId,  BigProjectVo bigProjectVo, Project project, ProjectChannel channel,String packageName,String oldKey) throws Exception {
        final List<PrjGroupVo> groupVoList = bigProjectVo.getGroupVoList();
        Assert.notEmpty(groupVoList,"产品组为空");
        String prjMore=project.getMore();
        Map<String,Object> prjMoreMap= Maps.newHashMap();
        if (StringUtils.isNotBlank(prjMore)){
            prjMoreMap= JSON.parseObject(prjMore,Map.class);
        }
        String portal=project.getPortal();
        Map<String,Object> portalMap=Maps.newHashMap();
        if (StringUtils.isNotBlank(portal)){
            portalMap=JSON.parseObject(portal,Map.class);
        }


        //TODO 多权益包时要改造
        //提前查询出所有的新系统shopitem product 再后面循环时可以不再查询
        Wrapper<Shop> shopWrapper=new Wrapper<Shop>() {
            @Override
            public String getSqlSegment() {
                return null;
            }
        };
        final List<Shop> shopList = shopService.selectList(shopWrapper);
        final Map<Integer,Shop> oldShopIdMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(shopList)){
            shopList.forEach(shop -> {
                oldShopIdMap.put(shop.getOldShopId(),shop);
            });
        }
        Wrapper<ShopItem> shopItemWrapper=new Wrapper<ShopItem>() {
            @Override
            public String getSqlSegment() {
                return "where old_item_id <> 0";
            }
        };
        final List<ShopItem> shopItemList = shopItemService.selectList(shopItemWrapper);
        final Map<Integer,ShopItem> oldItemIdMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(shopItemList)){
            shopItemList.forEach(item->{
                oldItemIdMap.put(item.getOldItemId(),item);
            });
        }
        Wrapper<Product> productWrapper=new Wrapper<Product>() {
            @Override
            public String getSqlSegment() {
                return null;
            }
        };
        final List<Product> productList = productService.selectList(productWrapper);
        Map<String,Product> itemGiftProductMap = Maps.newHashMap();
        Map<Integer,Product> oneShopProductMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(productList)){
            productList.forEach(product -> {
                String key = strip2null(product.getShopItemId()+"");
                if (StringUtils.isNotBlank(product.getGift())){
                    key+="-"+product.getGift();
                }
                if (itemGiftProductMap.containsKey(key)){
                    log.error("----------itemGiftProductMap key {} exists----------",key);
                }else {
                    itemGiftProductMap.put(key,product);
                }
                if (!oneShopProductMap.containsKey(product.getShopId())){
                    oneShopProductMap.put(product.getShopId(),product);
                }
            });
        }
        List<ProductGroup> result  =productGroupService.selectByGoodsId(goods.getId());
        Map<Integer,ProductGroup> productGroupMap= result.stream().collect(Collectors.toMap(ProductGroup::getOldId, bookOrder -> bookOrder));
        for (PrjGroupVo groupVo:groupVoList){
            //处理产品组产品列表信息
            final List<GoodsDetailVo> goodsDetailVoList = groupVo.getGoodsDetailVoList();
            if (CollectionUtils.isEmpty(goodsDetailVoList)){
                checkDataLogService.insertCheckDataLog("GOODS-ProductGroup",groupVo.getId()+"",newProjectId+"",groupVo.getProjectId()+"-"+newProjectId,"","当前产品组产品为空","");
                log.error("oldKey {} old group {} not contains product",oldKey,groupVo.getId());
                continue;
            }
            if(!productGroupMap.containsKey(Integer.parseInt(groupVo.getId()+""))){
                checkDataLogService.insertCheckDataLog("GOODS-ProductGroup",groupVo.getId()+"",null,groupVo.getProjectId()+"-"+newProjectId,"","当前产品组产品不存在","");
                log.error("oldKey {} old group {} not contains product",oldKey,groupVo.getId());
                continue;
            }
            ProductGroup productGroup = productGroupMap.get(Integer.parseInt(groupVo.getId()+""));
            List<ProductGroupProduct> productGroupProducts = productGroupProductService.getCostByGroupId(productGroup.getId());
           if(productGroupProducts.size() == goodsDetailVoList.size() || productGroupProducts.size() > goodsDetailVoList.size()){
                    continue;
            }
            Map<Integer,ProductGroupProduct>productGroupProductsMap = productGroupProducts.stream().collect(Collectors.toMap(ProductGroupProduct::getProductId, bookOrder -> bookOrder));

//            Map<Integer,ProductGroupProduct> integerProductGroupProductMap= productGroupProducts.stream().collect(Collectors.toMap(ProductGroupProduct::get, bookOrder -> bookOrder));
            Map<Integer,String> productBlockMap=Maps.newHashMap();
            Map<Integer,BigDecimal> productPointMap=Maps.newHashMap();
            for (GoodsDetailVo detailVo:goodsDetailVoList){
                Product product = null;
                String block = null;
                List<String> blockList=Lists.newArrayList();
                if (StringUtils.isNotBlank(detailVo.getBlockCode())){
                    blockList.add(detailVo.getBlockCode());
                }
                final String serviceCode = detailVo.getTypeCode();
                final String giftCode=detailVo.getGiftCode();

                //如果类型为健身、贵宾厅、接送机 则续特殊处理
                final String items = detailVo.getItems();
                final Long shopId = detailVo.getShopId();
                if (specialTypeList.contains(serviceCode)){
                    if (shopId == null){
                        log.error("oldKey {} old group {} old product {} serviceCode {} old shop is null",oldKey,groupVo.getId(),detailVo.getGoodsId(),serviceCode);
                        continue;
                    }
                    if (!oldShopIdMap.containsKey(Integer.valueOf(shopId.intValue()))){
                        checkDataLogService.insertCheckDataLog("GOODS-Product",groupVo.getId()+"",productGroup.getId()+"",shopId+"-"+items,"","商户不存在","");
                        log.error("oldKey {} old group {} old product {} serviceCode {} old shop {} not find",oldKey,groupVo.getId(),detailVo.getGoodsId(),serviceCode, shopId);
                        continue;
                    }
                    final Shop shop = oldShopIdMap.get(Integer.valueOf(shopId.intValue()));
                    if (!oneShopProductMap.containsKey(shop.getId())){
                        checkDataLogService.insertCheckDataLog("GOODS-Product",groupVo.getId()+"",productGroup.getId()+"",shop.getId()+"-"+items,"","商户不存在","");
                        log.error("oldKey {} old group {} old product {} serviceCode {} old shop {} not find",oldKey,groupVo.getId(),detailVo.getGoodsId(),serviceCode, shopId);
                        continue;
                    }
                    product=oneShopProductMap.get(shop.getId());
                    if (product == null){
                        log.error("oldKey {} old group {} old product {} old shop {} gift {} not find int new product",oldKey,groupVo.getId(),detailVo.getGoodsId(),shopId,detailVo.getGiftCode());
                        checkDataLogService.insertCheckDataLog("GOODS-Product",groupVo.getId()+"",productGroup.getId()+"",shopId+"-"+items,"","产品不存在","");

                        continue;
                    }
                }else {
                    final List<com.colourfulchina.bigan.api.entity.ShopItem> itemList = detailVo.getItemList();
                    if (CollectionUtils.isEmpty(itemList)){
                        log.error("old group {} old product {} not contain items",groupVo.getId(),detailVo.getGoodsId());

                        continue;
                    }
                    //分拆产品:按itemList中的shopItemId不同分拆
                    final Map<String, String> itemsBlockMap = getItemsBlockMap(items);
                    log.info("goods:{} items:{} itemsBlockMap:{} itemsList:{}",detailVo.getGoodsId(),items,JSON.toJSONString(itemsBlockMap),JSON.toJSONString(itemList));
                    for (com.colourfulchina.bigan.api.entity.ShopItem oldShopItem:itemList){
                        if (!oldItemIdMap.containsKey(Integer.valueOf(oldShopItem.getId().intValue()))){

                            checkDataLogService.insertCheckDataLog("GOODS-Product",groupVo.getId()+"",productGroup.getId()+"",shopId+"-"+items,"","产品不存在","");
                            log.error("oldKey {} old group {} old product {} old shop item {} not find int new shop item",oldKey,groupVo.getId(),detailVo.getGoodsId(),oldShopItem.getId());
                            continue;
                        }
                        ShopItem newShopItem=oldItemIdMap.get(Integer.valueOf(oldShopItem.getId().intValue()));
                        String key = strip2null(newShopItem.getId()+"");
                        if (StringUtils.isNotBlank(giftCode) && !oldIgnoreGiftList.contains(giftCode)){
                            key+="-"+giftCode;
                        }
                        if (serviceCode.equals("drink")){
                            key+="-F1";
                        }
                        if (!itemGiftProductMap.containsKey(key)){
                            checkDataLogService.insertCheckDataLog("GOODS-Product",groupVo.getId()+"",productGroup.getId()+"",key +"-"+shopId+"-"+items,"","产品不存在","");
                            log.error("oldKey {} old group {} old product {} old shop item {} gift {} not find int new product",oldKey,groupVo.getId(),detailVo.getGoodsId(),oldShopItem.getId(),detailVo.getGiftCode());
                            continue;
                        }
                        product = itemGiftProductMap.get(key);
                        if (product == null){
                            checkDataLogService.insertCheckDataLog("GOODS-Product",groupVo.getId()+"",productGroup.getId()+"",key +"-"+shopId,"","产品不存在",items);
                            log.error("oldKey {} old group {} old product {} old shop {} gift {} not find int new product",oldKey,groupVo.getId(),detailVo.getGoodsId(),shopId,detailVo.getGiftCode());
                            continue;
                        }
                        //设置block信息
                        if (!CollectionUtils.isEmpty(itemsBlockMap) && itemsBlockMap.containsKey(strip2null(oldShopItem.getId()+""))){
                            String blockString = itemsBlockMap.get(strip2null(oldShopItem.getId()+""));
                            if (StringUtils.isNotBlank(blockString)){
                                String[] arrBlock = blockString.split(",");
                                if (ArrayUtils.isNotEmpty(arrBlock)){
                                    for (String s : arrBlock) {
                                        if (!"null".equals(s)){
                                            blockList.add(s);
                                        }
                                    }
                                }
                            }
                        }
                        if (!CollectionUtils.isEmpty(blockList)){
                            block=String.join(",",blockList);
                        }
                        if (product == null){
                            checkDataLogService.insertCheckDataLog("GOODS-Product",groupVo.getId()+"",productGroup.getId()+"",shopId +"","","产品不存在",items);
                            log.error("oldKey {} old group {} old product {} old shop {} gift {} not find int new product",oldKey,groupVo.getId(),detailVo.getGoodsId(),shopId,detailVo.getGiftCode());
                            continue;
                        }
                        productBlockMap.put(product.getId(),block);
                        productPointMap.put(product.getId(),detailVo.getWeight() == null?BigDecimal.ONE:detailVo.getWeight());
                    }
                }

                if(productGroupProductsMap.containsKey(product.getId())){
                    continue;
                }
                Integer sort = productGroupProductService.queryMaxSort();
                if (sort == null){
                    sort = 0;
                }
                Wrapper sgWrapper = new Wrapper() {
                    @Override
                    public String getSqlSegment() {
                        return "where del_flag = 0 and service_id = '"+ShopTypeEnums.ACCOM.getCode()+"'";
                    }
                };
                List<ServiceGift> sgList = serviceGiftMapper.selectList(sgWrapper);
                String accomGift = null;
                //住宿类型的权益类型取产品组的权益类型
                Wrapper gWrapper = new Wrapper() {
                    @Override
                    public String getSqlSegment() {
                        return "where del_flag = 0 and product_group_id =" + productGroup.getId();
                    }
                };
                List<ProductGroupGift> giftList = productGroupGiftService.selectList(gWrapper);
                for (ServiceGift serviceGift : sgList) {
                    boolean flag = false;
                    for (ProductGroupGift productGroupGift : giftList) {
                        if (serviceGift.getGiftId().equals(productGroupGift.getGift())){
                            accomGift = serviceGift.getGiftId();
                            flag = true;
                            break;
                        }
                    }
                    if (flag == true){
                        break;
                    }
                }

                ShopItem shopItem = shopItemMapper.selectById(product.getShopItemId());

                ProductGroupProduct productGroupProduct = new ProductGroupProduct();
                productGroupProduct.setProductGroupId(productGroup.getId());
                productGroupProduct.setProductId(product.getId());
                productGroupProduct.setDelFlag(DelFlagEnums.NORMAL.getCode());
                productGroupProduct.setSort(sort+1);
                BigDecimal point=new BigDecimal(1);
                if (!CollectionUtils.isEmpty(productPointMap) && productPointMap.containsKey(product.getId())){
                    point=productPointMap.get(product.getId());
                }
                productGroupProduct.setPoint(point);
                if (ShopTypeEnums.ACCOM.getCode().equals(shopItem.getServiceType())){
                    productGroupProduct.setGift(accomGift);
                }else {
                    productGroupProduct.setGift(product.getGift());
                }
                productGroupProduct.setBlockRule(productBlockMap.get(product.getId()));
                productGroupProduct.setStatus(product.getStatus());
                productGroupProduct.setCreateUser(SecurityUtils.getLoginName());
                productGroupProductService.insert(productGroupProduct);

            }


        }
    }

    private static Map<String,String> getItemsBlockMap(String items){
        Map<String,String> result= Maps.newHashMap();
        if (StringUtils.isNotBlank(items)){
            String[] itemArr=items.split("item");
            if (itemArr != null & itemArr.length > 0){
                for (String item:itemArr){
                    final MatcherGroupDto matcher = PatternMatcherUtils.getMatcher("id=\"(\\d+)\".*block=\"(.*)\"", item);
                    log.debug(JSON.toJSONString(matcher));
                    if (matcher != null && StringUtils.isNotBlank(matcher.getGroup1()) && StringUtils.isNotBlank(matcher.getGroup2())){
                        result.put(matcher.getGroup1(),matcher.getGroup2());
                    }
                }
            }
        }
        return result;
    }
    /**
     * 获取银行code
     * @param portalSettingDto
     * @throws Exception
     */
    private void setBankCodeAndGiftUrl(GoodsPortalSettingDto portalSettingDto) throws Exception {
        Assert.notNull(portalSettingDto,"getBankCode参数不能为空");
        Assert.notNull(portalSettingDto.getGoodsId(),"getBankCode goodsId参数不能为空");
        final List<GoodsChannelRes> goodsChannelResList = goodsChannelsService.selectGoodsChannel(portalSettingDto.getGoodsId());
        Assert.notEmpty(goodsChannelResList,"商品未设置销售渠道");
        Assert.isTrue(goodsChannelResList.size() == 1,"商品不能有多个销售渠道");
        GoodsChannelRes goodsChannelRes=goodsChannelResList.get(0);
        portalSettingDto.setBankCode(goodsChannelRes.getBankCode());
        portalSettingDto.setGiftUrl(goodsProperties.getGiftUrl()+goodsChannelRes.getBankCode()+"&prjCode=");
    }

    private void setGiftShortUrl(GoodsPortalSetting goodsPortalSetting) throws Exception{
        Assert.notNull(goodsPortalSetting,"参数不能为空");
        Assert.notNull(goodsPortalSetting.getGoodsId(),"参数goodsId不能为空");
        Assert.hasText(goodsPortalSetting.getCode(),"参数code不能为空");
        GoodsPortalSettingDto goodsPortalSettingDto=new GoodsPortalSettingDto();
        BeanUtils.copyProperties(goodsPortalSetting,goodsPortalSettingDto);
        setBankCodeAndGiftUrl(goodsPortalSettingDto);
        final String shortUrl2 = ShortUrlUtil.getKlfShortUrl(goodsPortalSettingDto.getGiftFullUrl());
        goodsPortalSetting.setShortUrl(shortUrl2);
    }
    private static String strip2null(String input){
        final boolean blank = StringUtils.isBlank(input);
        if (blank){
            return null;
        }
        final boolean equalsIgnoreCase = "null".equalsIgnoreCase(input);
        if (equalsIgnoreCase){
            return null;
        }
        return input;
    }
}

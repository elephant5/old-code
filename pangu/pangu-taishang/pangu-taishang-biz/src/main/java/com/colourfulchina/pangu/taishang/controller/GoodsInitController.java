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
import com.colourfulchina.inf.base.dto.MatcherGroupDto;
import com.colourfulchina.inf.base.utils.PatternMatcherUtils;
import com.colourfulchina.inf.base.utils.ShortUrlUtil;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.dto.GoodsPortalSettingDto;
import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.colourfulchina.pangu.taishang.api.vo.req.GroupAddProductAndBlockReq;
import com.colourfulchina.pangu.taishang.api.vo.req.GroupSaveReq;
import com.colourfulchina.pangu.taishang.api.vo.req.ListSysFileReq;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsChannelRes;
import com.colourfulchina.pangu.taishang.config.GoodsProperties;
import com.colourfulchina.pangu.taishang.service.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/GoodsInit")
@Slf4j
@Api(tags = {"商品初始化"})
public class GoodsInitController {
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
    private static final Map<String,Integer> supportAuthTypeMap=Maps.newHashMap();
    private static final Map<String,String> oldNewLimitTypeMap=Maps.newHashMap();
    private static final List<String> oldIgnoreGiftList=Lists.newArrayList();
    private static final List<String> specialTypeList=Lists.newArrayList();
    private static final List<Integer> ignoreChannelList=Lists.newArrayList();
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
    public CommonResultVo<String> init(String username,String password,Integer projectId,HttpServletRequest request){
        CommonResultVo<String> resultVo=new CommonResultVo<>();
        try {
            Assert.hasText(username,"username不能为空");
            Assert.hasText(password,"password不能为空");
            Assert.isTrue("hyper.huang".equals(username),"username有误");
            Assert.isTrue("1qaz@WSX".equals(password),"password有误");
            Assert.notNull(projectId,"projectId不能为空");
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
            Assert.notEmpty(channelList,"remoteProjectService.getBigProjectById bigProjectVo channelList为空");
            //排除掉测试渠道(136,197)后，渠道列表大于1  暂不处理
            final ListIterator<ProjectChannel> channelListIterator = channelList.listIterator();
            while (channelListIterator.hasNext()){
                final ProjectChannel channel = channelListIterator.next();
                if (ignoreChannelList.contains(channel.getOldId())){
                    channelListIterator.remove();
                }
            }
//            Assert.isTrue(channelList.size()==1,"remoteProjectService.getBigProjectById bigProjectVo channelList不等于1");
            //为了不改动太大  针对多权益包 多销售渠道情况 项目ID为原ID后面补数字从1开始 最大为权益包数量*销售渠道数量 产品全部初始化 不分权益包 后续手工处理
            int copy=packageList.size()*channelList.size();
            int suffix = 0;
            for (int i=0;i<packageList.size();i++){
                for (int j=0;j<channelList.size();j++){
                    suffix++;
                    String tempStr = "";
                    int targetGoodsId=projectId;
                    String packageName=null;
                    if (copy>1){
                        int needSize = 6 - (projectId+"").length() - (suffix+"").length();
                        for (int h = 0;h<needSize;h++){
                            tempStr += "0";
                        }
                        targetGoodsId=Integer.valueOf(projectId+tempStr+suffix);
                        packageName=packageList.get(i).getName();
                    }
                    if(copy==1){
                        targetGoodsId = Integer.valueOf(projectId);
                    }
                    String oldKey=projectId+"-"+packageList.get(i).getId()+"-"+channelList.get(j).getOldId();
                    //添加关联表
                    //goodsService.insertSyncGoodsCode(projectId, targetGoodsId, packageList.get(i).getId(), channelList.get(j).getOldId(), channelList.get(j).getOldId());
                    initGoodsAllInfo(targetGoodsId, supportAuthTypeMap, bigProjectVo, project, channelList.get(j),packageName,oldKey);
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
     * @param targetGoodsId
     * @param supportAuthTypeMap
     * @param bigProjectVo
     * @param project
     * @param channel
     * @param packageName
     * @param oldKey 老系统项目id-权益包id-销售渠道id
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void initGoodsAllInfo(Integer targetGoodsId, Map<String, Integer> supportAuthTypeMap, BigProjectVo bigProjectVo, Project project, ProjectChannel channel,String packageName,String oldKey) throws Exception {
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
        List<GoodsClause> clauseList= Lists.newArrayList();
        GoodsPortalSetting portalSetting=null;
        //前面验证通过后
        GoodsBaseVo goods=new GoodsBaseVo();
        goods.setId(targetGoodsId);
        goods.setGoodsId(targetGoodsId);
        goods.setName(project.getName());
        goods.setShortName(project.getShortName());
        goods.setOldKey(oldKey);
        if (StringUtils.isNotBlank(packageName)){
            goods.setShortName(project.getShortName());
        }
        goods.setAuthType(strip2null(supportAuthTypeMap.get(project.getAuthType())+""));
        goods.setUpstartTime(project.getStartDate());
        goods.setDownEndTime(project.getEndDate());
        //TODO 需要重新确认
        goods.setExpiryDate(project.getExpiryDate());
        goods.setExpiryValue(project.getUnitExpiry());
        if (!CollectionUtils.isEmpty(prjMoreMap)){
            if (prjMoreMap.containsKey("hotline")){
                goods.setHotline(strip2null(prjMoreMap.get("hotline")+""));
            }
            if (prjMoreMap.containsKey("mc")){
                Map<String,Object> prjMoreMcMap=JSON.parseObject(strip2null(prjMoreMap.get("mc")+""),Map.class);
                if (!CollectionUtils.isEmpty(prjMoreMcMap)){
                    if (prjMoreMcMap.containsKey("bank")){
                        goods.setCustomSource(strip2null(prjMoreMcMap.get("bank")+""));
                    }
                    if (prjMoreMcMap.containsKey("validate_tip")){
                        goods.setVerifyReminder(strip2null(prjMoreMcMap.get("validate_tip")+""));
                    }
                }
            }
            if (prjMoreMap.containsKey("tips")){
                goods.setCallReminder(strip2null(prjMoreMap.get("tips")+""));
            }
            if (prjMoreMap.containsKey("limit")){
                Map<String,Object> prjMoreLimitMap=JSON.parseObject(strip2null(prjMoreMap.get("limit")+""),Map.class);
                if (!CollectionUtils.isEmpty(prjMoreLimitMap)){
                    if (prjMoreLimitMap.containsKey("max_night")){
                        if (StringUtils.isNotBlank(strip2null(prjMoreLimitMap.get("max_night")+""))){
                            goods.setMaxNight(NumberUtils.toInt(strip2null(prjMoreLimitMap.get("max_night")+"")));
                        }
                    }
                    if (prjMoreLimitMap.containsKey("no_superposition")){
                        goods.setSuperposition(strip2null(prjMoreLimitMap.get("no_superposition")+""));
                    }

                    if (prjMoreLimitMap.containsKey("book_after_leave")){
//                            goods.setAccomAfterLeave(strip2null(prjMoreLimitMap.get("book_after_leave")+""));
                        goods.setSingleThread(strip2null(prjMoreLimitMap.get("book_after_leave")+""));
                    }
                    if (prjMoreLimitMap.containsKey("single_thread")){
                        goods.setSingleThread(strip2null(prjMoreLimitMap.get("single_thread")+""));
                    }
                    if (prjMoreLimitMap.containsKey("enable_max_night")){
                        goods.setEnableMaxNight(strip2null(prjMoreLimitMap.get("enable_max_night")+""));
                    }
                    if (prjMoreLimitMap.containsKey("OK365")){
                        goods.setAllYear(strip2null(prjMoreLimitMap.get("OK365")+""));
                    }
                    if (prjMoreLimitMap.containsKey("disable_call_book")){
                        goods.setDisableCall(strip2null(prjMoreLimitMap.get("disable_call_book")+""));
                    }
                    if (prjMoreLimitMap.containsKey("disable_call_book")){
                        goods.setDisableCall(strip2null(prjMoreLimitMap.get("disable_call_book")+""));
                    }
                }
            }
        }
        goods.setDisableWechat(project.getDisableWxBook().compareTo(0)==0?"false":"true");
        goods.setRemark(project.getNotes());
        if (!CollectionUtils.isEmpty(portalMap)){
            if (portalMap.containsKey("lock_book_phone")){
                goods.setOnlySelf(strip2null(portalMap.get("lock_book_phone")+""));
            }
            if (portalMap.containsKey("summary")){
                goods.setDetail(strip2null(portalMap.get("summary")+""));
            }
            if (portalMap.containsKey("title") || portalMap.containsKey("code")
                    || portalMap.containsKey("logo") ||portalMap.containsKey("theme")
                    || portalMap.containsKey("mobile")){
                portalSetting=new GoodsPortalSetting();
                portalSetting.setGoodsId(targetGoodsId);
                if (portalMap.containsKey("title")){
                    portalSetting.setTitle(strip2null(portalMap.get("title")+""));
                }
                if (portalMap.containsKey("code")){
                    String code = strip2null(portalMap.get("code")+"");
                    if (StringUtils.isNotBlank(packageName)){
                        code+="-"+targetGoodsId;
                    }
                    portalSetting.setCode(code);
                }
                if (portalMap.containsKey("logo")){
                    String logo=strip2null(portalMap.get("logo")+"");
                    String logoCode=logo.substring(0,logo.lastIndexOf(".svg"));;
                    if (!logo.endsWith("svg")){
                        logoCode+="-white";
                    }
                    String path="svg/logo/";
                    ListSysFileReq fileReq=new ListSysFileReq();
                    fileReq.setType("bank.logo");
                    fileReq.setGuid(path + logoCode);
                    final List<SysFileDto> fileDtoList = sysFileService.listSysFileDtoByGuid(fileReq);
                    if (CollectionUtils.isEmpty(fileDtoList)){
                        log.error("old goods {} logo {} not find",targetGoodsId,logo);
                    }else {
                        final SysFileDto fileDto=fileDtoList.get(0);
                        portalSetting.setBankLogoId(fileDto.getObjId());
                    }
                }
                if (portalMap.containsKey("theme")){
//                        portalSetting.setThemeId(strip2null(portalMap.get("theme")+""));
                }
                if (portalMap.containsKey("mobile")){
                    String mobile=strip2null(portalMap.get("mobile")+"");
                    Map<String,Object> mobileMap=JSON.parseObject(mobile,Map.class);
                    if (!CollectionUtils.isEmpty(mobileMap)){
                        if (mobileMap.containsKey("style")){
                            Map<String,Object> styleMap=JSON.parseObject(strip2null(mobileMap.get("style")+""),Map.class);
                            if (styleMap.containsKey("bodyFill")){
                                portalSetting.setStyleBodyFill(strip2null(styleMap.get("bodyFill")+""));
                            }
                            if (styleMap.containsKey("bodyText")){
                                portalSetting.setStyleBodyText(strip2null(styleMap.get("bodyText")+""));
                            }
                            if (styleMap.containsKey("btnFill")){
                                portalSetting.setStyleBtnFill(strip2null(styleMap.get("btnFill")+""));
                            }
                            if (styleMap.containsKey("btnText")){
                                portalSetting.setStyleBtnText(strip2null(styleMap.get("btnText")+""));
                            }
                            if (styleMap.containsKey("enableBgImage")){
                                portalSetting.setStyleEnableBgImage(BooleanUtils.toInteger(BooleanUtils.toBoolean(strip2null(styleMap.get("enableBgImage")+""))));
                            }
                            if (styleMap.containsKey("bgImage")){
                                portalSetting.setStyleBgImage(strip2null(styleMap.get("bgImage")+""));
                            }
                            if (styleMap.containsKey("fontSize")){
                                portalSetting.setStyleFontSize(strip2null(styleMap.get("fontSize")+""));
                            }
                            if (styleMap.containsKey("linkText")){
                                portalSetting.setStyleLinkText(strip2null(styleMap.get("linkText")+""));
                            }
                        }
                    }
                }
            }
            if (portalMap.containsKey("clause")){
                String clauseStr=strip2null(portalMap.get("clause")+"");
                Map<String,Object> clauseMap=JSON.parseObject(clauseStr,Map.class);
                if (!CollectionUtils.isEmpty(clauseMap)){
                    clauseMap.forEach((type,value)->{
                        GoodsClause goodsClause=new GoodsClause();
                        goodsClause.setGoodsId(targetGoodsId);
                        goodsClause.setClauseType(type.equalsIgnoreCase("main")?"total":type);
                        goodsClause.setClause(strip2null(value+""));
                        clauseList.add(goodsClause);
                    });
                }
            }
        }


        //保存商品基本信息
        goodsService.insertGoodsAndSetting(goods);

        //保存销售渠道、商品详情
        goodsService.insertOrUpdateAnotherData(goods,channel.getOldId());

        //保存使用限制
        if (!CollectionUtils.isEmpty(clauseList)){
            goodsClauseService.insertBatch(clauseList);
        }

        //保存前端设置
        if (portalSetting != null){
            setGiftShortUrl(portalSetting);
            goodsPortalSettingService.insert(portalSetting);
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


        for (PrjGroupVo groupVo:groupVoList){
            final Set<String> serviceTypeSet= Sets.newHashSet();
            final Set<String> giftTypeSet= Sets.newHashSet();
            //先处理产品组信息并保存 后处理产品组产品列表信息
            GroupSaveReq groupSaveReq = new GroupSaveReq();
            groupSaveReq.setGoodsId(targetGoodsId);
            groupSaveReq.setName(groupVo.getTitle());
            groupSaveReq.setOldId(groupVo.getId().intValue());
            final String groupVoDefine = groupVo.getDefine();
            if (StringUtils.isNotBlank(groupVoDefine)){
                Map<String,Object> groupVoDefineMap=JSON.parseObject(groupVoDefine,Map.class);
                if (!CollectionUtils.isEmpty(groupVoDefineMap)){
                    if (groupVoDefineMap.containsKey("notes")){
                        groupSaveReq.setShortName(strip2null(groupVoDefineMap.get("notes")+""));
                    }
                    //TODO 点数 次数 不限外  其他都不支持
                    if (groupVoDefineMap.containsKey("limit_type")){
                        String limit_type=strip2null(groupVoDefineMap.get("limit_type")+"");
                        if (oldNewLimitTypeMap.containsKey(limit_type)){
                            groupSaveReq.setUseLimitId(oldNewLimitTypeMap.get(limit_type));
                        }else {
                            log.error("产品组 {}:{} {} 使用限制不支持",groupVo.getId(),groupVo.getTitle(),groupVo.getLimitType());
                        }
                    }
                    if (groupVoDefineMap.containsKey("times")){
                        groupSaveReq.setUseNum(new BigDecimal(strip2null(groupVoDefineMap.get("times")+"")));
                    }
                    if (groupVoDefineMap.containsKey("filter")){
                        Map<String,Object> groupFilterMap=JSON.parseObject(strip2null(groupVoDefineMap.get("filter")+""),Map.class);
//                        if (groupFilterMap.containsKey("type")){
//                            groupSaveReq.setService(new String[]{strip2null(groupFilterMap.get("type")+"")});
//                        }
//                        if (groupFilterMap.containsKey("gift")){
//                            List<String> groupFilterGiftList=JSON.parseArray(strip2null(groupFilterMap.get("gift")+""),String.class);
//                            if (!CollectionUtils.isEmpty(groupFilterGiftList)){
//                                String[] giftArr=new String[groupFilterGiftList.size()];
//                                giftArr=groupFilterGiftList.toArray(giftArr);
//                                groupSaveReq.setGift(giftArr);
//                            }
//                        }
                    }

                }
            }


            //处理产品组产品列表信息
            final List<GoodsDetailVo> goodsDetailVoList = groupVo.getGoodsDetailVoList();
            if (CollectionUtils.isEmpty(goodsDetailVoList)){
                log.error("oldKey {} old group {} not contains product",oldKey,groupVo.getId());
                continue;
            }

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
                serviceTypeSet.add(serviceCode);
                giftTypeSet.add(giftCode);
                //如果类型为健身、贵宾厅、接送机 则续特殊处理
                final String items = detailVo.getItems();
                final Long shopId = detailVo.getShopId();
                if (specialTypeList.contains(serviceCode)){
                    if (shopId == null){
                        log.error("oldKey {} old group {} old product {} serviceCode {} old shop is null",oldKey,groupVo.getId(),detailVo.getGoodsId(),serviceCode);
                        continue;
                    }
                    if (!oldShopIdMap.containsKey(Integer.valueOf(shopId.intValue()))){
                        log.error("oldKey {} old group {} old product {} serviceCode {} old shop {} not find",oldKey,groupVo.getId(),detailVo.getGoodsId(),serviceCode, shopId);
                        continue;
                    }
                    final Shop shop = oldShopIdMap.get(Integer.valueOf(shopId.intValue()));
                    if (!oneShopProductMap.containsKey(shop.getId())){
                        log.error("oldKey {} old group {} old product {} serviceCode {} old shop {} not find",oldKey,groupVo.getId(),detailVo.getGoodsId(),serviceCode, shopId);
                        continue;
                    }
                    product=oneShopProductMap.get(shop.getId());
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
                            giftTypeSet.add("F1");
                        }
                        if (!itemGiftProductMap.containsKey(key)){
                            log.error("oldKey {} old group {} old product {} old shop item {} gift {} not find int new product",oldKey,groupVo.getId(),detailVo.getGoodsId(),oldShopItem.getId(),detailVo.getGiftCode());
                            continue;
                        }
                        product = itemGiftProductMap.get(key);
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
                            log.error("oldKey {} old group {} old product {} old shop {} gift {} not find int new product",oldKey,groupVo.getId(),detailVo.getGoodsId(),shopId,detailVo.getGiftCode());
                            continue;
                        }
                        productBlockMap.put(product.getId(),block);
                        productPointMap.put(product.getId(),detailVo.getWeight() == null?BigDecimal.ONE:detailVo.getWeight());
                    }
                }

            }
            //保存产品组
            if (!CollectionUtils.isEmpty(serviceTypeSet)){
                String[] serviceArr=new String[serviceTypeSet.size()];
                serviceTypeSet.toArray(serviceArr);
                groupSaveReq.setService(serviceArr);
            }
            if (!CollectionUtils.isEmpty(giftTypeSet)){
                String[] giftArr=new String[giftTypeSet.size()];
                giftTypeSet.toArray(giftArr);
                groupSaveReq.setGift(giftArr);
            }
            log.info("groupSaveReq:{}",JSON.toJSONString(groupSaveReq));
            ProductGroup productGroup = productGroupService.save(groupSaveReq);
            if (productGroup == null){
                log.error("保存产品组失败,old group:{}",groupVo.getId());
                continue;
            }
            log.info("oldKey {} old group {} new group {}",oldKey,groupVo.getId(),productGroup.getId());
            //保存产品组对应产品
            if (CollectionUtils.isEmpty(productBlockMap)){
                log.error("oldKey {} old group {} new group {} not contains value",oldKey,groupVo.getId(),productGroup.getId());
                continue;
            }
            GroupAddProductAndBlockReq groupAddProductReq=new GroupAddProductAndBlockReq();
            groupAddProductReq.setProductGroupId(productGroup.getId());
            groupAddProductReq.setProductBlockMap(productBlockMap);
            groupAddProductReq.setProductPointMap(productPointMap);
            productGroupService.groupAddProductAndBlock(groupAddProductReq);
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

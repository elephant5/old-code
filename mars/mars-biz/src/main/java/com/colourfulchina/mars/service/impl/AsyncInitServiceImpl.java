package com.colourfulchina.mars.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.bigan.api.entity.Project;
import com.colourfulchina.bigan.api.feign.RemoteProjectService;
import com.colourfulchina.inf.base.utils.GoodsRulesUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.*;
import com.colourfulchina.mars.api.enums.GiftCodeDetailTypeEnum;
import com.colourfulchina.mars.api.enums.HxCodeStatusEnum;
import com.colourfulchina.mars.api.enums.PayOrderStatusEnum;
import com.colourfulchina.mars.service.*;
import com.colourfulchina.mars.service.sync.BookOrderService;
import com.colourfulchina.mars.utils.CodeUtils;
import com.colourfulchina.member.api.req.MemLoginReqDTO;
import com.colourfulchina.member.api.res.MemLoginResDTO;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.api.enums.GiftTypeEnum;
import com.colourfulchina.pangu.taishang.api.enums.ResourceTypeEnums;
import com.colourfulchina.pangu.taishang.api.feign.RemoteGoodsService;
import com.colourfulchina.pangu.taishang.api.feign.RemoteProductGroupService;
import com.colourfulchina.pangu.taishang.api.feign.RemoteShopService;
import com.colourfulchina.pangu.taishang.api.utils.GoodsUtils;
import com.colourfulchina.pangu.taishang.api.vo.GroupProductVo;
import com.colourfulchina.pangu.taishang.api.vo.RepeatInfoVo;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsChannelRes;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsGroupListRes;
import com.colourfulchina.yangjian.api.entity.*;
import com.colourfulchina.yangjian.api.entity.GiftUnit;
import com.colourfulchina.yangjian.api.feign.RemoteBookOrderService;
import com.colourfulchina.yangjian.api.feign.RemoteGiftUnitService;
import com.colourfulchina.yangjian.api.feign.RemoteGiftUnitUseService;
import com.colourfulchina.yangjian.api.vo.BookOrderReqVo;
import com.colourfulchina.yangjian.api.vo.BookOrderResultVo;
import com.colourfulchina.yangjian.api.vo.GiftTimesVo;
import com.colourfulchina.yangjian.api.vo.ReservOrderPriceVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class AsyncInitServiceImpl implements AsyncInitService {

    private final RemoteProjectService remoteProjectService;
    private final RemoteGiftUnitService remoteGiftUnitService;
    private final RemoteBookOrderService remoteBookOrderService;

    private final GiftCodeService giftCodeService;
    @Autowired
    private final GiftUnitService giftUnitService;
    private final EquityCodeDetailService equityCodeDetailService;
    private final RemoteGoodsService remoteGoodsService;
    private final RemoteProductGroupService remoteProductGroupService;

    private static final Map<String,Integer> supportAuthTypeMap=Maps.newHashMap();
    private static final Map<String,String> oldNewLimitTypeMap=Maps.newHashMap();
    private static final Map<Integer,GoodsGroupListRes> oldNewGroupMap=Maps.newHashMap();
    private static final List<String> oldIgnoreGiftList= Lists.newArrayList();
    private static final List<String> specialTypeList=Lists.newArrayList();
    private static final List<Integer> ignoreChannelList=Lists.newArrayList();

    @Autowired
    MemberInterfaceService memberInterfaceService;
    @Autowired
    PanguInterfaceService panguInterfaceService;

    private final RemoteGiftUnitUseService remoteGiftUnitUseService;

    private final RemoteShopService remoteShopService;

    @Autowired
    ReservOrderPriceService reservOrderPriceService;
    @Autowired
    ReservOrderDetailService reservOrderDetailService;

    @Autowired
    ReservCodeService reservCodeService;

    @Autowired
    ReservOrderService reservOrderService;

    @Autowired
    private BookOrderService bookOrderService;

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

    @Override
    @Async("taskExecutorPool")
    public void giftInit (String username, String password, Integer projectId, Integer packageId, Integer channelId, Integer goodsId, Integer oldUnitId,Integer state,Integer start, Integer stop) throws Exception {
        //TODO 增加激活码的出库时间和作废时间
        Assert.hasText(username,"username不能为空");
        Assert.hasText(password,"password不能为空");
        Assert.isTrue("hyper.huang".equals(username),"username有误");
        Assert.isTrue("1qaz@WSX".equals(password),"password有误");
        Assert.notNull(projectId,"项目id不能为空");
        Assert.notNull(packageId,"权益包id不能为空");
        Assert.notNull(channelId,"销售渠道id不能为空");
        Assert.notNull(goodsId,"新商品id不能为空");
        //Assert.notNull(state,"激活码状态不能为空");
        final CommonResultVo<Project> projectResultVo = remoteProjectService.getProjectById(projectId);
        Assert.notNull(projectResultVo,"查询项目信息失败");
        final Project project = projectResultVo.getResult();
        Assert.notNull(project,"项目信息为空");
        final CommonResultVo<Goods> goodsResultVo = remoteGoodsService.findGoodsById(goodsId);
        Assert.notNull(goodsResultVo,"查询新商品失败");
        final Goods goods = goodsResultVo.getResult();
        Assert.notNull(goods,"查询新商品为空");
        String oldKey=projectId+"-"+packageId+"-"+channelId;
        Assert.isTrue(goods.getOldKey().equals(oldKey),"传入的新商品id不正确");

        Assert.isTrue(supportAuthTypeMap.keySet().contains(project.getAuthType()),"项目验证类型不支持");
        GiftUnit giftUnit=new GiftUnit();
        giftUnit.setProjectId(projectId);
        giftUnit.setPackageId(packageId);
        giftUnit.setChannelId(channelId);
        giftUnit.setVtype(project.getAuthType());
        giftUnit.setUnitId(oldUnitId);
        giftUnit.setState(state);
        giftUnit.setStart(start);
        giftUnit.setStop(stop);
//            giftUnit.setCode("gift_unit");
        final CommonResultVo<List<GiftUnit>> unitListResultVo = remoteGiftUnitService.list(giftUnit);
        log.info("giftUnit{}",giftUnit);
        log.info("unitListResultVo{}",unitListResultVo.getResult());
        Assert.notNull(unitListResultVo,"权益列表查询失败");
        final List<GiftUnit> unitList = unitListResultVo.getResult();
        Assert.notEmpty(unitList,"权益列表为空");
        final CommonResultVo<List<GoodsGroupListRes>> goodsGroupResultVo = remoteProductGroupService.selectGoodsGroup(goodsId);
        Assert.notNull(goodsGroupResultVo,"查询产品组失败");
        final List<GoodsGroupListRes> groupListResList = goodsGroupResultVo.getResult();
        Assert.notNull(groupListResList,"查询产品组为空");
        groupListResList.forEach(group->{
            final Integer oldId = group.getOldId();
            if (oldId != null){
                oldNewGroupMap.put(oldId,group);
            }
        });
        GoodsBaseVo goodsBaseVo = panguInterfaceService.selectGoodsById(goodsId);
        //商品的产品列表
        List<GoodsGroupListRes> goodsGroupListRes = panguInterfaceService.selectGoodsGroup(goodsId);
        log.info("goodsGroupListRes:{}", JSON.toJSONString(goodsGroupListRes));
        //循环初始化权益信息
        for (GiftUnit unit:unitList){
            EntityWrapper<GiftCode> local  = new EntityWrapper<>();
            local.eq("unit_id",unit.getUnitId());
            GiftCode giftCode = giftCodeService.selectOne(local);
            if(null == giftCode){
                giftCode=new GiftCode();
            }
            giftCode.setUpdateTime(unit.getUpdateTime());
            giftCode.setUnitId(unit.getUnitId());
            if (unit.getLogId() != null){
                giftCode.setCodeBatchNo(unit.getLogId().toString());
            }
            giftCode.setActReturnDate(unit.getActReturnDate());//退货时间
            giftCode.setActObsoleteDate(unit.getActObsoleteDate());//作废时间
            giftCode.setActCodeStatus(unit.getState());
            giftCode.setActCode(unit.getCode());
            giftCode.setActOutDate(unit.getEnableDate());//出库时间
            //TODO 需要做转换
            giftCode.setActRule(GoodsRulesUtils.changeRules(unit.getUnitExpiry()));
            GoodsBaseVo goodsBaseVo1 = new GoodsBaseVo();
            goodsBaseVo1.setExpiryValue(giftCode.getActRule());
            goodsBaseVo1 = GoodsUtils.analyCodeDate(goodsBaseVo1,null,giftCode.getActOutDate());
            giftCode.setActExpireTime(unit.getExpiryDate());

            giftCode.setActEndTime(StringUtils.isNotBlank(goodsBaseVo1.getActiveDate()) ? DateUtil.parse(goodsBaseVo1.getActiveDate()):null);
            giftCode.setActCodeTime(unit.getActivationDate());

            //TODO 调用盘古接口 根据项目id-权益包id-销售渠道id
            giftCode.setGoodsId(goodsId);
            giftCode.setSalesChannelId(Integer.parseInt(goodsBaseVo.getSalesChannelId()));
            GiftName giftName = null;
            if (StringUtils.isNotBlank(unit.getPhone())){
                CommonResultVo<GiftName> giftNameCommonResultVo = remoteGiftUnitService.selectMemberById(unit.getPhone());
                if(null != giftNameCommonResultVo && giftNameCommonResultVo.getResult() != null) {
                    giftName = giftNameCommonResultVo.getResult();
                }
            }
            //TODO 调用member获取
            if(unit.getMemberId()  !=null  && unit.getVtype().equalsIgnoreCase("code")){
                CommonResultVo<GiftMember> giftMemberCommonResultVo = remoteGiftUnitService.selectGiftMemberById(unit.getMemberId());
                if(null != giftMemberCommonResultVo && giftMemberCommonResultVo.getResult() != null){
                    GiftMember giftMember = giftMemberCommonResultVo.getResult();
                    MemLoginReqDTO memMemberInfo = new MemLoginReqDTO();
                    if(StringUtils.isNotBlank(unit.getPhone())){
                        memMemberInfo.setMobile(unit.getPhone());
                    }else{
                        memMemberInfo.setMobile(giftMember.getPhone());
                    }
                    //优先从giftName中设置姓名
                    if (giftName == null){
                        memMemberInfo.setName(giftMember.getName());
                    }else {
                        memMemberInfo.setName(giftName.getName());
                    }
                    memMemberInfo.setAcChannel(goodsBaseVo.getBankCode());
                    memMemberInfo.setSmsCode("colour");
                    MemLoginResDTO member =  memberInterfaceService.memberLogin(memMemberInfo);
                    if(member == null){
                        log.error("权益会员注册失败:{}", JSON.toJSONString(unit));
                        continue;
                    }
                    giftCode.setBuyMemberId(member.getAcid());
                    giftCode.setMemberId(member.getAcid());
                }
            }
            if(unit.getVtype().equalsIgnoreCase("phone")){
                giftCode.setActCode(CodeUtils.generateSimpleCodeNo(giftCode.getId().longValue(),CodeUtils.CodeTypeNo.ACTIVATION_CODE));
                if(giftName != null){
                    MemLoginReqDTO memMemberInfo = new MemLoginReqDTO();
                    memMemberInfo.setMobile(unit.getPhone());
                    memMemberInfo.setName(giftName.getName());
                    memMemberInfo.setAcChannel(goodsBaseVo.getBankCode());
                    memMemberInfo.setSmsCode("colour");
                    MemLoginResDTO member =  memberInterfaceService.memberLogin(memMemberInfo);
                    if(member == null){
                        log.error("权益会员注册失败:{}", JSON.toJSONString(unit));
                        continue;
                    }
                    giftCode.setBuyMemberId(member.getAcid());
                    giftCode.setMemberId(member.getAcid());
                }
            }
//                giftCode.setTags(unit.getTag() == null ? "已迁移" : unit.getTag()+",已迁移");
//                giftCode.setRemarks();
//                giftCode.setOutRemarks();
//                giftCode.setReturnRemarks();
//                giftCode.setObsoleteRemarks();
            giftCode.setDelFlag(0);
            giftCode.setCreateTime(unit.getCreateTime());
//                giftCode.setCreateUser();
//                giftCode.setUpdateTime();
//                giftCode.setUpdateUser();
            if(null != giftCode.getId()){
//                    continue;
                final boolean insertCode = giftCodeService.updateById(giftCode);
                if (!insertCode){
                    log.error("插入激活码失败:{}",JSON.toJSONString(unit));
                    continue;
                }
            }else{
                final boolean insertCode = giftCodeService.insert(giftCode);
                if (!insertCode){
                    log.error("插入激活码失败:{}",JSON.toJSONString(unit));
                    continue;
                }
            }

            //把id更新成unit_id
//                final Integer unitId=giftCode.getUnitId();
//                giftCode.setId(unitId);
//                Wrapper<GiftCode> coderapper=new Wrapper<GiftCode>() {
//                    @Override
//                    public String getSqlSegment() {
//                        return "where unit_id = " + unitId;
//                    }
//                };
//                final boolean update = giftCodeService.updateGiftCode(giftCode);
//                if (!update){
//                    log.error("更新giftCode id失败:{}");
//                }
            final Integer unitId=giftCode.getUnitId();
            final boolean update = giftCodeService.updateIdByUnitId(giftCode);
            if (!update){
                log.error("更新giftCode id失败:{}",unitId);
                continue;
            }

            //TODO 如果商品的激活码类型是手机号，或者卡bin的话 需要做另外的逻辑 并且如果是手机号码 则还是给他发mars系统的激活码。
            giftCode.setId(unitId);
            //TODO 查询权益使用情况

            EntityWrapper<EquityCodeDetail> detail = new EntityWrapper<>();
            detail.eq("gift_code_id",giftCode.getId());
            equityCodeDetailService.delete(detail);
            BookOrderReqVo bookOrderReqVo = new BookOrderReqVo();
            bookOrderReqVo.setProjectId(projectId);
            bookOrderReqVo.setPackageId(unit.getPackageId());
            bookOrderReqVo.setUnitId(unit.getUnitId());
            CommonResultVo<List<GiftTimesVo>>  queryUnitGroups = remoteBookOrderService.queryUnitGroups(bookOrderReqVo);
            Map<Integer, GiftTimesVo> giftUnitUseMap = Maps.newHashMap();
            if(null != queryUnitGroups && null != queryUnitGroups.getResult() ){
                giftUnitUseMap.putAll(queryUnitGroups.getResult().stream().collect(Collectors.toMap(GiftTimesVo::getId, obj -> obj)));
            }
            for(GoodsGroupListRes vo : goodsGroupListRes){
                GiftTimesVo giftUnitUse = giftUnitUseMap.get(vo.getOldId());
                EquityCodeDetail codeDetail=new EquityCodeDetail();
                codeDetail.setGiftCodeId(giftCode.getId());
                codeDetail.setGoodsId(goodsId);
                codeDetail.setProductGroupId(vo.getId());
                codeDetail.setMemberId(giftCode.getMemberId());
                if(vo.getUseNum() != null){
                    codeDetail.setTotalCount(vo.getUseNum().intValue());
                }
                codeDetail.setType(0);
                //TODO 通过老的产品组id 从oldNewGroupMap获取新的产品组信息
                if(giftUnitUse != null ){
                    codeDetail.setUseCount(giftUnitUse.getPrv_cost());
                }else{
                    codeDetail.setUseCount(0);
                }
                final boolean insertDetail = equityCodeDetailService.insert(codeDetail);
                if (!insertDetail){
                    log.error("插入权益使用情况失败:{}",JSON.toJSONString(unit));
                }
            }
        }
    }

    @Override
    public void bookInit(String username, String password, Integer projectId, Integer goodsId,Integer orderId) throws Exception {
        Assert.hasText(username,"username不能为空");
        Assert.hasText(password,"password不能为空");
        Assert.isTrue("hyper.huang".equals(username),"username有误");
        Assert.isTrue("1qaz@WSX".equals(password),"password有误");
        Assert.notNull(projectId,"项目id不能为空");
        Assert.notNull(goodsId,"新商品id不能为空");

        final CommonResultVo<GoodsBaseVo> goodsResultVo = remoteGoodsService.selectById(goodsId);
        Assert.notNull(goodsResultVo,"查询新商品失败");
        final GoodsBaseVo goodsBaseVo = goodsResultVo.getResult();
        Assert.notNull(goodsBaseVo,"查询新商品为空");

        PgSyncInfo pgSyncInfo = new PgSyncInfo();
        pgSyncInfo.setObjId(projectId);
        pgSyncInfo.setTableName("book_order");
        pgSyncInfo.setSecId(orderId);
        log.info("pgSyncInfo:{}",JSON.toJSONString(pgSyncInfo));
        // 从查理系统获取预约单数据
        CommonResultVo<List<BookOrderResultVo>> booksResultVo =  remoteBookOrderService.selectBookOrderByProject(pgSyncInfo);
        // 从拷贝到mars系统获取预约单数据
//        CommonResultVo<List<BookOrderResultVo>> booksResultVo =  bookOrderService.selectBookOrderByProject(pgSyncInfo);
        Assert.notNull(booksResultVo,"查询预约单为空");
        final List<BookOrderResultVo> result = booksResultVo.getResult();
        Assert.notEmpty(result,"查询预约单结果为空");
        log.info("booksResultVo.getResult size:{}", result.size());
        //提前查询出所有的新系统shopitem product 再后面循环时可以不再查询
        final List<ShopChannel>  shopChannelList = remoteShopService.selectChannelList().getRecords();

        Map<Integer, Integer> shopChannelMap = Maps.newHashMap();
        //shopChannelList.stream().collect(Collectors.toMap(ShopChannel::getOldId, bank -> bank.getId(), (e1, e2) -> e1));
        for(ShopChannel sh : shopChannelList){
            if(null != sh.getOldId()){
                shopChannelMap.put(sh.getOldId(),sh.getId());
            }
        }
        final List<Shop> shopList = remoteShopService.selectShops().getRecords();
        final Map<Integer,Shop> oldShopIdMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(shopList)){
            shopList.forEach(shop -> {
                oldShopIdMap.put(shop.getOldShopId(),shop);
            });
        }
        final List<RepeatInfoVo> repeatInfoVos = remoteShopService.getRepeatInfoVo().getRecords();
        final Map<Integer,Integer> delItemIdMap = Maps.newHashMap();
        final Map<Integer,Integer> delProductMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(repeatInfoVos)){
            repeatInfoVos.forEach(vo -> {
                delItemIdMap.put(vo.getDelShopItemId(),vo.getKeepShopItemId());
                delProductMap.put(vo.getDelProductId(),vo.getKeepProductId());
            });
        }
        final List<ShopItem> shopItemList = remoteShopService.getShopItemList().getRecords();
        final Map<Integer,ShopItem> oldItemIdMap = Maps.newHashMap();
        final Map<Integer,ShopItem> noItemMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(shopItemList)){
            shopItemList.forEach(item->{
                oldItemIdMap.put(item.getOldItemId(),item);
                if(item.getServiceType().equalsIgnoreCase(ResourceTypeEnums.DRINK.getCode()) || item.getServiceType().equalsIgnoreCase(ResourceTypeEnums.GYM.getCode())){
                    noItemMap.put(item.getShopId(),item);
                }
            });
        }
        CommonResultVo<List<GoodsGroupListRes>> listCommonResultVo = remoteProductGroupService.selectGoodsGroup(goodsId);
        List<GoodsGroupListRes> goodsGroupListRes = listCommonResultVo.getRecords();
        int index =0;
        ReservOrder tempReservOrder = null;
        for(BookOrderResultVo vo  : result) {
            try {
                log.info("for id:{}",vo.getId());
                EntityWrapper<ReservOrder> old = new EntityWrapper<>();
                old.eq("old_order_id", vo.getId());
                ReservOrder reservOrder = reservOrderService.selectOne(old);
                if (reservOrder == null ) {
                    reservOrder = new ReservOrder();
                }
//                    ReservOrder reservOrder = new ReservOrder();
                reservOrder.setUpdateTime(new Date());
                reservOrder.setProseStatus("" + vo.getBookState());
                reservOrder.setOldOrderId(vo.getId());
                if (vo.getType().equalsIgnoreCase(ResourceTypeEnums.DRINK.getCode())) {
                    reservOrder.setServiceType(vo.getType());
                    reservOrder.setGiftType(GiftTypeEnum.F1.getCode());
                } else {
                    reservOrder.setServiceType(vo.getType());
                    reservOrder.setGiftType(vo.getGift());
                }
                reservOrder.setTags(vo.getTags() + ",");
                reservOrder.setOldOrderId(vo.getId());
                reservOrder.setGoodsId(goodsId);
                reservOrder.setIntereUsageCount(vo.getBookQty());//权益使用次数
                if (StringUtils.isNotBlank(vo.getPhone())) {
                    MemLoginReqDTO memMemberInfo = new MemLoginReqDTO();
                    memMemberInfo.setMobile(vo.getPhone());
                    memMemberInfo.setName(vo.getName());
                    memMemberInfo.setAcChannel(goodsBaseVo.getBankCode());
                    memMemberInfo.setSmsCode("colour");
                    MemLoginResDTO member = memberInterfaceService.memberLogin(memMemberInfo);
                    if (null != member) {
                        reservOrder.setMemberId(member.getAcid());
                    }
                }
                reservOrder.setSalesChannleId(Integer.parseInt(goodsBaseVo.getSalesChannelId()));//
                reservOrder.setReservNumber(vo.getBookNumber());
                reservOrder.setOrderCreator(vo.getOperator());
                reservOrder.setOperator(vo.getOperator());
                reservOrder.setGiftName(vo.getName());
                reservOrder.setGiftPeopleNum(vo.getPeople());
                reservOrder.setGiftPhone(vo.getPhone());
                reservOrder.setGiftTime(vo.getBookTime());
                reservOrder.setGiftDate(vo.getBookDate() == null ? null : DateUtil.format(vo.getBookDate(), "yyyy-MM-dd"));
                reservOrder.setExchangeNum(vo.getBookQty());
                reservOrder.setReservDate(vo.getBookDate());
                reservOrder.setCreateTime(vo.getOrderDate());
                reservOrder.setPayStatus(PayOrderStatusEnum.PREPAID.getCode());
//                    reservOrder.setPayAmount(vo.getShopSettleAmount());
                // TODO 去他的日志表中查询失败的或者成功日志。
                //
                EntityWrapper<GiftCode> codelocal = new EntityWrapper<>();
                codelocal.eq("unit_id", vo.getUnitId());
                List<GiftCode> codes = giftCodeService.selectList(codelocal);
                reservOrder.setGiftCodeId(CollectionUtils.isEmpty(codes) ? null : codes.get(0).getId());
                //
                if (oldShopIdMap.containsKey(vo.getShopId())) {
                    reservOrder.setShopId(oldShopIdMap.get(vo.getShopId()).getId());
                    ;//商户id
                }

                if (oldItemIdMap.containsKey(vo.getItemId())) {
                    ShopItem item = oldItemIdMap.get(vo.getItemId());
                    if(item.getDelFlag().compareTo(1) == 0 ){
                        //TODO
                        if(delItemIdMap.containsKey(item.getId())){
                            reservOrder.setShopItemId(delItemIdMap.get(item.getId()));
                        }

                    }else{
                        reservOrder.setShopItemId(item.getId());
                    }
                    ;//商户id
                }
                //根据新的goodsId+老的groupId = 新的 product_groupId
                //产品ID = shopId + shopItemId + gift
                for (GoodsGroupListRes res : goodsGroupListRes) {
                    List<GroupProductVo> groupProductList = res.getGroupProductList();
                    for (GroupProductVo groupProductVo : groupProductList) {
                        String gift = GiftTypeEnum.findByCode(reservOrder.getGiftType()).getName();
                        String serviceType = ResourceTypeEnums.findByCode(reservOrder.getServiceType()).getName();

                        if (reservOrder.getServiceType().equalsIgnoreCase(ResourceTypeEnums.SPA.getCode())) {// 单杯
                            if (groupProductVo.getShopId().equals(reservOrder.getShopId()) && reservOrder.getShopItemId().equals(groupProductVo.getShopItemId())) {
                                if(delProductMap.containsKey(groupProductVo.getProductId())){
                                    reservOrder.setProductId(delProductMap.get(groupProductVo.getProductId()));
                                }else{
                                    reservOrder.setProductId(groupProductVo.getProductId());//产品id
                                }
                                reservOrder.setProductGroupId(res.getId());//	产品组id
                                reservOrder.setProductGroupProductId(groupProductVo.getId());//	产品组产品id
                                break;
                            }
                        }else   if (reservOrder.getServiceType().equalsIgnoreCase(ResourceTypeEnums.ACCOM.getCode())) {// 单杯
                            if (groupProductVo.getShopId().equals(reservOrder.getShopId()) && reservOrder.getShopItemId().equals(groupProductVo.getShopItemId())) {
                                if(delProductMap.containsKey(groupProductVo.getProductId())){
                                    reservOrder.setProductId(delProductMap.get(groupProductVo.getProductId()));
                                }else{
                                    reservOrder.setProductId(groupProductVo.getProductId());//产品id
                                }
                                reservOrder.setProductGroupId(res.getId());//	产品组id
                                reservOrder.setProductGroupProductId(groupProductVo.getId());//	产品组产品id
                                break;
                            }
                        }else  if (reservOrder.getServiceType().equalsIgnoreCase(ResourceTypeEnums.DRINK.getCode()) || reservOrder.getServiceType().equalsIgnoreCase(ResourceTypeEnums.GYM.getCode())) {// 单杯
                            if (groupProductVo.getService().contains(serviceType) &&  groupProductVo.getGift().equalsIgnoreCase(GiftTypeEnum.F1.getName()) && groupProductVo.getShopId().equals(reservOrder.getShopId())
                            ) {
                                ShopItem newShopItem  = noItemMap.get(reservOrder.getShopId());
                                reservOrder.setShopItemId( newShopItem== null? null:newShopItem .getId());
                                if(delProductMap.containsKey(groupProductVo.getProductId())){
                                    reservOrder.setProductId(delProductMap.get(groupProductVo.getProductId()));
                                }else{
                                    reservOrder.setProductId(groupProductVo.getProductId());//产品id
                                }
                                reservOrder.setProductGroupId(res.getId());//	产品组id
                                reservOrder.setProductGroupProductId(groupProductVo.getId());//	产品组产品id
                                break;
                            }
                        } else if (reservOrder.getServiceType().equalsIgnoreCase(ResourceTypeEnums.BUFFET.getCode())
                                || reservOrder.getServiceType().equalsIgnoreCase(ResourceTypeEnums.SETMENU.getCode())
                                || reservOrder.getServiceType().equalsIgnoreCase(ResourceTypeEnums.TEA.getCode())
                        ){
                            //自助餐 下午茶 定制套餐 还有一个餐型需要匹配
//                            log.info(reservOrder.getShopId() + "--" + reservOrder.getShopItemId()+"--" + serviceType + "--" + gift);
//                            log.info(groupProductVo.getShopId() + "--" + groupProductVo.getShopItemId() + "--"  +groupProductVo.getService()+ "--" + groupProductVo.getGift()  );
                            if (groupProductVo.getShopId().equals(reservOrder.getShopId()) && groupProductVo.getShopItemId().equals(reservOrder.getShopItemId())) {
                                if (groupProductVo.getService().contains(serviceType) &&
                                        groupProductVo.getGift().contains(gift)) {
                                    if(delProductMap.containsKey(groupProductVo.getProductId())){
                                        reservOrder.setProductId(delProductMap.get(groupProductVo.getProductId()));
                                    }else{
                                        reservOrder.setProductId(groupProductVo.getProductId());//产品id
                                    }

                                    reservOrder.setProductGroupId(res.getId());//	产品组id
                                    reservOrder.setProductGroupProductId(groupProductVo.getId());//	产品组产品id
                                    break;
                                }
                            }

                        }
                        //drink没有gift
                    }
                }
//                    ShopDetailRes shopDetailRes = panguInterfaceService.getShopDetail(reservOrder.getShopId());
                reservOrder.setShopChannelId(shopChannelMap.get(vo.getChannelId()) == null? -1:shopChannelMap.get(vo.getChannelId()) );
                if (reservOrder.getProductId() == null) {
                    reservOrder.setProductGroupId(tempReservOrder == null ? null : tempReservOrder.getProductGroupId());//	产品组id
                    reservOrder.setProductGroupProductId(null);//	产品组产品id
                    Product product = new Product();
                    product.setShopId(reservOrder.getShopId());
                    product.setShopItemId(reservOrder.getShopItemId());
                    product.setGift(reservOrder.getGiftType());
                    if (reservOrder.getServiceType().equalsIgnoreCase(ResourceTypeEnums.GYM.getCode()) ||
                            reservOrder.getServiceType().equalsIgnoreCase(ResourceTypeEnums.DRINK.getCode())) {// 单杯
                        product.setGift(GiftTypeEnum.F1.getCode());
                    }
                    if (reservOrder.getServiceType().equalsIgnoreCase(ResourceTypeEnums.ACCOM.getCode())) {// 住宿
                        product.setGift(null);
                    }
                    if (reservOrder.getServiceType().equalsIgnoreCase(ResourceTypeEnums.SPA.getCode())) {// SPA
                        product.setGift(GiftTypeEnum.D5.getCode());
                    }
                    log.info("product param:{}",JSON.toJSONString(product));
                    product = panguInterfaceService.getProductByShop(product);
                    log.info("product result:{}",JSON.toJSONString(product));
                    if (null != product) {
                        if(delProductMap.containsKey(product.getId())){
                            reservOrder.setProductId(delProductMap.get(product.getId()));
                        }else{
                            reservOrder.setProductId(product.getId());
                        }

                    } else {
                        log.error("没找到酒店的预约单：{}", JSON.toJSONString(vo));
                        continue;
                    }

                }
                reservOrder.setMoveType(1);
                if (null == reservOrder.getId()) {
                    final boolean insert1 = reservOrderService.insert(reservOrder);
                    if (!insert1) {
                        log.error("插入预约信息失败", JSON.toJSONString(reservOrder));
                        continue;
                    }
                } else {
                    final boolean update = reservOrderService.updateById(reservOrder);
                    if (!update) {
                        log.error("更新预约信息失败", JSON.toJSONString(reservOrder));
                        continue;
                    }
                }
                final Integer oldOrderId = reservOrder.getOldOrderId();
                final boolean update1 = reservOrderService.updateIdByOldOrderId(reservOrder);
                if (!update1) {
                    log.error("更新reservOrder id:{}失败", oldOrderId);
                }
                reservOrder.setId(reservOrder.getOldOrderId());
                tempReservOrder = reservOrder;
                //详细信息
                ReservOrderDetail detail = reservOrderDetailService.selectOneReservOrderDetail(reservOrder.getId());
                if (null == detail) {
                    detail = new ReservOrderDetail();
                }
                detail.setOrderId(reservOrder.getId());
                detail.setProductType(vo.getType());
                detail.setGoodsId(goodsId);
                detail.setCheckDate(DateUtil.formatDate(vo.getBookDate()));
                detail.setDeparDate(vo.getLeaveDate() == null ? null : DateUtil.formatDate(vo.getLeaveDate()));
                detail.setNightNumbers(vo.getBookDays());
                detail.setCheckNight(vo.getBookQty());
                detail.setAccoNedds(vo.getNeeds());
                detail.setAccoAddon(vo.getAddon());
                detail.setBookName(vo.getName());
                detail.setBookPhone(vo.getPhone());
                detail.setPayAmoney(vo.getShopSettleAmount());
                detail.setPaymentAmount(vo.getShopSettleAmount());
                if (null == detail.getId()) {
                    final boolean insert2 = reservOrderDetailService.insert(detail);
                    if (!insert2) {
                        log.error("插入预约详情失败:{}", JSON.toJSONString(detail));
                    }
                } else {
                    final boolean insert2 = reservOrderDetailService.updateById(detail);
                }
                if (null != vo.getCode()) {
                    GiftActivateCode code = vo.getCode();
                    ReservCode reservCode = reservCodeService.selectOneReservCode(reservOrder.getId());
                    if (null == reservCode) {
                        reservCode = new ReservCode();
                    }
                    reservCode.setOrderId(reservOrder.getId());
                    if (code.getState().compareTo(1) == 0 ) {
                        reservCode.setVarStatus(HxCodeStatusEnum.HxCodeStatus.USED.getIndex() + "");
                    } else if (code.getState().compareTo(2) == 0) {
                        reservCode.setVarStatus(HxCodeStatusEnum.HxCodeStatus.INVALID.getIndex() + "");
                    } else if (code.getState().compareTo(3) == 0 ) {
                        reservCode.setVarStatus(HxCodeStatusEnum.HxCodeStatus.OVERTIME.getIndex() + "");
                    } else {
                        reservCode.setVarStatus(HxCodeStatusEnum.HxCodeStatus.NEWONE.getIndex() + "");
                    }
                    reservCode.setVarCrtTime(code.getUsedTime());
                    reservCode.setCreateTime(code.getCreateTime());
                    reservCode.setVarCode(code.getCode());
                    reservCode.setVarUseTime(code.getUsedTime());
                    reservOrder.setChannelNumber(code.getCode());//核销码
                    reservCode.setVarCrtTime(DateUtil.parse(reservOrder.getGiftDate(), DatePattern.NORM_DATE_PATTERN));
                    Calendar c = Calendar.getInstance();
                    c.setTime(reservCode.getVarCrtTime());
                    c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                            23, 59, 59);
                    reservCode.setVarExpireTime(c.getTime());
                    reservCode.setProductGroupId(reservOrder.getProductGroupId());
                    reservCode.setOldOrderId(code.getOrderId());
                    if (null == reservCode.getId()) {
                        final boolean insert = reservCodeService.insert(reservCode);
                        if (!insert) {
                            log.error("插入核销码失败:{}", JSON.toJSONString(reservCode));
                        }
                    } else {
                        final boolean insert = reservCodeService.updateById(reservCode);
                        if (!insert) {
                            log.error("插入核销码失败:{}", JSON.toJSONString(reservCode));
                        }
                    }

                    reservOrder.setReservCodeId(reservCode.getId());
                }

                //价格信息
                List<BookOrderPrice> prices = vo.getPrices();
                if (!prices.isEmpty()) {
                    CommonResultVo<ReservOrderPriceVo>  priceResult = null;
//                        CommonResultVo<ShopDetailRes> result = null;
                    while (true){
                        try {
                            priceResult =    remoteBookOrderService.priceInfo(vo.getId()) ;
                            if(priceResult == null){
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                            }else{
                                break;
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage(),e);
                        }
                    }
//                        return result.getResult() == null?null: result.getResult();
                    ReservOrderPriceVo reservOrderPriceVo = priceResult == null? null: priceResult.getResult();
                    EntityWrapper<ReservOrderPrice> local = new EntityWrapper<>();
                    local.eq("reserv_order_id", reservOrder.getId());
                    reservOrderPriceService.delete(local);
                    if (null != reservOrderPriceVo && null != reservOrderPriceVo.getNumber()) {
                        log.info("GiftUnitUseLog:{}==={}",reservOrder.getId(),reservOrderPriceVo);
                        reservOrder.setIntereUsageCount(new BigDecimal(reservOrderPriceVo.getNumber()).intValue());
                    } else {
//                            reservOrder.setIntereUsageCount(1);
                        if(vo.getUnitId() !=null ){
                            GiftUnitUseLog giftUnitUseLog = new GiftUnitUseLog();
                            giftUnitUseLog.setProjectId(projectId);
                            giftUnitUseLog.setGoodsId(vo.getGoodsId());
                            giftUnitUseLog.setOrderId(vo.getId());
                            giftUnitUseLog.setUnitId(vo.getUnitId());
                            CommonResultVo<List<GiftUnitUseLog>> resultVo1  = remoteGiftUnitUseService.newlist(giftUnitUseLog);
                            if(null == resultVo1 && resultVo1.getResult()  == null){
                                reservOrder.setIntereUsageCount(1);
                            }else{
                                log.info("GiftUnitUseLog:{}==={}",reservOrder.getId(),resultVo1);
                                List<GiftUnitUseLog> temp  = resultVo1.getRecords();
                                BigDecimal tatol = BigDecimal.ZERO;
                                for(GiftUnitUseLog item : temp){
                                    tatol = tatol.add(item.getWeight() == null ? new BigDecimal("1"): item.getWeight());
                                } ;
                                reservOrder.setIntereUsageCount(tatol.intValue());
                            }
                        }else{
                            reservOrder.setIntereUsageCount(1);
                        }
                    }
                    reservOrder.setExchangeNum(reservOrder.getIntereUsageCount());
                    //TODO
                    BigDecimal temp = BigDecimal.ZERO;
                    for (BookOrderPrice price : prices) {
                        ReservOrderPrice newPrice = new ReservOrderPrice();
                        if (null != reservOrderPriceVo) {
                            newPrice.setNetPrice(reservOrderPriceVo.getNetRrice());
                            newPrice.setServiceRate(reservOrderPriceVo.getServiceRate());
                            newPrice.setSettleRule(reservOrderPriceVo.getSettleRule());
                            newPrice.setProtocolPrice(reservOrderPriceVo.getProtocolPrice() == null ? BigDecimal.ZERO : new BigDecimal(reservOrderPriceVo.getProtocolPrice()));
                        }
                        newPrice.setReservOrderId(reservOrder.getId());
                        newPrice.setPrice(price.getPrice() == null ? BigDecimal.ZERO : price.getPrice());
                        newPrice.setNumber(vo.getBookQty() == null ? 1 : vo.getBookQty());
                        newPrice.setOrderDate(DateUtil.formatDate(price.getDate()));
                        newPrice.setSettleMethod(vo.getShopSettleMethod());
                        newPrice.setCurrency(vo.getShopSettleCurrency());
                        final boolean insert = reservOrderPriceService.insert(newPrice);
                        if (!insert) {
                            log.error("插入预约结算信息失败:{}", JSON.toJSONString(newPrice));
                        }
                        temp = temp.add(newPrice.getPrice().multiply(new BigDecimal(newPrice.getNumber())));
                    }
                    reservOrder.setOrderSettleAmount(temp);
                    reservOrder.setOrderDamageAmount((vo.getShopSettleAmount() == null ? BigDecimal.ZERO : vo.getShopSettleAmount()).subtract(temp));
                    reservOrder.setShopAmount(reservOrder.getOrderSettleAmount().add(reservOrder.getOrderDamageAmount()));
                }else{
                    ReservOrderPrice newPrice = new ReservOrderPrice();
                    newPrice.setReservOrderId(reservOrder.getId());
                    newPrice.setPrice(BigDecimal.ZERO );
                    newPrice.setNumber(vo.getBookQty() == null ? 1 : vo.getBookQty());
                    newPrice.setOrderDate(DateUtil.formatDate(vo.getBookDate()));
                    newPrice.setSettleMethod(vo.getShopSettleMethod());
                    newPrice.setCurrency(vo.getShopSettleCurrency());
                    newPrice.setProtocolPrice(BigDecimal.ZERO);
                    final boolean insert = reservOrderPriceService.insert(newPrice);
                    if (!insert) {
                        log.error("插入预约结算信息失败:{}", JSON.toJSONString(newPrice));
                    }
                }
                final boolean update = reservOrderService.updateById(reservOrder);
                if (!update) {
                    log.error("更新预约单失败:{}", JSON.toJSONString(reservOrder));
                }
                index++;
            }catch(Exception e){
                log.error(e.getMessage(),e);
            }
        }
    }

    @Override
    public void giftMemberSet() throws Exception {
        //查询激活和用完状态的老系统的所有激活码
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where member_id is null and act_code_status IN (2,3) AND unit_id IS NOT NULL";
            }
        };
        List<GiftCode> giftCodeList = giftCodeService.selectList(wrapper);
        Map<Integer,String> bankCodeMap = Maps.newHashMap();
//        && giftCodeList.size()==7543
        if (!CollectionUtils.isEmpty(giftCodeList)){
            for (GiftCode giftCode : giftCodeList) {
                log.info("本次恢复的权益为：{}",JSON.toJSONString(giftCode));
                if (giftCode.getMemberId() == null){
                    String bankCode = null;
                    if (StringUtils.isNotBlank(bankCodeMap.get(giftCode.getSalesChannelId()))){
                        bankCode = bankCodeMap.get(giftCode.getSalesChannelId());
                    }else {
                        CommonResultVo<GoodsChannelRes> remoteChannel = remoteGoodsService.findById(giftCode.getSalesChannelId());
                        bankCode = remoteChannel.getResult().getBankCode();
                        bankCodeMap.put(giftCode.getSalesChannelId(),bankCode);
                    }

                    GiftUnit giftUnit=new GiftUnit();
                    giftUnit.setUnitId(giftCode.getUnitId());
                    giftUnit.setStart(1);
                    giftUnit.setStop(5);
                    final CommonResultVo<List<GiftUnit>> unitListResultVo = remoteGiftUnitService.list(giftUnit);
                    log.info("unitListResultVo{}",unitListResultVo.getResult());
                    Assert.notNull(unitListResultVo,"权益列表查询失败");
                    final List<GiftUnit> unitList = unitListResultVo.getResult();
                    GiftUnit unit = unitList.get(0);
                    GiftName giftName = null;
                    if (StringUtils.isNotBlank(unit.getPhone())){
                        CommonResultVo<GiftName> giftNameCommonResultVo = remoteGiftUnitService.selectMemberById(unit.getPhone());
                        if(null != giftNameCommonResultVo && giftNameCommonResultVo.getResult() != null) {
                            giftName = giftNameCommonResultVo.getResult();
                        }
                    }
                    // 调用member获取
                    if(unit.getMemberId()  !=null  && unit.getVtype().equalsIgnoreCase("code")){
                        CommonResultVo<GiftMember> giftMemberCommonResultVo = remoteGiftUnitService.selectGiftMemberById(unit.getMemberId());
                        if(null != giftMemberCommonResultVo && giftMemberCommonResultVo.getResult() != null){
                            GiftMember giftMember = giftMemberCommonResultVo.getResult();
                            MemLoginReqDTO memMemberInfo = new MemLoginReqDTO();
                            if(StringUtils.isNotBlank(unit.getPhone())){
                                memMemberInfo.setMobile(unit.getPhone());
                            }else{
                                memMemberInfo.setMobile(giftMember.getPhone());
                            }
                            //优先从giftName中设置姓名
                            if (giftName == null){
                                memMemberInfo.setName(giftMember.getName());
                            }else {
                                memMemberInfo.setName(giftName.getName());
                            }
                            memMemberInfo.setAcChannel(bankCode);
                            memMemberInfo.setSmsCode("colour");
                            MemLoginResDTO member =  memberInterfaceService.memberLogin(memMemberInfo);
                            if(member == null){
                                log.error("权益会员注册失败:{}", JSON.toJSONString(unit));
                                continue;
                            }
                            giftCode.setBuyMemberId(member.getAcid());
                            giftCode.setMemberId(member.getAcid());
                        }
                    }
                    if(unit.getVtype().equalsIgnoreCase("phone")){
//                        giftCode.setActCode(CodeUtils.generateSimpleCodeNo(giftCode.getId().longValue(),CodeUtils.CodeTypeNo.ACTIVATION_CODE));
                        if(giftName != null){
                            MemLoginReqDTO memMemberInfo = new MemLoginReqDTO();
                            memMemberInfo.setMobile(unit.getPhone());
                            memMemberInfo.setName(giftName.getName());
                            memMemberInfo.setAcChannel(bankCode);
                            memMemberInfo.setSmsCode("colour");
                            MemLoginResDTO member =  memberInterfaceService.memberLogin(memMemberInfo);
                            if(member == null){
                                log.error("权益会员注册失败:{}", JSON.toJSONString(unit));
                                continue;
                            }
                            giftCode.setBuyMemberId(member.getAcid());
                            giftCode.setMemberId(member.getAcid());
                        }
                    }
                    giftCodeService.updateById(giftCode);
                    Wrapper wrapper1 = new Wrapper() {
                        @Override
                        public String getSqlSegment() {
                            return "where gift_code_id = "+giftCode.getId();
                        }
                    };
                    List<EquityCodeDetail> details = equityCodeDetailService.selectList(wrapper1);
                    for (EquityCodeDetail detail : details) {
                        detail.setMemberId(giftCode.getMemberId());
                        equityCodeDetailService.updateById(detail);
                    }
                }
            }
        }
    }

    /**
     * 权益缺失详情明细恢复
     * @throws Exception
     */
    @Override
    public void giftDetailSet() throws Exception {
        //查询所有缺失权益详情明细的数据
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "WHERE id NOT IN (SELECT gift_code_id FROM equity_code_detail) AND act_code_status NOT IN (0,5,6) ORDER BY goods_id";
            }
        };
        List<GiftCode> giftCodeList = giftCodeService.selectList(wrapper);
        Map<Integer,List<GoodsGroupListRes>> groupMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(giftCodeList) && giftCodeList.size() == 6512){
            int i = 1;
            for (GiftCode giftCode : giftCodeList) {
                log.info("开始处理第{}条数据权益id{}",i,giftCode.getId());
                i++;
                //查询权益对应的商品的产品组
                if (giftCode.getGoodsId() == null){
                    log.error("{}权益缺失商品id",giftCode.getId());
                    continue;
                }
//                Assert.notNull(giftCode.getGoodsId(),"权益缺失商品id"+giftCode.getId());
                List<GoodsGroupListRes> goodsGroup;
                if (groupMap.containsKey(giftCode.getGoodsId())){
                    goodsGroup = groupMap.get(giftCode.getGoodsId());
                }else {
                    goodsGroup = panguInterfaceService.selectGoodsGroup(giftCode.getGoodsId());
                    groupMap.put(giftCode.getGoodsId(),goodsGroup);
                }
                if (CollectionUtils.isEmpty(goodsGroup)){
                    log.error("{}商品{}缺失产品组",giftCode.getId(),giftCode.getGoodsId());
                    continue;
                }
//                Assert.notEmpty(goodsGroup,"商品缺失产品组"+giftCode.getGoodsId());
                List<EquityCodeDetail> detailList = Lists.newLinkedList();
                for (GoodsGroupListRes goodsGroupRes : goodsGroup) {
                    ProductGroup productGroup = new ProductGroup();
                    BeanUtils.copyProperties(goodsGroupRes,productGroup);
                    //查询已使用次数
                    Wrapper orderWrapper = new Wrapper() {
                        @Override
                        public String getSqlSegment() {
                            return "WHERE del_flag = 0 AND prose_status IN (0,1,4) AND gift_code_id = "+giftCode.getId()+" AND product_group_id = "+productGroup.getId();
                        }
                    };
                    List<ReservOrder> orders = reservOrderService.selectList(orderWrapper);

                    EquityCodeDetail equityCodeDetail = new EquityCodeDetail();
                    equityCodeDetail.setGiftCodeId(giftCode.getId());
                    equityCodeDetail.setGoodsId(giftCode.getGoodsId());
                    equityCodeDetail.setMemberId(giftCode.getMemberId());
                    equityCodeDetail.setProductGroupId(productGroup.getId());
                    if (productGroup.getUseNum() != null){
                        equityCodeDetail.setTotalCount(productGroup.getUseNum().intValue());
                    }
                    equityCodeDetail.setUseCount(CollectionUtils.isEmpty(orders) ? 0 : orders.size());
                    equityCodeDetail.setDelFlag(DelFlagEnums.NORMAL.getCode());
                    equityCodeDetail.setType(GiftCodeDetailTypeEnum.NOMAL_TYPE.getcode());
                    equityCodeDetail.setGroupDetail(JSON.toJSONString(productGroup));
                    detailList.add(equityCodeDetail);
                }
                equityCodeDetailService.insertBatch(detailList);
            }
        }
    }

    /**
     * 预约单产品组恢复
     * @throws Exception
     */
    @Override
    public void reservOrderGroupSet() throws Exception {
        //查询所有缺失产品组的预约单
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "WHERE product_group_id IS NULL";
            }
        };
        List<ReservOrder> list = reservOrderService.selectList(wrapper);
        if (!CollectionUtils.isEmpty(list) && list.size() == 10791){
            int i = 1;
            for (ReservOrder reservOrder : list) {
                log.info("开始处理第{}条数据:{}",i,reservOrder.getId());
                i++;
                Integer oldOrderId = reservOrder.getOldOrderId();
                if (oldOrderId != null){
                    PgSyncInfo pgSyncInfo = new PgSyncInfo();
                    pgSyncInfo.setTableName("book_order");
                    pgSyncInfo.setSecId(oldOrderId);
                    // 从查理系统获取预约单数据
                    CommonResultVo<List<BookOrderResultVo>> booksResultVo =  remoteBookOrderService.selectBookOrderByProject(pgSyncInfo);
                    if (booksResultVo == null || CollectionUtils.isEmpty(booksResultVo.getResult())){
                        log.error("{}预约单没有查到老系统预约单",reservOrder.getId());
                        continue;
                    }
                    final List<BookOrderResultVo> result = booksResultVo.getResult();
//                    Assert.notEmpty(result,"查询老系统预约单结果为空");
                    BookOrderResultVo oldOrder = result.get(0);
                    //根据老产品组id查询新产品组id
                    CommonResultVo<List<ProductGroup>> remoteGroup = remoteProductGroupService.selectGroupByOldId(oldOrder.getGroupId());
                    if (remoteGroup == null || CollectionUtils.isEmpty(remoteGroup.getResult())){
                        log.error("{}预约单没有查到新系统产品组",reservOrder.getId());
                        continue;
                    }
                    final  List<ProductGroup> newGroupList = remoteGroup.getResult();
//                    Assert.notEmpty(newGroupList,"查询新系统产品组结果为空");
                    if (newGroupList.size()>1){
                        log.error("{}预约单查到的新系统产品组数量不正确{}",reservOrder.getId(),newGroupList.size());
                        continue;
                    }
//                    Assert.isTrue(newGroupList.size()>1,"新系统产品组数量不正确");
                    ProductGroup productGroup = newGroupList.get(0);
                    reservOrder.setProductGroupId(productGroup.getId());
                    reservOrderService.updateById(reservOrder);
                }else {
                    log.error("{}预约单没有旧系统预约单id",reservOrder.getId());
                }
            }
        }
    }

}

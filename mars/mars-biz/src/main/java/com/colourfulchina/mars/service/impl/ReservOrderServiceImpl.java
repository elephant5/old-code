package com.colourfulchina.mars.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.aggregatePay.utils.PayDigestUtil;
import com.colourfulchina.aggregatePay.vo.req.OrderRefundReq;
import com.colourfulchina.colourfulCoupon.api.entity.CpnThirdCode;
import com.colourfulchina.colourfulCoupon.api.enums.ThirdSourceEnum;
import com.colourfulchina.colourfulCoupon.api.feign.RemoteThirdCouponsService;
import com.colourfulchina.colourfulCoupon.api.vo.req.QueryThirdCouponsInfoReqVO;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.god.door.api.vo.KltSysUser;
import com.colourfulchina.inf.base.enums.SysDictTypeEnums;
import com.colourfulchina.inf.base.utils.MaskUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.mars.api.entity.*;
import com.colourfulchina.mars.api.enums.*;
import com.colourfulchina.mars.api.vo.*;
import com.colourfulchina.mars.api.vo.req.*;
import com.colourfulchina.mars.config.CodeProperties;
import com.colourfulchina.mars.config.FileDownloadProperties;
import com.colourfulchina.mars.constants.GiftCodeConstants;
import com.colourfulchina.mars.constants.RedisKeys;
import com.colourfulchina.mars.mapper.*;
import com.colourfulchina.mars.service.*;
import com.colourfulchina.mars.utils.CodeUtils;
import com.colourfulchina.mars.utils.ExportReservOrderUtils;
import com.colourfulchina.mars.utils.HelpUtils;
import com.colourfulchina.member.api.entity.MemMemberAccount;
import com.colourfulchina.member.api.entity.MemMemberFamily;
import com.colourfulchina.member.api.entity.MemMemberInfo;
import com.colourfulchina.member.api.feign.RemoteMemberFamilyServcie;
import com.colourfulchina.member.api.req.MemLoginReqDTO;
import com.colourfulchina.member.api.res.MemLoginResDTO;
import com.colourfulchina.member.api.res.MemSimpleRes;
import com.colourfulchina.member.api.res.MemberAccountInfoVo;
import com.colourfulchina.nuwa.api.feign.RemoteKlfEmailService;
import com.colourfulchina.nuwa.api.vo.SysEmailSendReqVo;
import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.enums.*;
import com.colourfulchina.pangu.taishang.api.feign.RemoteProductGroupService;
import com.colourfulchina.pangu.taishang.api.feign.RemoteShopService;
import com.colourfulchina.pangu.taishang.api.feign.RemoteSysFileService;
import com.colourfulchina.pangu.taishang.api.utils.GoodsUtils;
import com.colourfulchina.pangu.taishang.api.vo.ShopBaseMsgVo;
import com.colourfulchina.pangu.taishang.api.vo.req.*;
import com.colourfulchina.pangu.taishang.api.vo.res.*;
import com.colourfulchina.pangu.taishang.api.vo.res.bookBasePayment.BookBasePaymentRes;
import com.colourfulchina.pangu.taishang.api.vo.res.shopItem.ShopItemConciseRes;
import com.colourfulchina.pangu.taishang.api.vo.res.shopProtocol.ShopProtocolRes;
import com.colourfulchina.tianyan.admin.api.entity.SysDict;
import com.colourfulchina.tianyan.admin.api.feign.RemoteDictService;
import com.colourfulchina.tianyan.common.core.util.R;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
public class ReservOrderServiceImpl extends ServiceImpl<ReservOrderMapper, ReservOrder> implements ReservOrderService {

    @Autowired
    ReservOrderMapper reservOrderMapper;
    @Autowired
    PanguInterfaceService panguInterfaceService;
    @Autowired
    MemberInterfaceService memberInterfaceService;
    @Autowired
    EquityCodeDetailService equityCodeDetailService;
    @Autowired
    GiftCodeMapper giftCodeMapper;
    @Autowired
    ProductListService productListService;
    @Autowired
    ReservOrderDetailService reservOrderDetailService;

    @Autowired
    ReservOrderHospitalService reservOrderHospitalService;

    private RemoteMemberFamilyServcie remoteMemberFamilyServcie;
    @Autowired
    LogisticsInfoMapper logisticsInfoMapper;

    @Autowired
    ReservOrderPriceService reservOrderPriceService;
    @Autowired
    ReservCodeService reservCodeService;
    @Autowired
    NuwaInterfaceService nuwaInterfaceService;

    @Autowired
    private final RemoteSysFileService remoteSysFileService;

    @Autowired
    private ReservOrderDetailMapper reservOrderDetailMapper;
    @Autowired
    GiftCodeService giftCodeService;
    @Autowired
    private FileDownloadProperties fileDownloadProperties;

    @Autowired
    PayInfoService payInfoService;

    @Autowired
    private SourceMerchantInfoMapper sourceMerchantInfoMapper;

    @Autowired
    private CodeProperties codeProperties;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RemoteThirdCouponsService remoteThirdCouponsService;

    private RemoteKlfEmailService remoteKlfEmailService;

    @Autowired
    VerifyCodesHistoryService verifyCodesHistoryService;

    @Autowired
    private RemoteProductGroupService remoteProductGroupService;
    @Autowired
    private CouponsService couponsService;

    @Autowired
    SysHospitalService hospitalService;

    private RemoteDictService remoteDictService;
    private RemoteShopService remoteShopService;
    /**
     * 根据激活码或者手机号查询客户匹配的权益（所有购买的权益列表）
     *
     * @param equityListVo
     * @return
     */
    @Override
    public List<EquityListVo> selectEquityList(EquityListVo equityListVo, Boolean isMobile) {

        //先查激活码的
        List<String> codeList = Lists.newArrayList();
        List<EquityListVo> selectEquityList = Lists.newArrayList();
        List<Long> members = Lists.newArrayList();
        MemberAccountInfoVo memberAccountInfoVo = new MemberAccountInfoVo();
        if (isMobile) {
            //先查出会员ID
            MemMemberInfo memMemberInfo = new MemMemberInfo();
            memMemberInfo.setMobile(equityListVo.getCode());
            memberAccountInfoVo = memberInterfaceService.getMember(memMemberInfo);
            members = memberAccountInfoVo.getAccList().stream().map(obj -> obj.getAcid()).collect(Collectors.toList());
            selectEquityList = reservOrderMapper.selectEquityListByMembers(members);
        } else {
            codeList.add(equityListVo.getCode());
            selectEquityList = reservOrderMapper.selectEquityList(codeList);
        }
        if (CollectionUtils.isEmpty(selectEquityList)) {
            return selectEquityList;
        }
        Set<Integer> goodsIds = selectEquityList.stream().map(obj -> obj.getGoodsId()).collect(Collectors.toSet());
        Map<Integer, GoodsBaseVo> goodsMap = Maps.newHashMap();
        for (Integer goodsId : goodsIds) {
            GoodsBaseVo goodsBaseVo = (GoodsBaseVo) redisTemplate.opsForValue().get(RedisKeys.MARS_GOODS_ID + goodsId);
            if (goodsBaseVo == null) {
                goodsBaseVo = panguInterfaceService.selectGoodsById(goodsId);
                if (goodsBaseVo != null) {
                    redisTemplate.opsForValue().set(RedisKeys.MARS_GOODS_ID + goodsId, goodsBaseVo, 15, TimeUnit.MINUTES);
                }
            }
            if (goodsBaseVo != null) {
                goodsMap.put(goodsBaseVo.getId(), goodsBaseVo);
            }
        }
        Map<Long, MemMemberAccount> memberAccountMap = Maps.newHashMap();
        if (isMobile) {
            memberAccountMap = memberAccountInfoVo.getAccList().stream().collect(Collectors.toMap(MemMemberAccount::getAcid, bank -> bank));
        } else {
            List<Long> memberIds = selectEquityList.stream().map(obj -> obj.getMemberId()).collect(Collectors.toList());
            memberAccountMap = memberInterfaceService.getAccountList(memberIds);
        }
        List<SysDict> bankList = (List<SysDict>) redisTemplate.opsForValue().get(RedisKeys.SYS_DICT_BANK_TYPE);
        if (CollectionUtils.isEmpty(bankList)) {
            bankList = memberInterfaceService.selectSysDict(SysDictTypeEnums.BANK_TYPE.getType());
            if (!CollectionUtils.isEmpty(bankList)) {
                redisTemplate.opsForValue().set(RedisKeys.SYS_DICT_BANK_TYPE, bankList, 15, TimeUnit.MINUTES);
            }
        }
        List<SysDict> salesChannelList = (List<SysDict>) redisTemplate.opsForValue().get(RedisKeys.SYS_DICT_SALES_CHANNEL_TYPE);
        if (CollectionUtils.isEmpty(salesChannelList)) {
            salesChannelList = memberInterfaceService.selectSysDict(SysDictTypeEnums.SALES_CHANNEL_TYPE.getType());
            if (!CollectionUtils.isEmpty(salesChannelList)) {
                redisTemplate.opsForValue().set(RedisKeys.SYS_DICT_SALES_CHANNEL_TYPE, salesChannelList, 15, TimeUnit.MINUTES);
            }
        }
        List<SysDict> salesWayList = (List<SysDict>) redisTemplate.opsForValue().get(RedisKeys.SYS_DICT_SALES_WAY_TYPE);
        if (CollectionUtils.isEmpty(salesWayList)) {
            salesWayList = memberInterfaceService.selectSysDict(SysDictTypeEnums.SALES_WAY_TYPE.getType());
            if (!CollectionUtils.isEmpty(salesWayList)) {
                redisTemplate.opsForValue().set(RedisKeys.SYS_DICT_SALES_WAY_TYPE, salesWayList, 15, TimeUnit.MINUTES);
            }
        }
        Map<String, SysDict> bankMap = bankList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));
        Map<String, SysDict> salesChannelMap = salesChannelList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));
        Map<String, SysDict> salesWayMap = salesWayList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));
        //Todo
        for (EquityListVo vo : selectEquityList) {
            GoodsBaseVo goodsBaseVo = goodsMap.get(vo.getGoodsId());
            if (null != goodsBaseVo) {
                List<String> salesChannels = goodsBaseVo.getSalesChannelIds();
                //TODO 去字典表里查询对应的数据
                SysDict sysDict1 = bankMap.get(goodsBaseVo.getBankId());
                if (null == sysDict1) {
                    goodsBaseVo.setBankName("-");
                } else {
                    goodsBaseVo.setBankName(sysDict1.getLabel());
                }
                SysDict sysDict2 = salesChannelMap.get(salesChannels.get(1));
                if (null == sysDict2) {
                    goodsBaseVo.setSalesChannelName("-");
                } else {
                    goodsBaseVo.setSalesChannelName(sysDict2.getLabel());
                }
                SysDict sysDict3 = salesWayMap.get(goodsBaseVo.getSalesWayId());
                if (null == sysDict3) {
                    goodsBaseVo.setSalesWayName("-");
                } else {
                    goodsBaseVo.setSalesWayName(sysDict3.getLabel());
                }
                // 如果不是手机号查询
                if (!isMobile) {
                    List<EquityCodeDetail> equityCodeDetails = equityCodeDetailService.selectByEquityByGoodsId(goodsBaseVo.getId(), vo.getGiftCodeId());
                    Set<Integer> productGroupIds = equityCodeDetails.stream().map(equityCodeDetail -> equityCodeDetail.getProductGroupId()).collect(Collectors.toSet());

                    List<GoodsGroupListRes> groupList = Lists.newLinkedList();
                    if (!CollectionUtils.isEmpty(productGroupIds)) {
                        String ids = org.apache.commons.lang.StringUtils.join(productGroupIds, ",");
                        groupList = (List<GoodsGroupListRes>) redisTemplate.opsForValue().get(RedisKeys.MARS_GOODS_GROUP + ids);
                        if (CollectionUtils.isEmpty(groupList)) {
                            groupList = panguInterfaceService.selectGoodsGroupByIds(ids);
                            if (!CollectionUtils.isEmpty(groupList)) {
                                redisTemplate.opsForValue().set(RedisKeys.MARS_GOODS_GROUP + ids, groupList, 15, TimeUnit.MINUTES);
                            }
                        }
                    }
                    Map<Integer, EquityCodeDetail> equityCodeDetailMap = equityCodeDetails.stream().collect(Collectors.toMap(EquityCodeDetail::getProductGroupId, bank -> bank));
                    if (!CollectionUtils.isEmpty(groupList)) {
                        List<GoodsGroupListRes> temp = Lists.newArrayList();
                        for (GoodsGroupListRes res : groupList) {
                            GoodsGroupListRes tesTemp = new GoodsGroupListRes();
                            BeanUtils.copyProperties(res, tesTemp);
                            EquityCodeDetail equityCodeDetail = equityCodeDetailMap.get(res.getId());
                            if (null != equityCodeDetail) {
                                tesTemp.setTotalCount(equityCodeDetail.getTotalCount());
                                tesTemp.setUseCount(equityCodeDetail.getUseCount());
                                if (null != equityCodeDetail.getType() && equityCodeDetail.getType().equals(1)) {
                                    SimpleDateFormat sd = new SimpleDateFormat();
                                    tesTemp.setStartTime(sd.format(equityCodeDetail.getStartTime()));
                                    tesTemp.setEndTime(sd.format(equityCodeDetail.getEndTime()));
                                }
                                temp.add(tesTemp);
                            }
                            if (null == res.getMinBookDays() && null != goodsBaseVo.getMinBookDays()) {
                                res.setMinBookDays(goodsBaseVo.getMinBookDays());
                            } else if (null == res.getMaxBookDays() && null != goodsBaseVo.getMaxBookDays()) {
                                res.setMaxBookDays(goodsBaseVo.getMaxBookDays());
                            } else if (null != res.getMinBookDays() && null != goodsBaseVo.getMinBookDays() && res.getMinBookDays() < goodsBaseVo.getMinBookDays()) {
                                res.setMinBookDays(goodsBaseVo.getMinBookDays());
                            } else if (null != res.getMaxBookDays() && null != goodsBaseVo.getMaxBookDays() && res.getMaxBookDays() > goodsBaseVo.getMaxBookDays()) {
                                res.setMaxBookDays(goodsBaseVo.getMaxBookDays());
                            }
                        }
                        vo.setGoodsGroupListRes(temp);
                    }
                }
            }
            if (vo.getMemberId() != null) {
                MemMemberAccount memMemberAccount = memberAccountMap.get(vo.getMemberId());
                if (null != memMemberAccount) {
                    vo.setMemberName(memMemberAccount.getAcName());
                    vo.setMemberPhone(memMemberAccount.getMobile());
                }
            }
            vo.setGoodsBaseVo(goodsBaseVo);
//
//            vo.setMemberName(MaskUtils.nameMask(vo.getMemberName()));
//            if(org.apache.commons.lang.StringUtils.isNotBlank(vo.getMemberPhone())){
//                vo.setMemberPhone(MaskUtils.maskPhone(vo.getMemberPhone()));
//            }
//
//            vo.setBuyMemberName(MaskUtils.nameMask(vo.getBuyMemberName()));
//            if(org.apache.commons.lang.StringUtils.isNotBlank(vo.getBuyMemberPhone())){
//                vo.setBuyMemberPhone(MaskUtils.maskPhone(vo.getBuyMemberPhone()));
//            }
        }
        return selectEquityList;
    }


    /*
     * 获取预定的预约单
     * */
    @Override
    public ReservOrderProductVo getReservOrder(Integer id, MemLoginResDTO memInfo) throws Exception {
        ReservOrderProductVo vo = reservOrderMapper.getReservOrder(id);
//        if(ReservOrderStatusEnums.ReservOrderStatus.done.getcode().equalsIgnoreCase(vo.getProseStatus())){
//            ReservCode  reservCode = reservCodeService.selectOneReservCode(id);
//            vo.setReservCode(reservCode);
//        }
        if (StringUtils.isEmpty(vo)) {
            throw new Exception("预约单不存在");
        }
        //第三方券类型需要查券信息
        if (vo.getServiceType().indexOf("_cpn") != -1) {
            QueryThirdCouponsInfoReqVO reqVO = new QueryThirdCouponsInfoReqVO();
            reqVO.setCpnThirdCodeId(vo.getThirdCpnNo());
            CommonResultVo<CpnThirdCode> resultVo = remoteThirdCouponsService.getThirdCouponsInfoById(reqVO);
            if (null != resultVo && null != resultVo.getResult()) {
                vo.setCouponsType(resultVo.getResult().getCouponsType());
                vo.setThirdCodePassword(resultVo.getResult().getThirdCodePassword());
                vo.setShortUrl(resultVo.getResult().getShortUrl());
                vo.setValidStartTime(resultVo.getResult().getValidStartTime() == null ? "" : DateUtil.format(resultVo.getResult().getValidStartTime(), "yyyy/MM/dd"));
                vo.setExperTime(resultVo.getResult().getExperTime() == null ? "" : DateUtil.format(resultVo.getResult().getExperTime(), "yyyy/MM/dd"));
            }
            QueryProductGroupInfoReqVo queryProductGroupInfoReqVo = new QueryProductGroupInfoReqVo();
            queryProductGroupInfoReqVo.setProductGroupId(vo.getProductGroupId());
            queryProductGroupInfoReqVo.setProductId(vo.getProductId());
            CommonResultVo<List<ProductGroupResVO>> commonResultVo = remoteProductGroupService.selectProductGroupById(queryProductGroupInfoReqVo);
            if (null != commonResultVo && !CollectionUtils.isEmpty(commonResultVo.getResult())) {
                ProductGroupResVO productGroupResVO = commonResultVo.getResult().get(0);
                if (null != productGroupResVO && null != productGroupResVO.getShopChannelId()) {
                    vo.setThirdCpnSource(ThirdSourceEnum.getCode(productGroupResVO.getShopChannelId()));
                }
            }
        }

        //实物券需要查询配送信息
        if (ResourceTypeEnums.OBJECT_CPN.getCode().equals(vo.getServiceType())) {
            LogisticsInfo logisticsInfo = logisticsInfoMapper.selectById(vo.getId());
            if (logisticsInfo != null) {
                vo.setExpressMode(logisticsInfo.getExpressMode());
                vo.setExpressModeStr(ExpressModeEnums.findByCode(logisticsInfo.getExpressMode()).getName());
                vo.setExpressAddress(logisticsInfo.getAddress());
                vo.setExpressStatus(logisticsInfo.getStatus());
                vo.setExpressStatusStr(ExpressStatusEnum.getNameByCode(logisticsInfo.getStatus()));
                vo.setExpressNumber(logisticsInfo.getExpressNumber());
                vo.setConsignee(logisticsInfo.getConsignee());
                vo.setExpressPhone(logisticsInfo.getPhone());
                if (!StringUtils.isEmpty(logisticsInfo.getExpressNameId())) {
                    SysDict sysDict = new SysDict();
                    sysDict.setType("express_type");
                    sysDict.setValue(logisticsInfo.getExpressNameId());
                    R<SysDict> dictR = remoteDictService.selectByType(sysDict);
                    if (dictR != null && dictR.getData() != null) {
                        vo.setCompanyName(dictR.getData().getLabel());
                    }
                }

            }
        }
        //医疗获取医院信息
        if (ResourceTypeEnums.MEDICAL.getCode().equals(vo.getServiceType())) {
            EntityWrapper<ReservOrderHospital> local = new EntityWrapper<>();
            local.eq("order_id", id);
            List<ReservOrderHospital> hospitals = reservOrderHospitalService.selectList(local);
            List<ReservOrderHospitalVO> list = new ArrayList<>();
            ReservOrderHospitalVO reservOrderHospitalVO;
            for(ReservOrderHospital hostpital : hospitals){
                reservOrderHospitalVO = new ReservOrderHospitalVO();
                BeanUtils.copyProperties(hostpital,reservOrderHospitalVO);
                CommonResultVo<MemMemberFamily> result = remoteMemberFamilyServcie.selectById(hostpital.getMemFamilyId());
                Assert.isTrue(result!=null&&result.getCode()==100&&result.getResult()!=null,"根据id获取就诊人信息有误");
                reservOrderHospitalVO.setMemberFamily(result.getResult());
                list.add(reservOrderHospitalVO);
            }
            vo.setHospitalInfos(list);
        }
        //防止被其他用户查到订单信息
        Long acId;
        Assert.isTrue(memInfo != null && (acId = memInfo.getAcid()) != null && acId.equals(vo.getMemberId()), "没有订单");
        //获取预约单详情
        EntityWrapper local = new EntityWrapper();
        local.eq("order_id", id);
        List<ReservOrderDetail> details = reservOrderDetailMapper.selectList(local);
        vo.setDetails(details);
        //fegen 根据id获取商品(项目) 信息   项目图片+项目名称
        Integer goodsId = vo.getGoodsId();
        GoodsBaseVo goods = panguInterfaceService.selectGoodsById(goodsId);
        if (goods != null) {
            vo.setGoodsName(goods.getName());
        }
        // fegen  根据id获取商户信息  商户名称
        Integer shopId = vo.getShopId();
        ShopDetailRes shopDetailRes = panguInterfaceService.getShopDetail(shopId);
        ShopBaseMsgVo shop;
        if (shopDetailRes != null && (shop = shopDetailRes.getShop()) != null) {
            vo.setShopName(shop.getName());
        }
        Integer productGroupProductId = vo.getProductGroupProductId();
        ReservOrderProductVo shopItem = productListService.selectReservOrderVo(productGroupProductId);
        if (shopItem != null) {
            vo.setServiceName(shopItem.getServiceName());
            //产品的名称
            vo.setProductName(shopItem.getProductName());
            vo.setAddon(shopItem.getAddon());
            vo.setNeeds(shopItem.getNeeds());
        }
        Hotel hotel = panguInterfaceService.selectHotelByShopId(shopId);
        if (hotel != null) {
            vo.setHotelName(hotel.getNameCh());
        }
        Date createTime = vo.getCreateTime();
        if (createTime != null) {
            //转化时间格式 便于前端取
            vo.setCreateTimeStr(HelpUtils.dateToStr(createTime, "yyyy-MM-dd HH:mm:ss"));
        }
        //图片信息
        ListSysFileReq shopFile = new ListSysFileReq();
        shopFile.setObjId(shopId);
        shopFile.setType(FileTypeEnums.SHOP_PIC.getCode());
        CommonResultVo<List<SysFileDto>> comm = remoteSysFileService.list(shopFile);
        if (!comm.getResult().isEmpty()) {
            SysFileDto sysFile = comm.getResult().get(0);
            vo.setGoodsImg(sysFile.getPgCdnHttpUrl() + '/' + sysFile.getGuid() + '.' + sysFile.getExt());
        }

        return vo;
    }

    private static void checkAccom(ReservOrderVo reservOrderVo) {
        Assert.notNull(reservOrderVo, "参数不能为空");
        final String giftType = reservOrderVo.getGiftType();
        final Integer nightNumbers = reservOrderVo.getNightNumbers();
        Assert.hasText(giftType, "权益类型不能为空");
        Assert.isTrue(giftType.length() == 2, "权益类型不能为空");
        Assert.isTrue(giftType.startsWith("N"), "权益类型有误");
        Assert.notNull(nightNumbers, "入住夜晚数不能为空");
        final String night = giftType.substring(1);
        if (!night.equals("X")) {
            Integer nightNum = Integer.parseInt(night);
            Assert.isTrue(nightNum.compareTo(nightNumbers) == 0, "入住夜晚数有误");
        }
        final DateTime checkDate = DateUtil.parse(reservOrderVo.getCheckDate());
        final DateTime deparDate = DateUtil.parse(reservOrderVo.getDeparDate());
        final long between = deparDate.between(checkDate, DateUnit.DAY);
        Assert.isTrue(nightNumbers.compareTo(Integer.parseInt(between + "")) == 0, "入住日期、离店日期与入住夜晚不符");
    }

    /**
     * 来电录单：新增预约订单
     *
     * @param reservOrderVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReservOrderVo insertReservOrder(ReservOrderVo reservOrderVo) throws Exception {
        final Integer giftCodeId = reservOrderVo.getGiftCodeId();
        final Boolean hasKey = redisTemplate.hasKey(RedisKeys.MARS_INSERTRESERVORDER_GIFT_CODE + giftCodeId);
        Assert.isTrue(!hasKey, "当前激活码正在处理中，请勿重复提交！");
        GiftCode giftCode = giftCodeMapper.selectById(giftCodeId);
        try {
            Assert.notNull(giftCode, giftCodeId + "对应权益不存在");
            redisTemplate.opsForValue().set(RedisKeys.MARS_INSERTRESERVORDER_GIFT_CODE + giftCodeId, giftCode, 5, TimeUnit.MINUTES);
            Date endTime = DateUtil.parse(DateUtil.format(giftCode.getActEndTime(), "yyyy-MM-dd"), "yyyy-MM-dd");
            Date nowTime = DateUtil.parse(DateUtil.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
            Assert.isTrue(giftCode.getActCodeStatus().compareTo(ActCodeStatusEnum.ActCodeStatus.OUT.getIndex()) == 0 || giftCode.getActCodeStatus().compareTo(ActCodeStatusEnum.ActCodeStatus.ACTIVATE.getIndex()) == 0, "当前激活码" + ActCodeStatusEnum.ActCodeStatus.findNameByIndex(giftCode.getActCodeStatus()) + "！");
            Assert.isTrue(!endTime.before(nowTime), "激活码已过期！");

            MemMemberAccount memMemberAccount = null;
            List<Long> memberIds = Lists.newArrayList();
//            if (reservOrderVo.getServiceTypeCode().equals(ShopTypeEnums.ACCOM.getCode())) {
//                checkAccom(reservOrderVo);
//            }
            //第三方产品编号和名称保存
            if (reservOrderVo.getShopItemId() != null) {
                ShopItemRes shopItemRes = panguInterfaceService.getShopItemById(reservOrderVo.getShopItemId());
                if (shopItemRes != null) {
                    reservOrderVo.setThirdCpnNum(shopItemRes.getThirdCpnNum());
                    reservOrderVo.setThirdCpnName(shopItemRes.getThirdCpnName());
                }
            }
            if (giftCode.getActCodeStatus() == ActCodeStatusEnum.ActCodeStatus.OUT.getIndex()) {//未激活已出库
//            //激活时间
                GoodsBaseVo goodsBaseVo = panguInterfaceService.selectGoodsById(reservOrderVo.getGoodsId());

                giftCode.setActCodeStatus(ActCodeStatusEnum.ActCodeStatus.ACTIVATE.getIndex());
                giftCode.setActCodeTime(new Date());
                giftCode.setUpdateTime(new Date());
                goodsBaseVo.setExpiryValue(giftCode.getActRule());
                goodsBaseVo = GoodsUtils.analyCodeDate(goodsBaseVo, new Date(), giftCode.getActOutDate());
                if (giftCode.getActExpireTime() == null) {
                    giftCode.setActExpireTime(goodsBaseVo.getExpiryDate() == "NULL" ? null : DateUtil.parse(goodsBaseVo.getExpiryDate(), new SimpleDateFormat("yyyy-MM-dd")));
                }

                GoodsChannelRes goodsChannelRes = panguInterfaceService.findChannelById(giftCode.getSalesChannelId());

                MemLoginReqDTO memLoginReqDTO = new MemLoginReqDTO();
                memLoginReqDTO.setMobile(reservOrderVo.getActivePhone());
                memLoginReqDTO.setAcChannel(goodsChannelRes.getBankCode());
                memLoginReqDTO.setName(reservOrderVo.getActiveName());
                memLoginReqDTO.setSex(reservOrderVo.getActiveSex());
                memMemberAccount = memberInterfaceService.register(memLoginReqDTO);
                giftCode.setBuyMemberId(memMemberAccount.getAcid());
                giftCode.setMemberId(memMemberAccount.getAcid());
                giftCodeMapper.updateById(giftCode);
                reservOrderVo.setMemberId(giftCode.getMemberId());

                Wrapper<EquityCodeDetail> local = new Wrapper<EquityCodeDetail>() {
                    @Override
                    public String getSqlSegment() {
                        return "where del_flag = 0 and gift_code_id = " + giftCode.getId();
                    }
                };
                List<EquityCodeDetail> equityCodeDetails = equityCodeDetailService.selectList(local);
                //根据新改动 循环周期需求
                if (!CollectionUtils.isEmpty(equityCodeDetails)) {
                    //按产品组分类入map
                    Map<Integer, List<EquityCodeDetail>> map = Maps.newHashMap();
                    for (EquityCodeDetail equityCodeDetail : equityCodeDetails) {
                        Integer key = equityCodeDetail.getProductGroupId();
                        List<EquityCodeDetail> list = Lists.newLinkedList();
                        if (map.containsKey(key)) {
                            list = map.get(key);
                        }
                        list.add(equityCodeDetail);
                        map.put(key, list);
                    }
                    //针对不同类型的权益修改或新增权益明细
                    if (!CollectionUtils.isEmpty(map)) {
                        for (Map.Entry<Integer, List<EquityCodeDetail>> entry : map.entrySet()) {
                            List<EquityCodeDetail> list = entry.getValue();
                            if (!CollectionUtils.isEmpty(list)) {
                                //添加用户id
                                for (EquityCodeDetail detail : list) {
                                    detail.setMemberId(memMemberAccount.getAcid());
                                    equityCodeDetailService.updateById(detail);
                                }
                                //循环周期权益（无限制产品组权益延长一年，到期时间大于权益最大截止时间则修改最大权益明细到期时间，并插入后续时间的权益）
                                if (null != list.get(0).getType() && list.get(0).getType().compareTo(GiftCodeDetailTypeEnum.CYCLE_TYPE.getcode()) == 0) {
                                    //找出list里最大的权益截止日期数据
                                    list.sort(new Comparator<EquityCodeDetail>() {
                                        @Override
                                        public int compare(EquityCodeDetail o1, EquityCodeDetail o2) {
                                            return o2.getEndTime().compareTo(o1.getEndTime());
                                        }
                                    });
                                    EquityCodeDetail equityCodeDetail = list.get(0);
                                    Date oldEndTime = equityCodeDetail.getEndTime();
                                    Date expiryDate = giftCode.getActExpireTime();
                                    //无限制权益，最后时间设置一年后
                                    if (expiryDate == null) {
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.add(Calendar.YEAR, 1);
                                        expiryDate = calendar.getTime();
                                    }
                                    oldEndTime = DateUtil.parse(DateUtil.format(oldEndTime, "yyyy-MM-dd"), "yyyy-MM-dd");
                                    expiryDate = DateUtil.parse(DateUtil.format(expiryDate, "yyyy-MM-dd"), "yyyy-MM-dd");
                                    //到期时间大于权益最大截止时间
                                    if (oldEndTime.before(expiryDate)) {
                                        JSONObject jsonObject = JSONObject.parseObject(equityCodeDetail.getGroupDetail());
                                        Integer cycleTime = Integer.valueOf(jsonObject.getString("cycleTime"));
                                        Integer cycleTypeInt = Integer.valueOf(jsonObject.getString("cycleType"));
                                        Integer productGroupId = Integer.valueOf(jsonObject.getString("id"));
                                        Integer cycleNum = Integer.valueOf(jsonObject.getString("cycleNum"));
                                        int cycleType = 0;
                                        if (cycleTypeInt.compareTo(CycleTypeEnums.CYCLE_DAY.getCode()) == 0) {
                                            cycleType = Calendar.DAY_OF_MONTH;
                                        } else if (cycleTypeInt.compareTo(CycleTypeEnums.CYCLE_WEEK.getCode()) == 0) {
                                            cycleType = Calendar.WEEK_OF_MONTH;
                                        } else if (cycleTypeInt.compareTo(CycleTypeEnums.CYCLE_MONTH.getCode()) == 0) {
                                            cycleType = Calendar.MONTH;
                                        } else if (cycleTypeInt.compareTo(CycleTypeEnums.CYCLE_YEAR.getCode()) == 0) {
                                            cycleType = Calendar.YEAR;
                                        }
                                        //更新过去权益明细的最大截止时间
                                        Date oldStartTime = DateUtil.parse(DateUtil.format(equityCodeDetail.getStartTime(), "yyyy-MM-dd"), "yyyy-MM-dd");
                                        Date nowDate = oldStartTime;
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTime(nowDate);
                                        calendar.add(cycleType, cycleTime);
                                        if (calendar.getTime().compareTo(expiryDate) >= 0) {
                                            nowDate = expiryDate;
                                        } else {
                                            nowDate = calendar.getTime();
                                        }
                                        equityCodeDetail.setEndTime(nowDate);
                                        equityCodeDetailService.updateById(equityCodeDetail);
                                        //新增权益明细信息
                                        List<EquityCodeDetail> codeDetails = Lists.newLinkedList();
                                        while (nowDate.before(expiryDate)) {
                                            Date tempDate = nowDate;
                                            calendar.setTime(nowDate);
                                            calendar.add(cycleType, cycleTime);
                                            if (calendar.getTime().compareTo(expiryDate) >= 0) {
                                                nowDate = expiryDate;
                                            } else {
                                                nowDate = calendar.getTime();
                                            }
                                            EquityCodeDetail equity = new EquityCodeDetail();
                                            equity.setGiftCodeId(giftCode.getId());
                                            equity.setGoodsId(giftCode.getGoodsId());
                                            equity.setProductGroupId(productGroupId);
                                            equity.setUseCount(0);
                                            equity.setType(GiftCodeDetailTypeEnum.CYCLE_TYPE.getcode());
                                            equity.setGroupDetail(equityCodeDetail.getGroupDetail());
                                            equity.setTotalCount(equityCodeDetail.getTotalCount());
                                            equity.setStartTime(tempDate);
                                            equity.setEndTime(nowDate);
                                            equity.setCycleCount(cycleNum);
                                            equity.setMemberId(memMemberAccount.getAcid());
                                            equity.setCreateUser(SecurityUtils.getLoginName());
                                            codeDetails.add(equity);
                                        }
                                        if (!CollectionUtils.isEmpty(codeDetails)) {
                                            equityCodeDetailService.insertBatch(codeDetails);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
//                for(EquityCodeDetail equityCodeDetail : equityCodeDetails){
//                    equityCodeDetail.setMemberId(memMemberAccount.getAcid());
//                    equityCodeDetailService.updateById(equityCodeDetail);
//                }
            } else {
                reservOrderVo.setMemberId(giftCode.getMemberId());
                memberIds.add(reservOrderVo.getMemberId());
                Map<Long, MemMemberAccount> memberAccountMap = memberInterfaceService.getAccountList(memberIds);
                memMemberAccount = memberAccountMap.get(reservOrderVo.getMemberId());
            }

//        if (!reservOrderVo.getActivePhone().equals(memMemberAccount.getMobile())) {
//            //当激活人和使用的手机号不一样的时候，
//            MemMemberInfo memMemberInfo = new MemMemberInfo();
//            memMemberInfo.setMobile(reservOrderVo.getActivePhone());
//            MemberAccountInfoVo memberAccountInfoVo = memberInterfaceService.getMember(memMemberInfo);
//            if(null  == memberAccountInfoVo) {
//                MemMemberAccount account = new MemMemberAccount();
//                account.setMobile(reservOrderVo.getActivePhone());
//                account.setAcName(reservOrderVo.getActiveName());
//                memberInterfaceService.getMemberAddAccount(account);
//                memMemberAccount = account;
//            }
//            if (reservOrderVo.getGiftPhone().equals(memMemberAccount.getMobile())) {
//                reservOrderVo.setMemberId(memMemberAccount.getAcid());
//            }
//         }


            ReservOrder reservOrder = new ReservOrder();
            BeanUtils.copyProperties(reservOrderVo, reservOrder);
            reservOrder.setProseStatus(ReservOrderStatusEnums.ReservOrderStatus.none.getcode() + "");
            if (null != reservOrder.getGiftType()) {
                reservOrder.setGiftType(GiftTypeEnum.findByName(reservOrder.getGiftType()).getCode());
            }
            if (null != reservOrder.getServiceType()) {
                reservOrder.setServiceType(ShopTypeEnums.findByName(reservOrder.getServiceType()).getCode());
            }
            //需要保存价格信息
            List<ReservOrderPrice> reservOrderPrices = Lists.newArrayList();
            ShopSettleMsgReq shopSettleMsgReq = new ShopSettleMsgReq();
            shopSettleMsgReq.setBookDate(DateUtil.parse(reservOrder.getGiftDate(), "yyyy-MM-dd"));
            reservOrder.setReservDate(DateUtil.parse(reservOrder.getGiftDate(), "yyyy-MM-dd"));
            shopSettleMsgReq.setShopId(reservOrder.getShopId());
            shopSettleMsgReq.setShopItemId(reservOrder.getShopItemId());
            shopSettleMsgReq.setShopChannelId(reservOrder.getShopChannelId());
            shopSettleMsgReq.setGift(reservOrder.getGiftType());

            if (reservOrder.getServiceType().equals(ShopTypeEnums.ACCOM.getCode())) {
//                checkAccom(reservOrderVo);
                BigDecimal checkNight = StringUtils.isEmpty(reservOrderVo.getCheckNight()) ? BigDecimal.ZERO : new BigDecimal(reservOrderVo.getCheckNight());
                Date endDate = DateUtil.parse(reservOrderVo.getDeparDate(), "yyyy-MM-dd");
                Long day = (endDate.getTime() - shopSettleMsgReq.getBookDate().getTime()) / (24 * 60 * 60 * 1000);
                for (int i = 1; i <= day; i++) {
                    if (i > 1) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(shopSettleMsgReq.getBookDate());
                        c.add(Calendar.DAY_OF_MONTH, 1);
                        shopSettleMsgReq.setBookDate(c.getTime());
                    }
                    ReservOrderPrice reservOrderPrice = new ReservOrderPrice();
//                ShopSettleMsgRes shopSettleMsgRes = panguInterfaceService.shopSettleMsg(shopSettleMsgReq);
//                if(null != shopSettleMsgRes){
//                    BigDecimal result  =  shopSettleMsgRes.getProtocolPrice().multiply(checkNight);
//                    reservOrderPrice.setPrice(result);
//                }else{
//                    reservOrderPrice.setPrice(BigDecimal.ZERO);
//                }
                    reservOrderPrice.setNumber(reservOrderVo.getCheckNight());
                    reservOrderPrice.setPrice(BigDecimal.ZERO);
                    reservOrderPrice.setOrderDate(DateUtil.format(shopSettleMsgReq.getBookDate(), "yyyy-MM-dd"));
                    reservOrderPrices.add(reservOrderPrice);
                }
            } else {
                ReservOrderPrice reservOrderPrice = new ReservOrderPrice();
                reservOrderPrice.setPrice(BigDecimal.ZERO);
                reservOrderPrice.setOrderDate(DateUtil.format(shopSettleMsgReq.getBookDate(), "yyyy-MM-dd"));
                reservOrderPrice.setNumber(1);
                reservOrderPrices.add(reservOrderPrice);
            }
            reservOrder.setSalesChannleId(giftCode.getSalesChannelId());
            reservOrder.setCreateUser(SecurityUtils.getLoginName());
            reservOrder.setUpdateUser(SecurityUtils.getLoginName());
            reservOrder.setTags("call,");
            reservOrder.setPayStatus(PayOrderStatusEnum.PREPAID.getCode());
//        List<SysDict> bankList = memberInterfaceService.selectSysDict(SysDictTypeEnums.RESERV_ORDER_TYPE.getType());
            reservOrder.setOrderSource(ReservOrderTypeEnums.ReservOrderType.COLOUR_CALL.getcode());
            //判断产品状态
            final ProductGroupProduct productGroupProduct = panguInterfaceService.findProductGroupProductById(reservOrder.getProductGroupProductId());
            Assert.notNull(productGroupProduct.getStatus().compareTo(0) == 0, "商品已停售");
            final Integer insertOrder = reservOrderMapper.insert(reservOrder);
            Assert.isTrue(insertOrder == 1, "新增预约单失败");
            reservOrderVo.setId(reservOrder.getId());
            Assert.notEmpty(reservOrderPrices, "预约单价格有误");
            for (ReservOrderPrice reservOrderPrice : reservOrderPrices) {
                reservOrderPrice.setReservOrderId(reservOrder.getId());
                final boolean insert = reservOrderPriceService.insert(reservOrderPrice);
                Assert.isTrue(insert, "保存预约单价格失败");
            }
            BigDecimal point = productGroupProduct.getPoint();
            reservOrderVo.setExchangeNum(point.intValue());
            if (ResourceTypeEnums.ACCOM.getCode().equalsIgnoreCase(reservOrder.getServiceType())) {
                if (GiftTypeEnum.NX.getCode().equalsIgnoreCase(reservOrder.getGiftType())) {
//                    reservOrderVo.setCheckNight(reservOrderVo.getCheckNight() == null? 0 : reservOrderVo.getCheckNight());
                    BigDecimal checkNight = new BigDecimal(reservOrderVo.getCheckNight()).multiply(new BigDecimal(reservOrderVo.getNightNumbers()));
                    //这单需要的总点数
                    point = point.multiply(checkNight);
                } else {
                    point = point.multiply(new BigDecimal(reservOrderVo.getCheckNight()));
                }
                reservOrderVo.setExchangeNum(point.intValue());
            }
                //保存完以后需要更新激活码次数表
            //扣减权益
            EquityCodeDetail detail1 = equityCodeDetailService.changeGiftTimes(ReservOrderStatusEnums.typeEnum.SUB.getCode(), null, reservOrder.getGoodsId(), reservOrder.getProductGroupId(), giftCodeId, reservOrderVo.getExchangeNum() == null ? 1 : reservOrderVo.getExchangeNum(), reservOrder.getCreateTime());
            if (detail1 == null) {
                throw new Exception("权益次数更改失败");
            }
            Assert.isTrue(detail1 != null, giftCodeId + "权益次数扣减失败");
            //住宿的保存字表的一些信息
//        if (reservOrder.getServiceType().equals(ShopTypeEnums.ACCOM.getCode())) {
            ReservOrderDetail detail = new ReservOrderDetail();
            if (null != reservOrderVo.getDetail()) {
                BeanUtils.copyProperties(reservOrderVo.getDetail(), detail);
                detail.setBookNameEn(reservOrderVo.getDetail().getBookNameEn());
            }
            ShopProtocolRes shopProtocolRes = panguInterfaceService.selectShopProtocol(reservOrder.getShopId());
            if (null != shopProtocolRes && org.apache.commons.lang.StringUtils.isNotBlank(shopProtocolRes.getNotice())) {
                detail.setNotice(shopProtocolRes.getNotice());
            }
            detail.setOrderId(reservOrder.getId());
            detail.setProductType(reservOrder.getServiceType());
            detail.setGoodsId(reservOrder.getGoodsId());
            detail.setCheckDate(reservOrder.getGiftDate());
            detail.setDeparDate(reservOrderVo.getDeparDate());
            detail.setCheckNight(reservOrderVo.getCheckNight());
            detail.setAccoAddon(reservOrderVo.getAccoAddon());
            detail.setAccoNedds(reservOrderVo.getAccoNedds());
            detail.setCreateUser(SecurityUtils.getLoginName());
            detail.setNightNumbers(reservOrderVo.getNightNumbers());
            detail.setBackAmountStatus(PayOrderStatusEnum.PREPAID.getCode());
            if (null != reservOrderVo.getDetail()) {
                //TODO  折扣比例获取值
                detail.setBackAmount(BigDecimal.ZERO);// = BigDecimal.ZERO;//退款金额
//            detail.setBackAmountDate(); ;//退款时间
                detail.setBackAmountStatus(PayOrderStatusEnum.PREPAID.getCode());//退款状态
//            detail.setSalesRatio();//折扣比例
            }


            final boolean insert = reservOrderDetailService.insert(detail);
            Assert.isTrue(insert, "新增预约单详情失败");
//        }
            //下单后，判断该权益是否用完
            Set<Integer> ids = Sets.newHashSet();
            ids.add(reservOrder.getGiftCodeId());
            giftCodeService.actCodesIsRunOut(ids);
            //发送短信
            nuwaInterfaceService.sendMsg(reservOrder);
        } catch (Exception e) {
            log.error("预约单下单失败", e);
            throw new Exception(e);
        } finally {
            //删除缓存
            giftCodeService.deleteRedis(giftCodeId);
        }
        return reservOrderVo;
    }

    /**
     * 预约单分页查询
     *
     * @param pageVoReq
     * @return
     */
    @Override
    public PageVo<ReservOrderVo> selectReservOrderPageList(PageVo<ReservOrderVo> pageVoReq) throws Exception {
        Map<String, Object> params = pageVoReq.getCondition() == null ? Maps.newHashMap() : pageVoReq.getCondition();

        List<SalesChannel> salesChannels = Lists.newLinkedList();
        if (params.containsKey("channel") && params.get("channel") != null) {
            List<Integer> salesChannelIds = (List) params.get("channel");
            if (salesChannelIds.size() > 0) {
                SalesChannel salesChannel = new SalesChannel();
                salesChannel.setSalesChannelId(salesChannelIds.get(1) == null ? null : salesChannelIds.get(1) + "");
                salesChannel.setBankId(salesChannelIds.get(0) + "");
                salesChannel.setSalesWayId(salesChannelIds.get(2) + "");
                List<SalesChannel> salesChannels1 = panguInterfaceService.selectByBCW(salesChannel);
                if (!CollectionUtils.isEmpty(salesChannels1)) {
                    salesChannels.addAll(salesChannels1);
                }
            }
        }
        if (params.containsKey("bankTypeId") && params.get("bankTypeId") != null) {
            SalesChannel salesChannel = new SalesChannel();
            salesChannel.setBankId(params.get("bankTypeId") + "");
            List<SalesChannel> salesChannels2 = panguInterfaceService.selectByBankIds(salesChannel);
            if (!CollectionUtils.isEmpty(salesChannels2)) {
                salesChannels.addAll(salesChannels2);
            }
        }
        List<Integer> salesChannelIds = salesChannels.stream().map(obj -> obj.getId()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(salesChannelIds)) {
            params.put("salesChannelIds", salesChannelIds);
        }
        List<ReservOrderVo> result = reservOrderMapper.selectReservOrderPageList(pageVoReq, params);
        Set<Integer> goodsId = result.stream().map(obj -> obj.getGoodsId()).collect(Collectors.toSet());
        Set<Integer> shopId = result.stream().map(obj -> obj.getShopId()).collect(Collectors.toSet());
        List<Goods> goodsList = panguInterfaceService.selectGoodsNameByIds(Lists.newArrayList(goodsId));
        Map<Integer, Goods> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getId, bank -> bank));
        List<ShopBaseMsgVo> shops = panguInterfaceService.shopDetailList(Lists.newArrayList(shopId));
        Map<Integer, ShopBaseMsgVo> shopMap = shops.stream().collect(Collectors.toMap(ShopBaseMsgVo::getId, bank -> bank));

        for (ReservOrderVo vo : result) {
            if (goodsMap.containsKey(vo.getGoodsId())) {
                Goods goodsBaseVo = goodsMap.get(vo.getGoodsId());
                vo.setGoodsName(goodsBaseVo.getShortName());
                vo.setOldKey(goodsBaseVo.getOldKey());
            }
//            vo.setGiftPhone(MaskUtils.mobile(vo.getGiftPhone()));
//            vo.setActivePhone(MaskUtils.mobile(vo.getActivePhone()));
            if (shopMap.containsKey(vo.getShopId())) {
                ShopBaseMsgVo goodsBaseVo = shopMap.get(vo.getShopId());
                vo.setShopName(goodsBaseVo.getName());
                vo.setHotelName(goodsBaseVo.getHotelName());
            }
        }
//        BeanUtils.copyProperties(pageVoReq,pageVoRes);
        return pageVoReq.setRecords(result);
    }

    /**
     * 导出预约单
     *
     * @param pageVoReq
     * @return
     * @throws Exception
     */
    @Override
    public String exportReservOrder(PageVo<ReservOrderVo> pageVoReq) throws Exception {
        String url = null;
        List<String> services = (List<String>) pageVoReq.getCondition().get("serviceTypes");
        if (!CollectionUtils.isEmpty(services)) {
            if (services.size() > 1 && services.contains("accom")) {

            } else {
                Map<String, Object> params = pageVoReq.getCondition() == null ? Maps.newHashMap() : pageVoReq.getCondition();

                List<SalesChannel> salesChannels = Lists.newLinkedList();
                if (params.containsKey("channel") && params.get("channel") != null) {
                    List<Integer> salesChannelIds = (List) params.get("channel");
                    if (salesChannelIds.size() > 0) {
                        SalesChannel salesChannel = new SalesChannel();
                        salesChannel.setSalesChannelId(salesChannelIds.get(1) == null ? null : salesChannelIds.get(1) + "");
                        salesChannel.setBankId(salesChannelIds.get(0) + "");
                        salesChannel.setSalesWayId(salesChannelIds.get(2) + "");
                        List<SalesChannel> salesChannels1 = panguInterfaceService.selectByBCW(salesChannel);
                        if (!CollectionUtils.isEmpty(salesChannels1)) {
                            salesChannels.addAll(salesChannels1);
                        }
                    }
                }
                if (params.containsKey("bankTypeId") && params.get("bankTypeId") != null) {
                    SalesChannel salesChannel = new SalesChannel();
                    salesChannel.setBankId(params.get("bankTypeId") + "");
                    List<SalesChannel> salesChannels2 = panguInterfaceService.selectByBankIds(salesChannel);
                    if (!CollectionUtils.isEmpty(salesChannels2)) {
                        salesChannels.addAll(salesChannels2);
                    }
                }
                List<ReservOrderVo> list = reservOrderMapper.exportReservOrderPageList(pageVoReq, params);
                if (!CollectionUtils.isEmpty(list)) {
                    url = exportItem(services.get(0), list);
                }
            }

        }
        return url;
    }

    /**
     * 订单详细信息查询
     *
     * @param id
     * @return
     */
    @Override
    public ReservOrderVo selectReservOrderById(Integer id) {
        ReservOrderVo equityListVo = reservOrderMapper.selectReservOrderById(id);
        equityListVo.setTags((org.apache.commons.lang.StringUtils.isBlank(equityListVo.getTags()) ? "" : equityListVo.getTags()) + "," + (org.apache.commons.lang.StringUtils.isBlank(equityListVo.getVarTags()) ? "" : equityListVo.getVarTags()));
        //如果是第三方券的预约单，获取券信息
        if (!StringUtils.isEmpty(equityListVo) && equityListVo.getServiceType().indexOf("_cpn") != -1) {
            QueryThirdCouponsInfoReqVO reqVO = new QueryThirdCouponsInfoReqVO();
            reqVO.setCpnThirdCodeId(equityListVo.getThirdCpnNo());
            CommonResultVo<CpnThirdCode> resultVo = remoteThirdCouponsService.getThirdCouponsInfoById(reqVO);
            equityListVo.setCpnThirdCode(null != resultVo ? resultVo.getResult() : null);
        }
        //如果是实物券，获取实物券信息
        if (!StringUtils.isEmpty(equityListVo) && ResourceTypeEnums.OBJECT_CPN.getCode().equals(equityListVo.getServiceType())) {
            LogisticsInfo logisticsInfo = logisticsInfoMapper.selectById(id);
            equityListVo.setLogisticsInfo(logisticsInfo);
        }
        //获取激活人信息
        MemLoginResDTO memLoginResDTO = equityListVo.getMemberId() == null ? null : memberInterfaceService.getMemberFullInfo(equityListVo.getMemberId());
        if (memLoginResDTO != null) {
            equityListVo.setActiveName(memLoginResDTO.getMbName());
            equityListVo.setActivePhone(memLoginResDTO.getMobile());
        }
        if (equityListVo.getGoodsId() != null) {
            GoodsBaseVo goodsBaseVo = panguInterfaceService.selectGoodsById(equityListVo.getGoodsId());
            equityListVo.setGoodsBaseVo(goodsBaseVo);
            List<SysDict> bankList = memberInterfaceService.selectSysDict(SysDictTypeEnums.BANK_TYPE.getType());
            List<SysDict> salesChannelList = memberInterfaceService.selectSysDict(SysDictTypeEnums.SALES_CHANNEL_TYPE.getType());
            List<SysDict> salesWayList = memberInterfaceService.selectSysDict(SysDictTypeEnums.SALES_WAY_TYPE.getType());
            Map<String, SysDict> bankMap = bankList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));
            Map<String, SysDict> salesChannelMap = salesChannelList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));
            Map<String, SysDict> salesWayMap = salesWayList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));
            if (null != goodsBaseVo) {
                SysDict sysDict1 = bankMap.get(goodsBaseVo.getBankId());
                if (null == sysDict1) {
                    goodsBaseVo.setBankName("-");
                } else {
                    goodsBaseVo.setBankName(sysDict1.getLabel());
                }
                SysDict sysDict2 = salesChannelMap.get(goodsBaseVo.getSalesChannelIds().get(1));
                if (null == sysDict2) {
                    goodsBaseVo.setSalesChannelName("-");
                } else {
                    goodsBaseVo.setSalesChannelName(sysDict2.getLabel());
                }
                SysDict sysDict3 = salesWayMap.get(goodsBaseVo.getSalesWayId());
                if (null == sysDict3) {
                    goodsBaseVo.setSalesWayName("-");
                } else {
                    goodsBaseVo.setSalesWayName(sysDict3.getLabel());
                }
            }
        }
        if (equityListVo.getShopId() != null) {
            ShopDetailRes shopDetailRes = panguInterfaceService.getShopDetail(equityListVo.getShopId());
            equityListVo.setShopDetailRes(shopDetailRes);
            if (null != shopDetailRes && shopDetailRes.getShopItemList().size() > 0) {
                for (ShopItem item : shopDetailRes.getShopItemList()) {
                    if (equityListVo.getShopItemId().compareTo(item.getId()) == 0) {
                        equityListVo.setShopItemName(item.getName() + (item.getNeeds() == null ? "" : "|" + item.getNeeds()) + (item.getAddon() == null ? "" : "|" + item.getAddon()));
                    }
                }
            }
        }

        Wrapper<ReservOrderPrice> local = new Wrapper<ReservOrderPrice>() {
            @Override
            public String getSqlSegment() {
                return "where  del_flag = 0 and  reserv_order_id = " + equityListVo.getId();
            }
        };
        List<ReservOrderPrice> reservPrices = reservOrderPriceService.selectList(local);

        if (reservPrices.size() > 0) {
            for (ReservOrderPrice price : reservPrices) {

                ShopSettleMsgRes shopSettleMsgRes = new ShopSettleMsgRes();
                // 如果是预定成功 则取当前数据库结算信息 否则取实时的价格
                if (equityListVo.getProseStatus().equals(ReservOrderStatusEnums.ReservOrderStatus.done.getcode())) {
                    shopSettleMsgRes.setNetPrice(price.getNetPrice());
                    shopSettleMsgRes.setServiceRate(price.getServiceRate());
                    shopSettleMsgRes.setTaxRate(price.getTaxRate());
                    shopSettleMsgRes.setSettleRule(price.getSettleRule());
                    shopSettleMsgRes.setProtocolPrice(price.getProtocolPrice());
                    shopSettleMsgRes.setSettleMethod(price.getSettleMethod());
                    shopSettleMsgRes.setRealPrice(price.getPrice());
                } else {
                    ShopSettleMsgReq shopSettleMsgReq = new ShopSettleMsgReq();
                    shopSettleMsgReq.setBookDate(DateUtil.parse(price.getOrderDate(), "yyyy-MM-dd"));
                    shopSettleMsgReq.setShopId(equityListVo.getShopId());
                    shopSettleMsgReq.setShopItemId(equityListVo.getShopItemId());
                    shopSettleMsgReq.setShopChannelId(equityListVo.getShopChannelId());
                    if (!equityListVo.getServiceType().equals(ResourceTypeEnums.ACCOM.getCode())) {
                        shopSettleMsgReq.setGift(equityListVo.getGiftType());
                    }
                    shopSettleMsgRes = panguInterfaceService.shopSettleMsg(shopSettleMsgReq);
                    if (null == shopSettleMsgRes) {
                        shopSettleMsgRes = new ShopSettleMsgRes();
                    }

                    //TODO 预约成功之前实报价都是0 成功以后才有实报价
                    if (equityListVo.getProseStatus().equals(ReservOrderStatusEnums.ReservOrderStatus.none.getcode()) || equityListVo.getProseStatus().equals(ReservOrderStatusEnums.ReservOrderStatus.process.getcode())) {
                        shopSettleMsgRes.setRealPrice(BigDecimal.ZERO);
                    } else {
                        shopSettleMsgRes.setRealPrice(price.getPrice());
                    }
                }
                shopSettleMsgRes.setBookDate(DateUtil.parse(price.getOrderDate(), "yyyy-MM-dd"));
                shopSettleMsgRes.setNightNumbers(price.getNumber());
                shopSettleMsgRes.setPriceId(price.getId());
                shopSettleMsgRes.setOrderId(equityListVo.getId());
                equityListVo.getShopSettleMsgRes().add(shopSettleMsgRes);
            }
        } else {

        }
        Wrapper<ReservOrderDetail> local2 = new Wrapper<ReservOrderDetail>() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and order_id = " + equityListVo.getId();
            }
        };
        List<ReservOrderDetail> detail = reservOrderDetailService.selectList(local2);
        if (detail.size() > 1) {
            ReservOrderDetail temp = detail.get(0);
            for (ReservOrderDetail obj : detail) {
                if (temp.getId() != obj.getId()) {
                    temp.setBookName(temp.getBookName() + "," + obj.getBookName() + ",");
                    if (!StringUtils.isEmpty(temp.getBookNameEn()) && !StringUtils.isEmpty(obj.getBookNameEn())) {
                        temp.setBookNameEn(temp.getBookNameEn() + "," + obj.getBookNameEn() + ",");
                    }
                }
            }
        } else {
            equityListVo.setDetail(detail.size() == 0 ? null : detail.get(0));
        }
        //TODO....医疗预约  medical other
        if (ResourceTypeEnums.MEDICAL.getCode().equalsIgnoreCase(equityListVo.getServiceType())) {
            Wrapper<ReservOrderHospital> local3 = new Wrapper<ReservOrderHospital>() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0 and order_id = " + id;
                }
            };
            ReservOrderHospital hospital = reservOrderHospitalService.selectOne(local3);
            if (null != hospital) {
                equityListVo.setHospital(hospital);
                CommonResultVo<MemMemberFamily> familyVo = remoteMemberFamilyServcie.selectById(hospital.getMemFamilyId());
                Assert.notNull(familyVo, "请求block接口失败");
                MemMemberFamily family = familyVo.getRecords();
                equityListVo.setHospitalName(family.getName());
                equityListVo.setHospitalSex(family.getSex());
                equityListVo.setHospitalMobile(family.getMobile());
                equityListVo.setHospitalCertificateType(family.getCertificateType());
                equityListVo.setHospitalCertificateNumber(family.getCertificateNumber());
                equityListVo.setMedicalInsuranceType(family.getMedicalInsuranceType());
            }
        }

        ReservCode reservCode = reservCodeService.selectOneReservCode(equityListVo.getId());
        if (null != reservCode && null == reservCode.getVarUseTime() && null != reservCode.getVarExpireTime() && reservCode.getVarExpireTime().getTime() < new Date().getTime()) {
            if (reservCode.getVarStatus().equalsIgnoreCase(HxCodeStatusEnum.HxCodeStatus.NEWONE.getIndex() + "") || reservCode.getVarStatus().equalsIgnoreCase(HxCodeStatusEnum.HxCodeStatus.OVERTIME.getIndex() + "")) {
                reservCode.setVarStatus(HxCodeStatusEnum.HxCodeStatus.OVERTIME.getIndex() + "");
                reservCodeService.updateById(reservCode);
            }

        }
        equityListVo.setReservCode(reservCode);
        equityListVo.setReservPrices(reservPrices);
        equityListVo.setServiceTypeCode(equityListVo.getServiceType());
        equityListVo.setServiceType(ResourceTypeEnums.findByCode(equityListVo.getServiceType()).getName());
        if (null != equityListVo.getGiftType()) {
            equityListVo.setGiftTypeName(GiftTypeEnum.findByCode(equityListVo.getGiftType()).getName());
        }
        return equityListVo;
    }

    /**
     * 变更预约订单信息
     *
     * @param reservOrderVo
     * @return
     */
    @Override
    public ReservOrderVo updateReservOrder(ReservOrderVo reservOrderVo) {

        ReservOrder reservOrder = reservOrderMapper.selectById(reservOrderVo.getId());
        Assert.isTrue(ReservOrderStatusEnums.ReservOrderStatus.canChange(reservOrder.getProseStatus()), "当前订单状态为:" + ReservOrderStatusEnums.ReservOrderStatus.getNameByCode(reservOrder.getProseStatus()));
        //如果预订成功 验证码作废
        if (reservOrder.getProseStatus().equals(ReservOrderStatusEnums.ReservOrderStatus.done.getcode() + "")) {
            if (reservOrder.getReservCodeId() != null) {
                ReservCode reservCode = reservCodeService.selectById(reservOrder.getReservCodeId());
                reservCode.setVarStatus(HxCodeStatusEnum.HxCodeStatus.INVALID.getIndex() + "");
                reservCode.setDelFlag(DelFlagEnums.DELETE.getCode());
                reservCodeService.updateById(reservCode);
            }
        }
        reservOrder.setShopId(reservOrderVo.getShopId());
        reservOrder.setShopItemId(reservOrderVo.getShopItemId());
        reservOrder.setGiftDate(reservOrderVo.getGiftDate());
        reservOrder.setGiftName(reservOrderVo.getGiftName());
        reservOrder.setGiftPhone(reservOrderVo.getGiftPhone());
        reservOrder.setReservRemark(reservOrderVo.getReservRemark());
        if (reservOrderVo.getDispensing() == 1) {
            reservOrderVo.setOldProductId(reservOrder.getProductId());
            if (!reservOrder.getTags().contains("调剂")) {
                reservOrder.setTags(reservOrder.getTags() + "调剂,");
            }
            Product product = new Product();
            product.setShopId(reservOrderVo.getShopId());
            product.setShopItemId(reservOrderVo.getShopItemId());
            product.setGift(reservOrder.getGiftType());

            product = panguInterfaceService.getProductByShop(product);
            if (null != product) {
                reservOrder.setProductId(product.getId());
            } else {
                reservOrder.setProductId(null);
            }
        } else {
            reservOrder.setProductId(reservOrderVo.getProductId());
            reservOrder.getTags().replaceAll(",调剂,", "");
//            reservOrder.setProductGroupProductId(reservOrderVo.getProductGroupProductId());
        }
        reservOrder.setDispensing(reservOrderVo.getDispensing());
        reservOrder.setProseStatus(ReservOrderStatusEnums.ReservOrderStatus.none.getcode() + "");
        reservOrder.setGiftTime(reservOrderVo.getGiftTime());
//        reservOrder.setUpdateUser(SecurityUtils.getLoginName());
        reservOrder.setGiftPeopleNum(reservOrderVo.getGiftPeopleNum());
        reservOrderMapper.updateById(reservOrder);
        Wrapper<ReservOrderDetail> local2 = new Wrapper<ReservOrderDetail>() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and order_id = " + reservOrder.getId();
            }
        };
        ReservOrderDetail detail = reservOrderDetailService.selectOne(local2);
        Assert.notNull(detail, "订单的详细信息有误");
        if (detail != null) {
            detail.setCheckDate(reservOrderVo.getGiftDate());
            detail.setDeparDate(reservOrderVo.getDeparDate());
            detail.setCheckNight(reservOrderVo.getCheckNight());
            detail.setAccoNedds(reservOrderVo.getAccoNedds());
            detail.setUpdateuser(SecurityUtils.getLoginName());
            detail.setNightNumbers(reservOrderVo.getNightNumbers());
            detail.setBookNameEn(reservOrderVo.getDetail() != null ? reservOrderVo.getDetail().getBookNameEn() : null);
            reservOrderDetailService.updateById(detail);
        }
        //TODO....医疗预约  medical other
        if (ResourceTypeEnums.MEDICAL.getCode().equalsIgnoreCase(reservOrder.getServiceType())) {
            //医疗预约
            Wrapper<ReservOrderHospital> local = new Wrapper<ReservOrderHospital>() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0 and order_id = " + reservOrder.getId() + " and id = " + reservOrderVo.getHospitalId();
                }
            };
            ReservOrderHospital hospital = reservOrderHospitalService.selectOne(local);
            if (hospital == null) {
                hospital = new ReservOrderHospital();
            }
            SysHospital sysHospital = hospitalService.selectById(reservOrderVo.getHospitalId());
            Assert.notNull(sysHospital, "医院数据不能为空！");
            Assert.notNull(reservOrderVo.getMemFamilyId(), "就诊人ID不能为空！");
            hospital.setMemFamilyId(reservOrderVo.getMemFamilyId());
            hospital.setHospitalId(sysHospital.getId());
            hospital.setCity(sysHospital.getCity());
            hospital.setProvince(sysHospital.getProvince());
            hospital.setName(sysHospital.getName());
            hospital.setGrade(sysHospital.getGrade());
            hospital.setHospitalType(sysHospital.getHospitalType());
            hospital.setDepartment(reservOrderVo.getDepartment());
            hospital.setVisit(reservOrderVo.getVisit());
            hospital.setSpecial(reservOrderVo.getSpecial());
            final boolean sysHospitalinsert = reservOrderHospitalService.updateById(hospital);
            Assert.isTrue(sysHospitalinsert, "修改预约单医院详情失败");

        }
        Wrapper<ReservOrderPrice> local = new Wrapper<ReservOrderPrice>() {
            @Override
            public String getSqlSegment() {
                return "where  del_flag = 0 and  reserv_order_id = " + reservOrder.getId();
            }
        };
        List<ReservOrderPrice> reservPrices = reservOrderPriceService.selectList(local);
        if (reservPrices.size() > 0) {
            List<Integer> ids = reservPrices.stream().map(obj -> obj.getId()).collect(Collectors.toList());
            reservOrderPriceService.deleteBatchIds(ids);
        }

        this.insertPriceInfo(reservOrderVo, reservOrder);//价格数据
        return reservOrderVo;
    }

    /**
     * 兑换券变更订单信息
     *
     * @param reservOrderVo
     * @return
     */
    @Override
    public ReservOrderVo updateCouponsReservOrder(ReservOrderVo reservOrderVo) {

        ReservOrder reservOrder = reservOrderMapper.selectById(reservOrderVo.getId());
        Assert.isTrue(ReservOrderStatusEnums.ReservOrderStatus.canChange(reservOrder.getProseStatus()), "当前订单状态为:" + ReservOrderStatusEnums.ReservOrderStatus.getNameByCode(reservOrder.getProseStatus()));
        //如果预订成功 验证码作废

        reservOrder.setShopId(reservOrderVo.getShopId());
        reservOrder.setShopItemId(reservOrderVo.getShopItemId());
        reservOrder.setProductId(reservOrderVo.getProductId());

        reservOrderMapper.updateById(reservOrder);
        this.insertPriceInfo(reservOrderVo, reservOrder);//价格数据
        return reservOrderVo;
    }

    @Override
    public List<ReservOrderComInfoVo> getReservOrderComInfoByOrderIds(List<Integer> orderIds) {

        List<ReservOrderComInfoVo> result = new ArrayList<>();

        //根据id获取订单原始信息
        List<ReservOrder> reservOrders = reservOrderMapper.selectReserveOrdersByIds(orderIds);
        reservOrders.forEach(order -> {
//            //获取商品信息,不在这里获取， 直接在盘古中获取
//            Goods goods = panguInterfaceService.findGoodsById(order.getGoodsId());
//
//            //获取产品名,不在这里获取， 直接在盘古中获取
//            ProductItem productItem = panguInterfaceService.selectProductNameById(order.getProductId());
//            //商户信息,不在这里获取， 直接在盘古中获取
//            Shop shop = panguInterfaceService.selectShopByShopId(order.getShopId());
//            //酒店信息,不在这里获取， 直接在盘古中获取
//            Hotel hotel = panguInterfaceService.selectHotelByShopId(order.getShopId());
            //激活码
            GiftCode giftCode = giftCodeService.selectGiftCodeInfo(order.getGiftCodeId());


            ReservOrderComInfoVo info = new ReservOrderComInfoVo();
            info.setOrderId(order.getId());
            info.setActCode(giftCode.getActCode());
            info.setOrderTime(order.getReservDate());
            info.setProductGroupId(order.getProductGroupId());
            info.setGoodsId(order.getGoodsId());
            info.setProductId(order.getProductId());
            info.setShopId(order.getShopId());
            info.setSaleChannelId(order.getSalesChannleId());
//            info.setGoodsName(goods.getName());
//            info.setGoodsId(goods.getId());
//            info.setProductName(productItem.getName());
//            info.setHotelName(hotel.getNameCh());
//            info.setShopName(shop.getName());
            result.add(info);
        });
        return result;
    }

    @Override
    public Boolean updateReservOrderStatusByMap(Map<String, Date> map) {
        List<ReservOrder> bps = new ArrayList<>();
        map.forEach((k, v) -> {
            ReservOrder ro = new ReservOrder();
            ro.setId(Integer.valueOf(k));
            ro.setPayStatus(3);
            bps.add(ro);
        });
        return this.updateBatchById(bps);
    }

    @Override
    public ReservOrder getReservOrderBySalesOrderId(String saleOrderId) {
        return reservOrderMapper.getReservOrderBySalesOrderId(saleOrderId);
    }

    @Override
    public void insertPriceInfo(ReservOrderVo reservOrderVo, ReservOrder reservOrder) {
        //需要保存价格信息
        List<ReservOrderPrice> reservOrderPrices = Lists.newArrayList();
        ShopSettleMsgReq shopSettleMsgReq = new ShopSettleMsgReq();
        shopSettleMsgReq.setBookDate(DateUtil.parse(reservOrder.getGiftDate(), "yyyy-MM-dd"));
        shopSettleMsgReq.setShopId(reservOrder.getShopId());
        shopSettleMsgReq.setShopItemId(reservOrder.getShopItemId());
        shopSettleMsgReq.setShopChannelId(reservOrder.getShopChannelId());
        if (!reservOrder.getServiceType().equals(ResourceTypeEnums.ACCOM.getCode())) {
            shopSettleMsgReq.setGift(reservOrder.getGiftType());
        }
//        shopSettleMsgReq.setGift(reservOrder.getGiftType());
        if (reservOrder.getServiceType().equals(ShopTypeEnums.ACCOM.getCode())) {

            for (int i = 1; i <= reservOrderVo.getNightNumbers(); i++) {
                if (i > 1) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(shopSettleMsgReq.getBookDate());
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    shopSettleMsgReq.setBookDate(c.getTime());
                }
                ReservOrderPrice reservOrderPrice = new ReservOrderPrice();
                reservOrderPrice.setNumber(reservOrderVo.getCheckNight() == null ? 1 : reservOrderVo.getCheckNight());
                reservOrderPrice.setPrice(BigDecimal.ZERO);
                reservOrderPrice.setOrderDate(DateUtil.format(shopSettleMsgReq.getBookDate(), "yyyy-MM-dd"));
                reservOrderPrices.add(reservOrderPrice);
            }
        } else {
            ShopSettleMsgRes shopSettleMsgRes = panguInterfaceService.shopSettleMsg(shopSettleMsgReq);
            ReservOrderPrice reservOrderPrice = new ReservOrderPrice();
            Wrapper<ReservOrderPrice> local = new Wrapper<ReservOrderPrice>() {
                @Override
                public String getSqlSegment() {
                    return "where  del_flag = 0 and  reserv_order_id = " + reservOrder.getId();
                }
            };
            List<ReservOrderPrice> reservPrices = reservOrderPriceService.selectList(local);
            if (!CollectionUtils.isEmpty(reservOrderPrices)) {
                reservOrderPrice = reservOrderPrices.get(0);
            }

            reservOrderPrice.setNumber(1);
            reservOrderPrice.setOrderDate(DateUtil.format(shopSettleMsgReq.getBookDate(), "yyyy-MM-dd"));
            reservOrderPrice.setNetPrice(shopSettleMsgRes.getNetPrice());
            reservOrderPrice.setServiceRate(shopSettleMsgRes.getServiceRate());
            reservOrderPrice.setTaxRate(shopSettleMsgRes.getTaxRate());
            reservOrderPrice.setSettleRule(shopSettleMsgRes.getSettleRule());
            reservOrderPrice.setProtocolPrice(shopSettleMsgRes.getProtocolPrice());
            reservOrderPrice.setSettleMethod(shopSettleMsgRes.getSettleMethod());
            reservOrderPrice.setCurrency(shopSettleMsgRes.getCurrency());
            reservOrderPrices.add(reservOrderPrice);
        }
        Assert.notEmpty(reservOrderPrices, "价格信息有误");
        for (ReservOrderPrice reservOrderPrice : reservOrderPrices) {
            reservOrderPrice.setReservOrderId(reservOrder.getId());
            reservOrderPrice.setUpdateUser(SecurityUtils.getLoginName());
            if (reservOrderPrice.getId() != null) {
                final boolean insert = reservOrderPriceService.updateById(reservOrderPrice);
                Assert.isTrue(insert, "保存价格失败");
            } else {
                final boolean insert = reservOrderPriceService.insert(reservOrderPrice);
                Assert.isTrue(insert, "保存价格失败");
            }

        }
    }

    private SelectBookPayReq getSelectBookPayReq(BookPayReq req) {
        SelectBookPayReq selectBookPayReq = new SelectBookPayReq();
        selectBookPayReq.setProductGroupProductId(req.getProductGroupProductId());
        List<Date> dates = HelpUtils.getDatesBetweenTwoDate(DateUtil.parseDate(req.getStartDate()), DateUtil.parseDate(req.getEndDate()), false);
        selectBookPayReq.setBookDates(dates);
        return selectBookPayReq;
    }

    @Override
    public List<BookBasePaymentRes> selectBookPay(BookPayReq req) {
        SelectBookPayReq selectBookPayReq = getSelectBookPayReq(req);
        List<BookBasePaymentRes> payments = panguInterfaceService.selectBookPay(selectBookPayReq);
        return payments;
    }

    /**
     * 绿通医疗发送邮件
     *
     * @param reservOrderVo
     */
    @Override
    public void sendMailforMedicalOrderData(ReservOrderVo reservOrderVo) {
        String fileName = "预约单" + "-" + System.currentTimeMillis() + ".xlsx";
        String sysUser = "ryan.wu@colourfulchina.com";
        String[] receiveEmailAddress = sysUser.split(",");
        // 发送邮件
        SysEmailSendReqVo sysEmailSendReqVo = new SysEmailSendReqVo();
        // 记录邮件发送记录
        sysEmailSendReqVo.setSubject("【订单管理-导出】" + fileName);
        sysEmailSendReqVo.setFrom(fileDownloadProperties.getSendEmailAddress());
        sysEmailSendReqVo.setTo(receiveEmailAddress);
        sysEmailSendReqVo.setContent("<html><a href='23423423'>" + fileName + "</a></html>");
        log.info("remoteKlfEmailService.send:{}", JSON.toJSONString(sysEmailSendReqVo));
        final CommonResultVo resultVo = remoteKlfEmailService.send(sysEmailSendReqVo);
//                Assert.notNull(resultVo,"邮件发送失败");
        log.info("remoteKlfEmailService.send result:{}", JSON.toJSONString(resultVo));
//                Assert.isTrue(resultVo.getCode()==100,"邮件发送失败");
        // 执行导出

    }

    /**
     * 预订成功
     *
     * @param reservOrderVo
     * @return
     */
    @Override
    public ReservOrderVo reservSuccess(ReservOrderVo reservOrderVo) throws Exception {
        log.info("reservSuccess reservOrderVo:{}", JSON.toJSONString(reservOrderVo));
        ReservOrder reservOrder = reservOrderMapper.selectById(reservOrderVo.getId());
        if (reservOrder.getPayStatus() != PayOrderStatusEnum.PREPAID.getCode() && reservOrder.getTags().contains("自付")) {
            Assert.notNull(null, "自付的订单未付款，无法预约成功！");
            return reservOrderVo;
        }
        ReservOrderDetail detail = reservOrderDetailService.selectOneReservOrderDetail(reservOrder.getId());
        BookPayReq req = new BookPayReq();
        BigDecimal casePayAmoney = BigDecimal.ZERO;
        req.setProductGroupProductId(reservOrder.getProductGroupProductId());
        if (reservOrder.getServiceType() == "accom") {
            req.setStartDate(detail.getCheckDate());
            req.setEndDate(detail.getDeparDate());
        } else {
            req.setStartDate(reservOrder.getGiftDate());
            req.setEndDate(reservOrder.getGiftDate());
        }
        List<BookBasePaymentRes> bookBasePayments = this.selectBookPay(req);
        log.info("bookBasePayments{}", bookBasePayments);
        if (!CollectionUtils.isEmpty(bookBasePayments)) {
            for (int i = 0; i < bookBasePayments.size(); i++) {
                BigDecimal number = bookBasePayments.get(i).getBookPrice();
                casePayAmoney = casePayAmoney.add(number);
                log.info("casePayAmoney{}", casePayAmoney);
            }
        }

        if (casePayAmoney == null && reservOrder.getPayAmount() != null && reservOrder.getPayAmount().intValue() != 0) {
            throw new Exception("订单信息有误，支付金额不符合！");
        }
        if(reservOrder.getUseFreeCount()<reservOrder.getExchangeNum()){
            if (casePayAmoney != null && casePayAmoney.intValue() != 0  && reservOrder.getPayAmount() == null) {
                throw new Exception("订单信息有误，支付金额不符合！");
            }
            if (casePayAmoney != null && reservOrder.getPayAmount() != null && casePayAmoney.compareTo(reservOrder.getPayAmount()) != 0) {
                throw new Exception("订单信息有误，支付金额不符合！");
            }
        }
        Assert.isTrue(ReservOrderStatusEnums.ReservOrderStatus.canSuccess(reservOrder.getProseStatus()), "当前订单状态为:" + ReservOrderStatusEnums.ReservOrderStatus.getNameByCode(reservOrder.getProseStatus()));
        reservOrder.setProseStatus(ReservOrderStatusEnums.ReservOrderStatus.done.getcode() + "");
        reservOrder.setShopChannelId(reservOrderVo.getShopChannelId() == null ? reservOrder.getShopChannelId() : reservOrderVo.getShopChannelId());
        reservOrder.setReservNumber(reservOrderVo.getReservNumber() == null ? reservOrder.getReservNumber() : reservOrderVo.getReservNumber());
        reservOrder.setChannelNumber(reservOrderVo.getChannelNumber() == null ? reservOrder.getChannelNumber() : reservOrderVo.getChannelNumber());
        reservOrder.setSuccessDate(new Date());
        //TODO 验证码的生成  //生成验证码 并发短信
        ReservCode reservCode = new ReservCode();
        if (!reservOrder.getServiceType().equals(ResourceTypeEnums.ACCOM.getCode()) && reservOrder.getServiceType().indexOf("_cpn") == -1) {
            EntityWrapper local = new EntityWrapper();
            local.eq("order_id", reservOrder.getId());
            local.eq("del_flag", DelFlagEnums.NORMAL.getCode());
            reservCode = reservCodeService.selectOne(local);
            log.info("预定成功,生成核销码{}", reservCode);
            String code = CodeUtils.getCodeByRedis(GiftCodeConstants.GIFT_VER_CODE);
            reservOrder.setReservNumber(code);
            if (null == reservCode) {
                reservCode = new ReservCode();
                reservCode.setOrderId(reservOrder.getId());
                reservCode.setProductGroupId(reservOrder.getProductGroupId());
                reservCode.setVarCode(code);
                reservCode.setCreateUser(SecurityUtils.getLoginName());
                reservCode.setVarStatus(HxCodeStatusEnum.HxCodeStatus.NEWONE.getIndex() + "");
                reservCode.setVarCrtTime(DateUtil.parse(reservOrder.getGiftDate(), DatePattern.NORM_DATE_PATTERN));
                Calendar c = Calendar.getInstance();
                c.setTime(reservCode.getVarCrtTime());
                c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                        23, 59, 59);
                reservCode.setVarExpireTime(c.getTime());
                reservCode.setCreateUser(SecurityUtils.getLoginName());
                reservCodeService.insert(reservCode);
            } else {
                reservCode.setVarCode(code);
                reservCode.setVarStatus(HxCodeStatusEnum.HxCodeStatus.NEWONE.getIndex() + "");
                Calendar c = Calendar.getInstance();
                c.setTime(reservCode.getVarCrtTime());
                c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                        23, 59, 59);
                reservCode.setVarExpireTime(c.getTime());
                reservCode.setUpdateuser(SecurityUtils.getLoginName());
                reservCodeService.updateById(reservCode);

            }
        }
        reservOrder.setReservCodeId(reservCode.getId());

        BigDecimal orderSettleAmount = BigDecimal.ZERO;


        List<ShopSettleMsgRes> settleMsgRes = reservOrderVo.getShopSettleMsgRes();
        //商户接单结算信息处理

//        Wrapper priceWrapper = new Wrapper() {
//            @Override
//            public String getSqlSegment() {
//                return "where del_flag = 0 and reserv_order_id ="+reservOrder.getId();
//            }
//        };
//        //获取约单所有价格列表
//        List<ReservOrderPrice> priceList = reservOrderPriceService.selectList(priceWrapper);


//        if ((!StringUtils.isEmpty(reservOrderVo.getTags()) && reservOrderVo.getTags().contains("商户接单")) || (ResourceTypeEnums.DRINK.getCode().equalsIgnoreCase(reservOrder.getServiceType()) || (reservOrder.getServiceType().indexOf("_cpn") != -1 ))){
//>>>>>>> repair-bug
//=======
        if (!StringUtils.isEmpty(reservOrderVo.getTags()) && reservOrderVo.getTags().contains("商户接单")
                || reservOrder.getServiceType().equals(ResourceTypeEnums.DRINK.getCode()) || (reservOrder.getServiceType().indexOf("_cpn") != -1 && (null == reservOrderVo.getPayAmoney() || reservOrderVo.getPayAmoney().compareTo(new BigDecimal(0)) == 0))) {
            Wrapper priceWrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0 and reserv_order_id =" + reservOrder.getId();
                }
            };
            //获取约单所有价格列表
            List<ReservOrderPrice> priceList = reservOrderPriceService.selectList(priceWrapper);
//>>>>>>> 0925-ichido
            if (!CollectionUtils.isEmpty(priceList)) {
                List<ShopSettleMsgRes> settleListTemp = Lists.newArrayList();
                for (ReservOrderPrice reservOrderPrice : priceList) {
                    ShopSettleMsgRes settleTemp = new ShopSettleMsgRes();
                    settleTemp.setPriceId(reservOrderPrice.getId());
                    settleListTemp.add(settleTemp);
                }
                settleMsgRes = settleListTemp;
            }
        }

        Assert.notEmpty(settleMsgRes, "预约单结算信息不能为空");
        for (ShopSettleMsgRes res : settleMsgRes) {
            ReservOrderPrice price = reservOrderPriceService.selectById(res.getPriceId());
            ShopSettleMsgReq shopSettleMsgReq = new ShopSettleMsgReq();
            shopSettleMsgReq.setBookDate(DateUtil.parse(price.getOrderDate(), "yyyy-MM-dd"));
            shopSettleMsgReq.setShopId(reservOrder.getShopId());
            shopSettleMsgReq.setShopItemId(reservOrder.getShopItemId());
            shopSettleMsgReq.setShopChannelId(reservOrder.getShopChannelId());
            if (!reservOrder.getServiceType().equals(ResourceTypeEnums.ACCOM.getCode())) {
                shopSettleMsgReq.setGift(reservOrder.getGiftType());
            }
//            shopSettleMsgReq.setGift(reservOrder.getGiftType());
            ShopSettleMsgRes shopSettleMsgRes = panguInterfaceService.shopSettleMsg(shopSettleMsgReq);
            log.info("shopSettleMsgReq:{} shopSettleMsgRes:{}", JSON.toJSONString(shopSettleMsgReq), JSON.toJSONString(shopSettleMsgRes));
            Assert.isTrue(!(isInternalChannel(reservOrder.getShopChannelId()) && shopSettleMsgRes == null), "公司资源结算信息有误");
            price.setNetPrice(shopSettleMsgRes == null ? "0" : shopSettleMsgRes.getNetPrice());
            price.setServiceRate(shopSettleMsgRes == null ? "0" : shopSettleMsgRes.getServiceRate());
            price.setTaxRate(shopSettleMsgRes == null ? "0" : shopSettleMsgRes.getTaxRate());
            price.setSettleRule(shopSettleMsgRes == null ? "0" : shopSettleMsgRes.getSettleRule());
            price.setProtocolPrice(shopSettleMsgRes == null ? BigDecimal.ZERO : shopSettleMsgRes.getProtocolPrice());
            price.setSettleMethod(shopSettleMsgRes == null ? "0" : shopSettleMsgRes.getSettleMethod());
            price.setCurrency(shopSettleMsgRes == null ? "0" : shopSettleMsgRes.getCurrency());
//                price.setPrice(res.getRealPrice().multiply(res.getNightNumbers() == null? new BigDecimal("1"): new BigDecimal(res.getNightNumbers())));
            price.setPrice(res.getRealPrice() == null ? shopSettleMsgRes.getProtocolPrice() : res.getRealPrice());
            BigDecimal priceTemp = price.getPrice() == null ? BigDecimal.ZERO : price.getPrice();
            BigDecimal number = res.getNightNumbers() == null ? new BigDecimal("1") : new BigDecimal(res.getNightNumbers());
            BigDecimal result = priceTemp.multiply(number);
            orderSettleAmount = orderSettleAmount.add(result);
//                orderSettleAmount = (res.getNightNumbers()  == null ? new  BigDecimal("1"): new BigDecimal(res.getNightNumbers() ).multiply(price.getPrice()));
            final boolean updateById = reservOrderPriceService.updateById(price);
            Assert.isTrue(updateById, "更新预约单价格失败");
        }
        reservOrder.setOrderSettleAmount(orderSettleAmount);
        reservOrder.setOrderSettleAmount(orderSettleAmount);
        orderSettleAmount = orderSettleAmount.add(reservOrder.getOrderDamageAmount() == null ? BigDecimal.ZERO : reservOrder.getOrderDamageAmount());
        reservOrder.setShopAmount(orderSettleAmount);
        reservOrder.setUpdateUser(SecurityUtils.getLoginName());
        if (org.apache.commons.lang.StringUtils.isNotBlank(reservOrderVo.getTags())) {
            reservOrder.setTags(reservOrder.getTags() + reservOrderVo.getTags());
        } else if (orderSettleAmount.intValue() != 0 && reservOrder.getTags().indexOf("贴现") < 0) {
            reservOrder.setTags(reservOrder.getTags() + "贴现,");
        }
        reservOrder.setOperator(SecurityUtils.getLoginName());
        final Integer updateById = reservOrderMapper.updateById(reservOrder);
        Assert.isTrue(updateById == 1, "更新预约单失败");
        //实物券需要自动发送券码
        if (reservOrder.getServiceType().equals(ResourceTypeEnums.OBJECT_CPN.getCode())) {
            QueryProductGroupInfoReqVo queryProductGroupInfoReqVo = new QueryProductGroupInfoReqVo();
            queryProductGroupInfoReqVo.setProductGroupId(reservOrder.getProductGroupId());
            queryProductGroupInfoReqVo.setProductId(reservOrder.getProductId());
            CommonResultVo<List<ProductGroupResVO>> commonResultVo = remoteProductGroupService.selectProductGroupById(queryProductGroupInfoReqVo);
            CouponThirdCodeReqVO reqVO = new CouponThirdCodeReqVO();
            if (null != commonResultVo && !CollectionUtils.isEmpty(commonResultVo.getResult())) {
                ProductGroupResVO productGroupResVO = commonResultVo.getResult().get(0);
                if (null != productGroupResVO && null != productGroupResVO.getShopChannelId()) {
                    reqVO.setSource(ThirdSourceEnum.getCode(productGroupResVO.getShopChannelId()));
                }
            }
            reqVO.setProductNo(reservOrder.getThirdCpnNum());
            reqVO.setReserveOrderId(reservOrder.getId());
            reqVO.setNum(1);
            couponsService.putThirdCoupons(reqVO);
        }
        //预定成功，判断该权益是否用完
        Set<Integer> ids = Sets.newHashSet();
        ids.add(reservOrder.getGiftCodeId());
        giftCodeService.actCodesIsRunOut(ids);
        //预约成功调用pangu商户销售次数增加接口
        if (null != reservOrder.getShopId()) {
            remoteShopService.shopSalesUp(reservOrder.getShopId());
        }
        //发送短信
        nuwaInterfaceService.sendMsg(reservOrder);

        return reservOrderVo;
    }

    /**
     * 预订失败
     *
     * @param reservOrderVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReservOrderVo reservFail(ReservOrderVo reservOrderVo) throws Exception {
        ReservOrder reservOrder = reservOrderMapper.selectById(reservOrderVo.getId());
        Assert.notNull(reservOrder, "预约单不存在");
        Assert.isTrue(ReservOrderStatusEnums.ReservOrderStatus.canFail(reservOrder.getProseStatus()), "当前订单状态为：" + ReservOrderStatusEnums.ReservOrderStatus.getNameByCode(reservOrder.getProseStatus()));
        reservOrder.setProseStatus(ReservOrderStatusEnums.ReservOrderStatus.failed.getcode() + "");
        reservOrder.setFailReason(reservOrderVo.getFailReason());
        reservOrder.setFailDate(new Date());
        reservOrderVo.setFailInter(reservOrderVo.getFailInter());//是否退回权益
        if (null != reservOrderVo.getFailInter() && reservOrderVo.getFailInter().equals("1")) {
            //保存完以后需要更新激活码次数表
            EquityCodeDetail equityCodeDetail = equityCodeDetailService.selectByEquityCode(reservOrder.getMemberId(), reservOrder.getGoodsId(), reservOrder.getProductGroupId(), reservOrder.getGiftCodeId(), reservOrder.getCreateTime());
            Assert.notNull(equityCodeDetail, "未找到权益明细");
            if (null != equityCodeDetail) {
                equityCodeDetail.setUseCount((equityCodeDetail.getUseCount() == null ? 0 : equityCodeDetail.getUseCount()) - (reservOrder.getExchangeNum() == null ? 1 : reservOrder.getExchangeNum()));
                Assert.isTrue(equityCodeDetail.getUseCount() >= 0, "权益使用次数有误");
                Assert.isTrue(equityCodeDetail.getTotalCount() == null || equityCodeDetail.getTotalCount().compareTo(equityCodeDetail.getUseCount()) >= 0, "权益使用次数有误");
                final boolean updateById = equityCodeDetailService.updateById(equityCodeDetail);
                Assert.isTrue(updateById, "权益次数更新失败");
            }
            reservOrder.setTags(reservOrder.getTags() + "退回权益,");
        }
        if (org.apache.commons.lang.StringUtils.isNotBlank(reservOrderVo.getTags())) {
            reservOrder.setTags(reservOrder.getTags() + reservOrderVo.getTags());
        }
        BigDecimal orderSettleAmount = BigDecimal.ZERO;
        reservOrderPriceService.updateReservOrderPrice(reservOrder);
        reservOrder.setOrderSettleAmount(orderSettleAmount);
        orderSettleAmount = orderSettleAmount.add(reservOrder.getOrderDamageAmount() == null ? BigDecimal.ZERO : reservOrder.getOrderDamageAmount());
        reservOrder.setShopAmount(orderSettleAmount);

        reservOrder.setUpdateUser(SecurityUtils.getLoginName());

        ReservCode reservCode = reservCodeService.selectOneReservCode(reservOrderVo.getId());
        if (null != reservCode) {
            reservCode.setVarStatus(HxCodeStatusEnum.HxCodeStatus.INVALID.getIndex() + "");
            Calendar c = Calendar.getInstance();
            c.setTime(DateUtil.parse(reservOrder.getGiftDate(), DatePattern.NORM_DATE_PATTERN));
            c.add(Calendar.HOUR_OF_DAY, 23);
            c.add(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            reservCode.setVarExpireTime(c.getTime());
            reservCodeService.updateById(reservCode);
        }

        //预定失败，判断该权益是否用完
        Set<Integer> ids = Sets.newHashSet();
        ids.add(reservOrder.getGiftCodeId());
        reservOrder.setOperator(SecurityUtils.getLoginName());
        giftCodeService.actCodesIsRunOut(ids);

        processRefund(reservOrderVo, reservOrder);
        Integer updateOrder = reservOrderMapper.updateById(reservOrder);
        Assert.isTrue(updateOrder == 1, "预约单更新失败！");
        if (null != reservOrderVo.getMessageFlag() && reservOrderVo.getMessageFlag().compareTo(1) == 0) {
            nuwaInterfaceService.sendMsg(reservOrder);
        }
        //实物券需要退货物流
        if (reservOrder.getServiceType().equals(ResourceTypeEnums.OBJECT_CPN.getCode())) {
            if (reservOrderVo.getExpressFlag().compareTo(1) == 0) {
                LogisticsInfo logisticsInfo = new LogisticsInfo();
                logisticsInfo.setReservOrderId(reservOrder.getId());
                logisticsInfo.setStatus(ExpressStatusEnum.CAN_REFUND.getCode());
                logisticsInfoMapper.updateById(logisticsInfo);
            }
        }
        return reservOrderVo;
    }

    /**
     * 预约单取消
     *
     * @param reservOrderVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReservOrderVo reservCancel(ReservOrderVo reservOrderVo) throws Exception {
        ReservOrder reservOrder = reservOrderMapper.selectById(reservOrderVo.getId());
        Assert.isTrue(ReservOrderStatusEnums.ReservOrderStatus.canCancel(reservOrder.getProseStatus()), "当前订单状态为：" + ReservOrderStatusEnums.ReservOrderStatus.getNameByCode(reservOrder.getProseStatus()));
        //用户自行取消时限制除了尚未预定其他状态不能取消
        if (org.apache.commons.lang.StringUtils.isNotBlank(reservOrderVo.getTags()) && "用户自行取消".equals(reservOrderVo.getTags())) {
            if (!reservOrder.getProseStatus().equals(ReservOrderStatusEnums.ReservOrderStatus.none.getcode())) {
                throw new Exception("当前订单状态为：" + ReservOrderStatusEnums.ReservOrderStatus.getNameByCode(reservOrder.getProseStatus()) + "，无法取消");
            }
        }
        reservOrder.setProseStatus(ReservOrderStatusEnums.ReservOrderStatus.cancel.getcode() + "");
        reservOrder.setCancelReason(reservOrderVo.getCancelReason());
        reservOrder.setCancelDate(new Date());
        reservOrder.setCancelIsPay(reservOrderVo.getCancelIsPay());
        reservOrder.setOrderDamageAmount(reservOrderVo.getOrderDamageAmount());
        reservOrder.setRefundInter(reservOrderVo.getRefundInter());//是否退回权益
        if (null != reservOrderVo.getRefundInter() && reservOrderVo.getRefundInter() == 1) {
            //保存完以后需要更新激活码次数表
            EquityCodeDetail equityCodeDetail = equityCodeDetailService.selectByEquityCode(reservOrder.getMemberId(), reservOrder.getGoodsId(), reservOrder.getProductGroupId(), reservOrder.getGiftCodeId(),reservOrder.getCreateTime());
                if (null != equityCodeDetail) {
                    equityCodeDetail.setUseCount((equityCodeDetail.getUseCount() == null ? 0 : equityCodeDetail.getUseCount()) - (reservOrder.getExchangeNum()==null?0:reservOrder.getExchangeNum()));
                    equityCodeDetail.setUseFreeCount((equityCodeDetail.getUseFreeCount() == null ? 0 : equityCodeDetail.getUseFreeCount()) - (reservOrder.getUseFreeCount()==null?0:reservOrder.getUseFreeCount()));
                    Assert.isTrue(equityCodeDetail.getUseCount()>=0,"权益使用次数有误");
                    Assert.isTrue(equityCodeDetail.getTotalCount() == null || equityCodeDetail.getTotalCount().compareTo(equityCodeDetail.getUseCount())>=0,"权益使用次数有误");
                    final boolean updateById = equityCodeDetailService.updateById(equityCodeDetail);
                Assert.isTrue(updateById,"权益次数更新失败");
            }
            reservOrder.setTags(reservOrder.getTags() + "退回权益,");
        }
        if (org.apache.commons.lang.StringUtils.isNotBlank(reservOrderVo.getTags())) {
            reservOrder.setTags(reservOrder.getTags() + reservOrderVo.getTags());
        }
        reservOrder.setUpdateUser(SecurityUtils.getLoginName());
        reservOrder.setCancelTime(DateUtil.formatTime(new Date()));
        BigDecimal orderSettleAmount = BigDecimal.ZERO;
        reservOrderPriceService.updateReservOrderPrice(reservOrder);
        reservOrder.setOrderSettleAmount(orderSettleAmount);
        orderSettleAmount = orderSettleAmount.add(reservOrder.getOrderDamageAmount() == null ? BigDecimal.ZERO : reservOrder.getOrderDamageAmount());
        reservOrder.setShopAmount(orderSettleAmount);

        if (null != reservOrderVo.getCancelDamageAmount() && reservOrderVo.getCancelDamageAmount().intValue() != 0) {
            reservOrder.setTags(reservOrder.getTags() + "商户赔付,");
        }
        ReservCode reservCode = reservCodeService.selectOneReservCode(reservOrderVo.getId());
        if (reservCode != null) {
            reservCode.setVarStatus(HxCodeStatusEnum.HxCodeStatus.INVALID.getIndex() + "");
            Calendar c = Calendar.getInstance();
            c.setTime(reservCode.getVarCrtTime());
            c.add(Calendar.HOUR_OF_DAY, 23);
            c.add(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            reservCode.setVarExpireTime(c.getTime());
            boolean updateById = reservCodeService.updateById(reservCode);
            Assert.isTrue(updateById, "核销码更新失败");
        }
        //预定取消，判断权益是否用完
        Set<Integer> ids = Sets.newHashSet();
        ids.add(reservOrder.getGiftCodeId());
        giftCodeService.actCodesIsRunOut(ids);
        //退款
        reservOrder.setOperator(SecurityUtils.getLoginName());
        processRefund(reservOrderVo, reservOrder);
        Integer updateOrder = reservOrderMapper.updateById(reservOrder);
        Assert.isTrue(updateOrder == 1, "更新预约单失败");
        if (null != reservOrderVo.getMessageFlag() && reservOrderVo.getMessageFlag().compareTo(1) == 0) {
            nuwaInterfaceService.sendMsg(reservOrder);
        }
        //如果是实物  物流信息改为已退货3
        if (reservOrder.getServiceType().equals(ResourceTypeEnums.OBJECT_CPN.getCode())) {
            if (reservOrderVo.getExpressFlag().compareTo(1) == 0) {
                LogisticsInfo logisticsInfo = new LogisticsInfo();
                logisticsInfo.setReservOrderId(reservOrder.getId());
                logisticsInfo.setStatus(ExpressStatusEnum.CAN_REFUND.getCode());
                logisticsInfoMapper.updateById(logisticsInfo);
            }
        }
        return reservOrderVo;
    }

    /**
     * 处理退款逻辑
     *
     * @param reservOrderVo
     * @param reservOrder
     * @throws Exception
     */
    private void processRefund(ReservOrderVo reservOrderVo, ReservOrder reservOrder) throws Exception {
        //状态为已支付 且tag包含自付,支付宝钻铂免费项目428不退款，此项目yandi定时跑批在支付宝小程序退款
        if (reservOrder.getTags().contains("自付") && reservOrder.getPayStatus().compareTo(PayOrderStatusEnum.PREPAID.getCode()) == 0 && reservOrder.getGoodsId() != 428) {
            ReservOrderDetail orderDetail = reservOrderVo.getDetail();
            ReservOrderDetail detail = reservOrderDetailService.selectOneReservOrderDetail(reservOrder.getId());
            if (detail == null) {
                log.error("预约详情为空:{}", JSON.toJSONString(reservOrder));
                return;
            }
            if (orderDetail == null) {
                //是否退款
                detail.setBackAmountStatus(1);
                detail.setBackAmount(detail.getPayAmoney());
            } else {
                detail.setBackAmountStatus(orderDetail.getBackAmountStatus());
                detail.setBackAmount(orderDetail.getBackAmount());
            }
            //如果退款标记为1  且退款金额大于0  则进行退款
            if (detail.getBackAmountStatus().compareTo(1) == 0 && detail.getBackAmount().compareTo(BigDecimal.ZERO) > 0) {
                final String orderSource = reservOrder.getOrderSource();
                Assert.notNull(orderSource, "预约渠道终端不存在");
                final SourceMerchantInfo sourceMerchantInfo = sourceMerchantInfoMapper.selectById(orderSource);
                Assert.notNull(sourceMerchantInfo, "未查到预约单对应的支付渠道信息");
                //TODO 退款给商户 记录退款信息到数据库
                reservOrderVo.setPayStatus(PayOrderStatusEnum.UNREFUND.getCode());
                detail.setBackAmount(detail.getBackAmount());
                detail.setBackAmountDate(new Date());
                reservOrder.setPayStatus(PayOrderStatusEnum.UNREFUND.getCode());
                OrderRefundReq orderRefundReq = new OrderRefundReq();
                orderRefundReq.setMerchantId(sourceMerchantInfo.getMerchantId());
                orderRefundReq.setOrderId(reservOrder.getId() + "");
                orderRefundReq.setPaymentType("预约单");
                orderRefundReq.setRefundAmount(detail.getBackAmount());
                orderRefundReq.setRefundAmount(detail.getBackAmount());
                String sign = PayDigestUtil.getSign(JSON.parseObject(JSON.toJSONString(orderRefundReq)), sourceMerchantInfo.getMerchantKey());
                orderRefundReq.setSign(sign);
                final String resultStr = payInfoService.payOrderRefund(orderRefundReq);
                log.info("reservOrder {} payOrderRefund resultStr {}", reservOrder.getId(), resultStr);
                detail.setBackAmountStatus(PayOrderStatusEnum.UNREFUND.getCode());
                if (resultStr.equalsIgnoreCase("success")) {
                    reservOrder.setPayStatus(PayOrderStatusEnum.REFUND.getCode());
                    detail.setBackAmountStatus(PayOrderStatusEnum.REFUND.getCode());
                    detail.setBackAmountDate(new Date());
                } else if (resultStr.equalsIgnoreCase("fail")) {
                    detail.setBackFailResean("退款失败");
                    Assert.notNull(null, "退款失败!");
                } else {
                    detail.setBackFailResean(resultStr);
                    Assert.notNull(null, "退款失败!");
                }
                final boolean updateById = reservOrderDetailService.updateById(detail);
                Assert.isTrue(updateById, "更新预约单详情失败");
            }
        } else {
            log.info("非自付订单无需退款:{}", JSON.toJSONString(reservOrder));
        }
    }


    /**
     * 修正
     *
     * @param reservOrderVo
     * @return
     */
    @Override
    public ReservOrderVo reservUpdateInfo(ReservOrderVo reservOrderVo) {
        ReservOrder reservOrder = reservOrderMapper.selectById(reservOrderVo.getId());
        reservOrder.setShopChannelId(reservOrderVo.getShopChannelId());
        reservOrder.setChannelNumber(reservOrderVo.getChannelNumber());
        BigDecimal orderSettleAmount = BigDecimal.ZERO;
        if (reservOrderVo.getShopSettleMsgRes().size() > 0) {
            for (ShopSettleMsgRes res : reservOrderVo.getShopSettleMsgRes()) {
                ReservOrderPrice price = reservOrderPriceService.selectById(res.getPriceId());
                price.setPrice(res.getRealPrice());
                BigDecimal priceTemp = price.getPrice() == null ? BigDecimal.ZERO : price.getPrice();
                BigDecimal number = res.getNightNumbers() == null ? new BigDecimal("1") : new BigDecimal(res.getNightNumbers());
                BigDecimal result = priceTemp.multiply(number);
                orderSettleAmount = orderSettleAmount.add(result);
                reservOrderPriceService.updateById(price);
            }
        }
        reservOrder.setUpdateUser(SecurityUtils.getLoginName());
        reservOrder.setOrderSettleAmount(orderSettleAmount);
        reservOrder.setOrderDamageAmount(reservOrderVo.getOrderDamageAmount());
        if (reservOrderVo.getOrderDamageAmount() != null && reservOrderVo.getOrderDamageAmount().intValue() != 0 && reservOrder.getTags().indexOf("商户赔付") < 0) {
            reservOrder.setTags(reservOrder.getTags() + ",商户赔付");
        }
        reservOrder.setShopAmount(reservOrder.getOrderSettleAmount().add(reservOrderVo.getOrderDamageAmount() == null ? BigDecimal.ZERO : reservOrderVo.getOrderDamageAmount()));//应付金额
        reservOrderMapper.updateById(reservOrder);

        if (reservOrder.getProseStatus().compareTo(ReservOrderStatusEnums.ReservOrderStatus.done.getcode()) == 0) {
            ReservCode reservCode = reservCodeService.selectOneReservCode(reservOrder.getId());
            if (reservCode.getVarStatus().compareTo(HxCodeStatusEnum.HxCodeStatus.USED.getIndex() + "") == 0) {
                EntityWrapper<VerifyCodesHistory> local = new EntityWrapper<>();
                local.eq("order_no", reservOrder.getId());
                VerifyCodesHistory verifyCodesHistory = verifyCodesHistoryService.selectOne(local);
                if (null != verifyCodesHistory) {
                    verifyCodesHistory.setAmount(reservOrder.getShopAmount() == null ? "0" : reservOrder.getShopAmount().toString());
                    verifyCodesHistoryService.updateById(verifyCodesHistory);
                } else {
                    log.info("当前订单没有核销数据，请确认：{}", reservOrder.getId());
                }
            }
        }

        if (!reservOrder.getServiceType().equals(ResourceTypeEnums.ACCOM.getCode())) {
//            ReservCode reservCode = reservCodeService.selectOneReservCode(reservOrder.getId());
//            reservCode.setVarStatus(HxCodeStatusEnum.HxCodeStatus.NEWONE.getIndex() + "");
//            Calendar c = Calendar.getInstance();
//            c.setTime(reservCode.getVarCrtTime());
//            c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
//                    23, 59, 59);
//            reservCode.setVarExpireTime(c.getTime());
//            reservCodeService.updateById(reservCode);
        }
        return reservOrderVo;
    }


    /**
     * 根据类型导出Excel
     *
     * @param type
     * @param list
     * @return
     */
    private String exportItem(String type, List<ReservOrderVo> list) {
        //获取商品列表
//        List<Goods> goodsList = panguInterfaceService.selectGoodsList();
//        Map<Integer,Goods> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getId,goods -> goods));
        //获取销售渠道信息
//        List<GoodsChannelRes> salesChannels = panguInterfaceService.selectSalesChannelList();
//        Map<Integer,GoodsChannelRes> salesChannelsMap = salesChannels.stream().collect(Collectors.toMap(GoodsChannelRes::getId,goodsChannelRes -> goodsChannelRes));
        //获取商户渠道列表
        List<ShopChannel> shopChannels = (List<ShopChannel>) redisTemplate.opsForValue().get(RedisKeys.MARS_EXPORT_SHOPS_CHANNEL);
        if (shopChannels == null || shopChannels.isEmpty()) {
            shopChannels = panguInterfaceService.selectChannelList();
            if (shopChannels != null) {
                redisTemplate.opsForValue().set(RedisKeys.MARS_EXPORT_SHOPS_CHANNEL, shopChannels, 15, TimeUnit.MINUTES);
            }
        }
        Map<Integer, ShopChannel> shopChannelsMap = shopChannels.stream().collect(Collectors.toMap(ShopChannel::getId, shopChannel -> shopChannel));

        String url = null;
        ExcelWriter excelWriter = new ExcelWriter(true);
        String fileName = "预约单" + "-" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
        excelWriter.setDestFile(new File(fileDownloadProperties.getPath() + "/" + fileName));
//        excelWriter.setDestFile(new File("F:\\download"+"/"+fileName));
        excelWriter.setOrCreateSheet("sheet1");
        excelWriter.writeHeadRow(ExportReservOrderUtils.headRow(type));
        if (!CollectionUtils.isEmpty(list)) {
            //获取所有商户规格信息
            List<Integer> items = list.stream().map(reservOrderVo -> reservOrderVo.getShopItemId()).collect(Collectors.toList());
            List<ShopItemConciseRes> shopItemList = panguInterfaceService.selectByItems(items);
            Map<Integer, ShopItemConciseRes> shopItemListMap = shopItemList.stream().collect(Collectors.toMap(ShopItemConciseRes::getId, shopItemConciseRes -> shopItemConciseRes));
            //获取所有member信息
            List<Long> acids = list.stream().map(reservOrderVo -> reservOrderVo.getMemberId()).collect(Collectors.toList());
            List<MemSimpleRes> memList = memberInterfaceService.selectMemByAcids(acids);
            Map<Long, MemSimpleRes> memListMap = memList.stream().collect(Collectors.toMap(MemSimpleRes::getAcid, memSimpleRes -> memSimpleRes));
            //非住宿
            if (!ShopTypeEnums.ACCOM.getCode().equals(type)) {

                for (ReservOrderVo record : list) {
                    Goods goods = null;
                    if (record.getGoodsId() != null) {
                        goods = (Goods) redisTemplate.opsForValue().get(RedisKeys.MARS_EXPORT_GOODS_ID + record.getGoodsId());
                        if (goods == null) {
                            goods = panguInterfaceService.findGoodsById(record.getGoodsId());
                            if (goods != null) {
                                redisTemplate.opsForValue().set(RedisKeys.MARS_EXPORT_GOODS_ID + record.getGoodsId(), goods, 15, TimeUnit.MINUTES);
                            }
                        }
                    }
                    GoodsChannelRes goodsChannelRes = null;
                    if (record.getSalesChannleId() != null) {
                        goodsChannelRes = (GoodsChannelRes) redisTemplate.opsForValue().get(RedisKeys.MARS_EXPORT_SALES_CHANNEL + record.getSalesChannleId());
                        if (goodsChannelRes == null) {
                            goodsChannelRes = panguInterfaceService.findChannelById(record.getSalesChannleId());
                            if (goodsChannelRes != null) {
                                redisTemplate.opsForValue().set(RedisKeys.MARS_EXPORT_SALES_CHANNEL + record.getSalesChannleId(), goodsChannelRes, 15, TimeUnit.MINUTES);
                            }
                        }
                    }
                    ShopChannel shopChannel = null;
                    if (record.getShopChannelId() != null) {
                        shopChannel = shopChannelsMap.get(record.getShopChannelId());
                    }
                    //权益人信息
                    MemSimpleRes memSimpleRes = null;
                    if (record.getMemberId() != null && !CollectionUtils.isEmpty(memListMap) && memListMap.get(record.getMemberId()) != null) {
                        memSimpleRes = memListMap.get(record.getMemberId());
                    }

                    List<Object> rowData = Lists.newArrayList();
                    rowData.add(record.getRank());
                    rowData.add(record.getId());
                    rowData.add(DateUtil.format(record.getCreateTime(), "yyyy年MM月"));
                    rowData.add(record.getGoodsId());
                    rowData.add(goods == null ? null : goods.getShortName());
                    rowData.add(goodsChannelRes == null ? null : goodsChannelRes.getBankName() + "/" + goodsChannelRes.getSalesChannelName() + "/" + goodsChannelRes.getSalesWayName());
                    rowData.add(record.getActOutDate() == null ? null : DateUtil.format(record.getActOutDate(), "yyyy"));
                    rowData.add(record.getActOutDate() == null ? null : DateUtil.format(record.getActOutDate(), "yyyy年MM月"));
                    rowData.add(StringUtils.isEmpty(record.getGiftType()) ? null : GiftTypeEnum.findByCode(record.getGiftType()).getName());
                    rowData.add(StringUtils.isEmpty(record.getServiceType()) ? null : ResourceTypeEnums.findByCode(record.getServiceType()).getName());
                    rowData.add(DateUtil.format(record.getCreateTime(), "yyyy/MM/dd"));
                    rowData.add(record.getOrderCreator());
                    rowData.add(ReservOrderStatusEnums.ReservOrderStatus.getNameByCode(record.getProseStatus()));
                    if (ReservOrderStatusEnums.ReservOrderStatus.none.getcode().equals(record.getProseStatus())) {
                        rowData.add(DateUtil.format(record.getCreateTime(), "yyyy/MM/dd"));
                    } else if (ReservOrderStatusEnums.ReservOrderStatus.done.getcode().equals(record.getProseStatus())) {
                        rowData.add(DateUtil.format(record.getSuccessDate(), "yyyy/MM/dd"));
                    } else if (ReservOrderStatusEnums.ReservOrderStatus.cancel.getcode().equals(record.getProseStatus())) {
                        rowData.add(DateUtil.format(record.getCancelDate(), "yyyy/MM/dd"));
                    } else if (ReservOrderStatusEnums.ReservOrderStatus.failed.getcode().equals(record.getProseStatus())) {
                        rowData.add(DateUtil.format(record.getFailDate(), "yyyy/MM/dd"));
                    } else if (ReservOrderStatusEnums.ReservOrderStatus.process.getcode().equals(record.getProseStatus())) {
                        rowData.add(DateUtil.format(record.getReservDate(), "yyyy/MM/dd"));
                    }
                    rowData.add(record.getActCode());
                    rowData.add(record.getVarCode());
                    rowData.add(record.getActExpireTime() == null ? null : DateUtil.format(record.getActExpireTime(), "yyyy/MM/dd"));
                    rowData.add(memSimpleRes == null ? null : memSimpleRes.getMbName());
                    rowData.add(memSimpleRes == null ? null : memSimpleRes.getMobile());
                    rowData.add(StringUtils.isEmpty(record.getGiftDate()) ? null : DateUtil.format(DateUtil.parse(record.getGiftDate(), "yyyy-MM-dd"), "yyyy年MM月"));
                    rowData.add(StringUtils.isEmpty(record.getGiftDate()) ? null : DateUtil.format(DateUtil.parse(record.getGiftDate(), "yyyy-MM-dd"), "yyyy/MM/dd"));
                    rowData.add(shopItemListMap.get(record.getShopItemId()).getShopItemName());//餐型
                    rowData.add(record.getGiftPeopleNum());//用餐人数
                    rowData.add(record.getGiftName());//用餐人姓名
                    rowData.add(shopItemListMap.get(record.getShopItemId()).getHotelName());//酒店名
                    rowData.add(shopItemListMap.get(record.getShopItemId()).getShopName());//餐厅名称
                    rowData.add(shopItemListMap.get(record.getShopItemId()).getHotelName() + "/" + shopItemListMap.get(record.getShopItemId()).getShopName());//酒店名/餐厅名
                    rowData.add(shopChannel == null ? null : shopChannel.getName());//预定渠道
                    rowData.add(shopItemListMap.get(record.getShopItemId()).getCityName());//所属地区
                    rowData.add(StringUtils.isEmpty(record.getVarStatus()) ? null : HxCodeStatusEnum.HxCodeStatus.findNameByIndex(Integer.valueOf(record.getVarStatus())));//使用状态
                    rowData.add(record.getProtocolPrice());//协议价格
                    rowData.add(record.getNetPrice());//净价
                    rowData.add(record.getServiceRate());//服务费
                    rowData.add(record.getTaxRate());//增值税
                    rowData.add(record.getSettleRule());//结算规则
                    rowData.add(record.getSettleMethod());//结算方式
                    rowData.add(record.getShopAmount());//贴现金额
                    rowData.add(null);//银行贴现金额
                    rowData.add(record.getUseCount());//权益已使用次数
                    rowData.add(record.getTotalCount() == null ? "无限制" : record.getTotalCount() - record.getUseCount());//权益剩余次数
                    excelWriter.writeRow(rowData);
                }
                url = fileDownloadProperties.getUrl() + fileName;
            } else {
                for (ReservOrderVo record : list) {
                    Goods goods = null;
                    if (record.getGoodsId() != null) {
                        goods = (Goods) redisTemplate.opsForValue().get(RedisKeys.MARS_EXPORT_GOODS_ID + record.getGoodsId());
                        if (goods == null) {
                            goods = panguInterfaceService.findGoodsById(record.getGoodsId());
                            if (goods != null) {
                                redisTemplate.opsForValue().set(RedisKeys.MARS_EXPORT_GOODS_ID + record.getGoodsId(), goods, 15, TimeUnit.MINUTES);
                            }
                        }
                    }
                    GoodsChannelRes goodsChannelRes = null;
                    if (record.getSalesChannleId() != null) {
                        goodsChannelRes = (GoodsChannelRes) redisTemplate.opsForValue().get(RedisKeys.MARS_EXPORT_SALES_CHANNEL + record.getSalesChannleId());
                        if (goodsChannelRes == null) {
                            goodsChannelRes = panguInterfaceService.findChannelById(record.getSalesChannleId());
                            if (goodsChannelRes != null) {
                                redisTemplate.opsForValue().set(RedisKeys.MARS_EXPORT_SALES_CHANNEL + record.getSalesChannleId(), goodsChannelRes, 15, TimeUnit.MINUTES);
                            }
                        }
                    }
                    ShopChannel shopChannel = null;
                    if (record.getShopChannelId() != null) {
                        shopChannel = shopChannelsMap.get(record.getShopChannelId());
                    }
                    //权益人信息
                    MemSimpleRes memSimpleRes = null;
                    if (record.getMemberId() != null && !CollectionUtils.isEmpty(memListMap) && memListMap.get(record.getMemberId()) != null) {
                        memSimpleRes = memListMap.get(record.getMemberId());
                    }

                    List<Object> rowData = Lists.newArrayList();
                    rowData.add(StringUtils.isEmpty(record.getServiceType()) ? null : ResourceTypeEnums.findByCode(record.getServiceType()).getName());
                    rowData.add(record.getId());
                    rowData.add(record.getOrderCreator());
                    rowData.add(record.getOperator());
                    rowData.add(ReservOrderStatusEnums.ReservOrderStatus.getNameByCode(record.getProseStatus()));
                    if (ReservOrderStatusEnums.ReservOrderStatus.none.getcode().equals(record.getProseStatus())) {
                        rowData.add(record.getCreateTime() == null ? null : DateUtil.format(record.getCreateTime(), "yyyy/MM/dd"));
                    } else if (ReservOrderStatusEnums.ReservOrderStatus.done.getcode().equals(record.getProseStatus())) {
                        rowData.add(record.getSuccessDate() == null ? null : DateUtil.format(record.getSuccessDate(), "yyyy/MM/dd"));
                    } else if (ReservOrderStatusEnums.ReservOrderStatus.cancel.getcode().equals(record.getProseStatus())) {
                        rowData.add(record.getCancelDate() == null ? null : DateUtil.format(record.getCancelDate(), "yyyy/MM/dd"));
                    } else if (ReservOrderStatusEnums.ReservOrderStatus.failed.getcode().equals(record.getProseStatus())) {
                        rowData.add(record.getFailDate() == null ? null : DateUtil.format(record.getFailDate(), "yyyy/MM/dd"));
                    } else if (ReservOrderStatusEnums.ReservOrderStatus.process.getcode().equals(record.getProseStatus())) {
                        rowData.add(record.getReservDate() == null ? null : DateUtil.format(record.getReservDate(), "yyyy/MM/dd"));
                    }
                    rowData.add(record.getSuccessDate() == null ? null : DateUtil.format(record.getSuccessDate(), "yyyy/MM/dd"));
                    rowData.add(record.getCancelDate() == null ? null : DateUtil.format(record.getCancelDate(), "yyyy/MM/dd"));
                    rowData.add(record.getCancelReason());
                    rowData.add(record.getFailDate() == null ? null : DateUtil.format(record.getFailDate(), "yyyy/MM/dd"));
                    rowData.add(record.getFailReason());
                    rowData.add(record.getDispensing().compareTo(1) == 0 ? "是" : "否");
                    rowData.add(record.getCreateTime() == null ? null : DateUtil.format(record.getCreateTime(), "yyyy年"));
                    rowData.add(record.getCreateTime() == null ? null : DateUtil.format(record.getCreateTime(), "yyyy年MM月"));
                    rowData.add(goodsChannelRes == null ? null : goodsChannelRes.getBankName());
                    rowData.add(goodsChannelRes == null ? null : goodsChannelRes.getSalesChannelName());
                    rowData.add(goodsChannelRes == null ? null : goodsChannelRes.getSalesWayName());
                    rowData.add(record.getActOutDate() == null ? null : DateUtil.format(record.getActOutDate(), "yyyy"));
                    rowData.add(record.getActOutDate() == null ? null : DateUtil.format(record.getActOutDate(), "yyyy年MM月"));
                    rowData.add(record.getGoodsId());
                    rowData.add(goods == null ? null : goods.getShortName());
                    rowData.add(goods == null ? null : goods.getName());
                    rowData.add("盘古系统");
                    rowData.add(record.getCreateTime() == null ? null : DateUtil.format(record.getCreateTime(), "yyyy/MM/dd"));
                    rowData.add(record.getActCode());
                    rowData.add(record.getOldExpireTime() == null ? null : DateUtil.format(record.getOldExpireTime(), "yyyy/MM/dd"));//原有效期
                    rowData.add(record.getProLongId() == null ? "否" : "是");//是否延期
                    rowData.add(record.getNewExpireTime() == null ? null : DateUtil.format(record.getNewExpireTime(), "yyyy/MM/dd"));//新有效期
                    rowData.add(null);//银行单号
                    rowData.add(memSimpleRes == null ? null : memSimpleRes.getMbName());
                    rowData.add(memSimpleRes == null ? null : memSimpleRes.getMobile());
                    rowData.add((memSimpleRes == null || StringUtils.isEmpty(memSimpleRes.getIdNumber())) ? null : "身份证");//证件类型
                    rowData.add((memSimpleRes == null || StringUtils.isEmpty(memSimpleRes.getIdNumber())) ? null : memSimpleRes.getIdNumber());//证件号
                    rowData.add((memSimpleRes == null || StringUtils.isEmpty(memSimpleRes.getThirdId())) ? null : memSimpleRes.getThirdId());//第三方客户号
                    rowData.add(record.getGiftName());//用餐人姓名
                    rowData.add(shopItemListMap.get(record.getShopItemId()).getHotelName());//酒店名
                    rowData.add(shopItemListMap.get(record.getShopItemId()).getCityName());//所属地区
                    rowData.add(shopChannel == null ? null : shopChannel.getName());//预定渠道
                    rowData.add(record.getSettleMethod());//结算方式
                    rowData.add(record.getChannelNumber());//渠道单号
                    rowData.add(StringUtils.isEmpty(record.getGiftDate()) ? null : DateUtil.format(DateUtil.parse(record.getGiftDate(), "yyyy-MM-dd"), "yyyy年"));
                    rowData.add(StringUtils.isEmpty(record.getGiftDate()) ? null : DateUtil.format(DateUtil.parse(record.getGiftDate(), "yyyy-MM-dd"), "yyyy年MM月"));
                    rowData.add(StringUtils.isEmpty(record.getCheckDate()) ? null : DateUtil.format(DateUtil.parse(record.getCheckDate(), "yyyy-MM-dd"), "yyyy/MM/dd"));//入住日期
                    rowData.add(StringUtils.isEmpty(record.getDeparDate()) ? null : DateUtil.format(DateUtil.parse(record.getDeparDate(), "yyyy-MM-dd"), "yyyy/MM/dd"));//离店日期
                    rowData.add(record.getCheckNight());//间数
                    rowData.add(record.getNightNumbers());//天数
                    rowData.add((record.getCheckNight() != null && record.getNightNumbers() != null) ? (record.getCheckNight() * record.getNightNumbers()) : null);//间夜数
                    if (record.getOrderSettleAmount() != null && record.getCheckNight() != null && record.getNightNumbers() != null) {
                        Integer num = record.getCheckNight() * record.getNightNumbers();
                        BigDecimal settle = record.getOrderSettleAmount();
                        BigDecimal amount = settle.divide(new BigDecimal(num), BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
                        rowData.add(amount);
                    } else {
                        rowData.add(null);
                    }
                    rowData.add(record.getOrderSettleAmount());//酒店总价
                    rowData.add(null);//外币种类
                    rowData.add(null);//外币金额
                    rowData.add(null);//参考汇率
                    rowData.add(null);//酒店预订类型
                    rowData.add(record.getPayAmoney());//客户支付
                    rowData.add(null);//银行补贴
                    rowData.add(record.getShopAmount());//权益结算总价
                    rowData.add(shopItemListMap.get(record.getShopItemId()).getShopItemName());//房型
                    rowData.add(shopItemListMap.get(record.getShopItemId()).getNeeds());//床型
                    rowData.add(shopItemListMap.get(record.getShopItemId()).getAddon());//早餐
                    rowData.add(record.getReservNumber());//酒店确认号
                    rowData.add(record.getReservRemark());//备注
                    rowData.add(record.getUseCount());//权益已使用次数
                    rowData.add(record.getTotalCount() == null ? "无限制" : record.getTotalCount() - record.getUseCount());//权益剩余次数
                    rowData.add(null);//点数
                    excelWriter.writeRow(rowData);
                }
                url = fileDownloadProperties.getUrl() + fileName;
            }
        }
        excelWriter.flush();
        return url;
    }

    /**
     * 导出
     *
     * @param out
     * @param historyOrderResBeanList
     */
    @Override
    public void exportExcel(ServletOutputStream out, List<ReservOrderVo> historyOrderResBeanList) {
        try {
            //创建HSSFWorkbook对象(excel的文档对象)
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFCellStyle style = wb.createCellStyle();
            style.setVerticalAlignment(VerticalAlignment.CENTER);
//            style.setAlignment(VerticalAlignment.);// 水平
            style.setFillForegroundColor(HSSFColor.AQUA.index);
            HSSFFont font = wb.createFont();
            font.setBold(true);//粗体显示
            style.setFont(font);
//
            HSSFCellStyle style1 = wb.createCellStyle();
            style1.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直
//            style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
//            style1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            style1.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
            font.setBold(true);//粗体显示
            style1.setFont(font);
//
            HSSFCellStyle style2 = wb.createCellStyle();
            style2.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直
            //建立新的sheet对象（excel的表单）
            HSSFSheet sheet = wb.createSheet("预约订单列表");
            int width = (int) 35.7 * 100;
            int width2 = (int) 35.7 * 200;
            sheet.setColumnWidth(1, width);
            sheet.setColumnWidth(5, width);
            sheet.setColumnWidth(6, width);
            sheet.setColumnWidth(8, width2);
            sheet.setColumnWidth(9, width2);
            //在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
            HSSFRow row1 = sheet.createRow(0);
            //创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
            //在sheet里创建第一行
            HSSFRow row2 = sheet.createRow(0);
            //创建单元格并设置单元格内容
            row2.createCell(0).setCellValue("编号");
            row2.createCell(1).setCellValue("订单日期\n");
            row2.createCell(2).setCellValue("酒店|商户");
            row2.createCell(3).setCellValue("客户");
            row2.createCell(4).setCellValue("预约日期");
            row2.createCell(5).setCellValue("处理人");
            row2.createCell(6).setCellValue("接单侠");
            row2.createCell(7).setCellValue("隶属项目");
            row2.createCell(8).setCellValue("预定渠道");
            row2.createCell(9).setCellValue("预约状态");
            row2.createCell(10).setCellValue("标签");
            row2.createCell(11).setCellValue("备注");
            //设置行高40px
            row2.setHeight((short) (15.625 * 20));
            row2.setHeightInPoints((float) 20);
            //在sheet里创建第二行
            Integer rowNum = 1;
//            for (VerifyCodesHis historyOrderResBean : historyOrderResBeanList) {
//                HSSFRow row = sheet.createRow(rowNum);
//                row.createCell(0).setCellValue(historyOrderResBean.getId());
//                row.createCell(1).setCellValue(historyOrderResBean.getProductname());
//                row.createCell(2).setCellValue(historyOrderResBean.getItem());
//                row.createCell(3).setCellValue(historyOrderResBean.getState());
//                row.createCell(4).setCellValue(historyOrderResBean.getPeople());
//                row.createCell(5).setCellValue(historyOrderResBean.getName());
//                row.createCell(6).setCellValue(MaskUtils.mobileMask(historyOrderResBean.getPhone()));
//                row.createCell(7).setCellValue(historyOrderResBean.getBank());
//                row.createCell(8).setCellValue(historyOrderResBean.getBookDate()+" "+historyOrderResBean.getBookTime());
//                row.createCell(9).setCellValue(DateUtils.date2String(historyOrderResBean.getVerdate()));
//                row.createCell(10).setCellValue(historyOrderResBean.getCodenum());
//                row.createCell(11).setCellValue(historyOrderResBean.getAmount() == null ? "0" : new BigDecimal(historyOrderResBean.getAmount()).setScale(2).toString());
//                rowNum ++;
//            }
            //输出Excel文件
            wb.write(out);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            try {
                out.close();
            } catch (IOException e) {
            }
        }
    }

    private void setReservOrderList(List<ReservOrderProductVo> list) {
        //缓存
        Map<Integer, GroupProductDetailRes> cachedMap = new HashMap<>();
        GroupProductDetailRes res = null;
        Integer productGroupProductId = null;
        for (ReservOrderProductVo vo : list) {
            //第三方券类型需要查询过期时间
            if (vo.getServiceType().indexOf("_cpn") != -1) {
                QueryThirdCouponsInfoReqVO reqVO = new QueryThirdCouponsInfoReqVO();
                reqVO.setCpnThirdCodeId(vo.getThirdCpnNo());
                CommonResultVo<CpnThirdCode> resultVo = remoteThirdCouponsService.getThirdCouponsInfoById(reqVO);
                if (null != resultVo && null != resultVo.getResult()) {
                    Date experTime = resultVo.getResult().getExperTime();
                    vo.setExperTime(DateUtil.format(experTime, "yyyy-MM-dd"));
                }
            }

            //实物券需要查询快递信息
            if (ResourceTypeEnums.OBJECT_CPN.getCode().equals(vo.getServiceType())) {
                LogisticsInfo logisticsInfo = logisticsInfoMapper.selectById(vo.getId());
                if (logisticsInfo != null) {
                    vo.setExpressMode(logisticsInfo.getExpressMode());
                    vo.setExpressModeStr(ExpressModeEnums.findByCode(logisticsInfo.getExpressMode()).getName());
                    vo.setExpressAddress(logisticsInfo.getAddress());
                    vo.setExpressStatus(logisticsInfo.getStatus());
                    vo.setExpressStatusStr(ExpressStatusEnum.getNameByCode(logisticsInfo.getStatus()));
                    vo.setExpressNumber(logisticsInfo.getExpressNumber());
                    vo.setConsignee(logisticsInfo.getConsignee());
                    vo.setExpressPhone(logisticsInfo.getPhone());
                    if (!StringUtils.isEmpty(logisticsInfo.getExpressNameId())) {
                        SysDict sysDict = new SysDict();
                        sysDict.setType("express_type");
                        sysDict.setValue(logisticsInfo.getExpressNameId());
                        R<SysDict> dictR = remoteDictService.selectByType(sysDict);
                        if (dictR != null && dictR.getData() != null) {
                            vo.setCompanyName(dictR.getData().getLabel());
                        }
                    }
                }
            }

            // fegen  根据id获取商户信息  商户名称
            productGroupProductId = vo.getProductGroupProductId();
            if (productGroupProductId == null) {
                continue;
            }
            if (cachedMap.containsKey(productGroupProductId)) {
                res = cachedMap.get(productGroupProductId);
            } else {
                res = panguInterfaceService.selectProductDetail(productGroupProductId);
                cachedMap.put(productGroupProductId, res);
            }
            //补全信息
            if (res != null) {
                //商铺信息
                ShopBaseMsgVo shop = res.getShop();
                if (shop != null) {
                    vo.setShopName(shop.getName());
                    vo.setCountryid(shop.getCountryId());
                }
                //商铺规格信息
                ShopItem shopItem = res.getShopItem();
                if (shopItem != null) {
                    vo.setShopItemName(shopItem.getName());
                    vo.setServiceName(shopItem.getServiceName());
                    vo.setServiceType(shopItem.getServiceType());
                    vo.setAddon(shopItem.getAddon());
                    vo.setNeeds(shopItem.getNeeds());
                }
                //酒店信息
                Hotel hotel = res.getHotel();
                if (hotel != null) {
                    vo.setHotelName(hotel.getNameCh());
                }
                ProductGroupProduct productGroupProduct = res.getProductGroupProduct();
                if (productGroupProduct != null) {
                    //todo 翻译
                    vo.setGiftText(productGroupProduct.getGift());
                }
                //图片信息
                if (!res.getShopPics().isEmpty()) {
                    SysFileDto sysFile = res.getShopPics().get(0);
                    vo.setGoodsImg(sysFile.getPgCdnHttpUrl() + '/' + sysFile.getGuid() + '.' + sysFile.getExt());
                }
            }
            String status = vo.getProseStatus();
            vo.setProseStatusStr(ReservOrderStatusEnums.ReservOrderStatus.getNameByCode(status));
        }
    }

    @Override
    public PageVo<ReservOrderProductVo> selectReservOrderList(PageVo<ReservOrder> pageVo) {
        //住宿没有核销码 如果是住宿要根据预定成功 和是否过了预约时间确定是否已经使用
        PageVo<ReservOrderProductVo> pageResVo = new PageVo<>();
        List<ReservOrderProductVo> list = reservOrderMapper.selectReservOrderListPage(pageVo, pageVo.getCondition());
        setReservOrderList(list);
        BeanUtils.copyProperties(pageVo, pageResVo);
        return pageResVo.setRecords(list);
    }

    /**
     * 当前项目同一时段权益不可叠加使用
     *
     * @param reservOrderVo
     * @return
     */
    @Override
    public ReservOrder selectReservOrderVoIsExsist(ReservOrderVo reservOrderVo) throws Exception {

        Set<Long> members = Sets.newHashSet();
        MemMemberInfo memMemberInfo = new MemMemberInfo();
        memMemberInfo.setMobile(reservOrderVo.getGiftPhone());
        MemberAccountInfoVo memberAccountInfoVo = memberInterfaceService.getMember(memMemberInfo);
        members = memberAccountInfoVo.getAccList().stream().map(obj -> obj.getAcid()).collect(Collectors.toSet());
        EntityWrapper local = new EntityWrapper();
        if (CollectionUtils.isEmpty(members)) {
            return null;
        } else {
            local.in("member_id", members);
            if (reservOrderVo.getMemberId() != null) {
                local.eq("member_id", reservOrderVo.getMemberId());
            }
            local.eq("goods_id", reservOrderVo.getGoodsId());
            //        local.eq("product_id",reservOrderVo.getProductId());
            local.eq("gift_date", reservOrderVo.getGiftDate());
            local.eq("gift_code_id", reservOrderVo.getGiftCodeId());
            //        if(reservOrderVo.getGiftTime() != null){
            //            local.eq("gift_time",reservOrderVo.getGiftTime());
            //        }

            local.eq("service_type", ShopTypeEnums.findByName(reservOrderVo.getServiceType()).getCode());
            local.notIn("prose_status", ReservOrderStatusEnums.ReservOrderStatus.cancel.getcode(), ReservOrderStatusEnums.ReservOrderStatus.failed.getcode());
            //        local.eq("gift_type",GiftTypeEnum.findByName(reservOrderVo.getGiftType()).getCode());
            List<ReservOrder> reservOrderList = reservOrderMapper.selectList(local);

            if (reservOrderVo.getServiceType().equals(ShopTypeEnums.ACCOM.getName())) {
                EntityWrapper detailLocal = new EntityWrapper();
                List<Integer> memberIds = reservOrderList.stream().map(obj -> obj.getId()).collect(Collectors.toList());
                detailLocal.in("order_id", memberIds);
                detailLocal.lt("STR_TO_DATE(check_date,'%Y-%m-%d')", DateUtil.parse(reservOrderVo.getDeparDate(), "yyyy-MM-dd"));
                detailLocal.gt("STR_TO_DATE(depar_date,'%Y-%m-%d')", DateUtil.parse(reservOrderVo.getGiftDate(), "yyyy-MM-dd"));
                List<ReservOrderDetail> orderDetails = reservOrderDetailService.selectList(detailLocal);
                if (CollectionUtils.isEmpty(orderDetails)) {
                    return null;
                } else {
                    return CollectionUtils.isEmpty(reservOrderList) ? null : reservOrderList.get(0);
                }
            } else {
                return CollectionUtils.isEmpty(reservOrderList) ? null : reservOrderList.get(0);
            }
//            return reservOrderList.size() == 0? null: reservOrderList.get(0);
        }
    }

    /**
     * 检查同一时段有相同预约单
     *
     * @param reservOrderVo
     * @return
     */
    @Override
    public List<ReservOrder> selectReservOrderVoByPhoneIsExsist(ReservOrderVo reservOrderVo) throws Exception {

        Set<Long> members = Sets.newHashSet();
        MemMemberInfo memMemberInfo = new MemMemberInfo();
        memMemberInfo.setMobile(reservOrderVo.getActivePhone());
        MemberAccountInfoVo memberAccountInfoVo = memberInterfaceService.getMember(memMemberInfo);
        members = memberAccountInfoVo.getAccList().stream().map(obj -> obj.getAcid()).collect(Collectors.toSet());
        EntityWrapper local = new EntityWrapper();
        if (members.isEmpty()) {

            return null;
        } else {
            local.in("member_id", members);
            if (reservOrderVo.getMemberId() != null) {
                local.eq("member_id", reservOrderVo.getMemberId());
            }
            local.notIn("prose_status", ReservOrderStatusEnums.ReservOrderStatus.cancel.getcode(), ReservOrderStatusEnums.ReservOrderStatus.failed.getcode());
            //        local.eq("gift_type",GiftTypeEnum.findByName(reservOrderVo.getGiftType()).getCode());

            List<ReservOrder> equityCodeDetails = reservOrderMapper.selectList(local);
            return equityCodeDetails;
        }
    }

    @Override
    public boolean updateIdByOldOrderId(ReservOrder reservOrder) {
        final int row = reservOrderMapper.updateIdByOldOrderId(reservOrder);
        return row == 1;
    }

    @Override
    public void getRedisReservOrder(Integer id) {
        final Boolean hasKey = redisTemplate.hasKey(RedisKeys.MARS_RESERVORDER_OPT + id);
        Assert.isTrue(!hasKey, id + "当前订单正在处理，请勿重复处理！");
        redisTemplate.opsForValue().set(RedisKeys.MARS_RESERVORDER_OPT + id, id, 5, TimeUnit.MINUTES);
    }

    @Override
    public void deleteRedisReservOrder(Integer id) {
        redisTemplate.delete(RedisKeys.MARS_RESERVORDER_OPT + id);
    }

    /**
     * 根据手机号码查询权益。
     *
     * @param members
     * @return
     */
    @Override
    public List<EquityListVo> selectEquityListByMembers(List<Long> members) {
        return reservOrderMapper.selectEquityListByMembers(members);
    }

    /**
     * @param reservOrderVo
     * @return
     */
    @Override
    public ReservOrder selectReservOrderVoIsSusess(ReservOrderVo reservOrderVo) {
//        MemMemberInfo memMemberInfo = new MemMemberInfo();
//        memMemberInfo.setMobile(reservOrderVo.getGiftPhone());
//        MemberAccountInfoVo memberAccountInfoVo = memberInterfaceService.getMember(memMemberInfo);
//        MemMemberAccount account = new MemMemberAccount();
//        EntityWrapper local = new EntityWrapper();
//        if(null  == memberAccountInfoVo) {
//            account.setMobile(reservOrderVo.getGiftPhone());
//            account.setAcName(reservOrderVo.getGiftName());
//            account =   memberInterfaceService.getMemberAddAccount(account);
//            if(null == account){
//                memberInterfaceService.getMemberAddAccount(account);
////                reservOrderVo.setMemberId(account.getAcid());
//                if(account != null ){
//                    local.eq("member_id",account.getAcid());
//                }
//            }
//        }else{
//            local.eq("member_id",account.getAcid() );
//        }
        Set<Long> members = Sets.newHashSet();
        MemMemberInfo memMemberInfo = new MemMemberInfo();
        memMemberInfo.setMobile(reservOrderVo.getGiftPhone());
        MemberAccountInfoVo memberAccountInfoVo = memberInterfaceService.getMember(memMemberInfo);
        members = memberAccountInfoVo.getAccList().stream().map(obj -> obj.getAcid()).collect(Collectors.toSet());
        EntityWrapper local = new EntityWrapper();
        if (members.isEmpty()) {
            return null;
        } else {
            if (reservOrderVo.getMemberId() != null) {
                local.eq("member_id", reservOrderVo.getMemberId());
            }
//            local.eq("goods_id", reservOrderVo.getGoodsId());
//        local.eq("product_id",reservOrderVo.getProductId());
//            local.eq("gift_date", reservOrderVo.getGiftDate());
//            local.notIn("prose_status", ReservOrderStatusEnums.ReservOrderStatus.cancel.getcode(),"prose_status", ReservOrderStatusEnums.ReservOrderStatus.failed.getcode());
//            local.eq("service_type",ShopTypeEnums.findByName(reservOrderVo.getServiceType()).getCode());
//            local.eq("gift_code_id",reservOrderVo.getGiftCodeId());
//        local.eq("gift_type",GiftTypeEnum.findByName(reservOrderVo.getGiftType()).getCode());
            ReservOrderPlaceReq req = new ReservOrderPlaceReq();
//            BeanUtils.copyProperties(reservOrderVo,req);
            req.setServiceType(ShopTypeEnums.findByName(reservOrderVo.getServiceType()).getCode());
            req.setGiftDate(reservOrderVo.getGiftDate());
            req.setGiftCodeId(reservOrderVo.getGiftCodeId());
            req.setCheckDate(reservOrderVo.getGiftDate());
            req.setDeparDate(reservOrderVo.getDeparDate());
            Integer result = reservOrderMapper.countSameTimeOrderNum(req);

            return result == 0 ? null : new ReservOrder();
        }
    }

    private boolean isInternalChannel(Integer channelId) {
        List<Integer> channelList = Lists.newArrayList();
        channelList.add(1);
        channelList.add(7);
        channelList.add(8);
        channelList.add(9);
        channelList.add(13);
        channelList.add(25);
        if (channelId != null) {
            return channelList.contains(channelId);
        }
        return false;
    }

    /**
     * @throws
     * @Title: exportReservOrderNew
     * @Description: 导出
     * @author: sunny.wang
     * @date: 2019年8月6日 下午2:29:43
     * @param: @param pageVoReq
     * @param: @return
     * @param: @throws Exception
     */
    @Override
    @Async("taskExecutorPool")
    public String exportReservOrderNew(PageVo<ReservOrderVo> pageVoReq, KltSysUser sysUser) throws Exception {
        List<String> services = (List<String>) pageVoReq.getCondition().get("serviceTypes");
        String url = null;
        if (!CollectionUtils.isEmpty(services)) {
            List<ReservOrderVo> list = reservOrderMapper.exportReservOrderPageList(pageVoReq, pageVoReq.getCondition());
            if (!CollectionUtils.isEmpty(list)) {
                if (sysUser == null || sysUser.getEmail() == null) {
                    throw new Exception("收件人不能为空");
                }
                String fileName = "预约单" + "-" + System.currentTimeMillis() + ".xlsx";
                url = fileDownloadProperties.getUrl() + fileName;
                String[] receiveEmailAddress = sysUser.getEmail().split(",");
                // 发送邮件
                SysEmailSendReqVo sysEmailSendReqVo = new SysEmailSendReqVo();
                // 记录邮件发送记录
                sysEmailSendReqVo.setSubject("【订单管理-导出】" + fileName);
                sysEmailSendReqVo.setFrom(fileDownloadProperties.getSendEmailAddress());
                sysEmailSendReqVo.setTo(receiveEmailAddress);
                sysEmailSendReqVo.setContent("<html><a href='" + url + "'>" + fileName + "</a></html>");
                log.info("remoteKlfEmailService.send:{}", JSON.toJSONString(sysEmailSendReqVo));
                final CommonResultVo resultVo = remoteKlfEmailService.send(sysEmailSendReqVo);
//                Assert.notNull(resultVo,"邮件发送失败");
                log.info("remoteKlfEmailService.send result:{}", JSON.toJSONString(resultVo));
//                Assert.isTrue(resultVo.getCode()==100,"邮件发送失败");
                // 执行导出
                this.exportItemNew(services.get(0), list, fileName);
            }
        }
        return url;
    }

    /**
     * 根据类型导出Excel
     *
     * @param type
     * @param list
     * @return
     */
    private void exportItemNew(String type, List<ReservOrderVo> list, String fileName) throws Exception {
        // 获取商户渠道列表
        List<ShopChannel> shopChannels = (List<ShopChannel>) redisTemplate.opsForValue()
                .get(RedisKeys.MARS_EXPORT_SHOPS_CHANNEL);
        if (shopChannels == null || shopChannels.isEmpty()) {
            shopChannels = panguInterfaceService.selectChannelList();
            if (shopChannels != null) {
                redisTemplate.opsForValue().set(RedisKeys.MARS_EXPORT_SHOPS_CHANNEL, shopChannels, 15,
                        TimeUnit.MINUTES);
            }
        }
        Map<Integer, ShopChannel> shopChannelsMap = shopChannels.stream()
                .collect(Collectors.toMap(ShopChannel::getId, shopChannel -> shopChannel));
        // 文件输出位置
        File file=new File(fileDownloadProperties.getPath()+"/"+fileName);
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        OutputStream out = new FileOutputStream(file);
        com.alibaba.excel.ExcelWriter writer = EasyExcelFactory.getWriter(out);
        if (!CollectionUtils.isEmpty(list)) {
            // 获取所有商户规格信息
            List<Integer> items = list.stream().map(reservOrderVo -> reservOrderVo.getShopItemId())
                    .collect(Collectors.toList());
            List<ShopItemConciseRes> shopItemList = panguInterfaceService.selectByItems(items);
            Map<Integer, ShopItemConciseRes> shopItemListMap = shopItemList.stream()
                    .collect(Collectors.toMap(ShopItemConciseRes::getId, shopItemConciseRes -> shopItemConciseRes));
            // 获取所有member信息
            List<Long> acids = list.stream().map(reservOrderVo -> reservOrderVo.getMemberId())
                    .collect(Collectors.toList());
            List<MemSimpleRes> memList = memberInterfaceService.selectMemByAcids(acids);
            Map<Long, MemSimpleRes> memListMap = memList.stream()
                    .collect(Collectors.toMap(MemSimpleRes::getAcid, memSimpleRes -> memSimpleRes));
            // 非住宿
            if (!ShopTypeEnums.ACCOM.getCode().equals(type)) {
                List<ExportReserveNoAccom> noAccoms = Lists.newLinkedList();
                for (ReservOrderVo record : list) {
                    Goods goods = null;
                    if (record.getGoodsId() != null) {
                        goods = (Goods) redisTemplate.opsForValue()
                                .get(RedisKeys.MARS_EXPORT_GOODS_ID + record.getGoodsId());
                        if (goods == null) {
                            goods = panguInterfaceService.findGoodsById(record.getGoodsId());
                            if (goods != null) {
                                redisTemplate.opsForValue().set(RedisKeys.MARS_EXPORT_GOODS_ID + record.getGoodsId(),
                                        goods, 15, TimeUnit.MINUTES);
                            }
                        }
                    }
                    GoodsChannelRes goodsChannelRes = null;
                    if (record.getSalesChannleId() != null) {
                        goodsChannelRes = (GoodsChannelRes) redisTemplate.opsForValue()
                                .get(RedisKeys.MARS_EXPORT_SALES_CHANNEL + record.getSalesChannleId());
                        if (goodsChannelRes == null) {
                            goodsChannelRes = panguInterfaceService.findChannelById(record.getSalesChannleId());
                            if (goodsChannelRes != null) {
                                redisTemplate.opsForValue().set(
                                        RedisKeys.MARS_EXPORT_SALES_CHANNEL + record.getSalesChannleId(),
                                        goodsChannelRes, 15, TimeUnit.MINUTES);
                            }
                        }
                    }
                    ShopChannel shopChannel = null;
                    if (record.getShopChannelId() != null) {
                        shopChannel = shopChannelsMap.get(record.getShopChannelId());
                    }
                    // 权益人信息
                    MemSimpleRes memSimpleRes = null;
                    if (record.getMemberId() != null && !CollectionUtils.isEmpty(memListMap)
                            && memListMap.get(record.getMemberId()) != null) {
                        memSimpleRes = memListMap.get(record.getMemberId());
                    }
                    ExportReserveNoAccom param = new ExportReserveNoAccom();
                    param.setRank(record.getRank());
                    param.setReserveId(record.getId());
                    param.setCreateMonth(DateUtil.format(record.getCreateTime(), "yyyy年MM月"));
                    param.setGoodsId(record.getGoodsId());
                    param.setGoodsShortName(goods == null ? null : goods.getShortName());
                    param.setSalesChannel(goodsChannelRes == null ? null
                            : goodsChannelRes.getBankName() + "/" + goodsChannelRes.getSalesChannelName() + "/"
                            + goodsChannelRes.getSalesWayName());
                    param.setSalesYear(
                            record.getActOutDate() == null ? null : DateUtil.format(record.getActOutDate(), "yyyy"));
                    param.setSalesMonth(record.getActOutDate() == null ? null
                            : DateUtil.format(record.getActOutDate(), "yyyy年MM月"));
                    param.setGiftType(StringUtils.isEmpty(record.getGiftType()) ? null
                            : GiftTypeEnum.findByCode(record.getGiftType()).getName());
                    param.setServiceType(StringUtils.isEmpty(record.getServiceType()) ? null
                            : ResourceTypeEnums.findByCode(record.getServiceType()).getName());
                    param.setCreateTime(DateUtil.format(record.getCreateTime(), "yyyy/MM/dd"));
                    param.setOrderCreator(record.getOrderCreator());
                    param.setReservOrderStatus(ReservOrderStatusEnums.ReservOrderStatus.getNameByCode(record.getProseStatus()));
                    if (ReservOrderStatusEnums.ReservOrderStatus.none.getcode().equals(record.getProseStatus())) {
                        param.setStatusDate(DateUtil.format(record.getCreateTime(), "yyyy/MM/dd"));
                    } else if (ReservOrderStatusEnums.ReservOrderStatus.done.getcode()
                            .equals(record.getProseStatus())) {
                        param.setStatusDate(DateUtil.format(record.getSuccessDate(), "yyyy/MM/dd"));
                    } else if (ReservOrderStatusEnums.ReservOrderStatus.cancel.getcode()
                            .equals(record.getProseStatus())) {
                        param.setStatusDate(DateUtil.format(record.getCancelDate(), "yyyy/MM/dd"));
                    } else if (ReservOrderStatusEnums.ReservOrderStatus.failed.getcode()
                            .equals(record.getProseStatus())) {
                        param.setStatusDate(DateUtil.format(record.getFailDate(), "yyyy/MM/dd"));
                    } else if (ReservOrderStatusEnums.ReservOrderStatus.process.getcode()
                            .equals(record.getProseStatus())) {
                        param.setStatusDate(DateUtil.format(record.getReservDate(), "yyyy/MM/dd"));
                    }
                    param.setActCode(record.getActCode());
                    param.setVarCode(record.getVarCode());
                    param.setActExpireTime(record.getActExpireTime() == null ? null
                            : DateUtil.format(record.getActExpireTime(), "yyyy/MM/dd"));
                    param.setMbName(memSimpleRes == null ? null : memSimpleRes.getMbName());
                    param.setMobile(memSimpleRes == null ? null : memSimpleRes.getMobile());
                    param.setGiftDateMonth(StringUtils.isEmpty(record.getGiftDate()) ? null
                            : DateUtil.format(DateUtil.parse(record.getGiftDate(), "yyyy-MM-dd"), "yyyy年MM月"));
                    param.setGiftDate(StringUtils.isEmpty(record.getGiftDate()) ? null
                            : DateUtil.format(DateUtil.parse(record.getGiftDate(), "yyyy-MM-dd"), "yyyy/MM/dd"));
                    param.setShopItemName(shopItemListMap.get(record.getShopItemId()).getShopItemName());// 餐型
                    param.setGiftPeopleNum(record.getGiftPeopleNum());// 用餐人数
                    param.setGiftName(record.getGiftName());// 用餐人姓名
                    param.setHotelName(shopItemListMap.get(record.getShopItemId()).getHotelName());// 酒店名
                    param.setShopName(shopItemListMap.get(record.getShopItemId()).getShopName());// 餐厅名称
                    param.setHotelAndShopName(shopItemListMap.get(record.getShopItemId()).getHotelName() + "/"
                            + shopItemListMap.get(record.getShopItemId()).getShopName());// 酒店名/餐厅名
                    param.setShopChannel(shopChannel == null ? null : shopChannel.getName());// 预定渠道
                    param.setCityName(shopItemListMap.get(record.getShopItemId()).getCityName());// 所属地区
                    param.setVarStatus(StringUtils.isEmpty(record.getVarStatus()) ? null
                            : HxCodeStatusEnum.HxCodeStatus.findNameByIndex(Integer.valueOf(record.getVarStatus())));// 使用状态
                    param.setProtocolPrice(record.getProtocolPrice());// 协议价格
                    param.setNetPrice(record.getNetPrice());// 净价
                    param.setServiceRate(record.getServiceRate());// 服务费
                    param.setTaxRate(record.getTaxRate());// 增值税
                    param.setSettleRule(record.getSettleRule());// 结算规则
                    param.setSettleMethod(record.getSettleMethod());// 结算方式
                    param.setShopAmount(record.getShopAmount());// 贴现金额
                    param.setBankAmount(null);// 银行贴现金额
                    param.setUseCount(record.getUseCount());// 权益已使用次数
                    param.setGiftTimes(record.getTotalCount() == null ? "无限制" : (record.getTotalCount() - record.getUseCount()) + "");// 权益剩余次数
                    noAccoms.add(param);
                }
                // 写仅有一个 Sheet 的 Excel 文件, 此场景较为通用
                Sheet sheet1 = new Sheet(1, 0, ExportReserveNoAccom.class);
                // 第一个 sheet 名称
                sheet1.setSheetName("sheet1");
                // 写数据到 Writer 上下文中
                // 入参1: 创建要写入的模型数据
                // 入参2: 要写入的目标 sheet
                writer.write(noAccoms, sheet1);
                // 将上下文中的最终 outputStream 写入到指定文件中
                writer.finish();
            } else {
                List<ExportReserveAccom> accoms = Lists.newLinkedList();
                for (ReservOrderVo record : list) {
                    Goods goods = null;
                    if (record.getGoodsId() != null) {
                        goods = (Goods) redisTemplate.opsForValue()
                                .get(RedisKeys.MARS_EXPORT_GOODS_ID + record.getGoodsId());
                        if (goods == null) {
                            goods = panguInterfaceService.findGoodsById(record.getGoodsId());
                            if (goods != null) {
                                redisTemplate.opsForValue().set(RedisKeys.MARS_EXPORT_GOODS_ID + record.getGoodsId(),
                                        goods, 15, TimeUnit.MINUTES);
                            }
                        }
                    }
                    GoodsChannelRes goodsChannelRes = null;
                    if (record.getSalesChannleId() != null) {
                        goodsChannelRes = (GoodsChannelRes) redisTemplate.opsForValue()
                                .get(RedisKeys.MARS_EXPORT_SALES_CHANNEL + record.getSalesChannleId());
                        if (goodsChannelRes == null) {
                            goodsChannelRes = panguInterfaceService.findChannelById(record.getSalesChannleId());
                            if (goodsChannelRes != null) {
                                redisTemplate.opsForValue().set(
                                        RedisKeys.MARS_EXPORT_SALES_CHANNEL + record.getSalesChannleId(),
                                        goodsChannelRes, 15, TimeUnit.MINUTES);
                            }
                        }
                    }
                    ShopChannel shopChannel = null;
                    if (record.getShopChannelId() != null) {
                        shopChannel = shopChannelsMap.get(record.getShopChannelId());
                    }
                    // 权益人信息
                    MemSimpleRes memSimpleRes = null;
                    if (record.getMemberId() != null && !CollectionUtils.isEmpty(memListMap)
                            && memListMap.get(record.getMemberId()) != null) {
                        memSimpleRes = memListMap.get(record.getMemberId());
                    }

                    ExportReserveAccom param = new ExportReserveAccom();
//					List<Object> rowData = Lists.newArrayList();
                    param.setServiceType(StringUtils.isEmpty(record.getServiceType()) ? null
                            : ResourceTypeEnums.findByCode(record.getServiceType()).getName());
                    param.setReserveId(record.getId());
                    param.setOrderCreator(record.getOrderCreator());
                    param.setOperator(record.getOperator());
                    param.setReservOrderStatus(ReservOrderStatusEnums.ReservOrderStatus.getNameByCode(record.getProseStatus()));
                    if (ReservOrderStatusEnums.ReservOrderStatus.none.getcode().equals(record.getProseStatus())) {
                        param.setStatusDate(record.getCreateTime() == null ? null
                                : DateUtil.format(record.getCreateTime(), "yyyy/MM/dd"));
                    } else if (ReservOrderStatusEnums.ReservOrderStatus.done.getcode()
                            .equals(record.getProseStatus())) {
                        param.setStatusDate(record.getSuccessDate() == null ? null
                                : DateUtil.format(record.getSuccessDate(), "yyyy/MM/dd"));
                    } else if (ReservOrderStatusEnums.ReservOrderStatus.cancel.getcode()
                            .equals(record.getProseStatus())) {
                        param.setStatusDate(record.getCancelDate() == null ? null
                                : DateUtil.format(record.getCancelDate(), "yyyy/MM/dd"));
                    } else if (ReservOrderStatusEnums.ReservOrderStatus.failed.getcode()
                            .equals(record.getProseStatus())) {
                        param.setStatusDate(record.getFailDate() == null ? null
                                : DateUtil.format(record.getFailDate(), "yyyy/MM/dd"));
                    } else if (ReservOrderStatusEnums.ReservOrderStatus.process.getcode()
                            .equals(record.getProseStatus())) {
                        param.setStatusDate(record.getReservDate() == null ? null
                                : DateUtil.format(record.getReservDate(), "yyyy/MM/dd"));
                    }
                    param.setSuccessDate(record.getSuccessDate() == null ? null
                            : DateUtil.format(record.getSuccessDate(), "yyyy/MM/dd"));
                    param.setCancelDate(record.getCancelDate() == null ? null
                            : DateUtil.format(record.getCancelDate(), "yyyy/MM/dd"));
                    param.setCancelReason(record.getCancelReason());
                    param.setFailDate(
                            record.getFailDate() == null ? null : DateUtil.format(record.getFailDate(), "yyyy/MM/dd"));
                    param.setFailReason(record.getFailReason());
                    param.setDispensing(record.getDispensing().compareTo(1) == 0 ? "是" : "否");
                    param.setCreateYear(
                            record.getCreateTime() == null ? null : DateUtil.format(record.getCreateTime(), "yyyy年"));
                    param.setCreateMonth(record.getCreateTime() == null ? null
                            : DateUtil.format(record.getCreateTime(), "yyyy年MM月"));
                    param.setBankName(goodsChannelRes == null ? null : goodsChannelRes.getBankName());
                    param.setSalesChannelName(goodsChannelRes == null ? null : goodsChannelRes.getSalesChannelName());
                    param.setSalesWayName(goodsChannelRes == null ? null : goodsChannelRes.getSalesWayName());
                    param.setSalesYear(
                            record.getActOutDate() == null ? null : DateUtil.format(record.getActOutDate(), "yyyy"));
                    param.setSalesMonth(record.getActOutDate() == null ? null
                            : DateUtil.format(record.getActOutDate(), "yyyy年MM月"));
                    param.setGoodsId(record.getGoodsId());
                    param.setGoodsShortName(goods == null ? null : goods.getShortName());
                    param.setGoodsName(goods == null ? null : goods.getName());
                    param.setSysName("盘古系统");
                    param.setCreateTime(record.getCreateTime() == null ? null
                            : DateUtil.format(record.getCreateTime(), "yyyy/MM/dd"));
                    param.setActCode(record.getActCode());
                    param.setOldExpireTime(record.getOldExpireTime() == null ? (record.getActExpireTime() == null ? null : DateUtil.format(record.getActExpireTime(), "yyyy/MM/dd")) : DateUtil.format(record.getOldExpireTime(), "yyyy/MM/dd"));// 原有效期
                    param.setProLong(record.getProLongId() == null ? "否" : "是");// 是否延期
                    param.setNewExpireTime(record.getNewExpireTime() == null ? null : DateUtil.format(record.getNewExpireTime(), "yyyy/MM/dd"));// 新有效期
                    param.setBankOrderNo(null);// 银行单号
                    param.setMbName(memSimpleRes == null ? null : memSimpleRes.getMbName());
                    param.setMobile(memSimpleRes == null ? null : memSimpleRes.getMobile());
                    param.setCardType(
                            (memSimpleRes == null || StringUtils.isEmpty(memSimpleRes.getIdNumber())) ? null : "身份证");// 证件类型
                    param.setIdNumber((memSimpleRes == null || StringUtils.isEmpty(memSimpleRes.getIdNumber())) ? null
                            : memSimpleRes.getIdNumber());// 证件号
                    param.setThirdId((memSimpleRes == null || StringUtils.isEmpty(memSimpleRes.getThirdId())) ? null
                            : memSimpleRes.getThirdId());// 第三方客户号
                    param.setGiftName(record.getGiftName());// 用餐人姓名
                    param.setHotelName(shopItemListMap.get(record.getShopItemId()).getHotelName());// 酒店名
                    param.setCityName(shopItemListMap.get(record.getShopItemId()).getCityName());// 所属地区
                    param.setShopChannel(shopChannel == null ? null : shopChannel.getName());// 预定渠道
                    param.setSettleMethod(record.getSettleMethod());// 结算方式
                    param.setChannelNumber(record.getChannelNumber());// 渠道单号
                    param.setGiftDateYear(StringUtils.isEmpty(record.getGiftDate()) ? null
                            : DateUtil.format(DateUtil.parse(record.getGiftDate(), "yyyy-MM-dd"), "yyyy年"));
                    param.setGiftDateMonth(StringUtils.isEmpty(record.getGiftDate()) ? null
                            : DateUtil.format(DateUtil.parse(record.getGiftDate(), "yyyy-MM-dd"), "yyyy年MM月"));
                    param.setCheckDate(StringUtils.isEmpty(record.getCheckDate()) ? null
                            : DateUtil.format(DateUtil.parse(record.getCheckDate(), "yyyy-MM-dd"), "yyyy/MM/dd"));// 入住日期
                    param.setDeparDate(StringUtils.isEmpty(record.getDeparDate()) ? null
                            : DateUtil.format(DateUtil.parse(record.getDeparDate(), "yyyy-MM-dd"), "yyyy/MM/dd"));// 离店日期
                    param.setCheckNight(record.getCheckNight());// 间数
                    param.setNightNumbers(record.getNightNumbers());// 天数
                    param.setJyTimes((record.getCheckNight() != null && record.getNightNumbers() != null)
                            ? (record.getCheckNight() * record.getNightNumbers())
                            : null);// 间夜数
                    if (record.getOrderSettleAmount() != null && record.getCheckNight() != null
                            && record.getNightNumbers() != null) {
                        Integer num = record.getCheckNight() * record.getNightNumbers();
                        BigDecimal settle = record.getOrderSettleAmount();
                        BigDecimal amount = settle.divide(new BigDecimal(num), BigDecimal.ROUND_HALF_UP).setScale(2,
                                BigDecimal.ROUND_HALF_UP);
                        param.setSingleAmount(amount);
                    } else {
                        param.setSingleAmount(null);
                    }
                    param.setShopAmount(record.getShopAmount());// 酒店总价
                    param.setCornType(null);// 外币种类
                    param.setCornAmount(null);// 外币金额
                    param.setOtherRate(null);// 参考汇率
                    param.setHotelReserveType(null);// 酒店预订类型
                    param.setPaymentAmount(record.getPaymentAmount());// 客户支付
                    param.setBankMoney(null);// 银行补贴
                    param.setSalesPrice(goods == null ? null : goods.getSalesPrice());// 权益结算总价
                    param.setShopItemName(shopItemListMap.get(record.getShopItemId()).getShopItemName());// 房型
                    param.setNeeds(shopItemListMap.get(record.getShopItemId()).getNeeds());// 床型
                    param.setAddon(shopItemListMap.get(record.getShopItemId()).getAddon());// 早餐
                    param.setReservNumber(record.getReservNumber());// 酒店确认号
                    param.setReservRemark(record.getReservRemark());// 备注
                    param.setUseCount(record.getUseCount());// 权益已使用次数
                    param.setGiftTimes(record.getTotalCount() == null ? "无限制" : (record.getTotalCount() - record.getUseCount()) + "");// 权益剩余次数
                    param.setPoint(null);// 点数
                    param.setThirdCpnNum(record.getThirdCpnNum());
                    param.setThirdCpnName(record.getThirdCpnName());
                    accoms.add(param);
                }
                // 写仅有一个 Sheet 的 Excel 文件, 此场景较为通用
                Sheet sheet1 = new Sheet(1, 0, ExportReserveAccom.class);
                // 第一个 sheet 名称
                sheet1.setSheetName("sheet1");
                // 写数据到 Writer 上下文中
                // 入参1: 创建要写入的模型数据
                // 入参2: 要写入的目标 sheet
                writer.write(accoms, sheet1);
                // 将上下文中的最终 outputStream 写入到指定文件中
                writer.finish();
            }
        }
        // 关闭流
        out.close();
    }

    /**
     * 查询该权益预约成功后并使用的最早时间 : 单杯的以预约时间 + 3天为 使用时间
     *
     * @param reqVO
     * @return
     */
    @Override
    public String getReserveOrderSucessDate(QueryReserveOrderDateReqVO reqVO) throws Exception {
        if (StringUtils.isEmpty(reqVO)) {
            throw new Exception("参数不能为空");
        }
        List<ReservOrder> reservOrderList = reservOrderMapper.selectReserveOrderSucessDate(reqVO);
        if (!CollectionUtils.isEmpty(reservOrderList)) {
            return reservOrderList.get(0).getGiftDate();
        }
        return null;
    }

    @Override
    public List<ReservOrder> getOrderList(Integer giftCodeId) throws Exception {
        Wrapper<ReservOrder> wrapper = new Wrapper<ReservOrder>() {
            @Override
            public String getSqlSegment() {
                StringBuffer sb = new StringBuffer();
                sb.append("WHERE del_flag = 0 AND gift_code_id = " + giftCodeId + " AND prose_status in (0, 1, 4) ");
                return sb.toString();
            }
        };
        List<ReservOrder> list = reservOrderMapper.selectList(wrapper);
        return list;
    }

    @Override
    public List<ReservOrder> getOrderListByCondition(ReservOrder order) {
        Wrapper<ReservOrder> wrapper = new Wrapper<ReservOrder>() {
            @Override
            public String getSqlSegment() {
                StringBuffer sb = new StringBuffer();
                sb.append("WHERE del_flag = 0 AND gift_code_id = " + order.getGiftCodeId() + " AND prose_status in (0,4) ");
                sb.append("and member_id=" + order.getMemberId());
                return sb.toString();
            }
        };
        return reservOrderMapper.selectList(wrapper);
    }

    @Override
    public List<BookOrderDetail> getBookOrderDetail(BookOrderReq req) throws Exception {
        try {
            List<BookOrderDetail> list = new ArrayList<BookOrderDetail>();
            Wrapper<ReservOrder> wrapper = new Wrapper<ReservOrder>() {
                @Override
                public String getSqlSegment() {
                    StringBuffer sb = new StringBuffer();
                    sb.append("WHERE order_source like '" + req.getChannel() + "%' ");
                    if (req.getOrderId() != null) {
                        sb.append("AND id ='" + req.getOrderId() + "' ");
                    }
                    if (req.getGoodsId() != null) {
                        sb.append("AND goods_id ='" + req.getGoodsId() + "' ");
                    }
                    if (req.getStartDate() != null) {
                        String createTime = HelpUtils.dateToStr(req.getStartDate(), "yyyy-MM-dd HH:mm:ss");
                        sb.append("AND create_time>='" + createTime + "'");
                    }
                    if (req.getEndDate() != null) {
                        String endTime = HelpUtils.dateToStr(req.getEndDate(), "yyyy-MM-dd HH:mm:ss");
                        sb.append("AND create_time<='" + endTime + "'");
                    }
                    sb.append("ORDER BY id DESC ");
                    return sb.toString();
                }
            };
            List<ReservOrder> details = reservOrderMapper.selectList(wrapper);
            for (ReservOrder rod : details) {
                BookOrderDetail bookOrderDetail = new BookOrderDetail();
//                BeanUtils.copyProperties(rod, bookOrderDetail);
                bookOrderDetail.setOrderId(rod.getId());
                bookOrderDetail.setGoodsId(rod.getGoodsId());
                GoodsBaseVo goodsBaseVo = panguInterfaceService.selectGoodsById(rod.getGoodsId());
                if (goodsBaseVo != null) {
                    bookOrderDetail.setGoodsName(goodsBaseVo.getName());
                }
                ReservOrderProductVo vo = reservOrderMapper.getReservOrder(rod.getId());
                bookOrderDetail.setOrderDate(rod.getCreateTime());
                switch (rod.getProseStatus()) {
                    case "0":
                        bookOrderDetail.setOrderStatus("尚未预订");
                        break;
                    case "1":
                        bookOrderDetail.setOrderStatus("预订成功");
                        break;
                    case "2":
                        bookOrderDetail.setOrderStatus("预订取消");
                        break;
                    case "3":
                        bookOrderDetail.setOrderStatus("预订失败");
                        break;
                    case "4":
                        bookOrderDetail.setOrderStatus("预定中");
                        break;
                }
                //fegen  根据id获取商户信息  商户名称
                Integer shopId = vo.getShopId();
                ShopDetailRes shopDetailRes = panguInterfaceService.getShopDetail(shopId);
                ShopBaseMsgVo shop;
                if (shopDetailRes != null && (shop = shopDetailRes.getShop()) != null) {
                    bookOrderDetail.setShopName(shop.getName());
                    bookOrderDetail.setShopCity(shop.getCity());
                    bookOrderDetail.setShopAddress(shop.getAddress());
                }
                Hotel hotel = panguInterfaceService.selectHotelByShopId(shopId);
                if (hotel != null) {
                    bookOrderDetail.setHotelName(hotel.getNameCh());
                }
                if (rod.getServiceType() != null) {
                    bookOrderDetail.setProductName(ResourceTypeEnums.findByCode(rod.getServiceType()).getName());
                }
                if (rod.getGiftType() != null) {
                    bookOrderDetail.setGiftType(GiftTypeEnum.findByCode(rod.getGiftType()).getName());
                }
                if (rod.getServiceType() != "accom") {
                    bookOrderDetail.setBookDate(rod.getGiftDate());
                    bookOrderDetail.setBookTime(rod.getGiftTime());
                }
                if (rod.getServiceType() == "accom") {
                    ReservOrderDetail detail = reservOrderDetailService.selectOneReservOrderDetail(rod.getId());
                    bookOrderDetail.setBookDate(detail.getCheckDate());
                    bookOrderDetail.setLeaveDate(detail.getDeparDate());
                    bookOrderDetail.setQty(detail.getCheckNight() * detail.getNightNumbers());
                }
                bookOrderDetail.setPeople(rod.getGiftPeopleNum());
                bookOrderDetail.setPeopleName(rod.getGiftName());
                bookOrderDetail.setPhone(rod.getGiftPhone());
                bookOrderDetail.setOrderAmount(rod.getTotalAmount());
                bookOrderDetail.setOfferAmount(rod.getDiscountAmount());
                bookOrderDetail.setActualPayAmount(rod.getPayAmount());
                list.add(bookOrderDetail);
            }
            return list;
        } catch (Exception e) {
            throw new Exception("查询异常");
        }
    }

    @Override
    public List<Map<String, Object>> getgiftConut(Map<String, Object> req) throws Exception {
        List<Map<String, Object>> giftcount = new ArrayList<Map<String, Object>>();

        String channel = req.get("channel").toString();
//            String phone = req.get("phone").toString();
        String goodsId = req.get("goodsId").toString();
        String giftCode = req.get("giftCode").toString();
        //校验手机号
//            MemAccMobileReqDTO dto = new MemAccMobileReqDTO();
//            dto.setAcChannel(channel);
//            dto.setMobile(phone);
//            MemMemberAccountDTO memberAccountDTO = memberInterfaceService.getMemAccount(dto);
//            if(memberAccountDTO == null){
//                throw new Exception("用户不存在");
//            }
        //获取激活码信息
        Map<String, Object> giftcode = reservOrderMapper.getgiftCount(req);
        if (giftcode == null) {
            throw new Exception("激活码信息与权益id不匹配");
        }
        Integer giftCodeId = (Integer) giftcode.get("id");
        List<EquityCodeDetail> equityCodeDetail = reservOrderMapper.getequityCodeDetail(giftCodeId);
        List<EquityCodeDetail> newDetail = new ArrayList<EquityCodeDetail>();
        if (equityCodeDetail != null) {
            //修改原因： 商区系统需要的返回值为原来equitycodedetail数据格式。现在按照条件返回原始格式
            //将产品组id相同的多条数据使用次数use_count 合并成一条数据放进map中
            Map<Integer, Integer> map = new HashMap<Integer, Integer>();
            for (EquityCodeDetail detail : equityCodeDetail) {
                if (detail.getStartTime() != null && detail.getEndTime() != null) {
                    if (map.containsKey(detail.getProductGroupId())) {
                        Integer a = map.get(detail.getProductGroupId());
                        a += detail.getUseCount().intValue();
                        map.put(detail.getProductGroupId(), a);
                    } else {
                        map.put(detail.getProductGroupId(), detail.getUseCount().intValue());
                    }
                } else {
                    newDetail.add(detail);
                }
            }
            //拿到刚刚键值对中的符合条件的第一条数据并将累加后的value值赋值到usecount 然后移除map
            for (EquityCodeDetail details : equityCodeDetail) {
                if (map.containsKey(details.getProductGroupId())) {
                    details.setUseCount(map.get(details.getProductGroupId()));
                    newDetail.add(details);
                    map.remove(details.getProductGroupId());
                }
            }
        }

        for (EquityCodeDetail cc : newDetail) {
            Map<String, Object> bb = new HashMap<>();
            Integer productGroupId = cc.getProductGroupId();
            CommonResultVo<GroupQueryOneRes> group = remoteProductGroupService.findOneById(productGroupId);
            bb.put("groupId", cc.getProductGroupId());
            bb.put("groupName", group.getResult().getName());
            bb.put("availablenumber", cc.getUseCount());
            bb.put("totalnumber", cc.getTotalCount());
            giftcount.add(bb);
        }

        return giftcount;
    }

    @Override
    public ReservOrder getReservOrderById(Integer reservOderId) throws Exception {
        if (reservOderId == null) {
            throw new Exception("参数为空");
        }
        return reservOrderMapper.selectById(reservOderId);
    }

    @Override
    public ReservOrderProductVo getReservOrderProById(Integer reservOderId) throws Exception {
        if (reservOderId == null) {
            throw new Exception("参数为空");
        }
        ReservOrderProductVo vo = reservOrderMapper.getReservOrder(reservOderId);
        if (StringUtils.isEmpty(vo)) {
            throw new Exception("预约单不存在");
        }
        //第三方券类型需要查券信息
        if (vo.getServiceType().indexOf("_cpn") != -1) {
            QueryThirdCouponsInfoReqVO reqVO = new QueryThirdCouponsInfoReqVO();
            reqVO.setCpnThirdCodeId(vo.getThirdCpnNo());
            CommonResultVo<CpnThirdCode> resultVo = remoteThirdCouponsService.getThirdCouponsInfoById(reqVO);
            if (null != resultVo && null != resultVo.getResult()) {
                vo.setCouponsType(resultVo.getResult().getCouponsType());
                vo.setThirdCodePassword(resultVo.getResult().getThirdCodePassword());
                vo.setShortUrl(resultVo.getResult().getShortUrl());
                vo.setValidStartTime(resultVo.getResult().getValidStartTime() == null ? "" : DateUtil.format(resultVo.getResult().getValidStartTime(), "yyyy/MM/dd"));
                vo.setExperTime(resultVo.getResult().getExperTime() == null ? "" : DateUtil.format(resultVo.getResult().getExperTime(), "yyyy/MM/dd"));
            }
            QueryProductGroupInfoReqVo queryProductGroupInfoReqVo = new QueryProductGroupInfoReqVo();
            queryProductGroupInfoReqVo.setProductGroupId(vo.getProductGroupId());
            queryProductGroupInfoReqVo.setProductId(vo.getProductId());
            CommonResultVo<List<ProductGroupResVO>> commonResultVo = remoteProductGroupService.selectProductGroupById(queryProductGroupInfoReqVo);
            if (null != commonResultVo && !CollectionUtils.isEmpty(commonResultVo.getResult())) {
                ProductGroupResVO productGroupResVO = commonResultVo.getResult().get(0);
                if (null != productGroupResVO && null != productGroupResVO.getShopChannelId()) {
                    vo.setThirdCpnSource(ThirdSourceEnum.getCode(productGroupResVO.getShopChannelId()));
                }
            }
        }
        Integer goodsId = vo.getGoodsId();
        GoodsBaseVo goods = panguInterfaceService.selectGoodsById(goodsId);
        if (goods != null) {
            vo.setGoodsName(goods.getName());
        }
        // fegen  根据id获取商户信息  商户名称
        Integer shopId = vo.getShopId();
        ShopDetailRes shopDetailRes = panguInterfaceService.getShopDetail(shopId);
        if (shopDetailRes != null && shopDetailRes.getShop() != null) {
            vo.setShopName(shopDetailRes.getShop().getName());
            vo.setShopAddress(shopDetailRes.getShop().getAddress());
            vo.setShopCity(shopDetailRes.getShop().getCity());
        }
        Integer productGroupProductId = vo.getProductGroupProductId();
        ReservOrderProductVo shopItem = productListService.selectReservOrderVo(productGroupProductId);
        if (shopItem != null) {
            vo.setServiceName(shopItem.getServiceName());
            //产品的名称
            vo.setProductName(shopItem.getProductName());
            vo.setAddon(shopItem.getAddon());
            vo.setNeeds(shopItem.getNeeds());
        }
        Hotel hotel = panguInterfaceService.selectHotelByShopId(shopId);
        if (hotel != null) {
            vo.setHotelName(hotel.getNameCh());
        }
        Date createTime = vo.getCreateTime();
        if (createTime != null) {
            //转化时间格式 便于前端取
            vo.setCreateTimeStr(HelpUtils.dateToStr(createTime, "yyyy-MM-dd HH:mm:ss"));
        }
        //图片信息
        ListSysFileReq shopFile = new ListSysFileReq();
        shopFile.setObjId(shopId);
        shopFile.setType(FileTypeEnums.SHOP_PIC.getCode());
        CommonResultVo<List<SysFileDto>> comm = remoteSysFileService.list(shopFile);
        if (!comm.getResult().isEmpty()) {
            SysFileDto sysFile = comm.getResult().get(0);
            vo.setGoodsImg(sysFile.getPgCdnHttpUrl() + '/' + sysFile.getGuid() + '.' + sysFile.getExt());
        }

        return vo;
    }

    /**
     * 判断是否可下此预约单
     *
     * @param reqVo
     * @return
     */
    @Override
    public Boolean checkResrrvOrderNum(ResrrvOrderReqVo reqVo) {
        Assert.notNull(reqVo, "参数不能为空");
        Integer count = reservOrderMapper.selectCount(new Wrapper<ReservOrder>() {
            @Override
            public String getSqlSegment() {
                return "where goods_id = " + reqVo.getGoodsId() + "  and  product_group_id = " + reqVo.getProductGroupId() + " and  product_group_product_id =" + reqVo.getProductGroupProductId() + " and product_id = " + reqVo.getProductId() + " and prose_status=1";
            }
        });
        if (count >= 100) {
            return false;
        }
        return true;
    }
}

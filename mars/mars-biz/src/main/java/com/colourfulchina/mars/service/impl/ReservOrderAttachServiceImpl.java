package com.colourfulchina.mars.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.colourfulchina.colourfulCoupon.api.enums.CpnVouchersStatusEunm;
import com.colourfulchina.colourfulCoupon.api.enums.TicketTypeEnum;
import com.colourfulchina.colourfulCoupon.api.feign.RemoteCouponCommonService;
import com.colourfulchina.colourfulCoupon.api.feign.RemoteCouponDiscountService;
import com.colourfulchina.colourfulCoupon.api.feign.RemoteCouponVouchersService;
import com.colourfulchina.colourfulCoupon.api.vo.req.CpnDiscountInfoReqVo;
import com.colourfulchina.colourfulCoupon.api.vo.req.CpnVoucherInfoReqVo;
import com.colourfulchina.colourfulCoupon.api.vo.req.MemCouponReqVo;
import com.colourfulchina.colourfulCoupon.api.vo.req.UpdateCpnReqVo;
import com.colourfulchina.colourfulCoupon.api.vo.res.CpnDiscountsInfoResVo;
import com.colourfulchina.colourfulCoupon.api.vo.res.CpnVoucherInfoResVo;
import com.colourfulchina.colourfulCoupon.api.vo.res.MemCouponListResVo;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.constant.ThirdOrderPushConstant;
import com.colourfulchina.mars.api.entity.*;
import com.colourfulchina.mars.api.enums.*;
import com.colourfulchina.mars.api.vo.ReservOrderVo;
import com.colourfulchina.mars.api.vo.req.BookPayReq;
import com.colourfulchina.mars.api.vo.req.GoodsSettingReq;
import com.colourfulchina.mars.api.vo.req.ReservOrderPlaceReq;
import com.colourfulchina.mars.api.vo.res.PriceRes;
import com.colourfulchina.mars.api.vo.res.ReservOrderResVO;
import com.colourfulchina.mars.constants.GiftCodeConstants;
import com.colourfulchina.mars.constants.RedisKeys;
import com.colourfulchina.mars.mapper.GiftCodeMapper;
import com.colourfulchina.mars.mapper.ReservCodeMapper;
import com.colourfulchina.mars.mapper.ReservOrderDetailMapper;
import com.colourfulchina.mars.mapper.ReservOrderMapper;
import com.colourfulchina.mars.service.*;
import com.colourfulchina.mars.utils.CodeUtils;
import com.colourfulchina.mars.utils.HelpUtils;
import com.colourfulchina.member.api.entity.MemMemberInfo;
import com.colourfulchina.member.api.entity.MemberCertificate;
import com.colourfulchina.member.api.feign.RemoteLoginService;
import com.colourfulchina.member.api.feign.RemoteMemberFamilyServcie;
import com.colourfulchina.member.api.feign.RemoteMemberInfoServcie;
import com.colourfulchina.member.api.res.MemLoginResDTO;
import com.colourfulchina.pangu.taishang.api.entity.GoodsSetting;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroupProduct;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemNetPriceRule;
import com.colourfulchina.pangu.taishang.api.enums.GiftTypeEnum;
import com.colourfulchina.pangu.taishang.api.enums.ResourceTypeEnums;
import com.colourfulchina.pangu.taishang.api.feign.RemoteBlockRuleService;
import com.colourfulchina.pangu.taishang.api.vo.req.QueryBookBlockReq;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectBookPayReq;
import com.colourfulchina.pangu.taishang.api.vo.res.QueryBookBlockRes;
import com.colourfulchina.pangu.taishang.api.vo.res.SelectBookProductRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopItemRes;
import com.colourfulchina.pangu.taishang.api.vo.res.bookBasePayment.BookBasePaymentRes;
import com.colourfulchina.pangu.taishang.api.vo.res.shopProtocol.ShopProtocolRes;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/5/27 17:46
 */
@Service
@AllArgsConstructor
@Slf4j
public class ReservOrderAttachServiceImpl implements ReservOrderAttachService {
    private RemoteBlockRuleService remoteBlockRuleService;
    @Autowired
    private GiftCodeMapper giftCodeMapper;
    @Autowired
    private ReservOrderMapper reservOrderMapper;
    @Autowired
    private ReservOrderService reservOrderService;
    @Autowired
    private ReservOrderDetailMapper reservOrderDetailMapper;

    private RemoteLoginService remoteLoginService;

    @Autowired
    private EquityCodeDetailService equityCodeDetailService;

    @Autowired
    private PanguInterfaceService panguInterfaceService;

    @SuppressWarnings("rawtypes")
    @Autowired
    protected RedisTemplate redisTemplate;

    @Autowired
    private ReservCodeService reservCodeService;

    @Autowired
    private ReservCodeMapper reservCodeMapper;

    @Autowired
    private GiftOrderApiService giftOrderApiService;

    @Autowired
    NuwaInterfaceService nuwaInterfaceService;
    private RemoteMemberInfoServcie remoteMemberInfoServcie;


    @Autowired
    GiftCodeService giftCodeService;
    @Autowired
    private LogisticsInfoService logisticsInfoService;

    @Autowired
    private CouponsService couponsService;

    @Autowired
    ReservOrderHospitalService reservOrderHospitalService;
    @Autowired
    SysHospitalService hospitalService;

    RemoteCouponVouchersService remoteCouponVouchersService;

    RemoteCouponDiscountService remoteCouponDiscountService;

    RemoteCouponCommonService remoteCouponCommonService;

    @Override
    public QueryBookBlockRes getBlockRule(Map<String, Integer> param) {
        Integer giftId = param.get("giftId");
        Assert.notNull(giftId, "giftId必输");
        //根据gift id 获取gift信息
        GiftCode giftCode = giftCodeMapper.selectById(giftId);
        //set ...
        QueryBookBlockReq queryBookBlockReq = new QueryBookBlockReq();
        queryBookBlockReq.setGoodsId(param.get("goodsId"));
        queryBookBlockReq.setProductGroupId(param.get("productGroupId"));
        queryBookBlockReq.setShopId(param.get("shopId"));
        queryBookBlockReq.setProductGroupProductId(param.get("productGroupProductId"));
        //出库时间
        queryBookBlockReq.setOutDate(giftCode.getActOutDate());
        //激活时间
        queryBookBlockReq.setActivationDate(giftCode.getActCodeTime());
		queryBookBlockReq.setActExpireTime(giftCode.getActExpireTime());
        CommonResultVo<QueryBookBlockRes> resultVo = remoteBlockRuleService.queryBookBlock(queryBookBlockReq);
        Assert.notNull(resultVo, "请求block接口失败");
        Assert.isTrue(resultVo.getCode() == 100, resultVo.getMsg());
        return resultVo.getResult();
    }


    /**
     * 在线下预约单
     * @param reservOrderPlaceReq
     * @param memInfo
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor=Exception.class,propagation = Propagation.REQUIRES_NEW)
    public ReservOrderResVO placeOrder(ReservOrderPlaceReq reservOrderPlaceReq, MemLoginResDTO memInfo) throws Exception {
        ReservOrderResVO resVO = new ReservOrderResVO();
        final Integer giftCodeId = reservOrderPlaceReq.getGiftCodeId();
        //银联446、448商品下单需要检测是否超过银联订单号设置的时间，超过则下单失败
        if (reservOrderPlaceReq.getGoodsId().compareTo(446) == 0 || reservOrderPlaceReq.getGoodsId().compareTo(448) ==0 ){
            String partnerOrderId = giftOrderApiService.getOrderInfoByGiftCode(giftCodeId);
            if (partnerOrderId == null) {
                throw new Exception("订单不存在");
            }
            String redRes = (String) redisTemplate.opsForValue().get(ThirdOrderPushConstant.THIRD_ORDER_ID + partnerOrderId);
            if (StringUtils.isEmpty(redRes)){
                throw new Exception("当前页面已超时，请重新登录！");
            }
        }
//        Integer cpnGiftType = null; //券权益类型:0:会员权益券;1:普通券
//        Boolean hasCpns = false; //查询用户在此预约单中是否有可用的会员权益券
        List<String> vipCpnList = Lists.newArrayList(); //用户在该产品下的会员权益券
        String serviceType = reservOrderPlaceReq.getServiceType();
        //0-会员权益券：如果在用户有该类型的券但是本次订单没有使用权益抵用券时，不扣减权益次数，只有使用了才扣减权益次数
        MemCouponReqVo memCouponReqVo = new MemCouponReqVo();
        memCouponReqVo.setAcId(memInfo.getAcid());
        List<Integer> statusList = new ArrayList<Integer>(){{add(0);}};
        memCouponReqVo.setStatusList(statusList);
        //TODO...
        memCouponReqVo.setGoodsId(reservOrderPlaceReq.getGoodsId());
        memCouponReqVo.setProductGroupId(reservOrderPlaceReq.getProductGroupId());
        memCouponReqVo.setProductGroupProductId(reservOrderPlaceReq.getProductGroupProductId());
        memCouponReqVo.setProductId(reservOrderPlaceReq.getProductId());
        memCouponReqVo.setUseType(1); //预约时可用
        memCouponReqVo.setCpnGiftType(0); //会员类型权益券
        memCouponReqVo.setLimitType(1);
        CommonResultVo<MemCouponListResVo> resultVo = remoteCouponCommonService.getMemCpnListByCondition(memCouponReqVo);
        if(null != resultVo){
//                    hasCpns = true;
            if(!CollectionUtils.isEmpty(resultVo.getResult().getCpnVoucherInfoList())){
                resultVo.getResult().getCpnVoucherInfoList().forEach(cpnVoucherInfoResVo -> {
                    vipCpnList.add(cpnVoucherInfoResVo.getVoucherNum());
                });
            }
            if(!CollectionUtils.isEmpty(resultVo.getResult().getCpnDiscountsInfoList())){
                resultVo.getResult().getCpnDiscountsInfoList().forEach(cpnDiscountsInfoResVo -> {
                    vipCpnList.add(cpnDiscountsInfoResVo.getDiscountNum());
                });
            }
        }
        GiftCode giftCode = giftCodeMapper.selectById(giftCodeId);
        Assert.notNull(giftCode, giftCodeId +"没有找到对应的权益");
        //验证前段传的预约时间是否可以预约
        QueryBookBlockReq queryBookBlockReq = new QueryBookBlockReq();
        queryBookBlockReq.setActExpireTime(giftCode.getActExpireTime());
        queryBookBlockReq.setProductGroupProductId(reservOrderPlaceReq.getProductGroupProductId());
        CommonResultVo<QueryBookBlockRes> remoteBlockResult = remoteBlockRuleService.queryBookBlockNew(queryBookBlockReq);
        Assert.isTrue(!(remoteBlockResult == null || remoteBlockResult.getCode() != 100 || CollectionUtils.isEmpty(remoteBlockResult.getResult().getBookDates())),"当前时间不可预约");
        List<Date> bookDates = remoteBlockResult.getResult().getBookDates();
        List<Date> paramDate = Lists.newLinkedList();
        if (ResourceTypeEnums.ACCOM.getCode().equals(serviceType) && StringUtils.isEmpty(reservOrderPlaceReq.getGiftDate())) {
            Date startDate = DateUtil.parse(reservOrderPlaceReq.getCheckDate(),"yyyy-MM-dd");
            Date endDate = DateUtil.parse(reservOrderPlaceReq.getDeparDate(),"yyyy-MM-dd");
            paramDate.add(DateUtil.date(startDate));
            Calendar calendarBlock = Calendar.getInstance();
            calendarBlock.setTime(startDate);
            while (endDate.after(calendarBlock.getTime())){
                calendarBlock.add(Calendar.DAY_OF_MONTH,1);
                paramDate.add(DateUtil.date(calendarBlock.getTime()));
            }
        }else {
            paramDate.add(DateUtil.parse(reservOrderPlaceReq.getGiftDate(),"yyyy-MM-dd"));
        }
        if (!CollectionUtils.isEmpty(paramDate)){
            for (Date orderDate : paramDate) {
                Assert.isTrue(bookDates.contains(orderDate),"当前时间不可预定");
            }
        }

        redisTemplate.opsForValue().set(RedisKeys.MARS_INSERTRESERVORDER_GIFT_CODE + giftCodeId, giftCode,5, TimeUnit.MINUTES);
//        Date endTime = DateUtil.parse(DateUtil.format(giftCode.getActEndTime(),"yyyy-MM-dd"),"yyyy-MM-dd");
//        Date nowTime = DateUtil.parse(DateUtil.format(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd"); //giftCode.getActCodeStatus().compareTo(ActCodeStatusEnum.ActCodeStatus.OUT.getIndex()) == 0 ||
        Assert.isTrue( giftCode.getActCodeStatus().compareTo(ActCodeStatusEnum.ActCodeStatus.ACTIVATE.getIndex()) == 0, "当前激活码状态为"+ActCodeStatusEnum.ActCodeStatus.findNameByIndex(giftCode.getActCodeStatus())+"！");
//        Assert.isTrue( !endTime.before(nowTime), "激活码已过期！");
        if(giftCode.getActEndTime()!=null){
            Date endTime = DateUtil.parse(DateUtil.format(giftCode.getActEndTime(),"yyyy-MM-dd"),"yyyy-MM-dd");
            Date nowTime = DateUtil.parse(DateUtil.format(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd");
            Assert.isTrue( !endTime.before(nowTime), "激活码已过期！");
        }
        reservOrderPlaceReq.setMemberId(memInfo.getAcid());
        if ("1".equals(reservOrderPlaceReq.getSuperposition())) {
            //查找是否有相同时段的预约单
            int flag = reservOrderMapper.countSameTimeOrderNum(reservOrderPlaceReq);
            Assert.isTrue(flag == 0, "预定失败，存在相同时段预约单");
        }
        if ("1".equals(reservOrderPlaceReq.getSingleThread())) {
            //行权完毕次日才能进行下次预约
            int flag = reservOrderMapper.countFinishNum(reservOrderPlaceReq);
            Assert.isTrue(flag == 0, "预定失败，行权完毕次日才能进行下次预约");
        }
        //优惠金额
        BigDecimal discountMoney = new BigDecimal(0);
        //初始化预定中
        BigDecimal payAmoney = reservOrderPlaceReq.getPayAmoney();
//        if(null == payAmoney || payAmoney.compareTo(new BigDecimal(0)) ==0) {
//            BookPayReq req = new BookPayReq();
//            BigDecimal casePayAmoney = reservOrderPlaceReq.getPayAmoney();
//            req.setProductGroupProductId(reservOrderPlaceReq.getProductGroupProductId());
//            if(reservOrderPlaceReq.getServiceType() == "accom") {
//                req.setStartDate(reservOrderPlaceReq.getCheckDate());
//                req.setEndDate(reservOrderPlaceReq.getDeparDate());
//            } else {
//                req.setStartDate(reservOrderPlaceReq.getGiftDate());
//                req.setEndDate(reservOrderPlaceReq.getGiftDate());
//            }
//            List<BookBasePaymentRes> bookBasePayments = this.selectBookPay(req);
//            log.info("bookBasePayments{}", bookBasePayments);
//            if (bookBasePayments != null || bookBasePayments.size() > 0) {
//                for (int i = 0; i < bookBasePayments.size(); i++) {
//                    BigDecimal number = bookBasePayments.get(i).getBookPrice();
//                    casePayAmoney = casePayAmoney.add(number);
//                    log.info("casePayAmoney{}", casePayAmoney);
//                }
//                if (casePayAmoney.compareTo(payAmoney) != 0) {
//                    throw new Exception("支付金额不符合");
//                }
//            }
//        }


        BookPayReq req = new BookPayReq();
        BigDecimal casePayAmoney = BigDecimal.ZERO;
        req.setProductGroupProductId(reservOrderPlaceReq.getProductGroupProductId());
        if(reservOrderPlaceReq.getServiceType().equalsIgnoreCase("accom") || serviceType.equalsIgnoreCase("accom")) {
            req.setStartDate(reservOrderPlaceReq.getCheckDate());
            req.setEndDate(reservOrderPlaceReq.getDeparDate());
        } else {
            req.setStartDate(reservOrderPlaceReq.getGiftDate());
            req.setEndDate(reservOrderPlaceReq.getGiftDate());
        }
        List<BookBasePaymentRes> bookBasePayments = this.selectBookPay(req);
        log.info("bookBasePayments{}", bookBasePayments);
        if(!CollectionUtils.isEmpty(bookBasePayments)){
            for (int i = 0; i < bookBasePayments.size(); i++) {
                BigDecimal number = bookBasePayments.get(i).getBookPrice();
                casePayAmoney = casePayAmoney.add(number);
                log.info("casePayAmoney{}", casePayAmoney);
            }
        }

        if (casePayAmoney == null && reservOrderPlaceReq.getPayAmoney() != null && reservOrderPlaceReq.getPayAmoney().intValue() != 0) {
            throw new Exception("订单信息有误，支付金额不符合！");
        }
        if(reservOrderPlaceReq.getUseFreeCount()<reservOrderPlaceReq.getExchangeNum()){
            if (casePayAmoney != null && casePayAmoney.intValue() != 0  && reservOrderPlaceReq.getPayAmoney() == null) {
                throw new Exception("订单信息有误，支付金额不符合！");
            }
            if (casePayAmoney != null && reservOrderPlaceReq.getPayAmoney() != null && casePayAmoney.compareTo(reservOrderPlaceReq.getPayAmoney()) != 0) {
                throw new Exception("订单信息有误，支付金额不符合！");
            }
        }
        if(payAmoney!=null&&!payAmoney.equals(0)){
            reservOrderPlaceReq.setTags("online,自付,");
            reservOrderPlaceReq.setPayStatus(PayOrderStatusEnum.UNPAID.getCode());

            //判断是否使用了优惠券
            if(null != reservOrderPlaceReq.getCpnType() && null != reservOrderPlaceReq.getCpnId()){
                if(TicketTypeEnum.VOUCHER.getValue().intValue() == reservOrderPlaceReq.getCpnType().intValue()){
                    //现金抵用券
                    CpnVoucherInfoReqVo cpnVoucherInfoReqVo = new CpnVoucherInfoReqVo();
                    cpnVoucherInfoReqVo.setVoucherNum(reservOrderPlaceReq.getCpnId());
                    CommonResultVo<CpnVoucherInfoResVo> commonResultVo = remoteCouponVouchersService.getCpnVoucherInfo(cpnVoucherInfoReqVo);
                    if (commonResultVo == null || commonResultVo.getResult() == null){
                        log.error("抵用券不存在{}",reservOrderPlaceReq.getCpnId());
                        throw new Exception("抵用券不存在");
                    }
                    Assert.isTrue(CpnVouchersStatusEunm.NOT_USE.getValue().intValue() == commonResultVo.getResult().getStatus(),"此优惠券不可用");
                    Assert.isTrue(commonResultVo.getResult().getUseType() == 1,"该优惠券在预约下单时不可用");
                    //优惠金额:可抵用金额
                    discountMoney = commonResultVo.getResult().getWorth();
                    //使用优惠券后需付金额
                    payAmoney = payAmoney.subtract(discountMoney);
//                        cpnGiftType = commonResultVo.getResult().getCpnGiftType();

                } else if(TicketTypeEnum.DISCOUNT.getValue().intValue() == reservOrderPlaceReq.getCpnType().intValue()){
                    //折扣券
                    CpnDiscountInfoReqVo cpnDiscountInfoReqVo = new CpnDiscountInfoReqVo();
                    cpnDiscountInfoReqVo.setDiscountNum(reservOrderPlaceReq.getCpnId());
                    CommonResultVo<CpnDiscountsInfoResVo> commonResultVo = remoteCouponDiscountService.getCpnDiscountInfo(cpnDiscountInfoReqVo);
                    if (commonResultVo == null || commonResultVo.getResult() == null){
                        log.error("折扣券不存在{}",reservOrderPlaceReq.getCpnId());
                        throw new Exception("折扣券不存在");
                    }
                    Assert.isTrue(CpnVouchersStatusEunm.NOT_USE.getValue().intValue() == commonResultVo.getResult().getStatus(),"优惠券不可用");
                    Assert.isTrue(commonResultVo.getResult().getUseType() == 1,"该优惠券在预约下单时不可用");
//                        cpnGiftType = commonResultVo.getResult().getCpnGiftType();
                    //折扣比例
                    BigDecimal discountRatio = commonResultVo.getResult().getDiscountRatio();
                    //最高可折扣金额(0:表示无限制)
                    BigDecimal upperAmount =  commonResultVo.getResult().getUpperAmount();
                    if(upperAmount.compareTo(new BigDecimal(0)) == 0){
                        //无限制
                        //优惠金额
                        discountMoney = payAmoney.subtract(payAmoney.multiply(discountRatio).divide(new BigDecimal(10)));
                        //打折后需付金额
                        payAmoney = payAmoney.multiply(discountRatio).divide(new BigDecimal(10));

                    } else if(payAmoney.subtract(payAmoney.multiply(discountRatio).divide(new BigDecimal(10))).compareTo(upperAmount) == 1) {
                        //打折优惠金额 > 最大优惠金额
                        //优惠金额
                        discountMoney = upperAmount;
                        //打折后需付金额
                        payAmoney = payAmoney.subtract(upperAmount);
                    } else {
                        //优惠金额
                        discountMoney = payAmoney.subtract(payAmoney.multiply(discountRatio).divide(new BigDecimal(10)));
                        //打折后需付金额
                        payAmoney = payAmoney.multiply(discountRatio.divide(new BigDecimal(10)));

                    }
                }
            }
        }else if((null == payAmoney || payAmoney.compareTo(new BigDecimal(0)) ==0) && (null != reservOrderPlaceReq.getCpnType() && null != reservOrderPlaceReq.getCpnId())){
            reservOrderPlaceReq.setTags("online,自付,");
            reservOrderPlaceReq.setPayStatus(PayOrderStatusEnum.PREPAID.getCode());
            payAmoney = new BigDecimal(0);
            //现金抵用券会出现用券后支付金额为0的情况
            CpnVoucherInfoReqVo cpnVoucherInfoReqVo = new CpnVoucherInfoReqVo();
            cpnVoucherInfoReqVo.setVoucherNum(reservOrderPlaceReq.getCpnId());
            CommonResultVo<CpnVoucherInfoResVo> commonResultVo = remoteCouponVouchersService.getCpnVoucherInfo(cpnVoucherInfoReqVo);
            if (commonResultVo == null || commonResultVo.getResult() == null){
                throw new Exception("优惠券不存在");
            }
            Assert.isTrue(CpnVouchersStatusEunm.NOT_USE.getValue().intValue() == commonResultVo.getResult().getStatus(),"此优惠券不可用");
            //优惠金额:可抵用金额
            discountMoney = commonResultVo.getResult().getWorth();
        }else {
            reservOrderPlaceReq.setTags("online,");
            reservOrderPlaceReq.setPayStatus(PayOrderStatusEnum.PREPAID.getCode());
        }

        if( StringUtils.isEmpty(reservOrderPlaceReq.getOrderSource())){
            reservOrderPlaceReq.setOrderSource(ReservOrderTypeEnums.ReservOrderType.ONLINE.getcode());
        }

        //单杯直接预约成功
        //不需要支付的礼券类型:直接预约成功
        if(ResourceTypeEnums.DRINK.getCode().equalsIgnoreCase(serviceType) || (serviceType.indexOf("_cpn") != -1 && (null == reservOrderPlaceReq.getPayAmoney() || reservOrderPlaceReq.getPayAmoney().compareTo(new BigDecimal(0)) == 0))){
            reservOrderPlaceReq.setGiftName(memInfo.getMbName());
            reservOrderPlaceReq.setGiftPhone(memInfo.getMobile());
        }
        reservOrderPlaceReq.setProseStatus( ReservOrderStatusEnums.ReservOrderStatus.none.getcode());
        //保存主表
        ReservOrder reservOrder = reservOrderPlaceReq;
        //第三方产品编号和名称保存
        if (reservOrder.getShopItemId() != null){
            ShopItemRes shopItemRes = panguInterfaceService.getShopItemById(reservOrder.getShopItemId());
            if (shopItemRes != null){
                reservOrder.setThirdCpnNum(shopItemRes.getThirdCpnNum());
                reservOrder.setThirdCpnName(shopItemRes.getThirdCpnName());
            }
        }
        //处理预约单
        String giftDate = reservOrderPlaceReq.getGiftDate();
        if (ResourceTypeEnums.ACCOM.getCode().equals(serviceType) && StringUtils.isEmpty(giftDate)) {
            //住宿入店时间为预定时间
            reservOrderPlaceReq.setGiftDate(reservOrderPlaceReq.getCheckDate());
        }
        reservOrder.setSalesChannleId(giftCode.getSalesChannelId());

        //判断产品状态
        final ProductGroupProduct productGroupProduct = panguInterfaceService.findProductGroupProductById(reservOrder.getProductGroupProductId());
        Assert.notNull(productGroupProduct.getStatus().compareTo(0)==0,"商品已停售");
        //第三方券产品编号(shopItem里取)
        reservOrder.setThirdCpnNum(reservOrderPlaceReq.getThirdCpnNum() != null?reservOrderPlaceReq.getThirdCpnNum():null);
        reservOrder.setPayAmount(payAmoney);
        reservOrder.setDiscountAmount(discountMoney);
        reservOrder.setTotalAmount(payAmoney);
        final Integer insert = reservOrderMapper.insert(reservOrder);
        Assert.isTrue(insert == 1,"预约单下单失败");

        //下预约单成功,如果使用了优惠券，修改优惠券状态为使用中
        if(null != reservOrderPlaceReq.getCpnType() && null != reservOrderPlaceReq.getCpnId()) {
            UpdateCpnReqVo updateCpnReqVo = new UpdateCpnReqVo();
            updateCpnReqVo.setCouponsType(reservOrderPlaceReq.getCpnType());
            updateCpnReqVo.setCpnId(reservOrderPlaceReq.getCpnId());
            updateCpnReqVo.setAcId(memInfo.getAcid());
            updateCpnReqVo.setUseTime(new Date());
            updateCpnReqVo.setStatus(CpnVouchersStatusEunm.IN_USE.getValue()); //使用中
            final Boolean f = couponsService.updateCoupon(updateCpnReqVo);
            if(!f){
                throw new Exception("更新券状态失败");
            }
        }

        //如果是实物券，则保存配送方式和地址
        if (ResourceTypeEnums.OBJECT_CPN.getCode().equalsIgnoreCase(serviceType)){
            LogisticsInfo logisticsInfo = new LogisticsInfo();
            logisticsInfo.setReservOrderId(reservOrder.getId());
            logisticsInfo.setConsignee(reservOrderPlaceReq.getConsignee());
            logisticsInfo.setPhone(reservOrderPlaceReq.getExpressPhone());
            logisticsInfo.setAddress(reservOrderPlaceReq.getExpressAddress());
            logisticsInfo.setExpressMode(reservOrderPlaceReq.getExpressMode());
            logisticsInfo.setStatus(ExpressStatusEnum.NON_SEND.getCode());
            logisticsInfoService.insert(logisticsInfo);
        }
        BigDecimal point = productGroupProduct.getPoint();
        reservOrderPlaceReq.setExchangeNum(point.intValue());

        //TODO....医疗预约  medical other
        if (ResourceTypeEnums.MEDICAL.getCode().equalsIgnoreCase(serviceType) ) {
            //医疗预约
            ReservOrderHospital hospital =new ReservOrderHospital();

            hospital.setOrderId(reservOrder.getId());
            SysHospital sysHospital = hospitalService.selectById(reservOrderPlaceReq.getHospitalId());
            Assert.notNull(sysHospital, "医院数据不能为空！");
            Assert.notNull(reservOrderPlaceReq.getMemFamilyId(), "就诊人ID不能为空！");
            hospital.setMemFamilyId(reservOrderPlaceReq.getMemFamilyId());
            hospital.setHospitalId(sysHospital.getId());
            hospital.setCity(sysHospital.getCity());
            hospital.setProvince(sysHospital.getProvince());
            hospital.setName(sysHospital.getName());
            hospital.setGrade(sysHospital.getGrade());
            hospital.setHospitalType(sysHospital.getHospitalType());
            hospital.setDepartment(reservOrderPlaceReq.getDepartment());
            hospital.setVisit(reservOrderPlaceReq.getVisit());
            hospital.setSpecial(reservOrderPlaceReq.getSpecial());
            final boolean sysHospitalinsert = reservOrderHospitalService.insert(hospital);
            Assert.isTrue(sysHospitalinsert,"新增预约单医院详情失败");

        }

        if (ResourceTypeEnums.ACCOM.getCode().equalsIgnoreCase(serviceType)) {
            checkAccom(reservOrderPlaceReq);
            if(GiftTypeEnum.NX.getCode().equalsIgnoreCase(reservOrder.getGiftType())){
                BigDecimal checkNight = new BigDecimal(reservOrderPlaceReq.getCheckNight()).multiply(new BigDecimal(reservOrderPlaceReq.getNightNumbers()));
                //这单需要的总点数
                point = point.multiply(checkNight);
            }else{
                point = point.multiply(new BigDecimal(reservOrderPlaceReq.getCheckNight()));
            }
            reservOrderPlaceReq.setExchangeNum(point.intValue());

            //保存用户信息
            if (reservOrderPlaceReq.getSelf()) {
                //当预订人是本人的时候才修改他的信息
                //补充身份证或者护照信息
                String idType = reservOrderPlaceReq.getBookIdType();
                if (ReservOrderStatusEnums.IdTypeStatus.ID_CARD.getCode().equals(idType)) {
                    memInfo.setIdNumber(reservOrderPlaceReq.getBookIdNum());
                } else if (ReservOrderStatusEnums.IdTypeStatus.PASSPORT.getCode().equals(idType)) {
                    memInfo.setPassportNum(reservOrderPlaceReq.getBookIdNum());
                } else if(ReservOrderStatusEnums.IdTypeStatus.HK_MACAO_PASS.getCode().equalsIgnoreCase(idType)){
                    //首次港澳通行证入库
                    //判断该用户是否已经填写过港澳通行证
                    MemMemberInfo memMemberInfo = new MemMemberInfo();
                    memMemberInfo.setMbid(memInfo.getMbid());
                    CommonResultVo<List<MemberCertificate>> res = remoteMemberInfoServcie.getMemberCertificateInfo(memMemberInfo);
                    if(null != res ){
                        if(CollectionUtils.isEmpty(res.getResult()) || null == res.getResult().stream().collect(Collectors.toMap(MemberCertificate::getCertificateType,
                                MemberCertificate::getCertificateNumber)).get(ReservOrderStatusEnums.IdTypeStatus.HK_MACAO_PASS.getCode())){
                            MemberCertificate memberCertificate = new MemberCertificate();
                            memberCertificate.setMbid(memInfo.getMbid());
                            memberCertificate.setCertificateType(reservOrderPlaceReq.getBookIdType());
                            memberCertificate.setCertificateNumber(reservOrderPlaceReq.getBookIdNum());
                            memberCertificate.setMbName(reservOrderPlaceReq.getBookName());
                            memberCertificate.setMbPhoneticName(reservOrderPlaceReq.getBookNameEn());
                            CommonResultVo<Boolean> f = remoteMemberInfoServcie.insertMemberCertificate(memberCertificate);
                            Assert.isTrue(f != null && resultVo.getCode() == 100, "插入用户证件信息失败！");
                        }
                    }
                }
                if(StringUtils.isEmpty(memInfo.getMbName())){
                    String name = reservOrderPlaceReq.getBookName();
                    if(!StringUtils.isEmpty(name)){
                        memInfo.setMbName(name.split(",")[0]);
                    }
                }
                String bookNameEn = reservOrderPlaceReq.getBookNameEn();
                //英文名
                if (!StringUtils.isEmpty(bookNameEn)) {
                    memInfo.setPassportName(bookNameEn.split(",")[0]);
                }
                //手机号
                if(memInfo.getMobile()==null) {
                    memInfo.setMobile(reservOrderPlaceReq.getBookPhone());
                }
                //执行 会员接口
//                CommonResultVo<MemLoginResDTO> resVo = remoteLoginService.updateMemlogInInfo(memInfo);
//                Assert.isTrue(resVo != null && resVo.getCode() == 100, "系统繁忙！");
//                if (resVo.getResult().getLoginToken() != null) {
//                    //放入缓存
//                    redisTemplate.opsForValue().set("MEM_" + resVo.getResult().getLoginToken(), resVo.getResult());
//                }
            }
        }
        //设置Detail
        ReservOrderDetail reservOrderDetail = new ReservOrderDetail();
        ShopProtocolRes shopProtocolRes =  panguInterfaceService.selectShopProtocol(reservOrderPlaceReq.getShopId());
        if(null != shopProtocolRes && org.apache.commons.lang.StringUtils.isNotBlank(shopProtocolRes.getNotice())){
            reservOrderDetail.setNotice(shopProtocolRes.getNotice());
        }
        BeanUtils.copyProperties(reservOrderPlaceReq,reservOrderDetail);
        reservOrderDetail.setProductType(serviceType);
        reservOrderDetail.setOrderId(reservOrder.getId());
        //实际支付金额
        reservOrderDetail.setPayAmoney(payAmoney);
        //优惠金额
        reservOrderDetail.setDiscountAmoney(discountMoney);
        //支付总额
        reservOrderDetail.setTotalAmoney(payAmoney);
        Integer updateFlag = reservOrderDetailMapper.insert(reservOrderDetail);

            if (updateFlag <= 0) {
                throw new Exception("保存预约单详单失败");
            }
            //扣减权益
            //TODO 用户有会员权益券(0),但是没使用的情况不扣减权益次数
            //1.无需自付的预约--扣减权益次数
            //2.用户在该产品下没有会员权益券---扣减权益次数;
            //3.用户在该产品下有会员权益券，并且已经选用---扣减权益次数
            if("online,".equals(reservOrderPlaceReq.getTags())
                    ||CollectionUtils.isEmpty(vipCpnList)
                    || ( null != reservOrderPlaceReq.getCpnId() && vipCpnList.contains(reservOrderPlaceReq.getCpnId()))){
                try {
                    EquityCodeDetail detail = equityCodeDetailService.changeGiftTimes(ReservOrderStatusEnums.typeEnum.SUB.getCode(), memInfo.getAcid(), reservOrderPlaceReq.getGoodsId(), reservOrderPlaceReq.getProductGroupId(), giftCodeId, reservOrderPlaceReq.getExchangeNum(), reservOrderPlaceReq.getCreateTime(),reservOrderPlaceReq.getUseFreeCount());
                    if (detail == null) {
                        throw new Exception("权益次数更改失败");
                    }
                    reservOrder.setCodeDetailId(detail.getId());
                } catch (Exception e){
                    throw new Exception("请检查权益使用次数");
                }
                final Integer updateById = reservOrderMapper.updateById(reservOrder);
                if (updateById <= 0) {
                    throw new Exception("保存reserve_order表中的CodeDetailId失败");
                }
            }else {
                //不需要扣减权益次数，设置reserv_order表的exchange_num为0
                reservOrder.setExchangeNum(0);
                Integer count = reservOrderMapper.updateById(reservOrder);
                Assert.isTrue(count == 1,"更新预约单信息失败");
            }
            //如果是单杯直接发核销码
//            if(ResourceTypeEnums.DRINK.getCode().equalsIgnoreCase(serviceType)){
//                ReservCode reservCode = createCode(reservOrderPlaceReq);
//                final Integer insertCode = reservCodeMapper.insert(reservCode);
//                Assert.isTrue(insertCode == 1,"生成核销码失败");
//                reservOrder.setReservNumber(reservCode.getVarCode());
//                reservOrder.setChannelNumber(reservCode.getVarCode());
//                final Integer updateReservOrder = reservOrderMapper.updateById(reservOrder);
//                Assert.isTrue(updateReservOrder == 1,"更新预约单信息失败");
//            }
        //保存价格
        ReservOrderVo reservOrderVo = new ReservOrderVo();
        reservOrderVo.setNightNumbers(reservOrderPlaceReq.getNightNumbers());
        reservOrderVo.setCheckNight(reservOrderPlaceReq.getCheckNight());
        reservOrderService.insertPriceInfo(reservOrderVo, reservOrderPlaceReq);

        //发送短信 预约确认
        nuwaInterfaceService.sendMsg(reservOrder);
        BeanUtils.copyProperties(reservOrder,resVO);
        return resVO;
    }


    //生成核销码
    private ReservCode createCode(ReservOrderPlaceReq reservOrderPlaceReq) {
        ReservCode reservCode = reservCodeService.selectOneReservCode(reservOrderPlaceReq.getId());
        if (null == reservCode) {
            reservCode = new ReservCode();
            reservCode.setOrderId(reservOrderPlaceReq.getId());
            reservCode.setProductGroupId(reservOrderPlaceReq.getProductGroupId());
            String code = reservOrderPlaceReq.getVarCode();
            if (StringUtils.isEmpty(reservOrderPlaceReq.getVarCode())) {
                try {
                    code = CodeUtils.getCodeByRedis(GiftCodeConstants.GIFT_VER_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            reservOrderPlaceReq.setReservNumber(code);
            reservCode.setVarCode(code);
            reservCode.setCreateUser(SecurityUtils.getLoginName());
            reservCode.setVarStatus(HxCodeStatusEnum.HxCodeStatus.NEWONE.getIndex() + "");
            reservCode.setVarCrtTime(DateUtil.parse(reservOrderPlaceReq.getGiftDate(), DatePattern.NORM_DATE_PATTERN));
            Calendar c = Calendar.getInstance();
            c.setTime(reservCode.getVarCrtTime());
            c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                    23, 59, 59);
            reservCode.setVarExpireTime(c.getTime());
            reservCode.setCreateUser(SecurityUtils.getLoginName());
        }
        return reservCode;
    }

    @Override
    public Map<String, Object> getGoodsSetting(GoodsSettingReq req) {
        Integer goodsId = req.getGoodsId();
        Integer productGroupId = req.getProductGroupId();
        Integer giftCodeId = req.getGiftCodeId();
        Assert.isTrue(goodsId != null && productGroupId != null & giftCodeId != null, "参数错误");
        GoodsSetting goodsSetting = panguInterfaceService.selectGoodsSettingById(goodsId);
        EquityCodeDetail equityCodeDetail = equityCodeDetailService.selectByEquityCode(null, goodsId, productGroupId, giftCodeId,new Date());
        Map<String, Object> result = new HashMap<>();
        result.put("goodsSetting", goodsSetting);
        result.put("equityCodeDetail", equityCodeDetail);
        return result;
    }

    private ReservOrderDetail setReservOrderDetailInfo(ReservOrderPlaceReq reservOrderPlaceReq) {
        ReservOrderDetail reservOrderDetail = new ReservOrderDetail();
        reservOrderDetail.setOrderId(reservOrderPlaceReq.getId());
        reservOrderDetail.setProductType(reservOrderPlaceReq.getServiceType());
        reservOrderDetail.setGoodsId(reservOrderPlaceReq.getGoodsId());
        reservOrderDetail.setCheckDate(reservOrderPlaceReq.getCheckDate());
        reservOrderDetail.setDeparDate(reservOrderPlaceReq.getDeparDate());
        reservOrderDetail.setCheckNight(reservOrderPlaceReq.getCheckNight());
        return reservOrderDetail;
    }



    @Override
    public BookBasePaymentRes getMinPrice(BookPayReq req) {
        SelectBookPayReq selectBookPayReq =getSelectBookPayReq(req);
        List<BookBasePaymentRes>  payments =  panguInterfaceService.selectBookPay(selectBookPayReq);
        BookBasePaymentRes minPriceRes = null;
        for(BookBasePaymentRes res :payments){
            if(minPriceRes==null){
                minPriceRes= res;
            }else if(minPriceRes.getBookPrice().compareTo(res.getBookPrice())>0){
                minPriceRes = res;
            }
        }
        return minPriceRes;
    }

    @Override
    public  List<BookBasePaymentRes> selectBookPay(BookPayReq req) {
        SelectBookPayReq selectBookPayReq =getSelectBookPayReq(req);
        List<BookBasePaymentRes>  payments =  panguInterfaceService.selectBookPay(selectBookPayReq);
        return payments;
    }


    @Override
    public PriceRes getStorePrice(BookPayReq req){
        List<ShopItemNetPriceRule> priceRules = panguInterfaceService.selectProductPrices(req.getProductGroupProductId());
        return getPrice(priceRules,req.getStartDate()) ;
    }
    /*
     * 获取单人总价格
     * */
    private PriceRes getPrice(List<ShopItemNetPriceRule> priceRules,String bookDate) {
        PriceRes priceRes = new PriceRes();
        if (priceRules == null || priceRules.isEmpty()) {
            return priceRes;
        }
        //获取当前时间
        Date date = DateUtil.parseDate(bookDate);
        //获取当前星期几  0 -星期天
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        for (ShopItemNetPriceRule priceRule : priceRules) {
            //判断是否在日期范围
            if (date.compareTo(priceRule.getStartDate()) >= 0 && date.compareTo(priceRule.getEndDate()) <= 0) {
                //判断当前周几是否可用
                int isUse = 0;
                switch (w) {
                    case 0:
                        isUse = priceRule.getSunday();
                        break;
                    case 1:
                        isUse = priceRule.getMonday();
                        break;
                    case 2:
                        isUse = priceRule.getTuesday();
                        break;
                    case 3:
                        isUse = priceRule.getWednesday();
                        break;
                    case 4:
                        isUse = priceRule.getThursday();
                        break;
                    case 5:
                        isUse = priceRule.getFriday();
                        break;
                    case 6:
                        isUse = priceRule.getSaturday();
                        break;
                }
                if (isUse == 1) {
                    //价格= (价格+（价格*服务费率）+（(价格+（价格*服务费率）)*税率）)
                   BigDecimal price = priceRule.getNetPrice().add(
                            priceRule.getNetPrice().multiply(priceRule.getServiceRate()));
                    price =price.add(price.multiply(priceRule.getTaxRate()));
                    priceRes.setNetPrice(priceRule.getNetPrice());
                    priceRes.setPayPrice(price);
                    priceRes.setServiceRate(priceRule.getServiceRate());
                    priceRes.setTaxRate(priceRule.getTaxRate());
                    return priceRes;
                }
            }
        }

        return priceRes;
    }

    private static void checkAccom(ReservOrderPlaceReq reservOrderPlaceReq)throws Exception{
        Assert.notNull(reservOrderPlaceReq,"参数不能为空");
        final String giftType = reservOrderPlaceReq.getGiftType();
        final Integer nightNumbers = reservOrderPlaceReq.getNightNumbers();
        Assert.hasText(giftType,"权益类型不能为空");
        Assert.isTrue(giftType.length()==2,"权益类型不能为空");
        Assert.isTrue(giftType.startsWith("N"),"权益类型有误");
        Assert.notNull(nightNumbers,"入住夜晚数不能为空");
        final String night = giftType.substring(1);
        if(!night.equals("X")){
            Integer nightNum=Integer.parseInt(night);
            Assert.isTrue(nightNum.compareTo(nightNumbers)==0,"入住夜晚数有误");
        }
        final DateTime checkDate = DateUtil.parse(reservOrderPlaceReq.getCheckDate(),"yyyy-MM-dd");
        final DateTime deparDate = DateUtil.parse(reservOrderPlaceReq.getDeparDate(),"yyyy-MM-dd");
        final long between = deparDate.between(checkDate, DateUnit.DAY);
        Assert.isTrue(nightNumbers.compareTo(Integer.parseInt(between+""))==0,"入住日期、离店日期与入住夜晚不符");
    }
    private SelectBookPayReq getSelectBookPayReq(BookPayReq req){
        SelectBookPayReq selectBookPayReq = new SelectBookPayReq();
        selectBookPayReq.setProductGroupProductId(req.getProductGroupProductId());
        List<Date> dates =  HelpUtils.getDatesBetweenTwoDate(DateUtil.parseDate(req.getStartDate()),DateUtil.parseDate(req.getEndDate()),false);
        selectBookPayReq.setBookDates(dates);
        return selectBookPayReq;
    }

//    public static void main(String[] args){
//        Integer status  = 2;
//        Assert.isTrue( status.compareTo(ActCodeStatusEnum.ActCodeStatus.ACTIVATE.getIndex()) == 0, "当前激活码状态为"+ActCodeStatusEnum.ActCodeStatus.findNameByIndex(status)+"！");
//    }
}

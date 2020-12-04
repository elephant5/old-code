package com.colourfulchina.mars.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.colourfulchina.aggregatePay.api.vo.res.BOSNetpayRes;
import com.colourfulchina.aggregatePay.enums.PayChannelEnum;
import com.colourfulchina.aggregatePay.fegin.RemotePaymentOrderService;
import com.colourfulchina.aggregatePay.utils.PayDigestUtil;
import com.colourfulchina.aggregatePay.vo.req.OrderRefundReq;
import com.colourfulchina.aggregatePay.vo.res.PaymentInfoRes;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.GiftCode;
import com.colourfulchina.mars.api.entity.ReservOrder;
import com.colourfulchina.mars.api.entity.ReservOrderDetail;
import com.colourfulchina.mars.api.entity.SourceMerchantInfo;
import com.colourfulchina.mars.api.enums.PayOrderStatusEnum;
import com.colourfulchina.mars.api.vo.req.AggregatePayParamsReqVo;
import com.colourfulchina.mars.api.vo.req.PayReqVO;
import com.colourfulchina.mars.api.vo.res.PayParamsResVo;
import com.colourfulchina.mars.config.PayInfoProperties;
import com.colourfulchina.mars.mapper.ReservOrderMapper;
import com.colourfulchina.mars.service.*;
import com.colourfulchina.member.api.req.MemLoginReqDTO;
import com.colourfulchina.member.api.res.MemLoginResDTO;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import netscape.javascript.JSObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 支付相关接口
 */
@Service
@Slf4j
@AllArgsConstructor
public class PayInfoServiceImpl implements PayInfoService {

    @Autowired
    ReservOrderMapper reservOrderMapper;

    @Autowired
    MemberInterfaceService memberInterfaceService;

    @Autowired
    GiftCodeService giftCodeService;

    @Autowired
    ReservOrderDetailService reservOrderDetailService;

    @Autowired
    PanguInterfaceService panguInterfaceService;

    @Autowired
    HttpServletRequest httpRequest;

    @Autowired
    private SourceMerchantInfoService sourceMerchantInfoService;
    @Autowired
    private PayInfoProperties payInfoProperties;

    RemotePaymentOrderService remotePaymentOrderService;


    public PaymentInfoRes orderPayment(String params) throws Exception {
        CommonResultVo<PaymentInfoRes> result = remotePaymentOrderService.orderPayment(params);
        return result == null ? null : result.getResult();
    }

    public String payOrderRefund(OrderRefundReq orderRefundReq) throws Exception {
        return remotePaymentOrderService.payOrderRefund(orderRefundReq).getResult();
    }

    /**
     * 预约单支付    new
     * @param reqVO
     * @return
     * @throws Exception
     */
    @Override
    public PaymentInfoRes reservOrderPay(PayReqVO reqVO) throws Exception {
        if (null == reqVO) {
            throw new Exception("参数不能为空");
        }
        PaymentInfoRes resultVo = new PaymentInfoRes();
        //获取预约单信息
//        ReservOrder reservOrder = reservOrderService.selectById(reqVO.getOrderId());
        ReservOrder reservOrder = reservOrderMapper.selectById(reqVO.getOrderId());
        Assert.notNull(reservOrder, "未查到对应的预约单");
        final String orderSource = reservOrder.getOrderSource();
        Assert.hasText(orderSource, "未查到预约单渠道来源");

        final SourceMerchantInfo sourceMerchantInfo = sourceMerchantInfoService.selectById(reqVO.getPayMethod());
        Assert.notNull(sourceMerchantInfo, "未查到预约单对应的支付渠道信息");

        GiftCode giftCode = giftCodeService.selectById(reservOrder.getGiftCodeId());
        Assert.notNull(giftCode, "激活码数据不能为空！");
        GoodsBaseVo goods = panguInterfaceService.selectGoodsById(reservOrder.getGoodsId());
        ReservOrderDetail detail = reservOrderDetailService.selectOneReservOrderDetail(reservOrder.getId());

        if (!reservOrder.getPayStatus().equals(PayOrderStatusEnum.UNPAID.getCode())) {
            Assert.notNull(null, "当前订单已支付或取消，请勿重复支付！");
        }

        if (BigDecimal.ZERO.compareTo(detail.getPayAmoney()) < 0) {
            MemLoginReqDTO memMemberInfo = new MemLoginReqDTO();
            memMemberInfo.setMobile(reservOrder.getGiftPhone());
            memMemberInfo.setAcChannel(goods.getBankCode());
            memMemberInfo.setSmsCode("colour");
            //获取用户信息
            MemLoginResDTO member = memberInterfaceService.memberLogin(memMemberInfo);
            //TODO test
//            MemLoginResDTO member = new MemLoginResDTO();
//            member.setAcid(Long.valueOf("1185003889918861"));
//            member.setMbid(Long.valueOf("1098869928885288"));
//            member.setOpenId("oSVtC1ldk-zeEu-NwYsNW0ZE391s");
//            member.setCtName("test");

            //封装聚合支付入参
            Map params = Maps.newHashMap();
            params.put("mbId", member.getMbid());
            params.put("acId", member.getAcid());
            params.put("accomName", member.getCtName());

            params.put("merchantId", sourceMerchantInfo.getMerchantId());      // 商户ID
            params.put("paymentType", "预约单");       // 支付单类型(销售单、预约单)
            params.put("goodsName", goods.getName());            // 商品名称
            params.put("body", goods.getName());                      // 商品描述信息
            params.put("orderId", reservOrder.getId());             // 商户订单号

            //根据支付类型获取渠道id
            params.put("payChannelId", PayChannelEnum.getPayChannelId(reqVO.getPayMethod()));
            params.put("amount", detail.getPayAmoney());  // 支付金额
            params.put("payMethod", 3);      // 支付方式(1:免费兑换、2:纯积分、3:纯金额,4:积分+金额,5:分期,6:赠送)
            params.put("curcode", "CNY");             // 币种：默认人民币
            final Map<String, String> source2Map = orderSource2Map(orderSource);
            if (CollectionUtils.isEmpty(source2Map) && source2Map.containsKey("channel") && source2Map.containsKey("type")) {
                params.put("mainPage", payInfoProperties.getBookingUrl() + source2Map.get("channel") + "&type=" + source2Map.get("type"));
            } else {
                log.info("没有找到回调地址:{}", orderSource);
            }
            params.put("source", orderSource);

            if(PayChannelEnum.PAY_CHANNEL_WX_JSAPI.getValue().equalsIgnoreCase(reqVO.getPayMethod())){
                //微信公众号支付,需要用户的openId
                params.put("openId",member.getOpenId());
            }

            String sign = PayDigestUtil.getSign(params, sourceMerchantInfo.getMerchantKey());
            params.put("sign", sign);

            String jsonString = JSON.toJSONString(params);// 签名
            log.info("验签数据{}", sourceMerchantInfo);
            log.info("{},{}", reservOrder.getId(), jsonString);
            //调用聚合支付
            try {
                CommonResultVo<PaymentInfoRes> result = remotePaymentOrderService.orderPayment(jsonString);
                if(null != result && null != result.getResult()){
                    BeanUtils.copyProperties(result.getResult(),resultVo);
                    return resultVo;
                }else {
                    return null;
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new Exception("调用聚合支付失败");
            }

        } else {
            reservOrder.setPayStatus(PayOrderStatusEnum.PREPAID.getCode());
//            reservOrderService.updateById(reservOrder);
            reservOrderMapper.updateById(reservOrder);
            PaymentInfoRes paymentInfoRes = new PaymentInfoRes();
            BOSNetpayRes res = new BOSNetpayRes();
            res.setMerOrderNum(reqVO.getOrderId() + "");
            res.setMerchantRemarks("支付金额为0，则直接支付成功！");
            paymentInfoRes.setBosNetpayRes(res);
            BeanUtils.copyProperties(paymentInfoRes,resultVo);
            return resultVo;
        }
    }

    @Override
    public PayParamsResVo getPayParamsByOrderId(String orderId) throws Exception {
        if (null == orderId) {
            throw new Exception("参数不能为空");
        }
        PayParamsResVo resVo = new PayParamsResVo();
        String jsonString = null ;
        ReservOrder reservOrder = reservOrderMapper.selectById(orderId);
        Assert.notNull(reservOrder, "未查到对应的预约单");
        final String orderSource = reservOrder.getOrderSource();
        Assert.hasText(orderSource, "未查到预约单渠道来源");

        final SourceMerchantInfo sourceMerchantInfo = sourceMerchantInfoService.selectById(reservOrder.getOrderSource());
        Assert.notNull(sourceMerchantInfo, "未查到预约单对应的支付渠道信息");

        GiftCode giftCode = giftCodeService.selectById(reservOrder.getGiftCodeId());
        Assert.notNull(giftCode, "激活码数据不能为空！");
        GoodsBaseVo goods = panguInterfaceService.selectGoodsById(reservOrder.getGoodsId());
        ReservOrderDetail detail = reservOrderDetailService.selectOneReservOrderDetail(reservOrder.getId());

        if (!reservOrder.getPayStatus().equals(PayOrderStatusEnum.UNPAID.getCode())) {
            Assert.notNull(null, "当前订单已支付或取消，请勿重复支付！");
        }

        if (BigDecimal.ZERO.compareTo(detail.getPayAmoney()) < 0) {
            MemLoginReqDTO memMemberInfo = new MemLoginReqDTO();
            memMemberInfo.setMobile(reservOrder.getGiftPhone());
            memMemberInfo.setAcChannel(goods.getBankCode());
            memMemberInfo.setSmsCode("colour");
            //获取用户信息
            MemLoginResDTO member = memberInterfaceService.memberLogin(memMemberInfo);
            //TODO test
//            MemLoginResDTO member = new MemLoginResDTO();
//            member.setAcid(Long.valueOf("1185003889918861"));
//            member.setMbid(Long.valueOf("1098869928885288"));
//            member.setOpenId("oSVtC1ldk-zeEu-NwYsNW0ZE391s");
//            member.setCtName("test");

            //封装聚合支付入参
            Map params = Maps.newHashMap();
            params.put("mbId", member.getMbid());
            params.put("acId", member.getAcid());
            params.put("accomName", member.getCtName());
            params.put("openId",member.getOpenId());
            params.put("merchantId", sourceMerchantInfo.getMerchantId());      // 商户ID
            params.put("paymentType", "预约单");       // 支付单类型(销售单、预约单)
            params.put("goodsName", goods.getName());            // 商品名称
            params.put("body", goods.getName());                      // 商品描述信息
            params.put("orderId", orderId);             // 商户订单号

            params.put("amount", detail.getPayAmoney());  // 支付金额
            params.put("payMethod", 3);      // 支付方式(1:免费兑换、2:纯积分、3:纯金额,4:积分+金额,5:分期,6:赠送)
            params.put("curcode", "CNY");             // 币种：默认人民币

            params.put("source", orderSource);


            String sign = PayDigestUtil.getSign(params, sourceMerchantInfo.getMerchantKey());
            params.put("sign", sign);

            jsonString = JSON.toJSONString(params);// 签名

            resVo.setParams(jsonString);
            log.info("参数{}", jsonString);
        }
        return resVo;

    }

    @Override
    public PayParamsResVo getAggregatePayParams(AggregatePayParamsReqVo reqVo) throws Exception {
        if (null == reqVo) {
            throw new Exception("参数不能为空");
        }
        PayParamsResVo resVo = new PayParamsResVo();
        String jsonString = null ;

        final SourceMerchantInfo sourceMerchantInfo = sourceMerchantInfoService.selectById(reqVo.getSource());
        Assert.notNull(sourceMerchantInfo, "未查到预约单对应的支付渠道信息");
        //封装聚合支付入参
        Map params = Maps.newHashMap();
        params.put("acId", reqVo.getAcId());
        params.put("merchantId", sourceMerchantInfo.getMerchantId());      // 商户ID
        params.put("paymentType", reqVo.getPaymentType());       // 支付单类型(销售单、预约单)
        params.put("goodsName", reqVo.getGoodsName());            // 商品名称
        params.put("body", reqVo.getBody());                      // 商品描述信息
        params.put("orderId", reqVo.getOrderId());             // 商户订单号
        params.put("amount", reqVo.getAmount());  // 支付金额
        params.put("payMethod", reqVo.getPayMethod());      // 支付方式(1:免费兑换、2:纯积分、3:纯金额,4:积分+金额,5:分期,6:赠送)
        params.put("curcode", reqVo.getCurcode());             // 币种：默认人民币
        params.put("source", reqVo.getSource());
        params.put("mbId",reqVo.getMbId());
        params.put("openId",reqVo.getOpenId());
        if(reqVo.getAgreementId() != null || reqVo.getAgreementId() != "") {
            params.put("agreementId", reqVo.getAgreementId());
        }
        if(!StringUtils.isEmpty(reqVo.getCustId())){
            params.put("custId",reqVo.getCustId());
        }
        log.info("加密入参{}", JSONObject.toJSONString(params));
        log.info("商户公钥{}", sourceMerchantInfo.getMerchantKey());
        String sign = PayDigestUtil.getSign(params, sourceMerchantInfo.getMerchantKey());
        log.info("生成签名为{}", sign);
        params.put("sign", sign);
        jsonString = JSON.toJSONString(params);
        resVo.setParams(jsonString);

        resVo.setMwebUrl(payInfoProperties.getMwebUrl());
        return resVo;
    }

    private static Map<String, String> orderSource2Map(String orderSource) {
        Map<String, String> result = Maps.newHashMap();
        if (org.apache.commons.lang.StringUtils.isNotBlank(orderSource)) {
            final String[] sourceArr = orderSource.split("_");
            if (sourceArr != null && sourceArr.length == 2) {
                result.put("channel", sourceArr[0].toUpperCase());
                result.put("type", sourceArr[1].toLowerCase());
            }
        }
        return result;
    }
}


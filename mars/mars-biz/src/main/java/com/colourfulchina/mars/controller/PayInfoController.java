package com.colourfulchina.mars.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.colourfulchina.aggregatePay.api.vo.res.BOSNetpayRes;
import com.colourfulchina.aggregatePay.enums.PayChannelEnum;
import com.colourfulchina.aggregatePay.utils.PayDigestUtil;
import com.colourfulchina.aggregatePay.vo.res.PaymentInfoRes;
import com.colourfulchina.colourfulCoupon.api.enums.CpnVouchersStatusEunm;
import com.colourfulchina.colourfulCoupon.api.enums.ThirdSourceEnum;
import com.colourfulchina.colourfulCoupon.api.vo.req.UpdateCpnReqVo;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.GiftCode;
import com.colourfulchina.mars.api.entity.ReservOrder;
import com.colourfulchina.mars.api.entity.ReservOrderDetail;
import com.colourfulchina.mars.api.entity.SourceMerchantInfo;
import com.colourfulchina.mars.api.enums.DateSourceEnum;
import com.colourfulchina.mars.api.enums.PayOrderStatusEnum;
import com.colourfulchina.mars.api.enums.ReservOrderStatusEnums;
import com.colourfulchina.mars.api.vo.PayInfoVo;
import com.colourfulchina.mars.api.vo.req.AggregatePayParamsReqVo;
import com.colourfulchina.mars.api.vo.req.CouponThirdCodeReqVO;
import com.colourfulchina.mars.api.vo.req.PayReqVO;
import com.colourfulchina.mars.api.vo.res.PayParamsResVo;
import com.colourfulchina.mars.config.PayInfoProperties;
import com.colourfulchina.mars.service.*;
import com.colourfulchina.member.api.req.MemLoginReqDTO;
import com.colourfulchina.member.api.res.MemLoginResDTO;
import com.colourfulchina.pangu.taishang.api.enums.ResourceTypeEnums;
import com.colourfulchina.pangu.taishang.api.feign.RemoteProductGroupService;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.colourfulchina.pangu.taishang.api.vo.req.QueryProductGroupInfoReqVo;
import com.colourfulchina.pangu.taishang.api.vo.res.ProductGroupResVO;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/pay")
@AllArgsConstructor
@Api(value = "预约单支付接口", tags = {"预约单支付接口"})
public class PayInfoController {
    @Autowired
    ReservOrderService reservOrderService;

    @Autowired
    MemberInterfaceService memberInterfaceService;

    @Autowired
    GiftCodeService giftCodeService;

    @Autowired
    ReservOrderDetailService reservOrderDetailService;

    @Autowired
    PanguInterfaceService panguInterfaceService;
    @Autowired
    PayInfoService payInfoService;

    @Autowired
    HttpServletRequest httpRequest;

    @Autowired
    private SourceMerchantInfoService sourceMerchantInfoService;
    @Autowired
    private PayInfoProperties payInfoProperties;

    @Autowired
    private CouponsService couponsService;
    private RemoteProductGroupService remoteProductGroupService;

    @SysGodDoorLog("预约单支付接口")
    @ApiOperation("预约单支付接口")
    @GetMapping("/{id}")
    public CommonResultVo<PaymentInfoRes> pay(@PathVariable(value = "id") Integer id) {
        CommonResultVo<PaymentInfoRes> resultVo = new CommonResultVo<>();
        try {
            Assert.notNull(id, "订单id不能为空！");
            ReservOrder reservOrder = reservOrderService.selectById(id);
            Assert.notNull(reservOrder, "未查到对应的预约单");
            final String orderSource = reservOrder.getOrderSource();
            Assert.hasText(orderSource, "未查到预约单渠道来源");
            final SourceMerchantInfo sourceMerchantInfo = sourceMerchantInfoService.selectById(orderSource);
            Assert.notNull(sourceMerchantInfo, "未查到预约单对应的支付渠道信息");

            GiftCode giftCode = giftCodeService.selectById(reservOrder.getGiftCodeId());
            Assert.notNull(giftCode, "激活码数据不能为空！");
            GoodsBaseVo goods = panguInterfaceService.selectGoodsById(reservOrder.getGoodsId());
            ReservOrderDetail detail = reservOrderDetailService.selectOneReservOrderDetail(reservOrder.getId());
            //TODO
            if (!reservOrder.getPayStatus().equals(PayOrderStatusEnum.UNPAID.getCode())) {
                Assert.notNull(null, "当前订单已支付或取消，请勿重复支付！");
            }

            if (BigDecimal.ZERO.compareTo(detail.getPayAmoney()) < 0) {


                Map params = Maps.newHashMap();
                MemLoginReqDTO memMemberInfo = new MemLoginReqDTO();
                memMemberInfo.setMobile(reservOrder.getGiftPhone());
                memMemberInfo.setAcChannel(goods.getBankCode());
                memMemberInfo.setSmsCode("colour");
                MemLoginResDTO member = memberInterfaceService.memberLogin(memMemberInfo);

//            JSONObject jsonObject = JSONObject.parseObject(params);
                params.put("mbId", member.getMbid());                      // 用户id
                params.put("acId", member.getAcid());
                params.put("accomName", member.getCtName());// 账户id
//            params.put("mbId" ,"1042742232324234"); 			          // 用户id
//            params.put("acId","1171615509575495"); 			          // 账户id
//            params.put("accomName","test"); 	  // 商户ID

                params.put("merchantId", sourceMerchantInfo.getMerchantId());      // 商户ID
//            String openId = params.getString("openId"); 	          // 用户openId
                params.put("paymentType", "预约单");       // 支付单类型(销售单、预约单)
                params.put("goodsName", goods.getName());            // 商品名称
                params.put("body", goods.getName());                      // 商品描述信息
                params.put("orderId", reservOrder.getId());             // 商户订单号
                if (reservOrder.getOrderSource().equalsIgnoreCase(DateSourceEnum.CCB_WECHAT.getcode()) || reservOrder.getOrderSource().equalsIgnoreCase(DateSourceEnum.CCB_APP.getcode())) {
                    params.put("payChannelId", PayChannelEnum.PAY_CHANNEL_CCB_MOBILE.getCode());   // 渠道ID
                } else {
                    params.put("payChannelId", PayChannelEnum.PAY_CHANNEL_SHBANK.getCode());   // 渠道ID
                }

                params.put("amount", detail.getPayAmoney());              // 支付金额（单位分）
//            Integer integral = params.getInteger("integral"); 		  // 积分
                params.put("payMethod", 3);      // 支付方式(1:免费兑换、2:纯积分、3:纯金额,4:积分+金额,5:分期,6:赠送)
//            Integer periods = params.getInteger("periods"); 		  // 分期期数
                params.put("curcode", "CNY");             // 币种
                final Map<String, String> source2Map = orderSource2Map(orderSource);
                if (CollectionUtils.isEmpty(source2Map) && source2Map.containsKey("channel") && source2Map.containsKey("type")) {
                    params.put("mainPage", payInfoProperties.getBookingUrl() + source2Map.get("channel") + "&type=" + source2Map.get("type"));
                } else {
                    log.info("没有找到回调地址:{}", orderSource);
                }
                params.put("source", orderSource);
//            Date payExpireTime = params.getDate("payExpireTime");     // 分期期数
//            String clientIp = params.getString("clientIp");	          // 客户端IP
//            String source = params.getString("source"); 	          // 来源
//            String remark = params.getString("remark"); 		      // 备注
                String sign = PayDigestUtil.getSign(params, sourceMerchantInfo.getMerchantKey());
                params.put("sign", sign);

                String jsonString = JSON.toJSONString(params);// 签名
                log.info("验签数据{}", sourceMerchantInfo);
                log.info("{},{}", reservOrder.getId(), jsonString);
                PaymentInfoRes paymentInfoRes = payInfoService.orderPayment(jsonString);
                resultVo.setResult(paymentInfoRes == null ? null : paymentInfoRes);
            } else {
                reservOrder.setPayStatus(PayOrderStatusEnum.PREPAID.getCode());
                reservOrderService.updateById(reservOrder);
                PaymentInfoRes paymentInfoRes = new PaymentInfoRes();
                BOSNetpayRes res = new BOSNetpayRes();
                res.setMerOrderNum(id + "");
                res.setMerchantRemarks("支付金额为0，则直接支付成功！");
                paymentInfoRes.setBosNetpayRes(res);
                resultVo.setResult(paymentInfoRes);
            }

        } catch (Exception e) {
            log.error("预约单支付失败", e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
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

    @SysGodDoorLog("预约单支付成功接口")
    @ApiOperation("预约单支付成功接口")
    @PostMapping("/payStatus/success")
    public String success(@RequestBody PayInfoVo payInfo) {
        try {
            String salesOrderId = payInfo.getSalesOrderId();
            String sign = payInfo.getSign();
            Assert.notNull(salesOrderId, "订单id不能为空！");
            ReservOrder reservOrder = reservOrderService.selectById(Integer.parseInt(salesOrderId));
            Assert.notNull(reservOrder, "订单不存在！");
            if (reservOrder.getPayStatus().compareTo(PayOrderStatusEnum.PREPAID.getCode()) == 0) {
                return "success";
            }
            final SourceMerchantInfo sourceMerchantInfo = sourceMerchantInfoService.selectById(reservOrder.getOrderSource());
            Assert.notNull(sourceMerchantInfo, "未查到预约单对应的支付渠道信息");

            String amount = payInfo.getAmount();
            String payChannel = payInfo.getPayChannel();
            Map<String, Object> params = Maps.newHashMap();
            params.put("salesOrderId", salesOrderId);
            params.put("amount", amount);
            params.put("payChannel", payChannel);
            String newSign = PayDigestUtil.getSign(params, sourceMerchantInfo.getMerchantKey());
            if (!newSign.equalsIgnoreCase(sign)) {
                Assert.notNull(null, "验证不通过！，数据有误");
            }

            ReservOrderDetail detail = reservOrderDetailService.selectOneReservOrderDetail(reservOrder.getId());
            Assert.notNull(amount, "支付确认后金额不能为空！");
            Assert.notNull(detail.getPayAmoney(), "详细价格数据为空！");
            if (detail.getPayAmoney().compareTo(new BigDecimal(amount)) != 0) {
                Assert.notNull(detail.getPayAmoney().compareTo(new BigDecimal(amount)) != 0, "订单金额与支付金额不匹配！");//TODO
            }
            reservOrder.setPayStatus(PayOrderStatusEnum.PREPAID.getCode());
            if(reservOrder.getServiceType().indexOf("_cpn") != -1){
                //礼券类型的预约单，支付成功后需将预约单状态改为预订成功
                reservOrder.setProseStatus(ReservOrderStatusEnums.ReservOrderStatus.done.getcode());
            }
            detail.setPaymentAmount(new BigDecimal(amount));
            detail.setPayChannel(payChannel);
            //detail.setPayOrderId(StringUtils.isEmpty(payInfo.getPayOrderId()) ? null : Integer.parseInt(payInfo.getPayOrderId()));
            detail.setPaySuccessTime(new Date());
            reservOrderService.updateById(reservOrder);
            reservOrderDetailService.updateById(detail);
            //实物券需要自动发送券码
            if (reservOrder.getServiceType().equals(ResourceTypeEnums.OBJECT_CPN.getCode())){
                QueryProductGroupInfoReqVo queryProductGroupInfoReqVo = new QueryProductGroupInfoReqVo();
                queryProductGroupInfoReqVo.setProductGroupId(reservOrder.getProductGroupId());
                queryProductGroupInfoReqVo.setProductId(reservOrder.getProductId());
                CommonResultVo<List<ProductGroupResVO>> commonResultVo = remoteProductGroupService.selectProductGroupById(queryProductGroupInfoReqVo);
                CouponThirdCodeReqVO reqVO = new CouponThirdCodeReqVO();
                if(null != commonResultVo && !CollectionUtils.isEmpty(commonResultVo.getResult())){
                    ProductGroupResVO productGroupResVO = commonResultVo.getResult().get(0);
                    if(null != productGroupResVO && null != productGroupResVO.getShopChannelId()){
                        reqVO.setSource(ThirdSourceEnum.getCode(productGroupResVO.getShopChannelId()));
                    }
                }
                reqVO.setProductNo(reservOrder.getThirdCpnNum());
                reqVO.setReserveOrderId(reservOrder.getId());
                reqVO.setNum(1);
                couponsService.putThirdCoupons(reqVO);
            }

            //支付成功,如果使用了优惠券,修改状态为已使用
            if(null != reservOrder.getCpnType() && null != reservOrder.getCpnId()) {
                UpdateCpnReqVo updateCpnReqVo = new UpdateCpnReqVo();
                updateCpnReqVo.setCouponsType(reservOrder.getCpnType());
                updateCpnReqVo.setCpnId(reservOrder.getCpnId());
                updateCpnReqVo.setStatus(CpnVouchersStatusEunm.ALREADY_USE.getValue()); //已使用
                final Boolean f = couponsService.updateCoupon(updateCpnReqVo);
                if(!f){
                    log.error("该更新券状态为已使用失败{}",reservOrder.getCpnId());
                    throw new Exception("更新券状态失败");
                }
            }
            return "success";
        } catch (Exception e) {
            log.error("预约单支付成功失败:{}", JSON.toJSONString(payInfo), e);
            return "fail";
        }

    }

    @SysGodDoorLog("预约单支付失败接口")
    @ApiOperation("预约单支付失败接口")
    @GetMapping("/payStatus/fail/{id}")
    public CommonResultVo<ReservOrder> fail(@PathVariable(value = "id") Integer id) {
        CommonResultVo<ReservOrder> resultVo = new CommonResultVo<>();
        try {
            Assert.notNull(id, "订单id不能为空！");
            ReservOrder reservOrder = reservOrderService.selectById(id);
//            ReservOrderDetail detail = reservOrderDetailService.selectOneReservOrderDetail(reservOrder.getId());
            reservOrder.setPayStatus(PayOrderStatusEnum.UNPAID.getCode());
            reservOrderService.updateById(reservOrder);
//            reservOrderDetailService.updateById(detail);
        } catch (Exception e) {
            log.error("预约单支付失败", e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("预约单支付退款成功接口")
    @ApiOperation("预约单支付退款成功接口")
    @GetMapping("/reback/success/{id}")
    public CommonResultVo<ReservOrder> rebackSuccess(@RequestBody PayInfoVo payInfo) {
        CommonResultVo<ReservOrder> resultVo = new CommonResultVo<>();
        try {
            Assert.notNull(payInfo.getSalesOrderId(), "订单id不能为空！");
            ReservOrder reservOrder = reservOrderService.selectById(Integer.parseInt(payInfo.getSalesOrderId()));
            ReservOrderDetail detail = reservOrderDetailService.selectOneReservOrderDetail(reservOrder.getId());
            reservOrder.setPayStatus(PayOrderStatusEnum.REFUND.getCode());
            reservOrderService.updateById(reservOrder);
            detail.setBackAmount(detail.getPayAmoney());
            detail.setBackAmountDate(new Date());
            detail.setBackOrderId(StringUtils.isEmpty(payInfo.getPayOrderId()) ? null : Integer.parseInt(payInfo.getPayOrderId()));
            reservOrderDetailService.updateById(detail);
        } catch (Exception e) {
            log.error("预约单支付退款失败", e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("预约单支付退款失败接口")
    @ApiOperation("预约单支付退款失败接口")
    @GetMapping("/reback/fail/{id}")
    public CommonResultVo<ReservOrder> rebackFail(@PathVariable(value = "id") Integer id) {
        CommonResultVo<ReservOrder> resultVo = new CommonResultVo<>();
        try {
            Assert.notNull(id, "订单id不能为空！");
            ReservOrder reservOrder = reservOrderService.selectById(id);
            reservOrder.setPayStatus(PayOrderStatusEnum.PREPAID.getCode());
            reservOrderService.updateById(reservOrder);
//            reservOrderDetailService.updateById(detail);
        } catch (Exception e) {
            log.error("预约单支付退款失败失败", e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("预约单支付新接口")
    @ApiOperation("预约单支付新接口(新增微信支付(公众号支付和H5支付))")
    @PostMapping("/goPay")
    public CommonResultVo<PaymentInfoRes> goPay(@RequestBody PayReqVO reqVO) {
        CommonResultVo<PaymentInfoRes> resultVo = new CommonResultVo<PaymentInfoRes>();
        try {
            PaymentInfoRes payResVO = payInfoService.reservOrderPay(reqVO);
            if (null != payResVO) {
                resultVo.setResult(payResVO);
                resultVo.setCode(100);
                resultVo.setMsg("成功");
            } else {
                resultVo.setCode(200);
                resultVo.setMsg("支付失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("封装聚合支付入参")
    @ApiOperation("封装聚合支付入参")
    @GetMapping("/getPayParams/{id}")
    public CommonResultVo<PayParamsResVo> getPayParams(@PathVariable(value = "id") String orderId) {
        CommonResultVo<PayParamsResVo> resultVo = new CommonResultVo<PayParamsResVo>();
        try {
            PayParamsResVo resVo = payInfoService.getPayParamsByOrderId(orderId);
            if (!StringUtils.isEmpty(resVo) && !StringUtils.isEmpty(resVo.getParams())) {
                resultVo.setResult(resVo);
                resultVo.setCode(100);
                resultVo.setMsg("成功");
            } else {
                resultVo.setCode(200);
                resultVo.setMsg("失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("封装聚合支付入参(new)")
    @ApiOperation("封装聚合支付入参(new)")
    @PostMapping("/getAggregatePayParams")
    public CommonResultVo<PayParamsResVo> getAggregatePayParams(@RequestBody AggregatePayParamsReqVo reqVo) {
        CommonResultVo<PayParamsResVo> resultVo = new CommonResultVo<PayParamsResVo>();
        try {
            PayParamsResVo resVo = payInfoService.getAggregatePayParams(reqVo);
            if (!StringUtils.isEmpty(resVo) && !StringUtils.isEmpty(resVo.getParams()) && !StringUtils.isEmpty(resVo.getMwebUrl())) {
                resultVo.setResult(resVo);
                resultVo.setCode(100);
                resultVo.setMsg("成功");
            } else {
                resultVo.setCode(200);
                resultVo.setMsg("失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("封装聚合支付入参(new copy)")
    @ApiOperation("封装聚合支付入参(new copy)")
    @RequestMapping("/getAggregatePayParams1")
    public CommonResultVo<PayParamsResVo> getAggregatePayParams1(@RequestBody AggregatePayParamsReqVo reqVo) {
        CommonResultVo<PayParamsResVo> resultVo = new CommonResultVo<PayParamsResVo>();
        try {
            PayParamsResVo resVo = payInfoService.getAggregatePayParams(reqVo);
            if (!StringUtils.isEmpty(resVo) && !StringUtils.isEmpty(resVo.getParams()) && !StringUtils.isEmpty(resVo.getMwebUrl())) {
                resultVo.setResult(resVo);
                resultVo.setCode(100);
                resultVo.setMsg("成功");
            } else {
                resultVo.setCode(200);
                resultVo.setMsg("失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("聚合支付参数入参 map版本")
    @ApiOperation("聚合支付参数入参 map版本")
    @PostMapping("/getAggregateParamsByMap")
   public CommonResultVo<PayParamsResVo> getAggregateParamsByMap(@RequestBody Map<String,Object> param){
        log.info("传入参数为：{}",JSONObject.toJSONString(param));
        AggregatePayParamsReqVo reqVo = JSON.parseObject(JSON.toJSONString(param), AggregatePayParamsReqVo.class);
        log.info("转换后的对象为：{}",JSONObject.toJSONString(reqVo));
       CommonResultVo<PayParamsResVo> resultVo = new CommonResultVo<PayParamsResVo>();
       try {
           PayParamsResVo resVo = payInfoService.getAggregatePayParams(reqVo);
           if (!StringUtils.isEmpty(resVo) && !StringUtils.isEmpty(resVo.getParams()) && !StringUtils.isEmpty(resVo.getMwebUrl())) {
               resultVo.setResult(resVo);
               resultVo.setCode(100);
               resultVo.setMsg("成功");
           } else {
               resultVo.setCode(200);
               resultVo.setMsg("失败");
           }
       } catch (Exception e) {
           log.error(e.getMessage());
           resultVo.setCode(200);
           resultVo.setMsg(e.getMessage());
       }
       return resultVo;

   }
}

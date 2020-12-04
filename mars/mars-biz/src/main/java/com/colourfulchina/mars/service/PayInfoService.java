package com.colourfulchina.mars.service;

import com.colourfulchina.aggregatePay.vo.req.OrderRefundReq;
import com.colourfulchina.aggregatePay.vo.res.PaymentInfoRes;
import com.colourfulchina.mars.api.vo.req.AggregatePayParamsReqVo;
import com.colourfulchina.mars.api.vo.req.PayReqVO;
import com.colourfulchina.mars.api.vo.res.PayParamsResVo;

public interface PayInfoService {

    /**
     * 支付
     * @param params
     * @return
     * @throws Exception
     */
    public PaymentInfoRes orderPayment(String params ) throws Exception;

    public String payOrderRefund( OrderRefundReq orderRefundReq) throws Exception;

    /**
     * 支付(新增微信支付)
     * @param reqVO
     * @return
     */
    PaymentInfoRes reservOrderPay(PayReqVO reqVO) throws Exception;

    /**
     * 封装聚合支付入参
     * @param orderId
     * @return
     */
    PayParamsResVo getPayParamsByOrderId(String orderId) throws Exception;

    PayParamsResVo getAggregatePayParams(AggregatePayParamsReqVo reqVo) throws Exception;
}

package com.colourfulchina.mars.service;

import com.colourfulchina.mars.api.vo.req.BrokerCouponsUsedReq;

public interface BrokerService {

    void batchUploadData(String merchantId);

    Boolean couponUsed(BrokerCouponsUsedReq couponsUsedReq);
//    /**
//     * 生成商品信息文件接口
//     */
//    void getGoodsInfoFile(String goodsUid, String merchantId);
//
//    /**
//     * 生成商户信息文件接口
//     */
//    void getMerchantInfoFile(String merchantId);
//
//    /**
//     * 生成优惠券信息文件接口
//     */
//    void getCouponInfoFile(List<GiftCode> codes, String goodsUid);
//
//    /**
//     *  生成优惠券使用情况信息文件接口
//     */
//    void getCouponUseInfoFile(List<GiftCode> codes, String goodsUid);

}

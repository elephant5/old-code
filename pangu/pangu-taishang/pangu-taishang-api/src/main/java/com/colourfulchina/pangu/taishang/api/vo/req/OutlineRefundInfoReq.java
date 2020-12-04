package com.colourfulchina.pangu.taishang.api.vo.req;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OutlineRefundInfoReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 产品组id
     */
    private Integer productGroupId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 商品id
     */
    private Integer goodsId;

    /**
     * 商品名
     */
    private String goodsName;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 酒店名称
     */
    private String hotelName;

    /**
     * 手机号
     */
    private String phoneReciever;

    /**
     * 收款方
     */
    private String refundReciever;

    /**
     * 激活码
     */
    private String actCode;

    /**
     * 订单时间
     */
    private Date orderTime;

    /**
     * 订单来源
     */
    private String orderSource;

    /**
     * 退款总价
     */
    private Double refundPrice;

    /**
     * 退款状态
     */
    private Boolean refundStatus;

    /**
     * 退款时间
     */
    private Date refundTime;

    /**
     * 退款渠道
     */
    private String refundChannel;

    /**
     * 订单开始时间
     */
    private String orderStartTime;

    /**
     * 订单结束时间
     */
    private String orderEndTime;

    /**
     * 退款起始时间
     */
    private String refundStartTime;

    /**
     * 退款结束时间
     */
    private String refundEndTime;

    private Integer pageNo;
    private Integer pageSize;
}
package com.colourfulchina.pangu.taishang.api.vo.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OutlineRefundInfoRes implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 关联商品或产品名称
     */
    @ApiModelProperty(value = "产品")
    private String productName;

    /**
     * 酒店或商户名称
     */
    @ApiModelProperty(value = "商户")
    private String merchantName;

    /**
     * 收款方
     */
    @ApiModelProperty(value = "收款方")
    private String refundReciever;

    /**
     * 激活码
     */
    @ApiModelProperty(value = "激活码")
    private String actCode;

    /**
     * 订单时间
     */
    @ApiModelProperty(value = "订单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orderTime;

    /**
     * 订单来源
     */
    @ApiModelProperty(value = "订单来源")
    private String orderSource;

    /**
     * 退款总价
     */
    @ApiModelProperty(value = "退款总价")
    @JsonFormat(pattern = "#0.00")
    private Double refundPrice;

    /**
     * 退款状态
     */
    @ApiModelProperty(value = "退款状态")
    private Boolean refundStatus;

    /**
     * 退款时间
     */
    @ApiModelProperty(value = "退款时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date refundTime;

    /**
     * 退款渠道
     */
    @ApiModelProperty(value = "退款渠道")
    private String refundChannel;

}
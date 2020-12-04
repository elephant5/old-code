package com.colourfulchina.mars.api.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class AggregatePayParamsReqVo implements Serializable {

    private static final long serialVersionUID = -5540400760778688148L;
    @ApiModelProperty(value = "用户账户id")
    private Long acId;

    @ApiModelProperty(value = "用户会员id")
    private Long mbId;

    @ApiModelProperty(value = "商户id")
    private Long merchantId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "用户openId")
    private String openId;

    @ApiModelProperty(value = "商品描述信息")
    private String body;

    @ApiModelProperty(value = "订单号")
    private String orderId;

    @ApiModelProperty(value = "支付币种")
    private String curcode;

    @ApiModelProperty(value = "来源")
    private String source;

    @ApiModelProperty(value = "订单类型:(销售单、预约单)")
    private String paymentType;

    @ApiModelProperty(value = "支付金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "支付方式(1:免费兑换、2:纯积分、3:纯金额,4:积分+金额,5:分期,6:赠送)")
    private Integer payMethod;

    @ApiModelProperty(value = "agreementId")
    private String agreementId;

    @ApiModelProperty(value = "客户编号")
    private String custId;
}

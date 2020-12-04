package com.colourfulchina.pangu.taishang.api.vo.res;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商户结算信息查询出参
 */
@Data
@Api("商户结算信息查询出参")
public class ShopSettleMsgRes implements Serializable {
    private static final long serialVersionUID = -8188708521749781135L;
    @ApiModelProperty("预订单ID")
    private Integer orderId;
    @ApiModelProperty("预订单价格ID")
    private Integer priceId;
    @ApiModelProperty("预定时间")
    private Date bookDate;
    @ApiModelProperty("净价")
    private String netPrice;
    @ApiModelProperty("服务费")
    private String serviceRate;
    @ApiModelProperty("税费")
    private String taxRate;
    @ApiModelProperty("结算规则")
    private String settleRule;
    @ApiModelProperty("协议价")
    private BigDecimal protocolPrice;
    @ApiModelProperty("实报价")
    private BigDecimal realPrice;
    @ApiModelProperty("结算方式")
    private String settleMethod;
    @ApiModelProperty("结算币种")
    private String currency;
    @ApiModelProperty("入住多少夜")
    private Integer nightNumbers;//
}

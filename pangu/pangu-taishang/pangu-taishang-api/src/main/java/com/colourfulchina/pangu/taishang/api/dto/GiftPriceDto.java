package com.colourfulchina.pangu.taishang.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class GiftPriceDto implements Serializable {
    private static final long serialVersionUID = -7717474332563368401L;

    @ApiModelProperty("权益类型")
    private String gift;
    @ApiModelProperty("结算价")
    private BigDecimal settlePrice;
}

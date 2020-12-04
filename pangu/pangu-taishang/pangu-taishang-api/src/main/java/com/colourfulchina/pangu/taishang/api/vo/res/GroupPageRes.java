package com.colourfulchina.pangu.taishang.api.vo.res;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 产品组列表分页查询出参
 */
@Data
@Api("产品组列表分页查询出参")
public class GroupPageRes implements Serializable {
    private static final long serialVersionUID = -6107029751543854187L;

    @ApiModelProperty("产品组id")
    private Integer id;
    @ApiModelProperty("产品组内部名称")
    private String shortName;
    @ApiModelProperty("银行名称id")
    private String bankId;
    @ApiModelProperty("销售渠道id")
    private String salesChannelId;
    @ApiModelProperty("销售方式id")
    private String salesWayId;
    @ApiModelProperty("销售渠道")
    private String salesChannel;
    @ApiModelProperty("资源类型")
    private String service;
    @ApiModelProperty("权益类型")
    private String gift;
    @ApiModelProperty("成本最高价")
    private BigDecimal maxCost;
    @ApiModelProperty("成本最低价")
    private BigDecimal minCost;
    @ApiModelProperty("成本平均价")
    private BigDecimal avgCost;
    @ApiModelProperty("商品id")
    private Integer goodsId;
    @ApiModelProperty("商品内部简称")
    private String goodsShortName;
}

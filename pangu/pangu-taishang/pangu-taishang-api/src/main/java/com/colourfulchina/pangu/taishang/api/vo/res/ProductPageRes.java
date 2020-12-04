package com.colourfulchina.pangu.taishang.api.vo.res;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 产品列表分页查询出参
 */
@Data
@Api("产品列表分页查询出参")
public class ProductPageRes implements Serializable {
    private static final long serialVersionUID = -2483188587824596038L;

    @ApiModelProperty("产品id")
    private Integer id;
    @ApiModelProperty("城市")
    private String cityName;
    @ApiModelProperty("国家")
    private String countryName;
    @ApiModelProperty("酒店名称")
    private String hotelName;
    @ApiModelProperty("商户名称")
    private String shopName;
    @ApiModelProperty("资源类型")
    private String service;
    @ApiModelProperty("权益类型")
    private String gift;
    @ApiModelProperty("资源类型code")
    private String serviceCode;
    @ApiModelProperty("资源名称")
    private String productName;
    @ApiModelProperty("住宿房型")
    private String needs;
    @ApiModelProperty("住宿床型")
    private String addon;
    @ApiModelProperty("最小成本")
    private BigDecimal minCost;
    @ApiModelProperty("最大成本")
    private BigDecimal maxCost;
    @ApiModelProperty("状态")
    private String status;
    @ApiModelProperty("渠道类型")
    private String channelType;
    @ApiModelProperty("商户block")
    private String shopBlock;
    @ApiModelProperty("商户规格block")
    private String shopItemBlock;
    @ApiModelProperty("儿童政策")
    private String childrenStr;
    @ApiModelProperty("不可预定时间")
    private String blockRule;
    @ApiModelProperty("商户规格id")
    private Integer shopItemId;
}

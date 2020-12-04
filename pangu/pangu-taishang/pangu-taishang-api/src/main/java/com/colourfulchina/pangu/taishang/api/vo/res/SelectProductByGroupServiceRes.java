package com.colourfulchina.pangu.taishang.api.vo.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SelectProductByGroupServiceRes implements Serializable {
    private static final long serialVersionUID = 4510769185194140811L;

    @ApiModelProperty("产品组产品id")
    private Integer productGroupProductId;
    @ApiModelProperty("产品组id")
    private Integer productGroupId;
    @ApiModelProperty("产品id")
    private Integer productId;
    @ApiModelProperty("商户规格id")
    private Integer shopItemId;
    @ApiModelProperty("商户id")
    private Integer shopId;
    @ApiModelProperty("酒店id")
    private Integer hotelId;
    @ApiModelProperty("城市id")
    private Integer cityId;
    @ApiModelProperty("城市名称")
    private String cityName;
    @ApiModelProperty("国家id")
    private String countryId;
    @ApiModelProperty("国家名称")
    private String countryName;
    @ApiModelProperty("资源类型")
    private String service;
    @ApiModelProperty("酒店名称")
    private String hotelName;
    @ApiModelProperty("商户名称")
    private String shopName;
    @ApiModelProperty("酒店地址")
    private String hotelAddress;
    @ApiModelProperty("商户地址")
    private String shopAddress;
    @ApiModelProperty("商户规格名称")
    private String shopItemName;
    @ApiModelProperty("有早无早")
    private String addon;
    @ApiModelProperty("住宿床型")
    private String needs;
    @ApiModelProperty("产品组产品权益类型")
    private String gift;
    @ApiModelProperty("产品组产品block")
    private String productBlock;
    @ApiModelProperty("产品组block")
    private String groupBlock;
    @ApiModelProperty("商户规格block")
    private String shopItemBlock;
    @ApiModelProperty("商品block")
    private String goodsBlock;
    @ApiModelProperty("商户block")
    private String shopBlock;
    @ApiModelProperty("所有block集合，中文翻译")
    private String blockRule;
    @ApiModelProperty("预约支付价格")
    private BigDecimal bookPrice;
}

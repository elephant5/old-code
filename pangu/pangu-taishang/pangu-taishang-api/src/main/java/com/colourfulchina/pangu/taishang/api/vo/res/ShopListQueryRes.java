package com.colourfulchina.pangu.taishang.api.vo.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ShopListQueryRes implements Serializable {
    private static final long serialVersionUID = -1281142572336436888L;

    @ApiModelProperty(value = "产品ID")
    private Integer productGroupProductId;

    @ApiModelProperty(value = "商户ID")
    private Integer shopId;

    @ApiModelProperty(value = "酒店名称")
    private String hotelName;

    @ApiModelProperty(value = "商户/商户资源名称")
    private String shopName;

    @ApiModelProperty(value = "产品标题")
    private String productName;

    @ApiModelProperty(value = "产品类型")
    private String service;

    @ApiModelProperty(value = "城市")
    private String cityName;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "商户图片地址")
    private String url;

    @ApiModelProperty(value = "权益类型")
    private String gift;

    @ApiModelProperty(value = "权益名称")
    private String giftName;

    @ApiModelProperty(value = "展示名称")
    private String showName;

    @ApiModelProperty(value = "xml")
    private String shopItemId;
}

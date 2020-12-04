package com.colourfulchina.pangu.taishang.api.vo.res.shop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShopListQueryResVo {

    @ApiModelProperty(value = "产品ID")
    private Integer goodsId;

    @ApiModelProperty(value = "商户ID")
    private Integer shopId;

    @ApiModelProperty(value = "酒店名称")
    private String hotel;

    @ApiModelProperty(value = "商户/商户资源名称")
    private String name;

    @ApiModelProperty(value = "产品标题")
    private String title;

    @ApiModelProperty(value = "产品类型")
    private String type;

    @ApiModelProperty(value = "城市")
    private String city;

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
    private String items;

}

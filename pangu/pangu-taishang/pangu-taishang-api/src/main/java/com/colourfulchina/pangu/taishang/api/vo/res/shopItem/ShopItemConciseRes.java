package com.colourfulchina.pangu.taishang.api.vo.res.shopItem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ShopItemConciseRes implements Serializable {
    private static final long serialVersionUID = 2343358024294549960L;

    @ApiModelProperty("商户规格id")
    private Integer id;
    @ApiModelProperty("商户规格名称")
    private String shopItemName;
    @ApiModelProperty("酒店名称")
    private String hotelName;
    @ApiModelProperty("商户名臣")
    private String shopName;
    @ApiModelProperty("城市名称")
    private String cityName;
    @ApiModelProperty("床型")
    private String needs;
    @ApiModelProperty("餐型")
    private String addon;
}

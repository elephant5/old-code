package com.colourfulchina.pangu.taishang.api.vo.res;

import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.vo.ShopVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 产品组下面产品的列表出参
 */
@Data
@Api("产品组下面产品的列表出参")
public class SelectBookProductRes implements Serializable {
    private static final long serialVersionUID = -2360775099424079282L;

    @ApiModelProperty("商户规格")
    private ShopItem shopItem;
    @ApiModelProperty("商户规格图片")
    private List<SysFileDto> shopItemPics;
    @ApiModelProperty("商户规格价格")
    private List<ShopItemNetPriceRule> priceRuleList;
    @ApiModelProperty("产品组信息")
    private ProductGroup productGroup;
    @ApiModelProperty("产品组下面的产品信息")
    private ProductGroupProduct productGroupProduct;
    @ApiModelProperty("商户信息")
    private ShopVo shop;
    @ApiModelProperty("酒店信息")
    private Hotel hotel;
    @ApiModelProperty("商户图片")
    private List<SysFileDto> shopPics;
    @ApiModelProperty("是否可以预约标识")
    private Boolean orderFlag;
    @ApiModelProperty("城市")
    private String cityName;
}

package com.colourfulchina.pangu.taishang.api.vo.res;

import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.vo.ShopBaseMsgVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 产品组下面产品的详情出参
 */
@Data
@Api("产品组下面产品的详情出参")
public class GroupProductDetailRes implements Serializable {
    private static final long serialVersionUID = -2413864934928372238L;

    @ApiModelProperty("商户规格")
    private ShopItem shopItem;
    @ApiModelProperty("商户规格图片")
    private List<SysFileDto> shopItemPics;
    @ApiModelProperty("商户规格价格")
    private List<ShopItemNetPriceRule> priceRuleList;
    @ApiModelProperty("产品组信息")
    private ProductGroup productGroup;
    @ApiModelProperty("使用限制")
    private GoodsSetting goodsSetting;
    @ApiModelProperty("适用规则")
    private List<GoodsClause> goodsClauseList;
    @ApiModelProperty("产品组下面的产品信息")
    private ProductGroupProduct productGroupProduct;
    @ApiModelProperty("商户")
    private ShopBaseMsgVo shop;
    @ApiModelProperty("酒店")
    private Hotel hotel;
    @ApiModelProperty("商户协议")
    private ShopProtocol shopProtocol;
    @ApiModelProperty("商户图片")
    private List<SysFileDto> shopPics;
}

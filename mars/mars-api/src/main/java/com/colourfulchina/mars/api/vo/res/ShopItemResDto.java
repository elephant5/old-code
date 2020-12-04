package com.colourfulchina.mars.api.vo.res;

import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroupProduct;
import com.colourfulchina.pangu.taishang.api.entity.ShopItem;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemNetPriceRule;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ShopItemResDto {

    private ShopItem shopItem;

    private List<ShopItemNetPriceRule> priceRules;

    private ProductGroupProduct productGroupProduct;

    private List<SysFileDto> shopItemPics;

    private BigDecimal discountRate;

    private BigDecimal shopPriceRule;

    private BigDecimal productPrice;
}

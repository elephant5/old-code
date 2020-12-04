package com.colourfulchina.pangu.taishang.api.vo.res;

import com.colourfulchina.pangu.taishang.api.entity.ShopItemSettlePrice;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ShopItemSettlePriceHisRes extends ShopItemSettlePrice {
    private BigDecimal minPirce;
    private BigDecimal maxPrice;
    private BigDecimal avgPrice;
}

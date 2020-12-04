package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.pangu.taishang.api.entity.Product;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemSettlePriceRule;

public interface ShopItemSettlePriceRuleMapper extends BaseMapper<ShopItemSettlePriceRule> {


    void updateShopItemSettlePriceRule(Product params);
}
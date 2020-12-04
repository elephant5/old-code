package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemNetPriceRule;

import java.math.BigDecimal;

public interface ShopItemNetPriceRuleMapper extends BaseMapper<ShopItemNetPriceRule> {

    BigDecimal selectMinPrice(Long itemId);
}
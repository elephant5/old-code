package com.colourfulchina.pangu.taishang.api.vo.res;

import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import com.colourfulchina.pangu.taishang.api.entity.ShopItem;
import lombok.Data;

import java.util.List;

@Data
public class ShopItemRes extends ShopItem {
    private static final long serialVersionUID = -391247613073756738L;
    private List<BlockRule> blockRuleList;
}

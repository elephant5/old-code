package com.colourfulchina.pangu.taishang.api.vo.req;

import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import com.colourfulchina.pangu.taishang.api.entity.ShopItem;
import lombok.Data;

import java.util.List;

@Data
public class ShopItemReq extends ShopItem {
    private static final long serialVersionUID = 8810845695752476254L;
    private List<BlockRule> blockRuleList;

}

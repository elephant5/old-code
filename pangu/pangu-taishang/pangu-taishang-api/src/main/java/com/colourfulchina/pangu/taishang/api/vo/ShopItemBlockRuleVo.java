package com.colourfulchina.pangu.taishang.api.vo;

import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 商户规格block规则操作入参
 */
@Data
@Api("商户规格block规则操作入参")
public class ShopItemBlockRuleVo extends GenerateBlockRuleVo {
    private static final long serialVersionUID = -8449628664039681101L;

    @ApiModelProperty("商户规格id")
    private Integer shopItemId;
    @ApiModelProperty("特殊block")
    private List<BlockRule> specialBlocks;
}

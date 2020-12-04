package com.colourfulchina.pangu.taishang.api.vo;

import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 产品组block规则操作入参
 */
@Data
@Api("产品组block规则操作入参")
public class ProductGroupBlockRuleVo extends GenerateBlockRuleVo {
    private static final long serialVersionUID = 2131256100773930710L;

    @ApiModelProperty("产品组id")
    private Integer productGroupId;
    @ApiModelProperty("特殊block")
    private List<BlockRule> specialBlocks;
}

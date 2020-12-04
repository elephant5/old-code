package com.colourfulchina.pangu.taishang.api.vo.req;

import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ShopItemBlockDelReq implements Serializable {
    private static final long serialVersionUID = 2136000506841192972L;

    @ApiModelProperty("规格id")
    private Integer shopItemId;
    @ApiModelProperty("block对象")
    private List<BlockRule> blockRuleList;
}

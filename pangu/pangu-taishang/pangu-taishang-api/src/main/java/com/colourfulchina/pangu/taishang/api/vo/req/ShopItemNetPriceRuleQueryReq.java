package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 规格价格查询入参
 */
@Data
@Api("规格价格查询入参")
public class ShopItemNetPriceRuleQueryReq implements Serializable {
    private static final long serialVersionUID = -8232424871885612982L;

    @ApiModelProperty("商户id")
    private Integer shopId;
    @ApiModelProperty("商户规格id")
    private Integer shopItemId;
}

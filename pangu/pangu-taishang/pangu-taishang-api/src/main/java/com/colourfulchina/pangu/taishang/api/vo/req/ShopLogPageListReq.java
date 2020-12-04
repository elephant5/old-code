package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 商户操作记录分页列表入参
 */
@Data
@Api("商户操作记录分页列表入参")
public class ShopLogPageListReq implements Serializable {
    private static final long serialVersionUID = -3666569919309960619L;

    @ApiModelProperty("商户id")
    private Integer shopId;
}

package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询预约时选择资源列表入参
 */
@Data
@Api("查询预约时选择资源列表入参")
public class SelectBookShopItemReq implements Serializable {
    private static final long serialVersionUID = -7057249729572816744L;

    @ApiModelProperty("产品组id")
    private Integer productGroupId;
    @ApiModelProperty("资源类型")
    private String service;
    @ApiModelProperty("商户id")
    private Integer shopId;
}

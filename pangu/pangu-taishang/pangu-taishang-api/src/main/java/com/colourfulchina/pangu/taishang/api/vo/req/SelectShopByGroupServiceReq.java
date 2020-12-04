package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 根据产品组和资源类型查询商户入参
 */
@Data
@Api("根据产品组和资源类型查询商户入参")
public class SelectShopByGroupServiceReq implements Serializable {
    private static final long serialVersionUID = 3009130732296344744L;

    @ApiModelProperty("产品组id")
    private Integer productGroupId;
    @ApiModelProperty("资源类型code")
    private String service;
}

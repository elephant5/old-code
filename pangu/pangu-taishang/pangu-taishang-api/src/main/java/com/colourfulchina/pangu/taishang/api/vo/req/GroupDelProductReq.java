package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 产品组移除产品入参
 */
@Data
@Api("产品组移除产品入参")
public class GroupDelProductReq implements Serializable {
    private static final long serialVersionUID = -183828648529007076L;

    @ApiModelProperty("移除的目标产品组id")
    private Integer productGroupId;
    @ApiModelProperty("移除的产品列表id")
    private List<Integer> productGroupProductIds;
}

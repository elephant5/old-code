package com.colourfulchina.pangu.taishang.api.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 打包产品及成本格式
 */
@Data
@Api("打包产品及成本格式")
public class PackProductVo implements Serializable {
    private static final long serialVersionUID = 4872326600751850522L;

    @ApiModelProperty("产品id")
    private Integer productId;
    @ApiModelProperty("产品子项ids")
    private List<Integer> productItemIds;
}

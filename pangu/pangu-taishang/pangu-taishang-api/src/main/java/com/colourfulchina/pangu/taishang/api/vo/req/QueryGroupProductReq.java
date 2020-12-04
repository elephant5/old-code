package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 产品组的所属产品条件查询入参
 */
@Data
@Api("产品组的所属产品条件查询入参")
public class QueryGroupProductReq implements Serializable {
    private static final long serialVersionUID = -4717103245355801356L;

    @ApiModelProperty("商户id或者商户名")
    private String shopIdOrShopName;
    @ApiModelProperty("城市")
    private String city;
    @ApiModelProperty("产品组id")
    private Integer groupId;
    /**
     * 状态（0在售，1停售，2申请停售）
     */
    @ApiModelProperty("停售状态")
    private Integer status;
}

package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 产品组添加产品入参
 */
@Data
@Api("产品组添加产品入参")
public class GroupAddProductAndBlockReq implements Serializable {
    private static final long serialVersionUID = 7503879390594135652L;

    @ApiModelProperty("添加的目标产品组id")
    private Integer productGroupId;
    @ApiModelProperty("添加的的源产品列表id-block")
    private Map<Integer,String> productBlockMap;
    @ApiModelProperty("添加的的源产品列表id-point")
    private Map<Integer, BigDecimal> productPointMap;
}

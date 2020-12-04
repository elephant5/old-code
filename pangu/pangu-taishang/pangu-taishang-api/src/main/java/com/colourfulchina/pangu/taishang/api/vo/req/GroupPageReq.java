package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.*;
import java.io.Serializable;

/**
 * 产品组列表分页查询入参
 */
@Data
@Api("产品组列表分页查询入参")
public class GroupPageReq implements Serializable {
    private static final long serialVersionUID = 7170308519834857413L;

    @ApiModelProperty("产品组id或者名称或者内部简称")
    private String idOrName;
    @ApiModelProperty("销售渠道")
    private List<String> salesChannel;
    @ApiModelProperty("资源类型")
    private String service;
    @ApiModelProperty("权益类型")
    private String gift;
}

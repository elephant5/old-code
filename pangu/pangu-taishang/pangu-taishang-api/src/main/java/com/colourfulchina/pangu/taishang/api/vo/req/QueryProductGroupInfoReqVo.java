package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class QueryProductGroupInfoReqVo implements Serializable{

    private static final long serialVersionUID = -6499769192917334315L;

    @ApiModelProperty(value = "产品组id")
    private Integer productGroupId;

    @ApiModelProperty(value = "产品id")
    private Integer productId;
}

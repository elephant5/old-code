package com.colourfulchina.mars.api.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryGoodsDetailReqVo {

    @ApiModelProperty(value = "激活码")
    private String actCode;
}

package com.colourfulchina.mars.api.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryGiftReqVO {

    @ApiModelProperty(value = "渠道")
    private String acChannel;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "账户id")
    private Long acId;

    @ApiModelProperty(value = "商品id")
    private Integer goodsId;
}

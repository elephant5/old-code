package com.colourfulchina.mars.api.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryBoscCardReqVo {

    @ApiModelProperty(value = "客编号")
    private String ecifNo;

    @ApiModelProperty(value = "来源")
    private String source;

    @ApiModelProperty(value = "激活人id")
    private String memberId;

    private Integer flag;
}

package com.colourfulchina.mars.api.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CancelCodeReq {

    @ApiModelProperty("gift_code表的id")
    private Integer giftCodeId;
}

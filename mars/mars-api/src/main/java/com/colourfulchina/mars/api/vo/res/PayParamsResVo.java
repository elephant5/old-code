package com.colourfulchina.mars.api.vo.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PayParamsResVo {

    @ApiModelProperty(value = "支付参数")
    private String params;

    @ApiModelProperty(value = "支付跳转地址")
    private String mwebUrl;
}

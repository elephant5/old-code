package com.colourfulchina.mars.api.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PayReqVO {

    @ApiModelProperty(value = "预约单id")
    private String orderId;

    @ApiModelProperty(value = "支付类型")
    private String payMethod;

}

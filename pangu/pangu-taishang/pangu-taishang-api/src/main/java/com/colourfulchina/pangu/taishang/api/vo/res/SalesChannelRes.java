package com.colourfulchina.pangu.taishang.api.vo.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SalesChannelRes {

    @ApiModelProperty(value = "渠道编号")
    private Integer id ;

    @ApiModelProperty(value = "隶属大客户")
    private String salesBankName;

    @ApiModelProperty(value = "销售渠道")
    private String channel;

    @ApiModelProperty(value = "销售方式")
    private String mode;

}

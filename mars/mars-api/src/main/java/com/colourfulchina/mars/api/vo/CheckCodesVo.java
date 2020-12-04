package com.colourfulchina.mars.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CheckCodesVo implements Serializable {
    private static final long serialVersionUID = -2391101094534776414L;

    @ApiModelProperty("激活码")
    private String actCode;
    @ApiModelProperty("激活码状态")
    private Integer actCodeStatus;
    @ApiModelProperty("激活码状态名称")
    private String actCodeName;
    @ApiModelProperty("商品id")
    private Integer goodsId;
    @ApiModelProperty("商品内部简称")
    private String goodsShortName;
}

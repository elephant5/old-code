package com.colourfulchina.pangu.taishang.api.vo.res.shopItem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShopItemResVo {

    @ApiModelProperty(value = "商户资源名称")
    private String name;

    @ApiModelProperty(value = "净价")
    private String net;

    @ApiModelProperty(value = "服务费")
    private String fee;

    @ApiModelProperty(value = "增值税")
    private String vat;

}

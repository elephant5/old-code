package com.colourfulchina.pangu.taishang.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * ShopItemSettleExpress翻译成中文公式Vo
 */
@Data
public class SettleExpressTranslateVo implements Serializable {
    private static final long serialVersionUID = 6057983854558955913L;

    @ApiModelProperty("权益类型")
    private String gift;
    @ApiModelProperty("结算公式内容")
    private SettleExpressBriefVo expressVo;
}

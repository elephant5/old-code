package com.colourfulchina.mars.api.vo.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 激活码生成出参
 */
@Slf4j
@Data
public class GenerateActCodeRes implements Serializable {
    private static final long serialVersionUID = -1267716364005884682L;

    @ApiModelProperty("激活码批次号")
    private String codeBatchNo;
    @ApiModelProperty("商品id")
    private Integer goodsId;
}

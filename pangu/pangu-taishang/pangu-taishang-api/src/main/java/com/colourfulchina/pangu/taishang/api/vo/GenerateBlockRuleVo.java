package com.colourfulchina.pangu.taishang.api.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * block规则组装入参
 */
@Data
@Api("block规则组装入参")
public class GenerateBlockRuleVo implements Serializable {
    private static final long serialVersionUID = -321213543851470775L;

    @ApiModelProperty("时间性质（0-阳历 1-阴历）")
    private Integer calendar;
    @ApiModelProperty("block起止日期")
    private Date[] blockTime;
    @ApiModelProperty("block包含星期")
    private String[] containWeek;
    @ApiModelProperty("每年重复（0-不重复 1-重复）")
    private Integer repeat;
    @ApiModelProperty("block原因")
    private String reason;
}

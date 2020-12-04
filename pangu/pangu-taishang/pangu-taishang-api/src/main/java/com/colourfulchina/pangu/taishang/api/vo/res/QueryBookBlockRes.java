package com.colourfulchina.pangu.taishang.api.vo.res;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询预定block出参
 */
@Data
@Api("查询预定block出参")
public class QueryBookBlockRes implements Serializable {
    private static final long serialVersionUID = 3721362186068088550L;

    @ApiModelProperty("不可预定时间")
    private String blockRule;
    @ApiModelProperty("可预定时间选择")
    private List<Date> bookDates;
}

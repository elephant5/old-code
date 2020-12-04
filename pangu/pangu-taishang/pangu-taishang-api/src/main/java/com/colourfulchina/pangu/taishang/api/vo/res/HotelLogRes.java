package com.colourfulchina.pangu.taishang.api.vo.res;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Api("酒店日志记录出参")
@Data
public class HotelLogRes implements Serializable {
    private static final long serialVersionUID = -43456978815017783L;

    @ApiModelProperty("酒店日志id")
    private Integer id;
    @ApiModelProperty("操作的类型")
    private String optName;
    @ApiModelProperty("操作前值")
    private String beforeValue;
    @ApiModelProperty("操作后值")
    private String afterValue;
    @ApiModelProperty("操作人")
    private String createUser;
    @ApiModelProperty("操作时间")
    private Date createTime;
}

package com.colourfulchina.pangu.taishang.api.vo.res;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Api("商户操作日志记录出参")
@Data
public class ShopLogPageListRes implements Serializable {
    private static final long serialVersionUID = -8160970713023173126L;

    @ApiModelProperty("商户日志id")
    private Integer id;
    @ApiModelProperty("操作范围")
    private String optRange;
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

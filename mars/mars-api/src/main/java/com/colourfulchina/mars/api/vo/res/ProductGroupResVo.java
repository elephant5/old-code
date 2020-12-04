package com.colourfulchina.mars.api.vo.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductGroupResVo implements Serializable{

    private static final long serialVersionUID = -6469585786696554795L;

    @ApiModelProperty(value = "产品组id")
    private Integer productGroupId;

    @ApiModelProperty(value = "产品组名称")
    private String name;

    @ApiModelProperty(value = "产品组使用总次数")
    private BigDecimal useNum;

    @ApiModelProperty(value = "产品组剩余可使用总次数")
    private BigDecimal leftNum;

    @ApiModelProperty(value = "产品组资源类型")
    private String service;

    @ApiModelProperty(value = "综合最小提前预约天数")
    private Integer minBookDay;

    @ApiModelProperty(value = "综合最大提前预约天数")
    private Integer maxBookDay;
}

package com.colourfulchina.pangu.taishang.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 城市
 */
@Data
public class CityVo implements Serializable {
    private static final long serialVersionUID = 8482820167523066653L;
    @ApiModelProperty("城市id")
    private String cityId;
    @ApiModelProperty("城市名称")
    private String cityName;
}

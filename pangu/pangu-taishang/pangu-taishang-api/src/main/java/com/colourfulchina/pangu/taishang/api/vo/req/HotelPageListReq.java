package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 酒店分页列表入参
 */
@Data
@Api("酒店分页列表入参")
public class HotelPageListReq implements Serializable {
    private static final long serialVersionUID = -350777987808784656L;

    @ApiModelProperty("酒店名称")
    private String hotelName;
    @ApiModelProperty("城市名称")
    private String cityName;
}

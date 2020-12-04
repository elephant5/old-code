package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 修改酒店名称入参
 */
@Data
@Api("修改酒店名称入参")
public class UpdHotelNameReq implements Serializable {
    private static final long serialVersionUID = 4706192114606040190L;

    @ApiModelProperty("酒店id")
    private Integer id;
    @ApiModelProperty("酒店中文名")
    private String hotelNameCh;
    @ApiModelProperty("酒店英文名")
    private String hotelNameEn;
}

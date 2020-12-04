package com.colourfulchina.pangu.taishang.api.vo.res;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Api("酒店列表模糊分页查询出参")
@Data
public class HotelPageListRes implements Serializable {
    private static final long serialVersionUID = 6227766188941942502L;

    @ApiModelProperty("酒店id")
    private Integer id;
    @ApiModelProperty("酒店中文名")
    private String nameCh;
    @ApiModelProperty("酒店英文名")
    private String nameEn;
    @ApiModelProperty("酒店城市名称")
    private String cityNameCh;
}

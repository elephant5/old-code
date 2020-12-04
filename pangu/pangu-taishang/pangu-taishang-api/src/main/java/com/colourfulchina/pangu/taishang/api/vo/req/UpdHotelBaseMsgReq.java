package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 修改酒店基础信息入参
 */
@Data
@Api("修改酒店基础信息入参")
public class UpdHotelBaseMsgReq implements Serializable {
    private static final long serialVersionUID = -7777457298399790089L;

    @ApiModelProperty("酒店id")
    private Integer id;
    @ApiModelProperty("酒店中文名")
    private String hotelNameCh;
    @ApiModelProperty("酒店英文名")
    private String hotelNameEn;
    @ApiModelProperty("挂星")
    private Integer star;
    @ApiModelProperty("城市id")
    private Integer cityId;
    @ApiModelProperty("中文地址")
    private String addressCh;
    @ApiModelProperty("英文地址")
    private String addressEn;
    @ApiModelProperty("酒店坐标经纬度")
    private String coordinate;
     @ApiModelProperty(" 账户类型(0商户1酒店2集团） 默认为商户")
    private Integer accountType ;
     @ApiModelProperty(" 用户名")
    private String username;
     @ApiModelProperty("密码")
    private String password;
}

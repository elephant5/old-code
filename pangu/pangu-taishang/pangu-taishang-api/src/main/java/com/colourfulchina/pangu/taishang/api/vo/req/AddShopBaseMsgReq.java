package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 新增商户基础信息入参
 */
@Data
@Api("新增商户基础信息入参")
public class AddShopBaseMsgReq implements Serializable {
    private static final long serialVersionUID = -5078108793960308905L;

    @ApiModelProperty("商户id")
    private Integer id;
    @ApiModelProperty("商户中文名")
    private String shopNameCh;
    @ApiModelProperty("商户英文名")
    private String shopNameEn;
    @ApiModelProperty("国家id")
    private String countryId;
    @ApiModelProperty("城市id")
    private Integer cityId;
    @ApiModelProperty("商户中文地址")
    private String addressCh;
    @ApiModelProperty("商户英文地址")
    private String addressEn;
    @ApiModelProperty("商户联系电话")
    private String phone;
    @ApiModelProperty("商户营业开始时间")
    private String openTime;
    @ApiModelProperty("商户营业结束时间")
    private String closeTime;
    @ApiModelProperty("商户入住时间（仅限于住宿商户）")
    private String checkInTime;
    @ApiModelProperty("商户退房时间（仅限于住宿商户）")
    private String checkOutTime;
    @ApiModelProperty("商户录单提示")
    private String tips;
    @ApiModelProperty("商户备注")
    private String notes;
    @ApiModelProperty("商户级别")
    private String level;
    @ApiModelProperty("商户性质（0附属商户，1独立商户）")
    private Integer shopNature;
    @ApiModelProperty("商户介绍")
    private String summary;
    @ApiModelProperty("最多提前N预定")
    private Integer  maxBookDays;
    @ApiModelProperty("最少提前N预定")
    private Integer  minBookDays ;
    @ApiModelProperty("商户详情")
    private String detail;
    @ApiModelProperty("商户坐标经纬度")
    private String coordinate;
}

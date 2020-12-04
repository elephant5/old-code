package com.colourfulchina.pangu.taishang.api.vo.req;

import com.baomidou.mybatisplus.annotations.TableField;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 新增商户第一步入参
 */
@Data
@Api("新增商户第一步入参")
public class AddShopOneReq implements Serializable {
    private static final long serialVersionUID = 1295045548052045295L;

    @ApiModelProperty("商户类型code")
    private String shopTypeId;
    @ApiModelProperty("所属酒店名称")
    private String hotelNameCh;
    @ApiModelProperty("商户名称")
    private String shopName;
    @ApiModelProperty("商户资源类型id")
    private Integer shopChannelId;
    @ApiModelProperty("结算币种")
    private String settleCurrency;
    @ApiModelProperty("结算方式")
    private String settleMethod;
    @ApiModelProperty("国家id")
    private String countryId;
    @ApiModelProperty("城市id")
    private Integer cityId;
    @ApiModelProperty("详细地址")
    private String address;
    @ApiModelProperty("备注")
    private String notes;
    @ApiModelProperty("商户性质(0附属商户，1独立商户)")
    private Integer shopNature;
    @ApiModelProperty("商户详情")
    private String detail;

    @ApiModelProperty("商户坐标经纬度")
    private String coordinate;
}

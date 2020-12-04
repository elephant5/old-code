package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 商户分页列表入参
 */
@Data
@Api("商户分页列表入参")
public class ShopPageListReq implements Serializable {
    private static final long serialVersionUID = 4710495967751565570L;

    @ApiModelProperty("商户名称或者酒店名称")
    private String shopNameOrHotelName;
    @ApiModelProperty("资源类型name（服务类型）")
    private String serviceName;
    @ApiModelProperty("商户标签id")
    private Integer shopTagId;
    @ApiModelProperty("商户资源渠道id")
    private Integer shopChannelId;
    @ApiModelProperty("商户状态id")
    private Integer shopStatusId;
    @ApiModelProperty("商户所在城市名称")
    private String cityName;
}

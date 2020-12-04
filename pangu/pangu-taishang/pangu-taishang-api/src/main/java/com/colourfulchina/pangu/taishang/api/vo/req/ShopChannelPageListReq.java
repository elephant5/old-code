package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 商户渠道分页列表入参
 */
@Data
@Api("商户渠道分页列表入参")
public class ShopChannelPageListReq implements Serializable {
    private static final long serialVersionUID = -4547059252774148702L;

    @ApiModelProperty("商戶渠道名稱")
    private String channelName;
    @ApiModelProperty("商戶渠道類型")
    private Integer channelInternal;
    @ApiModelProperty("商戶渠道狀態")
    private Integer channelDelFlag;
}

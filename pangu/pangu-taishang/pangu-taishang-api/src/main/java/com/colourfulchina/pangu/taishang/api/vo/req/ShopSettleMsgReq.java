package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商户结算信息查询入参
 */
@Data
@Api("商户结算信息查询入参")
public class ShopSettleMsgReq implements Serializable {
    private static final long serialVersionUID = 5297499049188290296L;

    @ApiModelProperty("商户id")
    private Integer shopId;
    @ApiModelProperty("商户规格id")
    private Integer shopItemId;
    @ApiModelProperty("权益类型")
    private String gift;
    @ApiModelProperty("预定渠道id")
    private Integer shopChannelId;
    @ApiModelProperty("预定时间")
    private Date bookDate;
}

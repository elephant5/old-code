package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询预定block入参
 */
@Data
@Api("查询预定block入参")
public class QueryBookBlockReq implements Serializable {
    private static final long serialVersionUID = 442570233557454061L;

    @ApiModelProperty("商品id")
    private Integer goodsId;
    @ApiModelProperty("产品组id")
    private Integer productGroupId;
    @ApiModelProperty("商户id")
    private Integer shopId;
    @ApiModelProperty("资源选择id")
    private Integer productGroupProductId;
    @ApiModelProperty("出库日期")
    private Date outDate;
    @ApiModelProperty("激活日期")
    private Date activationDate;
    @ApiModelProperty("激活码到期时间")
    private Date actExpireTime;
}

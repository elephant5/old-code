package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询购买的权益到期时间入参
 */
@Data
@Api("查询购买的权益到期时间入参")
public class QueryOrderExpireTimeReq implements Serializable {
    private static final long serialVersionUID = -3285976863582331551L;

    @ApiModelProperty("商品id")
    private Integer goodsId;
    @ApiModelProperty("出库日期")
    private Date outDate;
    @ApiModelProperty("激活日期")
    private Date activationDate;
}

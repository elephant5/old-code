package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 来电录单初步查询入参
 */
@Data
@Api("来电录单初步查询入参")
public class QueryCallBookReq implements Serializable {
    private static final long serialVersionUID = 8815453619922607054L;

    @ApiModelProperty("商品id")
    private Integer goodsId;
    @ApiModelProperty("产品组id")
    private Integer productGroupId;
    @ApiModelProperty("预约时间")
    private Date bookDate;
    @ApiModelProperty("出库日期")
    private Date outDate;
    @ApiModelProperty("激活日期")
    private Date activationDate;
    @ApiModelProperty("激活码到期时间")
    private Date actExpireTime;
}

package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询预约时产品列表入参
 */
@Data
@Api("查询预约时产品列表入参")
public class SelectBookProductReq implements Serializable {
    private static final long serialVersionUID = 8603807114845726356L;

    @ApiModelProperty("产品组id")
    private Integer productGroupId;
    @ApiModelProperty("资源类型")
    private String serviceType;
    @ApiModelProperty("权益失效时间")
    private Date actExpireTime;
    @ApiModelProperty("查询开始时间")
    private String beginDate;
    @ApiModelProperty("查询结束时间")
    private String endDate;
}

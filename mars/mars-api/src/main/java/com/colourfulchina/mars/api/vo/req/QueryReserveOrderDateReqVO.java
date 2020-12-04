package com.colourfulchina.mars.api.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryReserveOrderDateReqVO {

    @ApiModelProperty(value = "激活码id")
    private Long giftCodeId;

    @ApiModelProperty(value = "查询开始时间")
    private String startDate;

    @ApiModelProperty(value = "查询截止时间")
    private String endDate;
}

package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class SelectBookPayReq implements Serializable {
    private static final long serialVersionUID = 5212161557693922664L;

    @ApiModelProperty("产品组产品id")
    private Integer productGroupProductId;
    @ApiModelProperty("预约时间列表")
    private List<Date> bookDates;
}

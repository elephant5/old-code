package com.colourfulchina.pangu.taishang.api.vo;

import com.colourfulchina.pangu.taishang.api.entity.BookBasePayment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 预约支付金额vo
 */
@Data
public class BookBasePaymentVo extends BookBasePayment {
    private static final long serialVersionUID = -7994565430585277659L;

    @ApiModelProperty("时间中文详情")
    private String timeStr;
    @ApiModelProperty("星期列表")
    private List<Integer> weeks;
}

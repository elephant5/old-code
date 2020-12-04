package com.colourfulchina.pangu.taishang.api.vo.res.bookBasePayment;

import com.colourfulchina.pangu.taishang.api.entity.BookBasePayment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class BookBasePaymentRes extends BookBasePayment {
    private static final long serialVersionUID = -5169692561974598877L;

    @ApiModelProperty("预约时间")
    private Date bookDate;
}

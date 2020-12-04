package com.colourfulchina.mars.api.entity.sync;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2019-12-11
 */
@Data
public class BookOrderPriceVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    private Integer orderId;
    @ApiModelProperty(value = "")
    private LocalDate date;
    @ApiModelProperty(value = "")
    private BigDecimal price;
    @ApiModelProperty(value = "")
    private BigDecimal contract;


}

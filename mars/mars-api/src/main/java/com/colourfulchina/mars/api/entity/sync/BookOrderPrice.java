package com.colourfulchina.mars.api.entity.sync;

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
public class BookOrderPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer orderId;
    private LocalDate date;
    private BigDecimal price;
    private BigDecimal contract;
    /**
    * 每页显示条数
    */
    private Long pageSize;
    /**
    * 当前页码
    */
    private Long pageIndex;


}

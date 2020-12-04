package com.colourfulchina.mars.api.entity.sync;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
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
@TableName("chali_book_order_price")
public class BookOrderPricePo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("order_id")
    private Integer orderId;
    private LocalDate date;
    private BigDecimal price;
    private BigDecimal contract;


}

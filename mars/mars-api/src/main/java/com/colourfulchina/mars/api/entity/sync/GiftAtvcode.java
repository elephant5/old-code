package com.colourfulchina.mars.api.entity.sync;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2019-12-11
 */
@Data
public class GiftAtvcode implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer shopId;
    private String code;
    private Integer state;
    private Integer orderId;
    private LocalDate bookDate;
    private String bookTime;
    private String name;
    private Integer people;
    private LocalDateTime createTime;
    private LocalDateTime usedTime;
    private BigDecimal amount;
    private Boolean ignoreTime;
    private String bank;
    private LocalDateTime roger;
    /**
    * 每页显示条数
    */
    private Long pageSize;
    /**
    * 当前页码
    */
    private Long pageIndex;


}

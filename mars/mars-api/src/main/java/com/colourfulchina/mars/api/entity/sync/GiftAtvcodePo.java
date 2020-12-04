package com.colourfulchina.mars.api.entity.sync;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
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
@TableName("chali_gift_atvcode")
public class GiftAtvcodePo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer shopId;
    private String code;
    private Integer state;
    @TableId("order_id")
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


}

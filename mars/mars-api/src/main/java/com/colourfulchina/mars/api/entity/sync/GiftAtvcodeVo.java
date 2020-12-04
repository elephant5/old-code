package com.colourfulchina.mars.api.entity.sync;

import io.swagger.annotations.ApiModelProperty;
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
public class GiftAtvcodeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    private Integer shopId;
    @ApiModelProperty(value = "")
    private String code;
    @ApiModelProperty(value = "")
    private Integer state;
    @ApiModelProperty(value = "")
    private Integer orderId;
    @ApiModelProperty(value = "")
    private LocalDate bookDate;
    @ApiModelProperty(value = "")
    private String bookTime;
    @ApiModelProperty(value = "")
    private String name;
    @ApiModelProperty(value = "")
    private Integer people;
    @ApiModelProperty(value = "")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "")
    private LocalDateTime usedTime;
    @ApiModelProperty(value = "")
    private BigDecimal amount;
    @ApiModelProperty(value = "")
    private Boolean ignoreTime;
    @ApiModelProperty(value = "")
    private String bank;
    @ApiModelProperty(value = "")
    private LocalDateTime roger;


}

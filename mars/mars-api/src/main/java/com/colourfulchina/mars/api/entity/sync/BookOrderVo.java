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
public class BookOrderVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    private Integer id;
    @ApiModelProperty(value = "")
    private String status;
    @ApiModelProperty(value = "")
    private String sn;
    @ApiModelProperty(value = "")
    private String source;
    @ApiModelProperty(value = "")
    private Integer projectId;
    @ApiModelProperty(value = "")
    private Integer memberId;
    @ApiModelProperty(value = "")
    private String type;
    @ApiModelProperty(value = "")
    private Integer goodsId;
    @ApiModelProperty(value = "")
    private String gift;
    @ApiModelProperty(value = "")
    private Long unitId;
    @ApiModelProperty(value = "")
    private Integer shopId;
    @ApiModelProperty(value = "")
    private Integer itemId;
    @ApiModelProperty(value = "")
    private LocalDateTime orderDate;
    @ApiModelProperty(value = "")
    private String consignee;
    @ApiModelProperty(value = "")
    private String phone;
    @ApiModelProperty(value = "")
    private String name;
    @ApiModelProperty(value = "")
    private LocalDate bookDate;
    @ApiModelProperty(value = "")
    private String bookTime;
    @ApiModelProperty(value = "")
    private LocalDate leaveDate;
    @ApiModelProperty(value = "")
    private Integer bookQty;
    @ApiModelProperty(value = "")
    private Integer people;
    @ApiModelProperty(value = "")
    private String needs;
    @ApiModelProperty(value = "")
    private String addon;
    @ApiModelProperty(value = "")
    private LocalDate checkInDate;
    @ApiModelProperty(value = "")
    private LocalDate checkOutDate;
    @ApiModelProperty(value = "")
    private Integer bookRooms;
    @ApiModelProperty(value = "")
    private Integer bookDays;
    @ApiModelProperty(value = "")
    private Integer bookRoomNights;
    @ApiModelProperty(value = "")
    private String bookBed;
    @ApiModelProperty(value = "")
    private String bookBreakfast;
    @ApiModelProperty(value = "")
    private String bookNumber;
    @ApiModelProperty(value = "")
    private Integer channelId;
    @ApiModelProperty(value = "")
    private String channel;
    @ApiModelProperty(value = "")
    private String hotel;
    @ApiModelProperty(value = "")
    private String room;
    @ApiModelProperty(value = "")
    private String shopSettleMethod;
    @ApiModelProperty(value = "")
    private BigDecimal shopSettleAmount;
    @ApiModelProperty(value = "")
    private String shopSettleCurrency;
    @ApiModelProperty(value = "")
    private String shopSettleStatus;
    @ApiModelProperty(value = "")
    private LocalDate shopSettleDate;
    @ApiModelProperty(value = "")
    private BigDecimal shopCheckAmount;
    @ApiModelProperty(value = "")
    private LocalDate shopCheckDate;
    @ApiModelProperty(value = "")
    private LocalDate shopCashDate;
    @ApiModelProperty(value = "")
    private Boolean shopPaid;
    @ApiModelProperty(value = "")
    private BigDecimal shopPaidAmount;
    @ApiModelProperty(value = "")
    private LocalDate shopPaidDate;
    @ApiModelProperty(value = "")
    private BigDecimal bankSettleAmount;
    @ApiModelProperty(value = "")
    private LocalDate bankSettleDate;
    @ApiModelProperty(value = "")
    private Boolean bankPaid;
    @ApiModelProperty(value = "")
    private LocalDate bankPaidDate;
    @ApiModelProperty(value = "")
    private String notes;
    @ApiModelProperty(value = "")
    private String tags;
    @ApiModelProperty(value = "")
    private String operator;
    @ApiModelProperty(value = "")
    private String search;
    @ApiModelProperty(value = "")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "")
    private Integer bookSort;
    @ApiModelProperty(value = "")
    private String creator;
    @ApiModelProperty(value = "")
    private Integer groupId;
    @ApiModelProperty(value = "")
    private String more;
    @ApiModelProperty(value = "")
    private String meta;
    @ApiModelProperty(value = "")
    private Long version;
    @ApiModelProperty(value = "")
    private Integer bankId;
    @ApiModelProperty(value = "")
    private String title;
    @ApiModelProperty(value = "")
    private Integer bookState;
    @ApiModelProperty(value = "")
    private Integer auditState;
    @ApiModelProperty(value = "")
    private Integer verifyState;
    @ApiModelProperty(value = "")
    private Boolean alive;
    @ApiModelProperty(value = "")
    private Integer sms;
    @ApiModelProperty(value = "")
    private Integer packageId;


}

package com.colourfulchina.mars.api.entity.sync;

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
@TableName("chali_book_order")
public class BookOrderPo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String status;
    private String sn;
    private String source;
    private Integer projectId;
    private Integer memberId;
    private String type;
    private Integer goodsId;
    private String gift;
    private Long unitId;
    private Integer shopId;
    private Integer itemId;
    private LocalDateTime orderDate;
    private String consignee;
    private String phone;
    private String name;
    private LocalDate bookDate;
    private String bookTime;
    private LocalDate leaveDate;
    private Integer bookQty;
    private Integer people;
    private String needs;
    private String addon;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer bookRooms;
    private Integer bookDays;
    private Integer bookRoomNights;
    private String bookBed;
    private String bookBreakfast;
    private String bookNumber;
    private Integer channelId;
    private String channel;
    private String hotel;
    private String room;
    private String shopSettleMethod;
    private BigDecimal shopSettleAmount;
    private String shopSettleCurrency;
    private String shopSettleStatus;
    private LocalDate shopSettleDate;
    private BigDecimal shopCheckAmount;
    private LocalDate shopCheckDate;
    private LocalDate shopCashDate;
    private Boolean shopPaid;
    private BigDecimal shopPaidAmount;
    private LocalDate shopPaidDate;
    private BigDecimal bankSettleAmount;
    private LocalDate bankSettleDate;
    private Boolean bankPaid;
    private LocalDate bankPaidDate;
    private String notes;
    private String tags;
    private String operator;
    private String search;
    private LocalDateTime createTime;
    private Integer bookSort;
    private String creator;
    private Integer groupId;
    private String more;
    private String meta;
    private Long version;
    private Integer bankId;
    private String title;
    private Integer bookState;
    private Integer auditState;
    private Integer verifyState;
    private Boolean alive;
    private Integer sms;
    private Integer packageId;


}

package com.colourfulchina.mars.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ReservOrderComInfoVo implements Serializable {

    private Integer orderId;
    private Integer productGroupId;
    private String actCode;
    private Integer goodsId;
    private Integer productId;
    private Integer shopId;
    private Date orderTime;
    private Integer saleChannelId;
}

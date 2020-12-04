package com.colourfulchina.mars.api.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Nickal
 * @Description:
 * @Date: 2019/9/17 11:14
 */
@Data
public class BookOrderDetail {

    //    private Integer id;
    //订单id
    private Integer orderId;
    //商品id
    private Integer goodsId;
    //商品名称
    private String goodsName;
    //订单日期
    private Date orderDate;
    //订单状态
    private String orderStatus;
    //酒店名称
    private String hotelName;
    //商户名称
    private String shopName;
    //商户城市
    private String shopCity;
    //商户地址
    private String shopAddress;
    //资源名称
    private String productName;
    //权益类型
    private String giftType;
    //预约日期（入住日期）
    private String bookDate;
    //离店日期
    private String leaveDate;
    //预约时段
    private String bookTime;
    //间夜数
    private int qty;
    //使用人数
    private Integer people;
    //使用人姓名
    private String peopleName;
    //使用人电话
    private String phone;
    //订单金额
    private BigDecimal orderAmount;
    //优惠金额
    private BigDecimal offerAmount;
    //实际支付金额
    private BigDecimal actualPayAmount;

}

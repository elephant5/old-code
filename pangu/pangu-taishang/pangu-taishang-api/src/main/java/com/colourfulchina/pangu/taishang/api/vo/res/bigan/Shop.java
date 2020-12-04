package com.colourfulchina.pangu.taishang.api.vo.res.bigan;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Shop implements Serializable {
    private Long id;
    private String type;
    private Long hotelId;
    private String hotel;
    private String name;
    private String nameEn;
    private String py;
    private String title;
    private Long cityId;
    private String city;
    private String oversea;
    private String address;
    private String addressEn;
    private String phone;
    private String fax;
    private String notes;
    private String level;
    private Integer protocol;
    private String consignee;
    private String currency;
    private String settleMethod;
    private String summary;
    private Date createTime;
    private Long channelId;
    private Long geoId;
    private Date contractExpiry;
    private String portal;
    private String openTime;
    private String blockRule;
    private String more;
    private String titleEn;
    private String block;
}

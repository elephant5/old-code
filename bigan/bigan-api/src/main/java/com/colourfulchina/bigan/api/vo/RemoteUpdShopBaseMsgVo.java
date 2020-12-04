package com.colourfulchina.bigan.api.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RemoteUpdShopBaseMsgVo implements Serializable {
    private static final long serialVersionUID = 7546010191600550661L;

    /**
     * 商户id
     */
    private Integer shopId;
    /**
     * 商户中文名
     */
    private String shopName;
    /**
     * 商户英文名
     */
    private String shopNameEn;
    /**
     * 商户拼音
     */
    private String shopPy;
    /**
     * 商户标题
     */
    private String shopTitle;
    /**
     * 商户所属城市id
     */
    private Integer cityId;
    /**
     * 商户地址中文名
     */
    private String address;
    /**
     * 商户地址英文名
     */
    private String addressEn;
    /**
     * 商户联系方式
     */
    private String phone;
    /**
     * 商户营业开始时间
     */
    private String openTime;
    /**
     * 商户营业结束时间
     */
    private String closeTime;
    /**
     * 商户入住时间
     */
    private String checkInTime;
    /**
     * 商户退房时间
     */
    private String checkOutTime;
    /**
     * 最小提前预约时间
     */
    private Integer minBookDays;
    /**
     * 最大预约提前时间
     */
    private Integer maxBookDays;
    /**
     * 录单提示
     */
    private String tips;
    /**
     * 备注
     */
    private String notes;
    /**
     * 商户等级
     */
    private String level;
    /**
     * 商户介绍
     */
    private String summary;
}

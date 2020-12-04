package com.colourfulchina.bigan.api.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RemoteAddShopVo implements Serializable {
    private static final long serialVersionUID = 3880352443120514348L;

    /**
     * 酒店id
     */
    private Integer hotelId;
    /**
     * 城市id
     */
    private Integer cityId;
    /**
     * 商户类型
     */
    private String shopType;
    /**
     * 商户名称
     */
    private String shopName;
    /**
     * 商户拼音
     */
    private String shopPy;
    /**
     * 商户地址
     */
    private String address;
    /**
     * 备注
     */
    private String notes;
}

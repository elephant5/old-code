package com.colourfulchina.bigan.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * User: Ryan
 * Date: 2018/7/30
 */
@Data
public class ShopSaveInfoVo implements Serializable {


    private static final long serialVersionUID = -7817306215726576693L;

    private String  type;
    private String name ;
    private Long  hotel_id;
    private String  hotel;
    private Long  city_id;
    private String  address;
    private String  phone;
    private String  notes;
    private String  account;
    private String  pwd;
    private Long  uid;
    private Long  id;
    private String  error;
}

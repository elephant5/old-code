package com.colourfulchina.pangu.taishang.api.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RepeatInfoVo implements Serializable {

    private static final long serialVersionUID = 1343967681845120702L;

    private Integer id;

    private Integer delShopItemId;

    private Integer delProductId;

    private Integer keepShopItemId;

    private  Integer keepProductId;
}

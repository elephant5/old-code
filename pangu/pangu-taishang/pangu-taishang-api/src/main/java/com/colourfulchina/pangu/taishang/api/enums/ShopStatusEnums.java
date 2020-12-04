package com.colourfulchina.pangu.taishang.api.enums;

/**
 * 商户状态标识
 */
public enum ShopStatusEnums {
    UPSALES(0,"上架中"),
    DOWNSALES(1,"下架中"),
    ;
    private Integer code;
    private String name;

    ShopStatusEnums(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}

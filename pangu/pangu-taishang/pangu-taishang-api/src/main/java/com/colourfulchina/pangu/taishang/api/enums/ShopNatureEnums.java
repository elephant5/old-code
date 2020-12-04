package com.colourfulchina.pangu.taishang.api.enums;

/**
 * 商户性质
 */
public enum ShopNatureEnums {
    SUBSIDIARY_SHOP(0,"附属商户"),
    INDEPENDENT_SHOP(1,"独立商户"),
    ;
    private Integer code;
    private String name;

    ShopNatureEnums(Integer code, String name) {
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

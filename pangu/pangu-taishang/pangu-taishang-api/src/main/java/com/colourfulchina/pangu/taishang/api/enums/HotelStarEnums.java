package com.colourfulchina.pangu.taishang.api.enums;

/**
 * 酒店挂星枚举
 */
public enum HotelStarEnums {
    OTHERS(0,"其余"),
    ONSTAR(1,"挂星"),
    ;
    private Integer code;
    private String name;

    HotelStarEnums(Integer code, String name) {
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

package com.colourfulchina.pangu.taishang.api.enums;

/**
 * 产品组的产品的状态
 */
public enum ProductStatusEnums {
    ONSALE(0,"在售"),
    STOPSALE(1,"停售"),
    APPSALE(2,"申请停售"),
    ;
    private Integer code;
    private String name;

    ProductStatusEnums(Integer code, String name) {
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

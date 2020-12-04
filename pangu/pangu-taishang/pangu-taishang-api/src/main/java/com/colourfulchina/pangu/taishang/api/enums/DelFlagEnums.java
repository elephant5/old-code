package com.colourfulchina.pangu.taishang.api.enums;

/**
 * 删除标识
 */
public enum DelFlagEnums {
    NORMAL(0,"正常"),
    DELETE(1,"删除"),
    ;
    private Integer code;
    private String name;

    DelFlagEnums(Integer code, String name) {
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

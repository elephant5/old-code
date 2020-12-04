package com.colourfulchina.pangu.taishang.api.enums;

/**
 * 产品组使用类型
 */
public enum GroupUseTypeEnums {
    USE_TIMES(0,"次数"),
    USE_POINT(1,"点数"),
    ;
    private Integer code;
    private String name;

    GroupUseTypeEnums(Integer code, String name) {
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

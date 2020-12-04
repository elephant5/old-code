package com.colourfulchina.pangu.taishang.api.enums;

/**
 * 第三方资源
 */
public enum ThirdChannelEnums {
    THIRD_CHANNEL(-1,"第三方资源"),
    ;
    private Integer code;
    private String name;

    ThirdChannelEnums(Integer code, String name) {
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

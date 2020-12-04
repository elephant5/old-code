package com.colourfulchina.pangu.taishang.api.enums;

/**
 * 产品组使用限制类型
 */
public enum UseLimitTypeEnums {
    NONE("none","无限制"),
    FIXED_TIMES("fixed_times","固定次数"),
    FIXED_POINT("fixed_point","固定点数"),
    CYCLE_REPEAT("cycle_repeat","重复周期"),
    ;
    private String code;
    private String name;

    UseLimitTypeEnums(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}

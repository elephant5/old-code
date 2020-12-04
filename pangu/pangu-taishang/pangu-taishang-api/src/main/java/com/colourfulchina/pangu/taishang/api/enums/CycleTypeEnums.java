package com.colourfulchina.pangu.taishang.api.enums;

/**
 * 产品组重复周期类型
 */
public enum CycleTypeEnums {
    CYCLE_DAY(0,"天"),
    CYCLE_WEEK(1,"周"),
    CYCLE_MONTH(2,"月"),
    CYCLE_YEAR(3,"年"),
    ;
    private Integer code;
    private String name;

    CycleTypeEnums(Integer code, String name) {
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

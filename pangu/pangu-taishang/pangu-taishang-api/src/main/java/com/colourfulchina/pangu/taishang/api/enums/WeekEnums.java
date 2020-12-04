package com.colourfulchina.pangu.taishang.api.enums;

/**
 * 星期
 */
public enum WeekEnums {
    MONDAY("1","monday","星期一"),
    TUESDAY("2","tuesday","星期二"),
    WEDNESDAY("3","wednesday","星期三"),
    THURSDAY("4","thursday","星期四"),
    FRIDAY("5","friday","星期五"),
    SATURDAY("6","saturday","星期六"),
    SUNDAY("7","sunday","星期日"),
    ;
    private String code;
    private String desc;
    private String name;

    WeekEnums(String code,String desc, String name) {
        this.code = code;
        this.desc = desc;
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getNameByCode(String code){
        for (WeekEnums weekEnums : WeekEnums.values()) {
            if (weekEnums.code.equals(code)){
                return weekEnums.name;
            }
        }
        return null;
    }

    public static String getDescByCode(String code){
        for (WeekEnums weekEnums : WeekEnums.values()) {
            if (weekEnums.code.equals(code)){
                return weekEnums.desc;
            }
        }
        return null;
    }
}

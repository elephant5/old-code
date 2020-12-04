package com.colourfulchina.pangu.taishang.api.enums;

public enum ShopAccountTypeEnums  {

    SHOP(0,"商户"),
    HOTEL(1,"酒店"),
    GROUPS(2,"集团"),
    PEOPLE(3,"个人"),
    ;
    private Integer code;
    private String name;

    ShopAccountTypeEnums(Integer code, String name) {
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

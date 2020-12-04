package com.colourfulchina.mars.api.enums;

public enum PushStatusEnum {
    PUSH_SUCCESS("success","推送成功"),
    PUSH_FAIL("fail","推送失败"),
    ;
    private String code;
    private String name;

    PushStatusEnum(String code, String name) {
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

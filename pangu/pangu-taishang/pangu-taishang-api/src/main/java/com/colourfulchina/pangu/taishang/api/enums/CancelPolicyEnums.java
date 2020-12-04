package com.colourfulchina.pangu.taishang.api.enums;

/**
 * 取消政策
 */
public enum CancelPolicyEnums {
    NON_CANCEL(1,"不可变更"),
    ADVANCE_24(2,"提前24小时"),
    ADVANCE_48(3,"提前48小时"),
    ALL_CANCEL(4,"可变更"),
    ;
    private Integer code;
    private String name;

    CancelPolicyEnums(Integer code, String name) {
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

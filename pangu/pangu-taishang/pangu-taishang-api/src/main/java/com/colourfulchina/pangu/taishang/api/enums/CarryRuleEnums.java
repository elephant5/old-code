package com.colourfulchina.pangu.taishang.api.enums;

/**
 * 进位规则
 */
public enum CarryRuleEnums {
    ROUND_HALF_UP("0","四舍五入"),
    ROUND_UP("1","向上取值"),
    ROUND_DOWN("2","向下取值"),
    ;
    private String code;
    private String name;

    CarryRuleEnums(String code, String name) {
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

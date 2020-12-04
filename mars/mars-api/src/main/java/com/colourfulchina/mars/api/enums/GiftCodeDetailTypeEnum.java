package com.colourfulchina.mars.api.enums;

/**
 * 激活码权益明细类型枚举
 */
public enum GiftCodeDetailTypeEnum {

    NOMAL_TYPE(0,"正常权益"),
    CYCLE_TYPE(1,"周期循环权益"),
            ;
    private Integer code;
    private String codeName;

    GiftCodeDetailTypeEnum(Integer code, String codeName) {
        this.code = code;
        this.codeName = codeName;
    }

    public Integer getcode() {
        return code;
    }

    public String getcodeName() {
        return codeName;
    }
}

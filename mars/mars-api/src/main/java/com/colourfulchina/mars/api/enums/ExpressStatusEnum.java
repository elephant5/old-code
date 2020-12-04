package com.colourfulchina.mars.api.enums;

public enum ExpressStatusEnum {
    NON_SEND(0,"未发货"),
    CAN_SEND(1,"已发货"),
    CAN_RECEIVE(2,"已收货"),
    CAN_REFUND(3,"已退货"),
    ;
    private int code;
    private String name;

    ExpressStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
    public static String getNameByCode(int code){
        for (ExpressStatusEnum expressStatusEnum: ExpressStatusEnum.values()){
            if (code ==  expressStatusEnum.code){
                return expressStatusEnum.name;
            }
        }
        return null;
    }
}

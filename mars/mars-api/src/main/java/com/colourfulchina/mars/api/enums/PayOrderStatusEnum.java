package com.colourfulchina.mars.api.enums;

public enum PayOrderStatusEnum {
    UNREFUND(0,"退款中"),
    UNPAID(1,"待支付"),
    PREPAID(2,"已支付"),
    REFUND(3,"已退款"),
    FINISHED(4,"交易结束，不可退款"),
    USED(5,"已使用"),

    ;
    private int code;
    private String name;

    PayOrderStatusEnum(int code, String name) {
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
        for (PayOrderStatusEnum salesOrderEnum: PayOrderStatusEnum.values()){
            if (code ==  salesOrderEnum.code){
                return salesOrderEnum.name;
            }
        }
        return null;
    }
}

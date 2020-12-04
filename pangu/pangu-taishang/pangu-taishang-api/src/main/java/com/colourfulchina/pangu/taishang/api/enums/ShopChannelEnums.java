package com.colourfulchina.pangu.taishang.api.enums;

/**
 * 商户渠道
 */
public enum ShopChannelEnums {
    CHANNEL_RESOURCE(0,"渠道资源"),
    COMPANY_RESOURCE(1,"公司资源"),
    ;
    private Integer code;
    private String name;

    ShopChannelEnums(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getNameByCode(Integer code){
        for (ShopChannelEnums shopChannelEnums : ShopChannelEnums.values()) {
            if (shopChannelEnums.code == code){
                return shopChannelEnums.name;
            }
        }
        return null;
    }
}

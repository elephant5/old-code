package com.colourfulchina.pangu.taishang.api.enums;

/**
 * 配送方式
 */
public enum ExpressModeEnums {
    NO_EXPRESS(0,"无需配送"),
    EXPRESS_SEND(1,"快递发货"),
    ARR_NOW(2,"及时送达"),
    TAKE_ONESELF(3,"到店自取"),
    ;
    private Integer code;
    private String name;

    ExpressModeEnums(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    public static ExpressModeEnums findByCode(Integer code){
        for (ExpressModeEnums g : ExpressModeEnums.values()){
            if (code.compareTo(g.getCode()) == 0){
                return g;
            }
        }
        return null;
    }
}

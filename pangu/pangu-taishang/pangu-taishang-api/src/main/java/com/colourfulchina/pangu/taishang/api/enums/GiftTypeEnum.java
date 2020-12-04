package com.colourfulchina.pangu.taishang.api.enums;

public enum GiftTypeEnum {
    TWOFONE("2F1","二免一" ),
    THREEF("3F1","三免一" ),
    B1F1("B1F1","买一赠一" ),
    D5("D5","五折" ),
    F1("F1","单免"),
    F2("F2","双免" ),
    N1("N1","两天一晚"  ),
    N2("N2","三天两晚"  ),
    N3("N3","四天三晚"  ),
    N4("N4","五天四晚"  ),
    NX("NX","开放住宿"  );

    private String code;
    private String name;

    GiftTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
    /**
     * @param code
     * @return
     */
    public static GiftTypeEnum findByCode(String code){
        for (GiftTypeEnum g : GiftTypeEnum.values()){
            if (code.equalsIgnoreCase(g.getCode())){
                return g;
            }
        }
        return null;
    }

    /**
     * @param name
     * @return
     */
    public static GiftTypeEnum findByName(String name){
        for (GiftTypeEnum g : GiftTypeEnum.values()){
            if (name.equals(g.getName())){
                return g;
            }
        }
        return null;
    }
}
package com.colourfulchina.pangu.taishang.api.enums;

/**
 * 商户类型
 */
public enum ResourceTypeEnums {
    ACCOM("accom","住宿"),
    BUFFET("buffet","自助餐"),
    COUPON("coupon","兑换券"),
    GYM("gym","健身"),
    SPA("spa","SPA"),
    TRIP("trip","出行"),
    TEA("tea","下午茶"),
    DRINK("drink","单杯茶饮"),
    LOUNGE("lounge","贵宾厅"),
    CAR("car","机场出行"),
    SETMENU("setmenu","定制套餐"),
    OTHER("other","其他"),
    EXCHANGE("exchange","礼品兑换"),
    CHARGE_CPN("charge_cpn","手机充值券"),
    COFFEE_CPN("coffee_cpn","咖啡券"),
    MOVIE_CPN("movie_cpn","电影券"),
    SWEET_CPN("sweet_cpn","甜品券"),
    TRIP_CPN("trip_cpn","出行券"),
    VIDEO_CPN("video_cpn","视频VIP券"),
    TAKEOUT_CPN("takeout_cpn","外卖券"),
    SUPPERMARKET_CPN("suppermarket_cpn","商超卡券"),
    BAKERY_CPN("bakery_cpn","面包券"),
    MUSIC_CPN("music_cpn","音乐券"),
    TOOTH_CPN("tooth_cpn","口腔保健券"),
    SNACKS_CPN("snacks_cpn","零食券"),
    OTHERS_CPN("others_cpn","其他卡券"),
    OBJECT_CPN("object_cpn","实物券"),
    MEDICAL("medical","绿通就医"),
    ;
    private String code;
    private String name;

    ResourceTypeEnums(String code, String name) {
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
     * 根据code获取权益类型
     * @param code
     * @return
     */
    public static ResourceTypeEnums findByCode(String code){
        for (ResourceTypeEnums g : ResourceTypeEnums.values()){
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
    public static ResourceTypeEnums findByName(String name){
        for (ResourceTypeEnums g : ResourceTypeEnums.values()){
            if (name.equals(g.getName())){
                return g;
            }
        }
        return null;
    }
}

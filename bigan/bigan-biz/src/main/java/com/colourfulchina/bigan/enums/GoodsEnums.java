package com.colourfulchina.bigan.enums;

public class GoodsEnums {
    /**
     * 权益类型
     */
    public enum gift{
        TWO_F1("2F1","二免一","两人同行享五折优惠（双人服务费及税费需自付）"),
        THREE_F1("3F1","三免一","三人同行享六折优惠"),
        B1_F1("B1F1","买一赠一","买一赠一"),
        D5("D5","五折","五折尊享"),
        F1("F1","单免","单人礼遇"),
        F2("F2","双免","双人礼遇"),
        N1("N1","两天一晚","两天一晚"),
        N2("N2","三天两晚","三天两晚"),
        N3("N3","四天三晚","四天三晚"),
        N4("N4","五天四晚","五天四晚"),
        NX("NX","开放住宿","开放住宿"),
        ;
        private String code;
        private String shortName;
        private String name;

        gift(String code, String shortName, String name) {
            this.code = code;
            this.shortName = shortName;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getShortName() {
            return shortName;
        }

        public String getName() {
            return name;
        }

        /**
         * 根据code获取权益类型
         * @param code
         * @return
         */
        public static gift findByCode(String code){
            for (gift g : gift.values()){
                if (code.equalsIgnoreCase(g.getCode())){
                    return g;
                }
            }
            return null;
        }

        /**
         * 根据name或shortName获取权益
         * @param nameOrShort
         * @return
         */
        public static gift findByNameOrShort(String nameOrShort){
            for (gift g : gift.values()){
                if (nameOrShort.equalsIgnoreCase(g.getName())|| nameOrShort.equalsIgnoreCase(g.getShortName())){
                    return g;
                }
            }
            return null;
        }
    }

    public  enum service{
        ACCOM("accom","住宿"),
        BUFFET("buffet","自助餐"),
        EXCHANGE("exchange","礼品兑换"),
        GYM("gym","健身"),
        SPA("spa","水疗"),
        TEA("tea","下午茶"),
        DRINK("drink","单杯茶饮"),
        CAR("car","机场出行"),
        SETMENU("setmenu","定制套餐"),
        ;
        private String code;
        private String name;

        service(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public static service findByCode(String code){
            for (service s:service.values()){
                if (code.equalsIgnoreCase(s.getCode())){
                    return s;
                }
            }
            return null;
        }

        public static service findByName(String name){
            for (service s:service.values()){
                if (name.equalsIgnoreCase(s.getName())){
                    return s;
                }
            }
            return null;
        }

    }

    /**
     * @Description:根据权益code转换权益展现形式
     * @Author:DJ
     * @CreateDate:2018/11/28
     * @params:
     * @return:
     */
    public enum GiftTypeEnum {
        TWOFONE("2F1","两人同行享五折优惠（双人服务费及税费需自付","两人同行享五折优惠"),
        THREEF("3F1","三人同行享六折优惠",""),
        BONEFONE("B1F1","买一赠一",""),
        DFIVE("D5","五折尊享","两人同行享五折优惠"),
        FONE("F1","单人礼遇",""),
        FTWO("F2","双人礼遇","双人礼遇"),
        NONE("N1","两天一晚","1晚"),
        NTWO("N2","三天两晚","2晚"),
        NTHREE("N3","四天三晚","3晚"),
        NFOUR("N4","五天四晚","4晚"),
        NX("NX","开放住宿","开放住宿");

        private String code;

        private String name;

        private String desc;

        GiftTypeEnum(String code, String name, String desc) {
            this.code = code;
            this.name = name;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public static String getGiftDesc(String gift){
            String result = null;
            for (GiftTypeEnum giftTypeEnum : GiftTypeEnum.values()) {
                if(gift.equals(giftTypeEnum.getCode())){
                    result = giftTypeEnum.getDesc();
                }
            }
            return  result;
        }
    }

}

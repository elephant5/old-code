package com.colourfulchina.pangu.taishang.api.enums;

public class BlockRuleEnums {
    private BlockRuleEnums() {
    }

    public enum  Calendar{
        SOLAR_CALENDAR(0,"阳历"),
        LUNAR_CALENDAR(1,"阴历"),
        ;
        private Integer code;
        private String name;

        Calendar(Integer code, String name) {
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

    public enum  RepeatEnum{
        NO_REPETITION(0,"不重复"),
        REPEAT(1,"重复"),
        ;
        private Integer code;
        private String name;

        RepeatEnum(Integer code, String name) {
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
}

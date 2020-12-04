package com.colourfulchina.pangu.taishang.api.enums;

public class BlockReasonEnums {
    private BlockReasonEnums() {
    }

    public enum  ReasonType{
        TYPE_SHOP_PROTOCOL("shop_protocol","商户级别block"),
        TYPE_SHOP_ITEM("shop_item","商户规则级别block"),
        ;
        private String code;
        private String name;

        ReasonType(String code, String name) {
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
}

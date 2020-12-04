package com.colourfulchina.mars.api.enums;

public class ReservOrderTypeEnums {

    /**
     * 预约状态
     */
    public enum ReservOrderType{
        COLOUR_CALL("PANGU_CALL","内部系统来电录单"),
        ONLINE("ONLINE","在线统一预约"),
        SH_BANK_APP("BOSC_APP","上海银行App"),
        BOSC_WECHAT("BOSC_WECHAT","上海银行微信"),
        ;
        private String code;
        private String codeName;

        ReservOrderType(String code, String codeName) {
            this.code = code;
            this.codeName = codeName;
        }

        public String getcode() {
            return code;
        }

        public String getcodeName() {
            return codeName;
        }
        public static String getNameByCode(String code){
            for (ReservOrderTypeEnums.ReservOrderType bookState : ReservOrderTypeEnums.ReservOrderType.values()){
                if (bookState.code.equals(code) ){
                    return bookState.codeName;
                }
            }
            return null;
        }
    }

    /*
    * 取消政策
    * */
    public enum CancelPolicyStatus{
        no_change(1,"不可变更","订单确认后不可取消或并更"),
        adv24_change(2,"提前24小时","订单确认后可提前24小时取消或变更"),
        adv48_change(3,"提前48小时","订单确认后可提前48小时取消或变更"),
        change(4,"可变更","");
        private Integer code;
        private String value;
        private String text;
        CancelPolicyStatus(Integer code,String value,String text) {
            this.code = code;
            this.value=value;
            this.text=text;
        }
        public static String getTextByCode(Integer code){
            for (ReservOrderTypeEnums.CancelPolicyStatus cancelPolicyStatus : ReservOrderTypeEnums.CancelPolicyStatus.values()){
                if (cancelPolicyStatus.code.equals(code) ){
                    return cancelPolicyStatus.text;
                }
            }
            return null;
        }

        public static String getValueByCode(String code){
            for (ReservOrderTypeEnums.CancelPolicyStatus cancelPolicyStatus : ReservOrderTypeEnums.CancelPolicyStatus.values()){
                if (cancelPolicyStatus.code.equals(code) ){
                    return cancelPolicyStatus.value;
                }
            }
            return null;
        }
    }

    /*
     * 证件类型
     * */
    public enum IdTypeStatus{
        ID_CARD("idCard","身份证"),
        PASSPORT("passport","护照");
        private String code;
        private String value;
        IdTypeStatus(String code,String value) {
            this.code = code;
            this.value=value;
        }

        public static String getValueByCode(String code){
            for (ReservOrderTypeEnums.CancelPolicyStatus cancelPolicyStatus : ReservOrderTypeEnums.CancelPolicyStatus.values()){
                if (cancelPolicyStatus.code.equals(code) ){
                    return cancelPolicyStatus.value;
                }
            }
            return null;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /*
     * 类型
     * */
    public enum typeEnum{
        ADD("ADD"),//加权益
        SUB("SUB");//减权益
        private String code;
        typeEnum(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}

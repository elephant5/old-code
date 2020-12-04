package com.colourfulchina.mars.api.enums;

public class ReservOrderStatusEnums {

    /**
     * 预约状态
     */
    public enum ReservOrderStatus{
        none("0","尚未预订"),
        done("1","预订成功"),
        cancel("2","预订取消"),
        failed("3","预订失败"),
        process("4","预定中"),
        ;
        private String code;
        private String codeName;

        ReservOrderStatus(String code, String codeName) {
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
            for (ReservOrderStatusEnums.ReservOrderStatus bookState : ReservOrderStatusEnums.ReservOrderStatus.values()){
                if (bookState.code.equals(code) ){
                    return bookState.codeName;
                }
            }
            return null;
        }

        /**
         * 处理中控制
         * @param code
         * @return
         */
        public static boolean canProcess(String code){
            if(code.equals(ReservOrderStatus.none.getcode())){
                return true;
            }
            return false;
        }

        /**
         * 预定成功控制
         * @param code
         * @return
         */
        public static boolean canSuccess(String code){
            if(code.equals(ReservOrderStatusEnums.ReservOrderStatus.none.getcode())
                    ||code.equals(ReservOrderStatusEnums.ReservOrderStatus.done.getcode())
                    ||code.equals(ReservOrderStatusEnums.ReservOrderStatus.process.getcode())){
                return true;
            }
            return false;
        }

        /**
         * 预订变更控制
         * @param code
         * @return
         */
        public static boolean canChange(String code){
            if(code.equals(ReservOrderStatusEnums.ReservOrderStatus.none.getcode())
                    ||code.equals(ReservOrderStatusEnums.ReservOrderStatus.done.getcode())
                    ||code.equals(ReservOrderStatusEnums.ReservOrderStatus.process.getcode())){
                return true;
            }
            return false;
        }

        /**
         * 预订失败控制
         * @param code
         * @return
         */
        public static boolean canFail(String code){
            if(code.equals(ReservOrderStatusEnums.ReservOrderStatus.none.getcode())
                    ||code.equals(ReservOrderStatusEnums.ReservOrderStatus.process.getcode())){
                return true;
            }
            return false;
        }

        /**
         * 预订取消控制
         * @param code
         * @return
         */
        public static boolean canCancel(String code){
            if(code.equals(ReservOrderStatusEnums.ReservOrderStatus.none.getcode())
                    ||code.equals(ReservOrderStatusEnums.ReservOrderStatus.done.getcode())
                    ||code.equals(ReservOrderStatusEnums.ReservOrderStatus.process.getcode())){
                return true;
            }
            return false;
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
            for (ReservOrderStatusEnums.CancelPolicyStatus cancelPolicyStatus : ReservOrderStatusEnums.CancelPolicyStatus.values()){
                if (cancelPolicyStatus.code.equals(code) ){
                    return cancelPolicyStatus.text;
                }
            }
            return null;
        }

        public static String getValueByCode(String code){
            for (ReservOrderStatusEnums.CancelPolicyStatus cancelPolicyStatus : ReservOrderStatusEnums.CancelPolicyStatus.values()){
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
        PASSPORT("passport","护照"),
        HK_MACAO_PASS("HK_Macao_Pass","港澳通行证");
        private String code;
        private String value;
        IdTypeStatus(String code,String value) {
            this.code = code;
            this.value=value;
        }

        public static String getValueByCode(String code){
            for (ReservOrderStatusEnums.CancelPolicyStatus cancelPolicyStatus : ReservOrderStatusEnums.CancelPolicyStatus.values()){
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

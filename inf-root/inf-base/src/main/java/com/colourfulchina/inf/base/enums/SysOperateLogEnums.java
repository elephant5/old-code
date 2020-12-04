package com.colourfulchina.inf.base.enums;

public class SysOperateLogEnums {
    private SysOperateLogEnums() {
    }

    public enum  Type{
        INSERT("INSERT","新增"),
        UPDATE("UPDATE","更新"),
        DELETE("DELETE","删除"),
        SELECT("SELECT","查询"),
        ;
        private String code;
        private String name;

        Type(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public static String getTypeCode(String code){
            for (Type type: Type.values()){
                if (code.toLowerCase().startsWith(type.code.toLowerCase())){
                    return type.code;
                }
            }
            return null;
        }
    }
}

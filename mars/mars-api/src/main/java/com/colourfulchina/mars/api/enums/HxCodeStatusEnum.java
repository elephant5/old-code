package com.colourfulchina.mars.api.enums;


import lombok.Data;

/**
 * 激活码状态枚举
 */
@Data
public class HxCodeStatusEnum {

    public enum HxCodeStatus{
        NEWONE("未使用",0),
        OVERTIME("已过期",1),
        USED("已使用",2),
        INVALID("已作废",3);


        private String name;
        private int index;

        HxCodeStatus(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public int getIndex() {
            return index;
        }

        public static String findNameByIndex(int index){
            for (HxCodeStatus actCodeStatus : HxCodeStatus.values()) {
                if (actCodeStatus.getIndex() == index){
                    return actCodeStatus.getName();
                }
            }
            return null;
        }
    }
}

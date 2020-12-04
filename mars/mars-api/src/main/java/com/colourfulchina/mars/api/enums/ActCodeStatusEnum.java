package com.colourfulchina.mars.api.enums;


import lombok.Data;

/**
 * 激活码状态枚举
 */
@Data
public class ActCodeStatusEnum {

    public enum ActCodeStatus{
        CREATE("生成",0),
        OUT("出库",1),
        ACTIVATE("激活",2),
        RUN_OUT("已用完",3),
        PAST("过期",4),
        RETURN("退货",5),
        OBSOLETE("作废",6);


        private String name;
        private int index;

        ActCodeStatus(String name, int index) {
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
            for (ActCodeStatus actCodeStatus : ActCodeStatus.values()) {
                if (actCodeStatus.getIndex() == index){
                    return actCodeStatus.getName();
                }
            }
            return null;
        }
    }
}

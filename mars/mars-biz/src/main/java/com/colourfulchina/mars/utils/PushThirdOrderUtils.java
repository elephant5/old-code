package com.colourfulchina.mars.utils;

import lombok.Data;

import java.util.Calendar;
import java.util.Date;

/**
 * 推送第三方预约单工具类
 */
@Data
public class PushThirdOrderUtils {
    //推送的最大次数
    public static final Integer PUSH_MAX_COUNT = 8;


    public static Date calNextPushTime(Integer thisCount)throws Exception{
        Calendar calendar = Calendar.getInstance();
        switch (thisCount){
            case 1:
                calendar.add(Calendar.MINUTE,3);
                break;
            case 2:
                calendar.add(Calendar.MINUTE,5);
                break;
            case 3:
                calendar.add(Calendar.MINUTE,8);
                break;
            case 4:
                calendar.add(Calendar.MINUTE,20);
                break;
            case 5:
                calendar.add(Calendar.HOUR,1);
                break;
            case 6:
                calendar.add(Calendar.HOUR,6);
                break;
            case 7:
                calendar.add(Calendar.DAY_OF_MONTH,1);
                break;
                default:
                    calendar.add(Calendar.DAY_OF_MONTH,3);
                    break;
        }
        return calendar.getTime();
    }
}

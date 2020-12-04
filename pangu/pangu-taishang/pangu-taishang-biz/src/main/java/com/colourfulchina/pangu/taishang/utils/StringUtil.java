package com.colourfulchina.pangu.taishang.utils;

import java.util.Date;

public class StringUtil {

    public static String valueOf(Object obj){
        return (obj == null) ? null : obj.toString();
    }

    public static Integer valueIntOf(Object obj){
        String s = valueOf(obj);
        return s == null? null : Integer.parseInt(s);
    }

    public static Date valueDateOf(Object obj){
        String s = valueOf(obj);
        return s == null ? null : DateUtil.toDate(s);
    }
}

package com.colourfulchina.bigan.utils;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 *将百分比字符串转Double
 */
public class NumUtils {
    public static Double parseStringToDouble(String str)  {
        NumberFormat num   =   NumberFormat.getPercentInstance();
        Number parse = null;
        try {
            parse = num.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse.doubleValue();
    }
}

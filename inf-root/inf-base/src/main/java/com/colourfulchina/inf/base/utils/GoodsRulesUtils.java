package com.colourfulchina.inf.base.utils;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoodsRulesUtils {


    /**
     * erp 系统的项目权益有效期的规则转换到新系统
     * @param oldRules
     * @return
     */
    public static String changeRules(String oldRules){
        Map tempValue  = Maps.newHashMap();
        tempValue.put("M-","+M");
        tempValue.put("M","=M");
        tempValue.put("M+","-M");
        tempValue.put("M-S","+M");
        tempValue.put("MS","=M");
        tempValue.put("M+S","-M");
        tempValue.put("M-B","+M");
        tempValue.put("MB","=M");
        tempValue.put("M+B","-M");
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        String newRules = null;
        if(StringUtils.isBlank(oldRules)){
            newRules ="NULL";//不限
        }else   if(oldRules.length() == 10 ){//固定日期
            newRules ="D:"+oldRules;
        }else   if(oldRules.contains("/")){//售出后X个月内激活，激活后Y个月内使用
            // 6M-/24M-B   == XM-YU:-M12/+M12
            String[] temp  = oldRules.split("/");
            Matcher m = p.matcher(temp[0]);
            Matcher m2 = p.matcher(temp[1]);
            StringBuffer sb  = new StringBuffer("XM-YU:");
            sb.append(m.replaceAll(""));
            sb.append(tempValue.get(temp[0].substring(temp[0].indexOf("M"),temp[0].length())));
            sb.append("/");
            sb.append(m2.replaceAll(""));
            sb.append(tempValue.get(temp[1].substring(temp[1].indexOf("M"),temp[1].length())));
            newRules =sb.toString();
        }else{//售出后X个月内使用
            Matcher m = p.matcher(oldRules);
            newRules ="XM:"+m.replaceAll("").trim();
        }
        return newRules;
    }

//    public static void main(String[] args) {
//        System.out.println( changeRules("6M+/24M-B"));
//        String regEx="[^0-9]";
//        Pattern p = Pattern.compile(regEx);
//        String oldRules = "6M-/24M-B";
//        String[] temp  = oldRules.split("/");
//        Matcher m = p.matcher(temp[0]);
//        Matcher m2 = p.matcher(temp[1]);
//        System.out.println( m.replaceAll("") + "---" + m2.replaceAll(""));
//        System.out.println(temp[0].substring(temp[0].indexOf("M"),temp[0].length()));
//        System.out.println(temp[1].substring(temp[1].indexOf("M"),temp[1].length()));
//        System.out.println( m.replaceAll("") + "---" + m2.replaceAll(""));
//    }
}

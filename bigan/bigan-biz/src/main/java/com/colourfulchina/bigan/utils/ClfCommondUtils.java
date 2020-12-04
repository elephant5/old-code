package com.colourfulchina.bigan.utils;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Ryan
 * Date: 2018/9/1
 */
public class ClfCommondUtils  {

    public static String replaceTime(String timeStr){
        timeStr = timeStr.replaceAll("-","~");
        timeStr = timeStr.replaceAll("——","~");
        return timeStr;
    }


    public static int getNumber(String numStr){
        switch (numStr) {
            case "一":
                return 1;
            case "二":
                return 2;
            case "三":
                return 3;
            case "四":
                return 4;
            case "五":
                return 5;
            case "六":
                return 6;
            case "七":
                return 7;
            case "日":
                return 7;
            case "天":
                return 7;
            default:
                return 0;
        }
    }

    public  static List<Integer> getNumberList(String str){

        if(StringUtils.isEmpty(str)){
            return new ArrayList<>(0);
        }
        String regEx="\\d";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        List<Integer> list = new ArrayList<>(0);
        // 匹配的模式
        while (m.find()) {
            int i = 0;
            list.add(Integer.parseInt(m.group(i)));
            i++;
        }
        Collections.sort(list);
//        System.out.println(Collections.max());
        return list;
    }


//    public static void main(String args[]){
////        getNumberList(null  );
////
////    }
}

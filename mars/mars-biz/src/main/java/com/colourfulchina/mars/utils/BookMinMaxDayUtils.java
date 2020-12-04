package com.colourfulchina.mars.utils;

import cn.hutool.core.date.DateUtil;
import com.colourfulchina.pangu.taishang.api.dto.BookNum;

import java.util.Date;

/**
 * 预约最小最大默认时间计算类
 */
public class BookMinMaxDayUtils {

    /**
     * 根据国家和资源类型查询对应的最小和最大提前预约时间
     * 餐饮（自助餐、定制套餐、下午茶）——单杯茶饮不用选择预约时间，默认预约当日
     * 最早提前1天预约，最晚提前10天预约
     * 例如：3.29号可以预约3.30-4.9（当前最早可预订日期+最晚天数）
     *
     * 健身和SPA
     * 最高提前2天，最晚提前10天预约
     * 例如：3.29号可以预约3.31-4.10（当前最早预订日期+最晚天数）
     * 以上在每天下午17点之前算今天，17点之后就算第二天了，可以预约的日期往后顺延
     * 住宿
     * 国内：最高提前5天，最晚提前60天
     * 国外：最高提前7天，最晚提前60天
     * @param service
     * @param country
     * @return
     */
    public static BookNum getBookByService(String service,String country){
        BookNum bookNum = new BookNum();
        Date now = new Date();
        int hour = DateUtil.hour(now,true);
        if ("buffet".equals(service) || "tea".equals(service) || "setmenu".equals(service)){
            if (hour>=17){
                bookNum.setMinBook(2);
                bookNum.setMaxBook(2+10);
            }else {
                bookNum.setMinBook(1);
                bookNum.setMaxBook(1+10);
            }
        }
        if ("gym".equals(service) || "spa".equals(service)){
            if (hour>=17){
                bookNum.setMinBook(3);
                bookNum.setMaxBook(3+10);
            }else {
                bookNum.setMinBook(2);
                bookNum.setMaxBook(2+10);
            }
        }
        if ("accom".equals(service)){
            if ("CN".equals(country)){
                bookNum.setMinBook(5);
                bookNum.setMaxBook(5+60);
            }else {
                bookNum.setMinBook(7);
                bookNum.setMaxBook(7+60);
            }
        }
        return bookNum;
    }

    /**
     * 获取俩个integer数据中大的数据
     * @param one
     * @param two
     * @return
     */
    public static Integer maxInteger(Integer one,Integer two){
        if (one == null && two != null){
            return two;
        }
        if (one != null && two == null){
            return one;
        }
        if (one != null && two != null){
            if (one.compareTo(two) > 0){
                return one;
            }else {
                return two;
            }
        }
        return null;
    }

    /**
     * 获取俩个integer数据中小的数据
     * @param one
     * @param two
     * @return
     */
    public static Integer minInteger(Integer one,Integer two){
        if (one == null && two != null){
            return two;
        }
        if (one != null && two == null){
            return one;
        }
        if (one != null && two != null){
            if (one.compareTo(two) > 0){
                return two;
            }else {
                return one;
            }
        }
        return null;
    }
}

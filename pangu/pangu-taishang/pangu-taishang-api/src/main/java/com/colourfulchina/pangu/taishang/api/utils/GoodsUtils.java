package com.colourfulchina.pangu.taishang.api.utils;

import cn.hutool.core.date.DateUtil;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GoodsUtils  {
    public static SimpleDateFormat sdf1 =new SimpleDateFormat("yyyyMMdd");
    public static SimpleDateFormat sdf2 =new SimpleDateFormat("HHmmss");
    public static SimpleDateFormat sdf3 =new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat sdf4 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 解析项目的过期日期等
     * @param goodsExpiryDate
     * @return
     */
    public static String goodsExpiryDate(String goodsExpiryDate){
        if(StringUtils.isEmpty(goodsExpiryDate) || (StringUtils.isNotBlank(goodsExpiryDate)&& "NULL".equals(goodsExpiryDate))){
            return null;
        }
        String[] str = goodsExpiryDate.split(":");
        if(str.length == 1){
            return null;
        }
        String type = str[0];
        String expiry  = str[1];
        Calendar date  = Calendar.getInstance();
        String result = null;
        if(type.equals( "NULL")){

        }else if(type.equals( "XD")){
            result = expiry;
        }else if(type.equals( "D")){
            int days  = Integer.parseInt(expiry);
            date.set(Calendar.DATE,days );
            result = sdf3.format(date.getTime());
        }
        else if(type.equals( "XM")){
            int days  = Integer.parseInt(expiry);
            date.set(Calendar.MONTH,days );
            result = sdf3.format(date.getTime());
        }
        else if(type.equals( "XM-YU" )|| type.equals( "XD-YU" )){
            String[] temp  = expiry.split("/");

        }

        return result;
    }


    /**
     * 解析项目的过期日期等
     * @param goodsExpiryValue
     * @return
     */
    public static void goodsExpiryValue(String goodsExpiryValue , GoodsBaseVo goods){
        if(StringUtils.isEmpty(goodsExpiryValue) || (StringUtils.isNotBlank(goodsExpiryValue)&& "NULL".equals(goodsExpiryValue))){
            goods.setExpiryValue("NULL");
            return ;
        }
        String[] str = goodsExpiryValue.split(":");
        if(str.length == 1){
            goods.setExpiryValue("NULL");
            return ;
        }
        String type = str[0];
        String expiry  = str[1];
        Calendar date  = Calendar.getInstance();
        String result = null;
        goods.setExpiryValue(type);
        if(type.equals( "NULL")){//不限
            goods.setExpiryValue("NULL");
        }else if(type.equals( "XD")){//售出后X天内使用
            goods.setDays(expiry);
        }else if(type.equals( "D")){//固定日期

            goods.setDate(expiry);
        }
        else if(type.equals( "XM")){//售出后X个月内使用
            goods.setUsedDay(expiry);
        }
        else if(type.equals( "XM-YU" )){//售出后X个月内激活，激活后Y个月内使用 ||售出后X天内激活，激活后Y个月内使用
            String[] temp  = expiry.split("/");
            goods.setSalesDate(temp[0].substring(0,temp[0].indexOf("M")+1));
            goods.setActiveDate(temp[0].substring(temp[0].indexOf("M")+1,temp[0].length() ));
            goods.setActiveDay(temp[1].substring(0,temp[1].indexOf("M")+1));
            goods.setUsedDay(temp[1].substring(temp[1].indexOf("M")+1,temp[1].length() ));
        }else if( type.equals( "XD-YU" )){
            String[] temp  = expiry.split("/");
            goods.setSalesDate(temp[0].substring(0,temp[0].indexOf("M")+1));
            goods.setDays(temp[0].substring(temp[0].indexOf("M")+1,temp[0].length() ));
            goods.setActiveDay(temp[1].substring(0,temp[1].indexOf("M")+1));
            goods.setUsedDay(temp[1].substring(temp[1].indexOf("M")+1,temp[1].length() ));
        }
        else if(type.equals( "XM-YD" )){//售出后X个月内激活，激活后Y个天内使用
            String[] temp  = expiry.split("/");
            goods.setSalesDate(temp[0].substring(0,temp[0].indexOf("M")+1));
            goods.setActiveDate(temp[0].substring(temp[0].indexOf("M")+1,temp[0].length() ));
            goods.setDays(temp[1]);
        }else if( type.equals( "XD-YD" )){//售出后X天内激活，激活后Y个天内使用
            String[] temp  = expiry.split("/");
            goods.setDays(temp[0]);
            goods.setDaysUse(temp[1]);
        }
    }

    /**
     * 解析项目的过期日期等
     * @param goodsExpiryDate
     * @return
     */
    public static void expiryDate(GoodsBaseVo vo ,String goodsExpiryDate){
        if(StringUtils.isEmpty(goodsExpiryDate) || (StringUtils.isNotBlank(goodsExpiryDate)&& "NULL".equals(goodsExpiryDate))){
            vo.setExpiryDate("NULL");
            return ;
        }
        String[] str = goodsExpiryDate.split(":");
        if(str.length == 1){
            vo.setExpiryDate("NULL");
            return ;
        }
        String type = str[0];
        String expiry  = str[1];
        vo.setExpiryDate(type);
        if(type.equals( "NULL")){
        }else if(type.equals( "XD")){
            vo.setDate(expiry);
        }else if(type.equals( "D")){
            vo.setDays(expiry);
        }
        else if(type.equals( "XM")){
            vo.setUsedDay(expiry);
        }
        else if(type.equals( "XM-YU" )|| type.equals( "XD-YU" )){
            String[] temp  = expiry.split("/");
            vo.setSalesDate(temp[0].substring(0,temp[0].indexOf("M")+1));
            vo.setActiveDate(temp[0].substring(temp[0].indexOf("M"),temp[0].length()+1 ));
            vo.setActiveDay(temp[1].substring(0,temp[1].indexOf("M")+1));
            vo.setUsedDay(temp[1].substring(temp[1].indexOf("M"),temp[0].length()+1 ));
        }

    }
    public static  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 解析项目的过期日期等
     * @param vo
     * @param activeDate
     * @param outDate
     * @return
     */
    public static String expiryGoodsDate(GoodsBaseVo vo, Date activeDate, Date outDate) throws ParseException {
        String goodsExpiryDate  = vo.getExpiryValue();
        if(StringUtils.isEmpty(goodsExpiryDate) || (StringUtils.isNotBlank(goodsExpiryDate)&& "NULL".equals(goodsExpiryDate))){
            vo.setExpiryDate("NULL");
            return  vo.getExpiryDate();
        }
        String[] str = goodsExpiryDate.split(":");
        if(str.length == 1){
            vo.setExpiryDate("NULL");
            return   vo.getExpiryDate();
        }
        String type = str[0];
        String expiry  = str[1];
        vo.setExpiryDate(type);
        Calendar c = Calendar.getInstance();

        if(type.equals( "NULL")){//不限
            return   vo.getExpiryDate();
        }else if(type.equals( "XD")){//售出后X天内使用
            c.setTime(outDate);
            c.add(Calendar.DAY_OF_MONTH,Integer.parseInt(expiry));
            String strDate = dateFormat.format(c.getTime());
            vo.setExpiryDate(strDate);
        }else if(type.equals( "D")){//固定日期
            c.setTime(DateUtil.parse(expiry,"yyyy-MM-dd"));
        }
        else if(type.equals( "XM")){
            //售出后X个月内使用
            c.setTime(outDate);
            c.add(Calendar.MONTH,Integer.parseInt(expiry));
//            String strDate = dateFormat.format(c.getTime());
//            vo.setExpiryDate(strDate);
        }
        else if(type.equals( "XM-YU" )|| type.equals( "XD-YU" )){
            //售出后X个月内激活，激活后Y个月内使用
            //售出后X天内激活，激活后Y个月内使用
            String[] temp  = expiry.split("/");
            String salesDate=temp[0].substring(0,temp[0].indexOf("M")+1);
            String activeDateStr =temp[0].substring(temp[0].indexOf("M"),temp[0].length() );
            String activeDay=temp[1].substring(0,temp[1].indexOf("M")+1);
            String usedDay=temp[1].substring(temp[1].indexOf("M"),temp[0].length() );
//            Calendar ac = Calendar.getInstance();
            c.setTime(activeDate);
            if(type.equals("XM-YU")){
                if(salesDate.equals("-M")){
                    c.add(Calendar.MONTH,1);
                    c.add(Calendar.DAY_OF_MONTH,1);
                    c.add(Calendar.MONTH,Integer.parseInt(activeDateStr));
                    //最终激活时间
                    vo.setActiveDateStr(dateFormat.format(c.getTime()));
                }
                if(activeDay.equals("-M")){
                    c.add(Calendar.MONTH,1);
                    c.add(Calendar.DAY_OF_MONTH,1);
                    c.add(Calendar.MONTH,Integer.parseInt(usedDay));
                }
            }
            if(type.equals("XD-YU")){
                if(salesDate.equals("-M")){
                    c.add(Calendar.MONTH,1);
                    c.add(Calendar.DAY_OF_MONTH,1);
                    c.add(Calendar.DAY_OF_MONTH,Integer.parseInt(activeDateStr));
                    //最终激活时间
//                    String strDate = dateFormat.format(c.getTime());
                    vo.setActiveDateStr(dateFormat.format(c.getTime()));
                }
                if(activeDay.equals("-M")){
                    c.add(Calendar.MONTH,1);
                    c.add(Calendar.MONTH,Integer.parseInt(usedDay));
                }
            }

        }   else if(type.equals( "XM-YD" )){//售出后X个月内激活，激活后Y个天内使用
            String[] temp  = expiry.split("/");
            String salesDate =temp[0].substring(0,temp[0].indexOf("M")+1);
            String activeDateTemp= temp[0].substring(temp[0].indexOf("M")+1,temp[0].length() );
            String  DaysUse= temp[1];
            if(salesDate.equals("-M")){
                c.add(Calendar.MONTH,1);
                c.add(Calendar.DAY_OF_MONTH,1);
                c.add(Calendar.DAY_OF_MONTH,Integer.parseInt(activeDateTemp));
                //最终激活时间
//                    String strDate = dateFormat.format(c.getTime());
                vo.setActiveDateStr(dateFormat.format(c.getTime()));
            }
        }else if( type.equals( "XD-YD" )){//售出后X天内激活，激活后Y个天内使用
            String[] temp  = expiry.split("/");
            String salesDate = temp[0];
            String DaysUse =  temp[1];
            c.add(Calendar.DAY_OF_MONTH,Integer.parseInt(DaysUse));
            c.add(Calendar.DAY_OF_MONTH,Integer.parseInt(salesDate));
        }
        String strDate = dateFormat.format(c.getTime());
        vo.setExpiryDate(strDate);
        return   vo.getExpiryDate();
    }

    /**
     * 解析项目的过期日期等
     * @param vo
     * @param activeDate
     * @param outDate
     * @return
     */
    public static String expiryGiftCodeDate(GoodsBaseVo vo, Date activeDate, Date outDate) throws ParseException {
        String goodsExpiryDate  = vo.getExpiryValue();
        if(StringUtils.isEmpty(goodsExpiryDate) || (StringUtils.isNotBlank(goodsExpiryDate)&& "NULL".equals(goodsExpiryDate))){
            vo.setExpiryDate("NULL");
            return  vo.getExpiryDate();
        }
        String[] str = goodsExpiryDate.split(":");
        if(str.length == 1){
            vo.setExpiryDate("NULL");
            return   vo.getExpiryDate();
        }
        String type = str[0];
        String expiry  = str[1];
        vo.setExpiryDate(type);
        Calendar c = Calendar.getInstance();

        if(type.equals( "NULL")){//不限
            return   vo.getExpiryDate();
        }else if(type.equals( "XD")){//售出后X天内使用
            c.setTime(outDate);
            c.add(Calendar.DAY_OF_MONTH,Integer.parseInt(expiry));
            String strDate = dateFormat.format(c.getTime());
            vo.setExpiryDate(strDate);
        }else if(type.equals( "D")){//固定日期
            c.setTime(DateUtil.parse(expiry,"yyyy-MM-dd"));
        }
        else if(type.equals( "XM")){
            //售出后X个月内使用
            c.setTime(outDate);
            c.add(Calendar.MONTH,Integer.parseInt(expiry));
//            String strDate = dateFormat.format(c.getTime());
//            vo.setExpiryDate(strDate);
        }
        else if(type.equals( "XM-YU" )|| type.equals( "XD-YU" )){
            //售出后X个月内激活，激活后Y个月内使用
            //售出后X天内激活，激活后Y个月内使用
            String[] temp  = expiry.split("/");
            String salesDate=temp[0].substring(0,temp[0].indexOf("M")+1);
            String activeDateStr =temp[0].substring(temp[0].indexOf("M"),temp[0].length() );
            String activeDay=temp[1].substring(0,temp[1].indexOf("M")+1);
            String usedDay=temp[1].substring(temp[1].indexOf("M"),temp[0].length() );
//            Calendar ac = Calendar.getInstance();

            c.setTime(activeDate);

            if(type.equals("XM-YU")){
                if(salesDate.equals("-M")){
                    c.add(Calendar.MONTH,1);
                    c.add(Calendar.DAY_OF_MONTH,1);
                    c.add(Calendar.MONTH,Integer.parseInt(activeDateStr));
                    //最终激活时间
                    vo.setActiveDateStr(dateFormat.format(c.getTime()));
                }
                if(activeDay.equals("-M")){
                    c.add(Calendar.MONTH,1);
                    c.add(Calendar.DAY_OF_MONTH,1);
                    c.add(Calendar.MONTH,Integer.parseInt(usedDay));
                }
            }
            if(type.equals("XD-YU")){
                if(salesDate.equals("-M")){
                    c.add(Calendar.MONTH,1);
                    c.add(Calendar.DAY_OF_MONTH,1);
                    c.add(Calendar.DAY_OF_MONTH,Integer.parseInt(activeDateStr));
                    //最终激活时间
//                    String strDate = dateFormat.format(c.getTime());
                    vo.setActiveDateStr(dateFormat.format(c.getTime()));
                }
                if(activeDay.equals("-M")){
                    c.add(Calendar.MONTH,1);
                    c.add(Calendar.MONTH,Integer.parseInt(usedDay));
                }
            }

        }   else if(type.equals( "XM-YD" )){//售出后X个月内激活，激活后Y个天内使用
            String[] temp  = expiry.split("/");
            String salesDate =temp[0].substring(0,temp[0].indexOf("M")+1);
            String activeDateTemp= temp[0].substring(temp[0].indexOf("M")+1,temp[0].length() );
            String  DaysUse= temp[1];
            if(salesDate.equals("-M")){
                c.add(Calendar.MONTH,1);
                c.add(Calendar.DAY_OF_MONTH,1);
                c.add(Calendar.DAY_OF_MONTH,Integer.parseInt(activeDateTemp));
                //最终激活时间
//                    String strDate = dateFormat.format(c.getTime());
                vo.setActiveDateStr(dateFormat.format(c.getTime()));
            }
        }else if( type.equals( "XD-YD" )){//售出后X天内激活，激活后Y个天内使用
            String[] temp  = expiry.split("/");
            String salesDate = temp[0];
            String DaysUse =  temp[1];
            c.add(Calendar.DAY_OF_MONTH,Integer.parseInt(DaysUse));
            c.add(Calendar.DAY_OF_MONTH,Integer.parseInt(salesDate));
        }
        String strDate = dateFormat.format(c.getTime());
        vo.setExpiryDate(strDate);
        return   vo.getExpiryDate();
    }

    /**
     * 解析商品激活码的权益到期时间和激活截止时间
     * @param vo
     * @param activeDate
     * @param outDate
     * @return
     * @throws Exception
     */
    public static GoodsBaseVo analyCodeDate(GoodsBaseVo vo, Date activeDate, Date outDate)throws Exception{
        //获取到期规则
        String dateRule = vo.getExpiryValue();
        //不限
        if (StringUtils.isBlank(dateRule) || "NULL".equals(dateRule)){
            vo.setExpiryDate("NULL");//权益到期时间为NULL
            vo.setActiveDate("NULL");//激活截止时间为NULL
        }else {
            if (dateRule.contains(":")){
                String[] arr = dateRule.split(":");
                String type = arr[0];
                String expiry = arr[1];
                Calendar calendar = Calendar.getInstance();
                //固定日期
                if ("D".equals(type)){
                    vo.setExpiryDate(expiry);
                    vo.setActiveDate(expiry);
                }
                //售出后X个月内使用
                if ("XM".equals(type)){
                    calendar.setTime(outDate);
                    calendar.add(Calendar.MONTH,Integer.valueOf(expiry));
                    vo.setExpiryDate(DateUtil.format(calendar.getTime(),"yyyy-MM-dd"));
                    vo.setActiveDate(DateUtil.format(calendar.getTime(),"yyyy-MM-dd"));
                }
                //售出后Y天内使用
                if ("XD".equals(type)){
                    calendar.setTime(outDate);
                    calendar.add(Calendar.DAY_OF_MONTH,Integer.valueOf(expiry));
                    vo.setExpiryDate(DateUtil.format(calendar.getTime(),"yyyy-MM-dd"));
                    vo.setActiveDate(DateUtil.format(calendar.getTime(),"yyyy-MM-dd"));
                }
                //售出后X个月内激活，激活后Y个月使用
                if ("XM-YU".equals(type)){
                    String[] str = expiry.split("/");
                    String type1 = str[0].substring(0,str[0].indexOf("M")+1);
                    String month1 = str[0].substring(str[0].indexOf("M")+1);
                    String type2 = str[1].substring(0,str[1].indexOf("M")+1);
                    String month2 = str[1].substring(str[1].indexOf("M")+1);
                    //售出当月起
                    if ("+M".equals(type1)){
                        calendar.setTime(outDate);
                        calendar.add(Calendar.MONTH,Integer.valueOf(month1));
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH)+1;
                        String time = year+"-"+month+"-1";
                        calendar.setTime(DateUtil.parse(time,"yyyy-MM-dd"));
                        calendar.add(Calendar.DAY_OF_MONTH,-1);
                        vo.setActiveDate(DateUtil.format(calendar.getTime(),"yyyy-MM-dd"));
                    }
                    //售出当天起
                    if ("=M".equals(type1)){
                        calendar.setTime(outDate);
                        calendar.add(Calendar.MONTH,Integer.valueOf(month1));
                        vo.setActiveDate(DateUtil.format(calendar.getTime(),"yyyy-MM-dd"));
                    }
                    //售出次月起
                    if ("-M".equals(type1)){
                        calendar.setTime(outDate);
                        calendar.add(Calendar.MONTH,Integer.valueOf(month1)+1);
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH)+1;
                        String time = year+"-"+month+"-1";
                        calendar.setTime(DateUtil.parse(time,"yyyy-MM-dd"));
                        calendar.add(Calendar.DAY_OF_MONTH,-1);
                        vo.setActiveDate(DateUtil.format(calendar.getTime(),"yyyy-MM-dd"));
                    }
                    //激活时间不为空
                    if (activeDate != null){
                        //激活当月起
                        if ("+M".equals(type2)){
                            calendar.setTime(activeDate);
                            calendar.add(Calendar.MONTH,Integer.valueOf(month2));
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH)+1;
                            String time = year+"-"+month+"-1";
                            calendar.setTime(DateUtil.parse(time,"yyyy-MM-dd"));
                            calendar.add(Calendar.DAY_OF_MONTH,-1);
                            vo.setExpiryDate(DateUtil.format(calendar.getTime(),"yyyy-MM-dd"));
                        }
                        //激活当天起
                        if ("=M".equals(type2)){
                            calendar.setTime(activeDate);
                            calendar.add(Calendar.MONTH,Integer.valueOf(month2));
                            vo.setExpiryDate(DateUtil.format(calendar.getTime(),"yyyy-MM-dd"));
                        }
                        //激活次月起
                        if ("-M".equals(type2)){
                            calendar.setTime(activeDate);
                            calendar.add(Calendar.MONTH,Integer.valueOf(month2)+1);
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH)+1;
                            String time = year+"-"+month+"-1";
                            calendar.setTime(DateUtil.parse(time,"yyyy-MM-dd"));
                            calendar.add(Calendar.DAY_OF_MONTH,-1);
                            vo.setExpiryDate(DateUtil.format(calendar.getTime(),"yyyy-MM-dd"));
                        }
                    }
                }
                //售出后X个月内激活，激活后Y天内使用
                if ("XM-YD".equals(type)){
                    String[] str = expiry.split("/");
                    String type1 = str[0].substring(0,str[0].indexOf("M")+1);
                    String month1 = str[0].substring(str[0].indexOf("M")+1);
                    String day = str[1];
                    //售出当月起
                    if ("+M".equals(type1)){
                        calendar.setTime(outDate);
                        calendar.add(Calendar.MONTH,Integer.valueOf(month1));
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH)+1;
                        String time = year+"-"+month+"-1";
                        calendar.setTime(DateUtil.parse(time,"yyyy-MM-dd"));
                        calendar.add(Calendar.DAY_OF_MONTH,-1);
                        vo.setActiveDate(DateUtil.format(calendar.getTime(),"yyyy-MM-dd"));
                    }
                    //售出当天起
                    if ("=M".equals(type1)){
                        calendar.setTime(outDate);
                        calendar.add(Calendar.MONTH,Integer.valueOf(month1));
                        vo.setActiveDate(DateUtil.format(calendar.getTime(),"yyyy-MM-dd"));
                    }
                    //售出次月起
                    if ("-M".equals(type1)){
                        calendar.setTime(outDate);
                        calendar.add(Calendar.MONTH,Integer.valueOf(month1)+1);
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH)+1;
                        String time = year+"-"+month+"-1";
                        calendar.setTime(DateUtil.parse(time,"yyyy-MM-dd"));
                        calendar.add(Calendar.DAY_OF_MONTH,-1);
                        vo.setActiveDate(DateUtil.format(calendar.getTime(),"yyyy-MM-dd"));
                    }
                    //激活时间不为空
                    if (activeDate != null){
                        calendar.setTime(activeDate);
                        calendar.add(Calendar.DAY_OF_MONTH,Integer.valueOf(day));
                        vo.setExpiryDate(DateUtil.format(calendar.getTime(),"yyyy-MM-dd"));
                    }
                }
                //售出后X天内激活，激活后Y个月内使用
                if ("XD-YU".equals(type)){
                    String[] str = expiry.split("/");
                    String day = str[0];
                    String type2 = str[1].substring(0,str[1].indexOf("M")+1);
                    String month2 = str[1].substring(str[1].indexOf("M")+1);
                    calendar.setTime(outDate);
                    calendar.add(Calendar.DAY_OF_MONTH,Integer.valueOf(day));
                    vo.setActiveDate(DateUtil.format(calendar.getTime(),"yyyy-MM-dd"));
                    if (activeDate != null){
                        //激活当月起
                        if ("+M".equals(type2)){
                            calendar.setTime(activeDate);
                            calendar.add(Calendar.MONTH,Integer.valueOf(month2));
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH)+1;
                            String time = year+"-"+month+"-1";
                            calendar.setTime(DateUtil.parse(time,"yyyy-MM-dd"));
                            calendar.add(Calendar.DAY_OF_MONTH,-1);
                            vo.setExpiryDate(DateUtil.format(calendar.getTime(),"yyyy-MM-dd"));
                        }
                        //激活当天起
                        if ("=M".equals(type2)){
                            calendar.setTime(activeDate);
                            calendar.add(Calendar.MONTH,Integer.valueOf(month2));
                            vo.setExpiryDate(DateUtil.format(calendar.getTime(),"yyyy-MM-dd"));
                        }
                        //激活次月起
                        if ("-M".equals(type2)){
                            calendar.setTime(activeDate);
                            calendar.add(Calendar.MONTH,Integer.valueOf(month2)+1);
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH)+1;
                            String time = year+"-"+month+"-1";
                            calendar.setTime(DateUtil.parse(time,"yyyy-MM-dd"));
                            calendar.add(Calendar.DAY_OF_MONTH,-1);
                            vo.setExpiryDate(DateUtil.format(calendar.getTime(),"yyyy-MM-dd"));
                        }
                    }
                }
                //售出后X天内激活，激活后Y天内使用
                if ("XD-YD".equals(type)){
                    String[] str = expiry.split("/");
                    String day1 = str[0];
                    String day2 = str[1];
                    calendar.setTime(outDate);
                    calendar.add(Calendar.DAY_OF_MONTH,Integer.valueOf(day1));
                    vo.setActiveDate(DateUtil.format(calendar.getTime(),"yyyy-MM-dd"));
                    if (activeDate != null){
                        calendar.setTime(activeDate);
                        calendar.add(Calendar.DAY_OF_MONTH,Integer.valueOf(day2));
                        vo.setExpiryDate(DateUtil.format(calendar.getTime(),"yyyy-MM-dd"));
                    }
                }
            }
        }
        return vo;
    }

    /**
     * 翻译项目的权益到期规则
     * @param expiryValue
     * @return
     * @throws Exception
     */
    public static String translateExpiryValue(String expiryValue)throws Exception{
        //获取到期规则
        String dateRule = expiryValue;
        StringBuffer stringBuffer = new StringBuffer();
        //不限
        if (StringUtils.isBlank(dateRule) || "NULL".equals(dateRule)){
            stringBuffer.append("永久有效");
        }else {
            if (dateRule.contains(":")){
                String[] arr = dateRule.split(":");
                String type = arr[0];
                String expiry = arr[1];
                Calendar calendar = Calendar.getInstance();
                //固定日期
                if ("D".equals(type)){
                    stringBuffer.append("购买之日起至"+expiry);
                }
                //售出后X个月内使用
                if ("XM".equals(type)){
                    stringBuffer.append("购买之日起"+expiry+"个月内有效");
                }
                //售出后Y天内使用
                if ("XD".equals(type)){
                    stringBuffer.append("购买之日起"+expiry+"天内有效");
                }
                //售出后X个月内激活，激活后Y个月使用
                if ("XM-YU".equals(type)){
                    String[] str = expiry.split("/");
                    String type1 = str[0].substring(0,str[0].indexOf("M")+1);
                    String month1 = str[0].substring(str[0].indexOf("M")+1);
                    String type2 = str[1].substring(0,str[1].indexOf("M")+1);
                    String month2 = str[1].substring(str[1].indexOf("M")+1);
                    //售出当月起
                    if ("+M".equals(type1)){
                        stringBuffer.append("购买当月起"+month1+"个月内激活,");
                    }
                    //售出当天起
                    if ("=M".equals(type1)){
                        stringBuffer.append("购买当天起"+month1+"个月内激活,");
                    }
                    //售出次月起
                    if ("-M".equals(type1)){
                        stringBuffer.append("购买次月起"+month1+"个月内激活,");
                    }
                    //激活当月起
                    if ("+M".equals(type2)){
                        stringBuffer.append("激活当月起"+month2+"个月内有效");
                    }
                    //激活当天起
                    if ("=M".equals(type2)){
                        stringBuffer.append("激活当天起"+month2+"个月内有效");
                    }
                    //激活次月起
                    if ("-M".equals(type2)){
                        stringBuffer.append("激活次月起"+month2+"个月内有效");
                    }
                }
                //售出后X个月内激活，激活后Y天内使用
                if ("XM-YD".equals(type)){
                    String[] str = expiry.split("/");
                    String type1 = str[0].substring(0,str[0].indexOf("M")+1);
                    String month1 = str[0].substring(str[0].indexOf("M")+1);
                    String day = str[1];
                    //售出当月起
                    if ("+M".equals(type1)){
                        stringBuffer.append("购买当月起"+month1+"个月内激活,");
                    }
                    //售出当天起
                    if ("=M".equals(type1)){
                        stringBuffer.append("购买当天起"+month1+"个月内激活,");
                    }
                    //售出次月起
                    if ("-M".equals(type1)){
                        stringBuffer.append("购买次月起"+month1+"个月内激活,");
                    }
                    stringBuffer.append("激活后"+day+"天内有效");
                }
                //售出后X天内激活，激活后Y个月内使用
                if ("XD-YU".equals(type)){
                    String[] str = expiry.split("/");
                    String day = str[0];
                    String type2 = str[1].substring(0,str[1].indexOf("M")+1);
                    String month2 = str[1].substring(str[1].indexOf("M")+1);
                    stringBuffer.append("购买之日起"+day+"天内激活,");
                    //激活当月起
                    if ("+M".equals(type2)){
                        stringBuffer.append("激活当月起"+month2+"个月内有效");
                    }
                    //激活当天起
                    if ("=M".equals(type2)){
                        stringBuffer.append("激活当天起"+month2+"个月内有效");
                    }
                    //激活次月起
                    if ("-M".equals(type2)){
                        stringBuffer.append("激活次月起"+month2+"个月内有效");
                    }
                }
                //售出后X天内激活，激活后Y天内使用
                if ("XD-YD".equals(type)){
                    String[] str = expiry.split("/");
                    String day1 = str[0];
                    String day2 = str[1];
                    stringBuffer.append("购买之日起"+day1+"天内激活,");
                    stringBuffer.append("激活后"+day2+"个月内有效");
                }
            }
        }
        return stringBuffer.toString();
    }
}


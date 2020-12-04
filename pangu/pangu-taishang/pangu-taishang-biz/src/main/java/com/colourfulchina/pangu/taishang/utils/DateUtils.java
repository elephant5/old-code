package com.colourfulchina.pangu.taishang.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * 时间工具类
 */
public class DateUtils {
    //获取星期的dsf
    final public static SimpleDateFormat weekSDF = new SimpleDateFormat("EEEE");

    //获取星期的dsf
    //final public static SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");


    /**
     * 根据时间获取星期
     * @param date
     * @return
     */
    public static String getWeek(Date date){
        return weekSDF.format(date);
    }

    /**
     * 判读日期是否在时间段内
     * @param time
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean belongCalendar(Date time,Date beginTime,Date endTime){
        Calendar date = Calendar.getInstance();
        date.setTime(time);
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        if (date.after(begin) && date.before(end)){
            return true;
        }else if (time.compareTo(beginTime) == 0 || time.compareTo(endTime) == 0){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 获取时间段之间的所有时间，剔除传入的时间exceptDates
     * @param startDate
     * @param endDate
     * @param exceptDates
     * @return
     */
    public static List<Date> containDateList(Date startDate,Date endDate,HashSet<Date> exceptDates){
        startDate = DateUtil.parse(DateUtil.format(startDate, DatePattern.NORM_DATE_PATTERN),DatePattern.NORM_DATE_PATTERN);
        endDate = DateUtil.parse(DateUtil.format(endDate,DatePattern.NORM_DATE_PATTERN),DatePattern.NORM_DATE_PATTERN);
        List<Date> result = Lists.newLinkedList();
        result.add(DateUtil.date(startDate));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        while (endDate.after(calendar.getTime())){
            calendar.add(Calendar.DAY_OF_MONTH,1);
            result.add(DateUtil.date(calendar.getTime()));
        }
        if (!CollectionUtils.isEmpty(exceptDates)){
            result.removeAll(exceptDates);
        }
        return result;
    }

    /**
     * 获取时间段中具体星期几的日期
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param weeks 星期，格式为（星期日 1  星期一 2  星期二 3   星期三 4 星期四 5  星期五 6 星期六 7），多个星期直接拼接，如星期一星期二（23）
     * @return
     */
    public static List<Date> containDateByWeeks(Date startDate,Date endDate,String weeks){
        startDate = DateUtil.parse(DateUtil.format(startDate,"yyyy-MM-dd"),"yyyy-MM-dd");
        endDate = DateUtil.parse(DateUtil.format(endDate,"yyyy-MM-dd"),"yyyy-MM-dd");
        List<Date> result = Lists.newLinkedList();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        while (true){
            calendar.add(Calendar.DAY_OF_MONTH,1);
            if (calendar.getTime().compareTo(endDate) <= 0){
                //查询的某一时间的星期系数
                Integer weekDay = dateForWeek(calendar.getTime());
                //判断当期日期的星期系数是否是需要查询的
                if (weeks.indexOf(weekDay.toString()) != -1) {
                    result.add(calendar.getTime());
                }
            }else {
                break;
            }
        }
        return result;
    }

    /**
     * 得到当期时间的周系数。星期日：1，星期一：2，星期二：3，星期三：4，星期四：5，星期五：6，星期六：7
     * @param date
     * @return
     */
    public static Integer dateForWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取某年某月的第几个星期几
     * @param year
     * @param month
     * @param weekOfmonth
     * @param dayofweek (0-星期日，1-星期一，2-星期二，3-星期三，4-星期四，5星期五，6-星期六)
     * @return
     */
    public static Date getYMNumWeekDate(int year,int month,int weekOfmonth,int dayofweek) {
        Calendar c = Calendar.getInstance();
        //得到当年当月1号的日子
        c.set(year, month-1, 1);
        //得出当年当月1号是星期几（日，一，二，三，四，五，六）
        // 周日是1
        int weekday = c.get(Calendar.DAY_OF_WEEK);
        //计算该月总天数
        int countDay = 0;
        if(weekday==1) {
            countDay = (weekOfmonth-1)*7 + dayofweek+1;
        }
        else {
            countDay = (weekOfmonth-1)*7 + 7-weekday+1 + dayofweek+1;
        }
        c.set(Calendar.DAY_OF_MONTH, countDay);
        Date date = c.getTime();
        return DateUtil.parse(DateUtil.format(date,"yyyy/MM/dd"),"yyyy/MM/dd");
    }
}

package com.colourfulchina.pangu.taishang.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Date toDate(String dateStr){
        return toDate(dateStr, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date toDate(String dateStr, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date parse = null;
        try {
            parse = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }

    public static String toString(Date date){
        return toString(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String toString(Date date, String format){
        if(date == null){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String toString(LocalDateTime localDateTime){
        return toString(localDateTime, "yyyy-MM-dd HH:mm:ss");
    }

    public static String toString(LocalDateTime localDateTime, String format){
        if(localDateTime == null){
            return null;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return dateTimeFormatter.format(localDateTime);
    }

}

package com.colourfulchina.mars.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

/**
 * 存放其它公共方法
 */
@Component
public class HelpUtils {
    private static final Logger logger = LoggerFactory.getLogger(HelpUtils.class);
    private static final Map<String,String> mask=Maps.newHashMap();
    public final static String numReg="^[1-9]\\d*$";//整数正则
    public final static String dateReg="^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";

    static {
        mask.put("a","z");
        mask.put("A","Z");
        mask.put("b","y");
        mask.put("B","Y");
        mask.put("c","x");
        mask.put("C","X");
        mask.put("d","w");
        mask.put("D","W");
        mask.put("e","v");
        mask.put("E","V");
        mask.put("f","u");
        mask.put("F","U");
        mask.put("g","t");
        mask.put("G","T");
        mask.put("h","s");
        mask.put("H","S");
        mask.put("i","r");
        mask.put("I","R");
        mask.put("j","q");
        mask.put("J","Q");
        mask.put("k","p");
        mask.put("K","P");
        mask.put("l","o");
        mask.put("L","O");
        mask.put("m","n");
        mask.put("M","N");
        mask.put("n","m");
        mask.put("N","M");
        mask.put("o","l");
        mask.put("O","L");
        mask.put("p","k");
        mask.put("P","K");
        mask.put("q","j");
        mask.put("Q","J");
        mask.put("r","i");
        mask.put("R","I");
        mask.put("s","h");
        mask.put("S","H");
        mask.put("t","g");
        mask.put("T","G");
        mask.put("u","f");
        mask.put("U","F");
        mask.put("v","e");
        mask.put("V","E");
        mask.put("w","d");
        mask.put("W","D");
        mask.put("x","c");
        mask.put("X","C");
        mask.put("y","b");
        mask.put("Y","B");
        mask.put("z","a");
        mask.put("Z","A");
        mask.put("0","9");
        mask.put("1","8");
        mask.put("2","7");
        mask.put("3","6");
        mask.put("4","5");
        mask.put("5","4");
        mask.put("6","3");
        mask.put("7","2");
        mask.put("8","1");
        mask.put("9","0");
    }


    private static ConcurrentHashMap<String,BeanCopier> cache=new ConcurrentHashMap<String, BeanCopier>();

    /**
     * 正则：手机号（精确）
     * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188、198</p>
     * <p>联通：130、131、132、145、155、156、175、176、185、186、166</p>
     * <p>电信：133、153、173、177、180、181、189、199</p>
     * <p>全球星：1349</p>
     * <p>虚拟运营商：170</p>
     */
    public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";


    /**
     * 获取0~maxValue之间的一个随机整数
     * @param maxValue 生成的随机数最大值
     * @return 返回生成的随机数
     */
    public static Integer getRandomInt(int maxValue){
        Random random = new Random();
        return Math.abs(random.nextInt())%maxValue;
    }
    
    /**
     * @Title 取随机数
     * @param  生成的随机数最大值
     * @return 返回生成的随机数
     */
    public static Integer getRandomNum(int length){
        Random random = new Random();
        //生成一个1-9的随机数 开头
        String randStr = (random.nextInt(9)+1)+"";
        //循环5次，生成一个5位随机数0-9
        for (int i = 0; i < length-1; i++) {
            // 产生一个0-9的随机数
            randStr += random.nextInt(10);
        }
        return Integer.parseInt(randStr);
    }

    /**
     * 根据文件名，判断是否是excel
     * @param filePath 文档完整路径
     * @return 是否是excel文档
     */
    public static boolean validateExcel(String filePath){
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))){
            return false;
        }
        return true;
    }

    /**
     * 是否是2003的excel，返回true是2003
     * @param filePath 文件路径
     * @return 是否是excel 2003
     */
    public static boolean isExcel2003(String filePath)  {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    /**
     * 是否是2007的excel，返回true是2007
     * @param filePath 文件路径
     * @return 是否是excel 2007
     */
    public static boolean isExcel2007(String filePath)  {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }



    /**
     * 从请求中获得ip
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 对象转json
     * @param obj
     * @return
     */
    public static String objectToJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        // Convert object to JSON string
        String jsonStr = "";
        try {
            jsonStr = mapper.writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    /**
     * 日期转string
     * @param dateDate
     * @param pattern
     * @return
     */
    public static String dateToStr(Date dateDate, String pattern){
        try{
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            return formatter.format(dateDate);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日期转string
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Date strToDate(String dateStr, String pattern){
        try{
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            return formatter.parse(dateStr);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对象转换
     * @param sourceObj
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T beanConvert(Object sourceObj, T target){
        return copyBeanProperties(sourceObj, target, false);
    }

    /**
     * list对象的相互转换
     * @param sourceObjs
     * @param targets
     * @param targetType
     * @param <T>
     * @return
     */
    public static <T> List<T> beansConvert(List<?> sourceObjs, List<T> targets, Class<T> targetType){
        return copyListBeanPropertiesToList(sourceObjs, targets, targetType);
    }


    private static <T> List<T> copyListBeanPropertiesToList(List<?> sourceObjs, List<T> targets, Class<T> targetType){
        if(sourceObjs==null||targets==null||targetType==null) {
            return null;
        }
        T t;
        for(Object o:sourceObjs){
            try {
                t=targetType.newInstance();
                targets.add(copyBeanProperties(o,t,false));
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        return targets;
    }
    /**
     *
     *
     * @param sourceObj  源对象
     * @param target  目标对象
     * @param useConverter
     * @return
     * @throws Exception
     */
    private static <T> T copyBeanProperties(Object sourceObj, T target, boolean useConverter) {
        if(sourceObj==null||target==null) {
            return null;
        }
        String key=sourceObj.getClass().getSimpleName()+target.getClass().getSimpleName();
        BeanCopier copier = cache.get(key);
        if(copier==null){
            copier=createBeanCopier(sourceObj.getClass(), target.getClass(), useConverter, key);
        }
        copier.copy(sourceObj, target, null);
        return target;
    }


    @SuppressWarnings({"rawtypes" })
    private static BeanCopier createBeanCopier(Class sourceClass,Class targetClass,boolean useConverter,String cacheKey){
        BeanCopier copier = BeanCopier.create(sourceClass,targetClass, useConverter);
        cache.putIfAbsent(cacheKey, copier);
        return copier;
    }

    /**
     * 从cookie中获取数据
     * @param request
     * @param name
     * @return
     */
    public static Cookie getCookieByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = ReadCookieMap(request);
        if (cookieMap.containsKey(name)) {
            Cookie cookie = (Cookie) cookieMap.get(name);
            return cookie;
        } else {
            return null;
        }
    }

    /**
     * 将cookie封装到Map里面
     *
     * @param request
     * @return
     */
    private static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }



    /**
     * 验证手机号（精确）
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMobileExact(CharSequence input) {
        return isMatch(REGEX_MOBILE_EXACT, input);
    }

    private static boolean isMatch(String regex, CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }


    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     *
     * @param beginDate
     * @param endDate
     * @return List
     */
    public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate,boolean isAddEndDate) {
        List<Date> lDate = new ArrayList<Date>();
        lDate.add(beginDate);// 把开始时间加入集合
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {
                break;
            }
        }
        if(isAddEndDate){
            lDate.add(endDate);// 把结束时间加入集合
        }
        return lDate;
    }

    /*
    * 判断字符串是否是指定格式
    * */
    public static boolean validStrFormat(String str,String reg){
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
    
}

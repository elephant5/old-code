package com.colourfulchina.mars.code;

import com.colourfulchina.mars.constants.GiftCodeConstants;
import com.colourfulchina.mars.utils.JedisPoolInstance;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 生成卡券平台券码
 */
public class CreateCouponCode {

    public static final String FORMAT_Hour = "yyMMddHH";


//    public static void main(String[] args) {
//        createGiftCpnCode("VOSP");
//    }

    /**
     *
     * @param projectCode 项目编码 4位
     */
    public static String getCpnCode(String projectCode){
        StringBuffer couponCode = new StringBuffer(projectCode);
        //去除B I O U
        char[] array = {'A','C','D','E','F','G','H','J',
                'K','L','M','N','P','Q','R','S',
                'T','V','W','X','Y','Z'
        };
        Map<Integer,String> map = new HashMap<Integer, String>();
        for (int i = 0; i < array.length; i++) {
            map.put(i+10,String.valueOf(array[i]));
        }

        Calendar now = Calendar.getInstance();
        //年 后两位
        Integer yearLast = Integer.valueOf(new SimpleDateFormat("yy", Locale.CHINESE).format(now.getTime()));
        //月
        Integer month = now.get(Calendar.MONTH) + 1;
        //日
        Integer day =  now.get(Calendar.DAY_OF_MONTH);
        //时
        Integer hour = now.get(Calendar.HOUR_OF_DAY);

        System.out.println(yearLast+" " + month +" "+ day +" "+hour);

        //生成4位年月日时编码 20年2月6号18时
        StringBuffer timeCode = new StringBuffer() ;
        if(!StringUtils.isEmpty(map.get(yearLast))){
           timeCode =  timeCode.append(map.get(yearLast));
        }else {
            timeCode = timeCode.append(yearLast);
        }
        if(!StringUtils.isEmpty(map.get(month))){
            timeCode =  timeCode.append(map.get(month));
        }else {
            timeCode = timeCode.append(month);
        }
        if(!StringUtils.isEmpty(map.get(day))){
            timeCode =  timeCode.append(map.get(day));
        }else {
            timeCode = timeCode.append(day);
        }
        if(!StringUtils.isEmpty(map.get(hour))){
            timeCode =  timeCode.append(map.get(hour));
        }else {
            timeCode = timeCode.append(hour);
        }
        System.out.println(timeCode.toString());
        couponCode.append(timeCode);
        //四位随机数字
        couponCode.append((int) ((Math.random() * 9 + 1) * 1000));
        System.out.println(couponCode.toString());
        return couponCode.toString();
    }


    public static void createGiftCpnCode(String projectCode){
        Jedis jedis = JedisPoolInstance.getJedisPoolInstance().getResource();
        Pipeline pipelined = jedis.pipelined();
        System.out.println("开始执行");
        long start = System.currentTimeMillis();
        try {

            for(int i =1 ;i < 200000;i++){
                pipelined.sadd(GiftCodeConstants.GIFT_CPN_CODE,getCpnCode(projectCode));
            }
            pipelined.sync();
            System.out.println(System.currentTimeMillis()-start);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != jedis){
                jedis.close();
            }
        }
    }


}

package com.colourfulchina.mars.code;

import com.colourfulchina.mars.constants.GiftCodeConstants;
import com.colourfulchina.mars.utils.JedisPoolInstance;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

/**
 * 生成权益激活码
 */
public class CreateActCode {


//    public static void main(String[] args) {
//        createGiftActCode();
//    }

    public static void createGiftActCode(){
        Jedis jedis = JedisPoolInstance.getJedisPoolInstance().getResource();
        Pipeline pipelined = jedis.pipelined();
        System.out.println("开始执行"); //15126 15078
        long start = System.currentTimeMillis();
        try {

            for(int i =1 ;i < 200000;i++){
                StringBuffer times = new StringBuffer(String.valueOf(System.currentTimeMillis()).substring(1,6));
//                jedis.sadd("giftCode",times.append((int)((Math.random()*9+1)*1000000)).toString());
                //权益激活码: 时间戳2-6位 + 7 位随机数
                pipelined.sadd(GiftCodeConstants.GIFT_ACT_CODE,times.append((int)((Math.random()*9+1)*1000000)).toString());
                pipelined.expire(GiftCodeConstants.GIFT_ACT_CODE,15);//
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

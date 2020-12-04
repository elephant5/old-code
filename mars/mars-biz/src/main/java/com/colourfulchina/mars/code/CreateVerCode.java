package com.colourfulchina.mars.code;

import com.colourfulchina.mars.constants.GiftCodeConstants;
import com.colourfulchina.mars.utils.JedisPoolInstance;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

/**
 * 生成权益核销码
 */
public class CreateVerCode {


//    public static void main(String[] args) {
//
//        createGiftVerCode();
//    }


    public static void createGiftVerCode(){
        Jedis jedis = JedisPoolInstance.getJedisPoolInstance().getResource();
        Pipeline pipelined = jedis.pipelined();
        System.out.println("开始执行"); //15126 15078
        long start = System.currentTimeMillis();
        try {

            for(int i =1 ;i < 200000;i++){
                StringBuffer times = new StringBuffer(String.valueOf(System.currentTimeMillis()).substring(1,6));
                //权益核销码: 时间戳2-6位 + 5 位随机数
                pipelined.sadd(GiftCodeConstants.GIFT_VER_CODE,times.append((int)((Math.random()*9+1)*10000)).toString());
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

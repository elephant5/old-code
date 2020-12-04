package com.colourfulchina.mars.code;

import com.colourfulchina.mars.constants.GiftCodeConstants;
import com.colourfulchina.mars.utils.JedisPoolInstance;
import redis.clients.jedis.Jedis;

/**
 * 从Redis中获取权益码
 */
public class GetGiftCode {

//    public static void main(String[] args) {
//        Jedis jedis = JedisPoolInstance.getJedisPoolInstance().getResource();
////        jedis.sadd("test","a","b","c");
////        pipelined.sync();
//        String test = getGiftCodeByRedis("giftActCode",null);
//        System.out.println(test);
//    }

    /**
     * 从Redis中获取权益码,并清除
     * @param key 权益码key
     * @param projectCode 项目编码(卡券平台券码需要)
     * @return 权益码
     */
    public static String getGiftCodeByRedis(String key,String projectCode){
        Jedis jedis = JedisPoolInstance.getJedisPoolInstance().getResource();
        try {
            //判断这个key对应的set中是否还有码未使用
            if(jedis.scard(key) > 0){
                //随机取出一个权益码
                String code = jedis.srandmember(key);
                //从set中移除这个权益码
                jedis.srem(key,code);
                return code;

            }else {
                //调用对应的创建权益码的方法
                switch(key){
                    case GiftCodeConstants.GIFT_ACT_CODE:
                        //创建权益激活码
                        CreateActCode.createGiftActCode();
                        break;
                    case GiftCodeConstants.GIFT_VER_CODE:
                        //创建权益核销码
                        CreateVerCode.createGiftVerCode();
                        break;
                    case GiftCodeConstants.GIFT_CPN_CODE:
                        //创建卡券平台券码
                        CreateCouponCode.createGiftCpnCode(projectCode);
                        break;
                    default:
                        System.out.println("不存在此类型的权益码");
                        return null;
                }
                System.out.println("11111");
                return getGiftCodeByRedis(key,projectCode);
            }

        }finally {
            if(null != jedis){
                jedis.close();
            }
        }
    }
}

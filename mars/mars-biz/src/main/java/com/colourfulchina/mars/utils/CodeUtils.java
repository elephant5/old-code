package com.colourfulchina.mars.utils;

import cn.hutool.core.date.DateUtil;
import com.colourfulchina.mars.code.CreateActCode;
import com.colourfulchina.mars.code.CreateCouponCode;
import com.colourfulchina.mars.code.CreateVerCode;
import com.colourfulchina.mars.constants.GiftCodeConstants;
import lombok.experimental.UtilityClass;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * 码工具类
 */
@UtilityClass
public class CodeUtils {
    private static final Integer CODE_MAX_LENGTH=12;
    /**
     * 批次号生成
     * @param batchTypeNo
     * @return
     */
    public static String generateBatchNo(BatchTypeNo batchTypeNo)throws Exception{
        int random = (int)(0 + Math.random() * 10);
        String no = batchTypeNo.getCode() + DateUtil.format(new Date(),"yyyyMMddHHmmssSSS") + random;
        return no;
    }


    private static final  Jedis jedis = JedisPoolInstance.getJedisPoolInstance().getResource();

    /**
     * 码生成(生成一串唯一的字符串)第一代
     * @param num 唯一的数字
     * @param codeTypeNo
     * @return
     */
    public static String generateCodeNo(Long num,CodeTypeNo codeTypeNo)throws Exception{
        //设置码的总位数
        int count = 12;
        //第一步,将传入的唯一主键转换为16进制字符串
        String tail = Long.toHexString(num).toUpperCase();
        //第二步，计算随机生成字符串的位数
        int randomInt = count - codeTypeNo.getCode().length() - tail.length();
        //第三步，生成随机的字母和数字字符串
        if (randomInt < 0){
            throw new Exception("码总长度设置过短");
        }
        String val = "";
        Random random = new Random();
        for ( int i = 0; i < randomInt; i++ ) {
            String str = random.nextInt( 2 ) % 2 == 0 ? "num" : "char";
            if ( "char".equalsIgnoreCase( str ) ) {
                // 产生字母
                int nextInt = random.nextInt( 2 ) % 2 == 0 ? 65 : 97;
                val += (char) ( nextInt + random.nextInt( 26 ) );
            }
            else if ( "num".equalsIgnoreCase( str ) ) {
                // 产生数字
                val += String.valueOf( random.nextInt( 10 ) );
            }
        }
        //第四步，组合字符串
        String no = codeTypeNo.getCode().toUpperCase() + val.toUpperCase() + tail.toUpperCase();
        return no;
    }

    /**
     * 生成简短码(第二代)
     * @param codeTypeNo
     * @return
     */
    public static String generateSimpleCodeNo(Long num,CodeTypeNo codeTypeNo){
        StringBuffer no=new StringBuffer("");
        if (CodeTypeNo.VERIFY_CODE!=codeTypeNo){
            no.append(codeTypeNo.code);
        }
        String orderNo=num+"";
        final int length = orderNo.length();
        if (length >3){
            orderNo=orderNo.substring(length-4,length);
        }
        no.append(orderNo);
        Calendar now=Calendar.getInstance();
        String val = now.getTimeInMillis()+"";
        val=val.substring(val.length()+no.length()-CODE_MAX_LENGTH);
        no.append(val);
        return no.toString();
    }

    /**
     * 码生成，放入redis中等待获取，wu新规则（第三代）
     * @param num
     * @param codeTypeNo
     */
    public static void generateWuRuleCodeNo(Long num,CodeTypeNo codeTypeNo){
//        Jedis jedis = JedisPoolInstance.getJedisPoolInstance().getResource();
        Pipeline pipelined = jedis.pipelined();
        try {
            //激活码生成
            if (codeTypeNo==CodeTypeNo.ACTIVATION_CODE){
                for(int i =1 ;i < num;i++){
                    StringBuffer times = new StringBuffer(String.valueOf(System.currentTimeMillis()).substring(1,6));
//                jedis.sadd("giftCode",times.append((int)((Math.random()*9+1)*1000000)).toString());
                    //权益激活码: 时间戳2-6位 + 7 位随机数
                    pipelined.sadd(GiftCodeConstants.GIFT_ACT_CODE,times.append((int)((Math.random()*9+1)*1000000)).toString());
//                    pipelined.expire(GiftCodeConstants.GIFT_ACT_CODE,15);//
                }
            }
            //核销码生成
            if (codeTypeNo==CodeTypeNo.VERIFY_CODE){
                for(int i =1 ;i < num;i++){
                    StringBuffer times = new StringBuffer(String.valueOf(System.currentTimeMillis()).substring(1,6));
                    //权益核销码: 时间戳2-6位 + 5 位随机数
                    pipelined.sadd(GiftCodeConstants.GIFT_VER_CODE,times.append((int)((Math.random()*9+1)*10000)).toString());
                }
            }
            pipelined.sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            /*if(null != jedis){
                jedis.close();
            }*/
        }
    }

    /**
     * 从Redis中获取权益码,并清除
     * @param key 权益码key
     * @param projectCode 项目编码(卡券平台券码需要)
     * @return 权益码
     */
    public static String getCodeByRedis(String key){
//        Jedis jedis = JedisPoolInstance.getJedisPoolInstance().getResource();
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
                        generateWuRuleCodeNo(200000L,CodeTypeNo.ACTIVATION_CODE);
                        break;
                    case GiftCodeConstants.GIFT_VER_CODE:
                        //创建权益核销码
                        generateWuRuleCodeNo(5000L,CodeTypeNo.VERIFY_CODE);
                        break;
                    default:
                        return null;
                }
                return getCodeByRedis(key);
            }

        }finally {
//            if(null != jedis){
//                jedis.close();
//            }
        }
    }

    /**
     * 批次号类型前缀
     */
    public enum BatchTypeNo{
        ACTIVATION_CODE("ACT","激活码批次号前缀"),
        ;

        private String code;
        private String name;

        BatchTypeNo(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 码类型前缀
     */
    public enum CodeTypeNo{
        ACTIVATION_CODE("AC","激活码前缀"),
        VERIFY_CODE("HX","核销码前缀"),
        ;

        private String code;
        private String name;

        CodeTypeNo(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }
}

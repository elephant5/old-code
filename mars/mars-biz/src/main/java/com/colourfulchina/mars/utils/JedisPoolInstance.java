package com.colourfulchina.mars.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * ClassName:JedisPool
 * Package:com.colourful.utils
 * DESC: 创建 Redis连接池
 * DATE:2018/10/9 14:47
 * AUTHOR:DJ
 */
@Component
public class JedisPoolInstance {

    private static String host;
    @Value("${redis.host}")
    public void setHost(String host){
        JedisPoolInstance.host = host;
    }

    private static int port;
    @Value("${redis.port}")
    public void setPort(int port){
        JedisPoolInstance.port = port;
    }

    private static String password ;
    @Value("${redis.password}")
    public void setPassword(String password){
        JedisPoolInstance.password = password;
    }

    private static int maxActive =10;
    @Value("${redis.pool.max-active}")
    public void setMaxActive(int maxActive){
        JedisPoolInstance.maxActive = maxActive;
    }

    private static int maxIdle =2;
    @Value("${redis.pool.max-idle}")
    public void setMaxIdle(int maxIdle){
        JedisPoolInstance.maxIdle = maxIdle;
    }

    private static long timeout =60000  ;
    @Value("${redis.timeout}")
    public void setTimeout(long timeout){
        JedisPoolInstance.timeout = timeout;
    }


    //redis连接池对象
    private static JedisPool jedisPool = null;

    private JedisPoolInstance() {
    }

    public static JedisPool getJedisPoolInstance() {
        if (null == jedisPool) {
            synchronized (JedisPoolInstance.class) {
                if (null == jedisPool) {
                    JedisPoolConfig poolConfig = new JedisPoolConfig();
                    poolConfig.setMaxTotal(maxActive);
                    poolConfig.setMaxIdle(maxIdle);
                    poolConfig.setMaxWaitMillis(timeout);
                    poolConfig.setTestOnBorrow(true);
                    if(StringUtils.isEmpty(password)){
                        jedisPool = new JedisPool(poolConfig, host, port, (int) timeout);
                    } else {
                        jedisPool = new JedisPool(poolConfig, host, port, (int) timeout,password);
                    }
                }
            }
        }
        return jedisPool;
    }


}

package com.colourfulchina.mars.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.colourfulchina.aggregatePay.utils.ICBCUtils;
import com.colourfulchina.mars.api.config.GoodLifeConfig;
import com.colourfulchina.mars.api.vo.req.AccessTokenReq;
import com.colourfulchina.mars.api.vo.req.BaseTokenReq;
import com.colourfulchina.mars.service.GoodLifeLoginService;
import com.colourfulchina.mars.utils.GoodLifeUtil;
import com.mysql.jdbc.TimeUtil;
import io.micrometer.core.instrument.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 罗幸
 * @Description: 美好生活登陆对接
 * @Date: 2020/7/13 17:35
 */
@Slf4j
@Service
public class GoodLifeLoginServiceImpl implements GoodLifeLoginService {
    @Autowired
    private GoodLifeConfig config;

    @Autowired
    private RedisTemplate redisTemplate;

    private final static String SECRET_KEY_PRE = "GOODLIFE_CLIENT_SECRET";
    private final static String SPLIT = "#";

    /**
     * 美丽生活登陆
     *
     * @param authorizationCode 前端js返回的code
     * @throws Exception
     */
    @Override
    public String login(String authorizationCode) throws Exception {
        //setp1 ： 获取clientSecret
        String clientSecret = (String) redisTemplate.opsForValue().get(SECRET_KEY_PRE);
        log.info("clientSecret :{}", clientSecret);
        if (StringUtils.isEmpty(clientSecret)) {
            log.info("secret是空的");
            BaseTokenReq baseTokenReq = new BaseTokenReq();
            baseTokenReq.setTimestamp(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN));
            baseTokenReq.setClientId(config.getClientId());
            baseTokenReq.setNonceStr(GoodLifeUtil.getRandomStr(16));
            String result = GoodLifeUtil.sendRequest(config.getHost() + config.getBaseTokenUrl(), JSONObject.toJSONString(baseTokenReq), config);
            log.info("执行BaseToken接口结果:{}", result);
            JSONObject resultJson = JSONObject.parseObject(result);
            clientSecret = resultJson.getString("clientSecret");
            Long expiresIn = resultJson.getLong("expiresIn");//有效期
            log.info("待缓存数据 clientSecret:{},expiresIn:{}", clientSecret, expiresIn);
            redisTemplate.opsForValue().set(SECRET_KEY_PRE , clientSecret, expiresIn/2,TimeUnit.SECONDS);
            log.info("缓存结束1");
        }
        log.info("clientSecret:{}", clientSecret);
        //setp2 获取Access Token和openid
        AccessTokenReq accessTokenReq = new AccessTokenReq();
        accessTokenReq.setGrantType(config.getGrantType());
        accessTokenReq.setClientId(config.getClientId());
        accessTokenReq.setClientSecret(clientSecret);
        accessTokenReq.setChannelFlag(config.getChannelFlag());
        accessTokenReq.setCode(authorizationCode);
        accessTokenReq.setRedirectUri(config.getLoginRedirectUri());
        log.info("请求{}接口,参数:{}", config.getAccessTokenUrl(), JSONObject.toJSONString(accessTokenReq));
        String accessTokenResp = GoodLifeUtil.sendRequest(config.getHost() + config.getAccessTokenUrl(), JSONObject.toJSONString(accessTokenReq), config);
        log.info("执行AccessToken接口结果:{}", accessTokenResp);
        JSONObject accessTokenRespJson = JSONObject.parseObject(accessTokenResp);
        //加密的商户号
        String enccifseq = accessTokenRespJson.getString("enccifseq");
        return enccifseq;
    }
}

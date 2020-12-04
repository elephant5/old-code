package com.colourfulchina.mars.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: 罗幸
 * @Description:
 * @Date: 2020/7/13 15:58
 */
@Data
@ConfigurationProperties
@Component
public class GoodLifeConfig {
    //商户代码
    private String instID;
    //应用代码
    private String appID;
    //银行分配给第三方的clientid，最长64位
    private String clientId;
    //签名摘要算法
    private String signMethod;
    //报文格式
    private String format;
    //接口版本
    private String version;
    //银行公钥证书路径
    private String publicKeyPath;
    //商户证书路径
    private String merchKeyPath;
    //商户证书密码
    private String merchKeyPassword;
    //授权类型，此值固定为“authorization_code”。
    private String grantType;
    //渠道标识，默认上送000
    private String channelFlag;
    //成功授权后的回调地址, 注意需要将url进行URLEncode
    private String loginRedirectUri;
    private String host;

    //获取基础令牌接口
    private String baseTokenUrl;
    //获取Access Token和openid 接口
    private String accessTokenUrl;
}

package com.colourfulchina.mars.api.vo.req;

import lombok.Data;

/**
 * @Author: 罗幸
 * @Description:
 * @Date: 2020/7/16 13:48
 */
@Data
public class AccessTokenReq {
    private String grantType;
    private String clientId;
    private String clientSecret;
    private String channelFlag;
    private String code;
    private String redirectUri;
}

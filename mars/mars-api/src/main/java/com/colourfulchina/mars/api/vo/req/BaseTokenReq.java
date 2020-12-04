package com.colourfulchina.mars.api.vo.req;

import lombok.Data;

/**
 * @Author: 罗幸
 * @Description: base_token 接口请求类封装
 * @Date: 2020/7/13 17:39
 */
@Data
public class BaseTokenReq {
    private String clientId;
    private String nonceStr;
    private String timestamp;
}

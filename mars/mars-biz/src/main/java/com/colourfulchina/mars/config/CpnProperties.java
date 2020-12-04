package com.colourfulchina.mars.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 对接银商渠道第三方券接口的参数配置
 */

@Data
@Component
public class CpnProperties {

//    @Value("${ys.apiKey}")
    private String apiKey;

//    @Value("${ys.privateKey}")
    private String privateKey;

//    @Value(("${ys.apiUrl}"))
    private String ysApiUrl;  //银商第三方券接口地址
}

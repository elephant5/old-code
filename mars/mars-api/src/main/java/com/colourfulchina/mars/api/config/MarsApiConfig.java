package com.colourfulchina.mars.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * @ClassName: MarsApiConfig     
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: sunny.wang     
 * @date:   2019年5月14日 下午7:47:58   
 * @version V1.0 
 * @Copyright: www.colourfulchina.com@2019.All rights reserved.
 */
@Data
@Configuration
public class MarsApiConfig {

    @Value("${interface.encrypt.key}")
    private String encryptKey;

}

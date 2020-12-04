package com.colourfulchina.god.door.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 开联通权限系统对接配置文件
 */
@Data
@ConfigurationProperties(prefix = "klt.auth")
public class KltAuthProperties {
    private String baseUrl;
    private String userUrl;
    private String menuUrl;
    private String buttonUrl;
    private String sysKey;
    private String s3desKeyMenu;
    private String s3desKeyInfo;
    private Map<String,String> sysAppMap;
}

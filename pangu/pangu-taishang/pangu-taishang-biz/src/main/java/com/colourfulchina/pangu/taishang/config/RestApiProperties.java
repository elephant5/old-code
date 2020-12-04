package com.colourfulchina.pangu.taishang.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "restapi")
public class RestApiProperties {
    private String auth;
    private String colourfulServicePath;
}

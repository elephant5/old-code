package com.colourfulchina.pangu.taishang.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({FtpProperties.class,GoodsProperties.class,FileDownloadProperties.class, RestApiProperties.class})
public class PanGuAutoConfiguration {
}

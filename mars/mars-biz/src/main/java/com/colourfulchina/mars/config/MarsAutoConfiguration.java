package com.colourfulchina.mars.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({FileDownloadProperties.class,PayInfoProperties.class,CodeProperties.class, MqscProperties.class})
public class MarsAutoConfiguration {
}

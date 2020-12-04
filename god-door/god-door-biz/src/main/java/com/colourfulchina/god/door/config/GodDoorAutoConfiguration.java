package com.colourfulchina.god.door.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({KltAuthProperties.class})
public class GodDoorAutoConfiguration {
}

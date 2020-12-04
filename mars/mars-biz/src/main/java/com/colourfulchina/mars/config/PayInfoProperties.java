package com.colourfulchina.mars.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 导出文件配置文件
 */
@Data
@ConfigurationProperties(prefix = "pay.info")
public class PayInfoProperties {
    private String bookingUrl;
    private String salesUrl;
    private String mwebUrl;
}

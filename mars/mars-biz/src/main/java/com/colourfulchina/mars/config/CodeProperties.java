package com.colourfulchina.mars.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 导出文件配置文件
 */
@Data
@ConfigurationProperties(prefix = "code")
public class CodeProperties {
    private boolean simpleGiftCode;
    private boolean simpleReservCode;
}

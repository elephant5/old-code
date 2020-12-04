package com.colourfulchina.pangu.taishang.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 导出文件配置文件
 */
@Data
@ConfigurationProperties(prefix = "file.download")
public class FileDownloadProperties {
    private String url;
    private String path;
    private String sendEmailAddress; //发件人地址
}

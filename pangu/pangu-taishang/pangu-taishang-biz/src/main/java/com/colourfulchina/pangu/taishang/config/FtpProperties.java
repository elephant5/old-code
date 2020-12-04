package com.colourfulchina.pangu.taishang.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

@Data
@ConfigurationProperties("ftp")
public class FtpProperties implements Serializable {
    /**
     * erp ftp服务器的ip或域名
     */
    private String erpHost;
    /**
     * erp ftp服务器的端口
     */
    private Integer erpPort;
    /**
     * erp ftp服务器的用户名
     */
    private String erpUsername;
    /**
     * erp ftp服务器的密码
     */
    private String erpPassword;
    /**
     * erp cdn url
     */
    private String erpCdnUrl;
    /**
     * pangu ftp服务器的ip或域名
     */
    private String pgHost;
    /**
     * pangu ftp服务器的端口
     */
    private Integer pgPort;
    /**
     * pangu ftp服务器的用户名
     */
    private String pgUsername;
    /**
     * pangu ftp服务器的密码
     */
    private String pgPassword;

    /**
     * pg cdn url
     */
    private String pgCdnUrl;
}

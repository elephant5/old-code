package com.colourfulchina.pangu.taishang.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 商品相关配置
 */
@Data
@ConfigurationProperties(prefix = "goods")
public class GoodsProperties {
    private String giftUrl;
}

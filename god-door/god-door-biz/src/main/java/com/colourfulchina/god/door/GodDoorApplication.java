package com.colourfulchina.god.door;

/**
 * User: Ryan
 * Date: 2018/8/13
 */

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 上帝之眼系统
 */
@RefreshScope
@EnableFeignClients
@SpringCloudApplication
public class GodDoorApplication {
    public static void main(String[] args) {
        SpringApplication.run(GodDoorApplication.class, args);
    }
}

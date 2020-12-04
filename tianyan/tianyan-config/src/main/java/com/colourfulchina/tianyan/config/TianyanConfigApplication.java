

package com.colourfulchina.tianyan.config;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

/**
 *
 * 配置中心
 */
@RefreshScope
@EnableHystrix
@EnableConfigServer
@SpringCloudApplication
public class TianyanConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(TianyanConfigApplication.class, args);
	}
}

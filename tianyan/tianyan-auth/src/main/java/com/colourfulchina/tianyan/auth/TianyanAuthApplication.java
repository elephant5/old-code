package com.colourfulchina.tianyan.auth;


import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 认证授权中心
 */
@RefreshScope
@EnableHystrix
@SpringCloudApplication
@EnableFeignClients({"com.colourfulchina.tianyan.admin.api.feign"})
public class TianyanAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(TianyanAuthApplication.class, args);
	}
}

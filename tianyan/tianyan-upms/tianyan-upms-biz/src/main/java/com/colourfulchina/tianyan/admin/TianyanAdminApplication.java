package com.colourfulchina.tianyan.admin;


import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 用户统一管理系统
 */
@RefreshScope
@EnableHystrix
@EnableFeignClients
@SpringCloudApplication
public class TianyanAdminApplication {
	public static void main(String[] args) {
		SpringApplication.run(TianyanAdminApplication.class, args);
	}
}

package com.colourfulchina.pangu.taishang;


import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 太上系统
 */
@EnableAsync
@RefreshScope
@EnableFeignClients({"com.colourfulchina"})
@EnableHystrix
@SpringCloudApplication
public class TaishangApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaishangApplication.class, args);
	}
}

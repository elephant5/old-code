

package com.colourfulchina.tianyan.zipkin;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import zipkin2.server.internal.EnableZipkinServer;

/**
 *
 * 链路监控
 */
@RefreshScope
@EnableHystrix
@EnableZipkinServer
@SpringBootApplication
public class TianyanZipkinApplication {

	public static void main(String[] args) {
		SpringApplication.run(TianyanZipkinApplication.class, args);
	}
}



package com.colourfulchina.tianyan.monitor;


import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

/**
 *
 * 服务监控
 */
@RefreshScope
@EnableHystrix
@EnableAdminServer
@SpringBootApplication
public class TianyanMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(TianyanMonitorApplication.class, args);
	}
}

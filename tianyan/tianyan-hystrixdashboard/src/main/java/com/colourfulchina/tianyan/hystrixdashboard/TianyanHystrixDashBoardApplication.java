

package com.colourfulchina.tianyan.hystrixdashboard;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 *
 * 服务监控
 */
@EnableHystrixDashboard
@SpringBootApplication
public class TianyanHystrixDashBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(TianyanHystrixDashBoardApplication.class, args);
	}
}

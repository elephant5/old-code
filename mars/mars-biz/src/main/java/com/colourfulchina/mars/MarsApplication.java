package com.colourfulchina.mars;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @ClassName: MarsApplication  
 * @Description: TODO(这里用一句话描述这个类的作用)   
 * @author Sunny  
 * @date 2018年9月28日  
 * @company Colourful@copyright(c) 2018
 * @version V1.0
 */
@RefreshScope
@EnableHystrix
@EnableFeignClients({"com.colourfulchina"})
@SpringCloudApplication
@EnableAsync
@MapperScan(value = "com.colourfulchina.mars.sync")
//@EnableAutoConfiguration(exclude={DruidDataSourceAutoConfigure.class,DataSourceAutoConfiguration.class})
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class MarsApplication {
    public static void main(String[] args) {
        SpringApplication.run(MarsApplication.class, args);
    }
}

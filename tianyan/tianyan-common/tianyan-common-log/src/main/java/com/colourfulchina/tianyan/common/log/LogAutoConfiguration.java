

package com.colourfulchina.tianyan.common.log;

import com.colourfulchina.tianyan.admin.api.feign.RemoteLogService;
import com.colourfulchina.tianyan.common.log.aspect.SysLogAspect;
import com.colourfulchina.tianyan.common.log.event.SysLogListener;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * 日志自动配置
 */
@Configuration
@AllArgsConstructor
@ConditionalOnWebApplication
@EnableFeignClients({"com.colourfulchina.tianyan.admin.api.feign"})
public class LogAutoConfiguration {
	private final RemoteLogService remoteLogService;

	@Bean
	public SysLogListener sysLogListener() {
		return new SysLogListener(remoteLogService);
	}

	@Bean
	public SysLogAspect sysLogAspect() {
		return new SysLogAspect();
	}
}

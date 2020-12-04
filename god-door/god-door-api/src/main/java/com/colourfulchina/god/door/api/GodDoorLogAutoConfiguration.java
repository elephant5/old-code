

package com.colourfulchina.god.door.api;

import com.colourfulchina.god.door.api.aspect.SysGodDoorLogAspect;
import com.colourfulchina.god.door.api.event.SysGodDoorLogListener;
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
public class GodDoorLogAutoConfiguration {
	private final RemoteLogService remoteLogService;

	@Bean
	public SysGodDoorLogListener sysGodDoorLogListener() {
		return new SysGodDoorLogListener(remoteLogService);
	}

	@Bean
	public SysGodDoorLogAspect sysGodDoorLogAspect() {
		return new SysGodDoorLogAspect();
	}
}

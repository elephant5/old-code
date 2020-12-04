

package com.colourfulchina.tianyan.gateway.config;

import com.colourfulchina.tianyan.gateway.handle.HystrixFallbackHandler;
import com.colourfulchina.tianyan.gateway.handle.ImageCodeHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

/**
 *
 * 路由配置信息
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class RouterFunctionConfiguration {
	private final HystrixFallbackHandler hystrixFallbackHandler;
	private final ImageCodeHandler imageCodeHandler;

	@Bean
	public RouterFunction routerFunction() {
		return RouterFunctions.route(
			RequestPredicates.GET("/fallback")
				.and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), hystrixFallbackHandler)
			.andRoute(RequestPredicates.GET("/code")
				.and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), imageCodeHandler);

	}

}

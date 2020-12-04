

package com.colourfulchina.tianyan.common.security.interceptor;

import feign.RequestInterceptor;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

/**
 *
 * feign 拦截器传递 header 中oauth token，
 * 使用hystrix 的信号量模式
 */
//@Configuration
//@ConditionalOnProperty("security.oauth2.client.client-id")
public class TianyanFeignClientIntercept {
//	@Bean
	public RequestInterceptor oauth2FeignRequestInterceptor(OAuth2ClientContext oAuth2ClientContext,
															OAuth2ProtectedResourceDetails resource) {
		return new OAuth2FeignRequestInterceptor(oAuth2ClientContext, resource);
	}
}

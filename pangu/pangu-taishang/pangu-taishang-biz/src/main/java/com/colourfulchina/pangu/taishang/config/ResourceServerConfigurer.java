package com.colourfulchina.pangu.taishang.config;

import com.colourfulchina.tianyan.common.security.compoent.ResourceAuthExceptionEntryPoint;
import com.colourfulchina.tianyan.common.security.compoent.TianyanAccessDeniedHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {
	private final TianyanAccessDeniedHandler tianyanAccessDeniedHandler;
	private final ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/actuator/**", "/log/**","/**").permitAll()
			.anyRequest().authenticated()
			.and().csrf().disable();
	}

	/**
	 * why add  resourceId
	 * https://stackoverflow.com/questions/28703847/how-do-you-set-a-resource-id-for-a-token
	 *
	 * @param resources
	 * @throws Exception
	 */
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.authenticationEntryPoint(resourceAuthExceptionEntryPoint)
			.accessDeniedHandler(tianyanAccessDeniedHandler);
	}
}

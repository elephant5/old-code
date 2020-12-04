

package com.colourfulchina.tianyan.admin.api.feign;

import com.colourfulchina.tianyan.admin.api.dto.UserInfo;
import com.colourfulchina.tianyan.admin.api.feign.fallback.RemoteUserServiceFallbackImpl;
import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.tianyan.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = ServiceNameConstant.UMPS_SERVICE, fallback = RemoteUserServiceFallbackImpl.class)
public interface RemoteUserService {
	/**
	 * 通过用户名查询用户、角色信息
	 *
	 * @param username 用户名
	 * @return R
	 */
	@GetMapping("/user/info/{username}")
	R<UserInfo> info(@PathVariable("username") String username);
}

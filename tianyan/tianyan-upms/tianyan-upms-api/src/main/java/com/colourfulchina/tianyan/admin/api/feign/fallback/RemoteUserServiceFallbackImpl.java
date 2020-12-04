

package com.colourfulchina.tianyan.admin.api.feign.fallback;

import com.colourfulchina.tianyan.admin.api.dto.UserInfo;
import com.colourfulchina.tianyan.admin.api.feign.RemoteUserService;
import com.colourfulchina.tianyan.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoteUserServiceFallbackImpl implements RemoteUserService {
	/**
	 * 通过用户名查询用户、角色信息
	 *
	 * @param username 用户名
	 * @return R
	 */
	@Override
	public R<UserInfo> info(String username) {
		log.error("feign 查询用户信息失败:{}", username);
		return null;
	}
}

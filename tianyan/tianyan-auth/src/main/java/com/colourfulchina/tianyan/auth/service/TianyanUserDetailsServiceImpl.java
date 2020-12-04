

package com.colourfulchina.tianyan.auth.service;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.colourfulchina.tianyan.admin.api.dto.UserInfo;
import com.colourfulchina.tianyan.admin.api.entity.SysUser;
import com.colourfulchina.tianyan.admin.api.feign.RemoteUserService;
import com.colourfulchina.tianyan.common.core.constant.CommonConstant;
import com.colourfulchina.tianyan.common.core.constant.SecurityConstants;
import com.colourfulchina.tianyan.common.core.util.R;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户详细信息
 *
 *
 */
@Slf4j
@Service
@AllArgsConstructor
public class TianyanUserDetailsServiceImpl implements UserDetailsService {
	private final RemoteUserService remoteUserService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		R<UserInfo> result = remoteUserService.info(username);

		if (result == null || result.getData() == null) {
			throw new UsernameNotFoundException("用户不存在");
		}

		UserInfo info = result.getData();
		Set<String> dbAuthsSet = new HashSet<>();
		if (ArrayUtil.isNotEmpty(info.getRoles())) {
			// 获取角色
			Arrays.stream(info.getRoles()).forEach(role -> dbAuthsSet.add(SecurityConstants.ROLE + role));
			// 获取资源
			dbAuthsSet.addAll(Arrays.asList(info.getPermissions()));

		}
		Collection<? extends GrantedAuthority> authorities
			= AuthorityUtils.createAuthorityList(dbAuthsSet.toArray(new String[0]));
		SysUser user = info.getSysUser();
		boolean enabled = StrUtil.equals(user.getDelFlag(), CommonConstant.STATUS_NORMAL);
		// 构造security用户
		return new User(username, SecurityConstants.BCRYPT + user.getPassword(), enabled,
			true, true, true, authorities);
	}
}



package com.colourfulchina.tianyan.auth.endpoint;

import cn.hutool.core.util.StrUtil;
import com.colourfulchina.tianyan.common.core.util.R;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 删除token端点
 */
@RestController
@AllArgsConstructor
public class RevokeTokenEndpoint {
	private final TokenStore tokenStore;

	/**
	 * 删除token
	 *
	 * @param authHeader Authorization
	 */
	@RequestMapping("/oauth/removeToken")
	public R<Boolean> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
		if (StringUtils.hasText(authHeader)) {
			String tokenValue = authHeader.replace("Bearer", "").trim();
			OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
			if (accessToken == null || StrUtil.isBlank(accessToken.getValue())) {
				return new R<>(false, "退出失败，token 为空");
			}
			tokenStore.removeAccessToken(accessToken);
		}
		return new R<>(Boolean.TRUE);
	}
}

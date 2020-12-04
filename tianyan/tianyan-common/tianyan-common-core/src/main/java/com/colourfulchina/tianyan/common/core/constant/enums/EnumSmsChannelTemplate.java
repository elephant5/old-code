

package com.colourfulchina.tianyan.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 短信通道模板
 */
@Getter
@AllArgsConstructor
public enum EnumSmsChannelTemplate {
	/**
	 * 登录验证
	 */
	LOGIN_NAME_LOGIN("loginCodeChannel", "登录验证"),
	/**
	 * 服务异常提醒
	 */
	SERVICE_STATUS_CHANGE("serviceStatusChange", "Tianyan");

	/**
	 * 模板名称
	 */
	private final String template;
	/**
	 * 模板签名
	 */
	private final String signName;
}

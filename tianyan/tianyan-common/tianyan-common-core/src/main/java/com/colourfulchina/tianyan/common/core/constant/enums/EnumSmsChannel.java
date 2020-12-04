

package com.colourfulchina.tianyan.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * 短信通道枚举
 */
@Getter
@AllArgsConstructor
public enum EnumSmsChannel {
	/**
	 * 阿里大鱼短信通道
	 */
	ALIYUN("ALIYUN_SMS", "阿里大鱼");
	/**
	 * 通道名称
	 */
	private final String name;
	/**
	 * 通道描述
	 */
	private final String description;
}

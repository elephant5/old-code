

package com.colourfulchina.tianyan.common.core.constant;

/**
 *
 * 服务名称
 */
public interface ServiceNameConstant {
	/**
	 * 认证服务的SERVICEID（zuul 配置的对应）
	 */
	String AUTH_SERVICE = "tianyan-auth";

	/**
	 * UMPS模块
	 */
	String UMPS_SERVICE = "tianyan-upms";

	/**
	 * 女娲模块(基础服务)
	 */
	String NUWA_SERVICE = "nuwa";

	/**
	 * 比干模块(资源管理)
	 */
	String BIGAN_SERVICE = "bigan";

	/**
	 * 杨戬模块(销售发码、预约)
	 */
	String YANGJIAN_SERVICE = "yangjian";

	/**
	 * 子牙模块(支付宝)
	 */
	String ZIYA_SERVICE = "ziya";
}

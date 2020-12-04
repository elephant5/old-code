

package com.colourfulchina.tianyan.common.core.constant;

public interface SecurityConstants {
	/**
	 * 角色前缀
	 */
	String ROLE = "ROLE_";
	/**
	 * 前缀
	 */
	String TIANYAN_PREFIX = "tianyan_";

	/**
	 * oauth 相关前缀
	 */
	String OAUTH_PREFIX = "oauth:";
	/**
	 * 项目的license
	 */
	String TIANYAN_LICENSE = "made by tianyan";
	/**
	 * 基础角色
	 */
	String BASE_ROLE = "ROLE_USER";
	/**
	 * 授权码模式
	 */
	String AUTHORIZATION_CODE = "authorization_code";
	/**
	 * 密码模式
	 */
	String PASSWORD = "password";
	/**
	 * 刷新token
	 */
	String REFRESH_TOKEN = "refresh_token";


	/**
	 * {bcrypt} 加密的特征码
	 */
	String BCRYPT = "{bcrypt}";
	/**
	 * sys_oauth_client_details 表的字段，不包括client_id、client_secret
	 */
	String CLIENT_FIELDS = "client_id, CONCAT('{noop}',client_secret) as client_secret, resource_ids, scope, "
		+ "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
		+ "refresh_token_validity, additional_information, autoapprove";

	/**
	 * JdbcClientDetailsService 查询语句
	 */
	String BASE_FIND_STATEMENT = "select " + CLIENT_FIELDS
		+ " from sys_oauth_client_details";

	/**
	 * 默认的查询语句
	 */
	String DEFAULT_FIND_STATEMENT = BASE_FIND_STATEMENT + " order by client_id";

	/**
	 * 按条件client_id 查询
	 */
	String DEFAULT_SELECT_STATEMENT = BASE_FIND_STATEMENT + " where client_id = ?";

}

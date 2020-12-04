

package com.colourfulchina.tianyan.common.security.exception;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 *
 * 自定义OAuth2Exception
 */
public class TianyanOAuth2Exception extends OAuth2Exception {

	public TianyanOAuth2Exception(String msg) {
		super(msg);
	}
}



package com.colourfulchina.tianyan.common.security.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends TianyanOAuth2Exception {

	public UnauthorizedException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "unauthorized";
	}

	@Override
	public int getHttpErrorCode() {
		return HttpStatus.UNAUTHORIZED.value();
	}

}

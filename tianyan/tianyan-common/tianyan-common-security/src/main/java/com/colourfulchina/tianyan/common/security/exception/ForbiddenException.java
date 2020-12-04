

package com.colourfulchina.tianyan.common.security.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends TianyanOAuth2Exception {

	public ForbiddenException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "access_denied";
	}

	@Override
	public int getHttpErrorCode() {
		return HttpStatus.FORBIDDEN.value();
	}

}


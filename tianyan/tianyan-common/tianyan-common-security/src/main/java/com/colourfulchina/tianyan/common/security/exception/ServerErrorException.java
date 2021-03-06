

package com.colourfulchina.tianyan.common.security.exception;

import org.springframework.http.HttpStatus;

public class ServerErrorException extends TianyanOAuth2Exception {

	public ServerErrorException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "server_error";
	}

	@Override
	public int getHttpErrorCode() {
		return HttpStatus.INTERNAL_SERVER_ERROR.value();
	}

}

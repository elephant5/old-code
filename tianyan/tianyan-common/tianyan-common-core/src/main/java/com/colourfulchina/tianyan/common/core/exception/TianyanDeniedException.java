

package com.colourfulchina.tianyan.common.core.exception;

import lombok.NoArgsConstructor;

/**
 *
 * 403 授权拒绝
 */
@NoArgsConstructor
public class TianyanDeniedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TianyanDeniedException(String message) {
		super(message);
	}

	public TianyanDeniedException(Throwable cause) {
		super(cause);
	}

	public TianyanDeniedException(String message, Throwable cause) {
		super(message, cause);
	}

	public TianyanDeniedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

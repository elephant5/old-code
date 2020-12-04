

package com.colourfulchina.tianyan.common.core.util;

import com.colourfulchina.tianyan.common.core.constant.enums.EnumsReqCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @param <T>
 *
 */
@ToString
public class R<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	@Getter
	@Setter
	private String msg = "success";

	@Getter
	@Setter
	private int code = EnumsReqCode.SUCCESS.getCode();

	@Getter
	@Setter
	private T data;

	public R() {
		super();
	}

	public R(T data) {
		super();
		this.data = data;
	}

	public R(T data, String msg) {
		super();
		this.data = data;
		this.msg = msg;
	}

	public R(Throwable e) {
		super();
		this.msg = e.getMessage();
		this.code = EnumsReqCode.FAIL.getCode();
	}
}

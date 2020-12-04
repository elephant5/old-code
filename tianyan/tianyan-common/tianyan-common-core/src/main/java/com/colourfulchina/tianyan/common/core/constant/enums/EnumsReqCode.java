package com.colourfulchina.tianyan.common.core.constant.enums;

import lombok.Getter;

/**
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/3/1 11:20
 */
@Getter
public enum  EnumsReqCode {
	SUCCESS(0),
	FAIL(1);
	private int code;
	EnumsReqCode(int code){
		this.code = code;
	}
}

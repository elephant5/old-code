package com.colourfulchina.mars.api.vo.res;

import java.io.Serializable;

import lombok.Data;

@Data
public class BoscBankCustInfoRes implements Serializable{

	private static final long serialVersionUID = -732142403575234341L;
	
	private String name; // 姓名
	private String mobile; // 手机号
	private Integer self; // 1主卡 ，0 副卡
	
}

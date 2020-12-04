package com.colourfulchina.mars.api.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class GoodsReqVo implements Serializable{ 
	
	private static final long serialVersionUID = 6898917450094503641L;
	
	private Long memberId;
	
	private String prjCode;

	private String actCode;

}

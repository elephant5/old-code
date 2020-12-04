package com.colourfulchina.mars.api.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class GoodsListReqVo implements Serializable{
	
	private static final long serialVersionUID = -4378633581887730212L;
	
	private Integer giftcodeId;
	private String beginDate;
	private String endDate;
	
	private Integer groupId;
	private String service;
	
	private Integer recommend; // 推荐酒店

	//分页信息
	private int size=10;
	private int current=1;

	//城市  输入框筛选条件
	private String hotelAddress;
	private String hotelName;
}

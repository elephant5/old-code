package com.colourfulchina.mars.api.vo.req;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class GiftOrderIdReqVo implements Serializable{

	private static final long serialVersionUID = 5218543493791498091L;
	//主订单ID 原来的订单ID
	private String oldCapitalOrderId;
	// 需要更新成的订单ID
	private String newCapitalOrderId;

}

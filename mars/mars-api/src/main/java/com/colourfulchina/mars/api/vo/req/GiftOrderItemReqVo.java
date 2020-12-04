package com.colourfulchina.mars.api.vo.req;

import java.io.Serializable;

import lombok.Data;

@Data
public class GiftOrderItemReqVo implements Serializable{ 
	
	private static final long serialVersionUID = -2350206049146457554L;
	
	//子订单ID
	private String itemOrderId;
	//商品ID
	private Integer goodsId; 
	//购买数量
	private Integer goodsNum;
	
}

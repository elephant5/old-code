package com.colourfulchina.mars.api.vo.req;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class GiftOrderQueryReqVo implements Serializable{

	private static final long serialVersionUID = -4723983408631958661L;
	//主订单ID
	private String capitalOrderId;
	//手机号
	private String buyerMobile;
	//渠道
	private String acChannel;
	//商品ID
	private Integer goodsId;
	
}

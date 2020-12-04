package com.colourfulchina.mars.api.vo.req;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class GiftOrderPayReqVo implements Serializable{ 
	
	private static final long serialVersionUID = 2838838293583190724L;
	
	//主订单ID
	private String capitalOrderId;
	
	//支付完成时间 
	private Date payTime;
	
	//第三方支付单号
	private String payNumber;
	
	//支付方式  非必填
	private String payMethod;
	
}

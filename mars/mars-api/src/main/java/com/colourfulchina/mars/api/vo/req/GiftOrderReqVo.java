package com.colourfulchina.mars.api.vo.req;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class GiftOrderReqVo implements Serializable{
	
	private static final long serialVersionUID = 2838838293583990724L;
	
	//主订单ID
	private String capitalOrderId;
	//手机号
	private String buyerMobile;
	//渠道
	private String acChannel;
	//子订单
	private List<GiftOrderItemReqVo> itemlist;
	//是否直接激活
	private Boolean activateTag;
	//是否发送短信
	private Boolean smsTag;
	//是否需要支付 1需要支付，0无需支付
	private Boolean payTag;
	//加密验签  除sign外其它入参都参与加密;参考加密接口.
	private String sign;
	
	//订单原价总金额  非必填
	private BigDecimal totalAmount;
	//订单实付金额  非必填
	private BigDecimal orderAmount;
	//订单优惠金额  非必填
	private BigDecimal offerAmount;
	//优惠券ID  非必填
	private String couponsId;
	//积分  非必填
	private Integer integral;
	// 支付方式  非必填
	private String payMethod;
	
}

package com.colourfulchina.mars.api.vo.req;

import java.io.Serializable;

import lombok.Data;

@Data
public class GiftCodeSendVo implements Serializable { 
	
	private static final long serialVersionUID = 2838838293583990724L;

	public String itemOrderId;
	public String mobile; 
	public String acChannel; 
	public Integer goodsId; 
	public Integer goodsNum; 
	public Boolean smsTag; 
	public Boolean activateTag;
	public String giftUrl; 
	
}

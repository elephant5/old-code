package com.colourfulchina.mars.api.vo.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GiftCodeResVo {
	
	@ApiModelProperty(value = "子订单ID")
	private String itemOrderId;
	
	@ApiModelProperty(value = "子订单ID")
	private Integer goodsId;
	
	@ApiModelProperty(value = "子订单ID")
	private Integer giftId;
	
	@ApiModelProperty(value = "激活码")
	private String giftCode;

	@ApiModelProperty(value = "行权地址(短链)")
	private String giftUrl;
	
	
	
}

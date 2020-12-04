package com.colourfulchina.mars.api.vo.res;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: GiftOrderResVo     
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: sunny.wang     
 * @date:   2019年7月29日 下午12:02:28   
 * @version V1.0 
 * @Copyright: www.colourfulchina.com@2019.All rights reserved.
 */
@Data
public class GiftOrderResVo {

	@ApiModelProperty(value = "订单号")
	private String capitalOrderId;
	
	@ApiModelProperty(value = "订单状态")
	private Integer status;
	
	@ApiModelProperty(value = "激活码信息")
	private List<GiftCodeResVo> itemlist;
	
}

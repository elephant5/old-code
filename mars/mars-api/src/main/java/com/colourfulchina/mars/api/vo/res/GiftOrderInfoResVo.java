package com.colourfulchina.mars.api.vo.res;

import com.colourfulchina.mars.api.entity.GiftOrderCapital;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: GiftOrderResVo     
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: sunny.wang     
 * @date:   2019年7月29日 下午12:02:28   
 * @version V1.0 
 * @Copyright: www.colourfulchina.com@2019.All rights reserved.
 */
@Data
public class GiftOrderInfoResVo {

	@ApiModelProperty(value = "主订单信息")
	private GiftOrderCapital giftOrderCapital;

	@ApiModelProperty(value = "激活码信息")
	private List<GiftCodeResVo> itemlist;
}

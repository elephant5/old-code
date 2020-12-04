package com.colourfulchina.mars.api.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@TableName("gift_order_item_detail")
@Data
public class GiftOrderItemDetail extends Model<GiftOrderItemDetail> {

	private static final long serialVersionUID = -3117367082361020960L;

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	
	@ApiModelProperty(value = "子订单ID")
	@TableField(value = "item_order_id")
	private String itemOrderId;
	
	@ApiModelProperty(value = "商品ID")
	@TableField(value = "goods_id")
	private Integer goodsId;
	
	@ApiModelProperty(value = "激活码ID")
	@TableField(value = "gift_id")
	private Integer giftId;
	
	@ApiModelProperty(value = "激活码")
	@TableField(value = "gift_code")
	private String giftCode;

	@ApiModelProperty(value = "行权地址(短链)")
	@TableField(value = "gift_url")
	private String giftUrl;
	
	@ApiModelProperty("创建时间")
	@TableField(value = "create_time")
	private Date createTime;        
	
	@ApiModelProperty("更新时间")
	@TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
	private Date updateTime; 

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

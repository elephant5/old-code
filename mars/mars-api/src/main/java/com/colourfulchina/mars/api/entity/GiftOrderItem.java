package com.colourfulchina.mars.api.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@TableName("gift_order_item")
@Data
public class GiftOrderItem extends Model<GiftOrderItem>{

	private static final long serialVersionUID = 584982169820314332L;

	@ApiModelProperty("激活码订单主表ID")
	@TableId(value = "id",type = IdType.AUTO)
	private Integer id; 
	
	@ApiModelProperty("子订单ID")
	@TableField(value = "item_order_id")
	private String itemOrderId;          
	
	@ApiModelProperty("主订单ID")
	@TableField(value = "capital_order_id")
	private String capitalOrderId;                  
	
	@ApiModelProperty("商品ID")
	@TableField(value = "goods_id")
	private Integer goodsId;               
	
	@ApiModelProperty("商品名称")
	@TableField(value = "goods_name")
	private String goodsName;             
	
	@ApiModelProperty("商品原价")
	@TableField(value = "goods_price")
	private BigDecimal goodsPrice;            
	
	@ApiModelProperty("商品售卖价格")
	@TableField(value = "goods_sales_price")
	private BigDecimal goodsSalesPrice;      
	
	@ApiModelProperty("商品购买数量")
	@TableField(value = "goods_num")
	private Integer goodsNum;              
	
	@ApiModelProperty("消耗的积分")
	@TableField(value = "integral")
	private Integer integral;               
	
	@ApiModelProperty("下单时间")
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

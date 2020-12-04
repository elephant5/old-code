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

@TableName("gift_order_config_portal")
@Data
public class GiftOrderConfigPortal extends Model<GiftOrderConfigPortal>{
	
	private static final long serialVersionUID = 1886390905226036882L;

	@ApiModelProperty("主键")
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	
	@ApiModelProperty("渠道编码")
	@TableField(value = "ac_channel")
	private String acChannel;
	
	@ApiModelProperty("渠道名称")
	@TableField(value = "channel_name")
	private String channelName;
	
	@ApiModelProperty("商品ID")
	@TableField(value = "goods_id")
	private Integer goodsId;
	
	@ApiModelProperty("商品名称")
	@TableField(value = "goods_name")
	private String goodsName;
	
	@ApiModelProperty("最大值")
	@TableField(value = "max_num")
	private Integer maxNum;
	
	@ApiModelProperty("告警阀")
	@TableField(value = "alarm_num")
	private Integer alarmNum;
	
	@ApiModelProperty("创建人")
	@TableField(value = "create_user")
	private String createUser;
	
	@ApiModelProperty("创建时间")
	@TableField(value = "create_time")
	private Date createTime;        
	
	@ApiModelProperty("更新人")
	@TableField(value = "update_user")
	private String updateUser;
	
	@ApiModelProperty("更新时间")
	@TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
	private Date updateTime; 

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}

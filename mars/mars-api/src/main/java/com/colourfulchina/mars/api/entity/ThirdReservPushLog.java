package com.colourfulchina.mars.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("third_reserv_push_log")
@Data
public class ThirdReservPushLog extends Model<ThirdReservPushLog> {
	private static final long serialVersionUID = -6310277922307994311L;

	/**
	 * 预约单推送主键id
	 */
	@TableId(value = "id",type = IdType.AUTO)
	private Integer id;
	/**
	 * 预约单id
	 */
	@TableField(value = "order_id")
	private Integer orderId;
	/**
	 * 预约单状态
	 */
	@TableField(value = "prose_status")
	private String proseStatus;
	/**
	 * 推送总次数
	 */
	@TableField(value = "push_count")
	private Integer pushCount;
	/**
	 * 推送状态
	 */
	@TableField(value = "push_status" )
	private String pushStatus;
	/**
	 * 核销码状态
	 */
	@TableField(value = "hx_status")
	private String hxStatus;
	/**
	 * 下次推送时间
	 */
	@TableField(value = "next_push_time")
	private Date nextPushTime;

	@TableField(value = "del_flag")
	private Integer delFlag;
	@TableField(value = "create_time")
	private Date createTime;
	@TableField(value = "create_user")
	private String createUser;
	@TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
	private Date updateTime;
	@TableField(value = "update_user")
	private String updateUser;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

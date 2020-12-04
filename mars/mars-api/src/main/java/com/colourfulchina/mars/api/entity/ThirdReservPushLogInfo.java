package com.colourfulchina.mars.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("third_reserv_push_log_info")
@Data
public class ThirdReservPushLogInfo extends Model<ThirdReservPushLogInfo> {
	private static final long serialVersionUID = 7786847864798637499L;

	/**
	 * 预约单推动明细id
	 */
	@TableId(value = "id",type = IdType.AUTO)
	private Integer id;
	/**
	 * 预约单推送表id
	 */
	@TableField(value = "log_id")
	private Integer logId;
	/**
	 * 本次推送约单的实际状态
	 */
	@TableField(value = "prose_status")
	private String proseStatus;
	/**
	 * 本次推送的核销码实际状态
	 */
	@TableField(value = "hx_status")
	private String hxStatus;
	/**
	 * 本次是第几次推送
	 */
	@TableField(value = "this_count")
	private Integer thisCount;
	/**
	 * 返回状态码
	 */
	@TableField(value = "resp_cd" )
	private String respCd;
	/**
	 * 返回信息
	 */
	@TableField(value = "resp_msg")
	private String respMsg;
	/**
	 * 所有返回信息
	 */
	@TableField(value = "remark")
	private String remark;

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

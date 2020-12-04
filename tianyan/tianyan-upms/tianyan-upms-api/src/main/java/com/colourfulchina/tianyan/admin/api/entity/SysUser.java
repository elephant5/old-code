

package com.colourfulchina.tianyan.admin.api.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 */
@Data
@TableName("sys_user")
public class SysUser implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value = "user_id", type = IdType.AUTO)
	private Integer userId;
	/**
	 * 用户名
	 */
	private String username;

	private String password;
	/**
	 * 随机盐
	 */
	@JsonIgnore
	private String salt;
	/**
	 * 创建时间
	 */
	@TableField("create_time")
	private Date createTime;
	/**
	 * 修改时间
	 */
	@TableField("update_time")
	private Date updateTime;
	/**
	 * 0-正常，1-删除
	 */
//	@TableLogic
	@TableField("del_flag")
	private String delFlag;

	/**
	 * 手机号码
	 */
	private String phone;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 头像
	 */
	private String avatar;

	/**
	 * 部门ID
	 */
	@TableField("dept_id")
	private Integer deptId;

	@Override
	public String toString() {
		return "SysUser{" +
			"userId=" + userId +
			", username='" + username + '\'' +
			", password='" + password + '\'' +
			", salt='" + salt + '\'' +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", delFlag='" + delFlag + '\'' +
			", phone='" + phone + '\'' +
			", avatar='" + avatar + '\'' +
			", deptId=" + deptId +
			'}';
	}
}

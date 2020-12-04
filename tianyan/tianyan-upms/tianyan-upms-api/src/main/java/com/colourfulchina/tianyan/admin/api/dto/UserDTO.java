

package com.colourfulchina.tianyan.admin.api.dto;

import com.colourfulchina.tianyan.admin.api.entity.SysUser;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO extends SysUser {
	/**
	 * 角色ID
	 */
	private List<Integer> role;

	private Integer deptId;

	/**
	 * 新密码
	 */
	private String newpassword1;
}

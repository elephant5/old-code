

package com.colourfulchina.tianyan.admin.api.dto;

import com.colourfulchina.tianyan.admin.api.entity.SysRole;
import lombok.Data;

/**
 * 角色Dto
 */
@Data
public class RoleDTO extends SysRole {
	/**
	 * 角色部门Id
	 */
	private Integer roleDeptId;

	/**
	 * 部门名称
	 */
	private String deptName;
}

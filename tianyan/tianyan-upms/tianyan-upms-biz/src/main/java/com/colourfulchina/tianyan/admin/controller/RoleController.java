

package com.colourfulchina.tianyan.admin.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.colourfulchina.tianyan.admin.api.dto.Query;
import com.colourfulchina.tianyan.admin.api.dto.RoleDTO;
import com.colourfulchina.tianyan.admin.api.entity.SysRole;
import com.colourfulchina.tianyan.admin.service.SysRoleMenuService;
import com.colourfulchina.tianyan.admin.service.SysRoleService;
import com.colourfulchina.tianyan.common.core.constant.CommonConstant;
import com.colourfulchina.tianyan.common.core.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;

	/**
	 * 通过ID查询角色信息
	 *
	 * @param id ID
	 * @return 角色信息
	 */
	@GetMapping("/{id}")
	public SysRole role(@PathVariable Integer id) {
		return sysRoleService.selectById(id);
	}

	/**
	 * 添加角色
	 *
	 * @param roleDto 角色信息
	 * @return success、false
	 */
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_role_add')")
	public R<Boolean> role(@RequestBody RoleDTO roleDto) {
		return new R<>(sysRoleService.insertRole(roleDto));
	}

	/**
	 * 修改角色
	 *
	 * @param roleDto 角色信息
	 * @return success/false
	 */
	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys_role_edit')")
	public R<Boolean> roleUpdate(@RequestBody RoleDTO roleDto) {
		return new R<>(sysRoleService.updateRoleById(roleDto));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_role_del')")
	public R<Boolean> roleDel(@PathVariable Integer id) {
		SysRole sysRole = sysRoleService.selectById(id);
		sysRole.setDelFlag(CommonConstant.STATUS_DEL);
		return new R<>(sysRoleService.updateById(sysRole));
	}

	/**
	 * 获取角色列表
	 *
	 * @param deptId 部门ID
	 * @return 角色列表
	 */
	@GetMapping("/roleList/{deptId}")
	public List<SysRole> roleList(@PathVariable Integer deptId) {
		return sysRoleService.selectListByDeptId(deptId);

	}

	/**
	 * 分页查询角色信息
	 *
	 * @param params 分页对象
	 * @return 分页对象
	 */
	@RequestMapping("/rolePage")
	public Page rolePage(@RequestParam Map<String, Object> params) {
		params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
		return sysRoleService.selectwithDeptPage(new Query<>(params), new EntityWrapper<>());
	}

	/**
	 * 更新角色菜单
	 *
	 * @param roleId  角色ID
	 * @param menuIds 菜单结合
	 * @return success、false
	 */
	@PutMapping("/roleMenuUpd")
	@PreAuthorize("@pms.hasPermission('sys_role_perm')")
	public R<Boolean> roleMenuUpd(Integer roleId, @RequestParam("menuIds[]") Integer[] menuIds) {
		SysRole sysRole = sysRoleService.selectById(roleId);
		return new R<>(sysRoleMenuService.insertRoleMenus(sysRole.getRoleCode(), roleId, menuIds));
	}
}

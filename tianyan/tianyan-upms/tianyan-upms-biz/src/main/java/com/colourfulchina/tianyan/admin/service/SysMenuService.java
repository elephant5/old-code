

package com.colourfulchina.tianyan.admin.service;


import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.tianyan.admin.api.entity.SysMenu;
import com.colourfulchina.tianyan.admin.api.vo.MenuVO;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 */
public interface SysMenuService extends IService<SysMenu> {
	/**
	 * 通过角色编号查询URL 权限
	 *
	 * @param role 角色编号
	 * @return 菜单列表
	 */
	List<MenuVO> findMenuByRoleCode(String role);

	/**
	 * 级联删除菜单
	 *
	 * @param id 菜单ID
	 * @return 成功、失败
	 */
	Boolean deleteMenu(Integer id);

	/**
	 * 更新菜单信息
	 *
	 * @param sysMenu 菜单信息
	 * @return 成功、失败
	 */
	Boolean updateMenuById(SysMenu sysMenu);

	/**
	 * 通过角色ID 查询权限
	 *
	 * @param roleIds
	 * @return
	 */
	List<String> findPermissionsByRoleIds(List<Integer> roleIds);
}



package com.colourfulchina.tianyan.admin.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.colourfulchina.tianyan.admin.api.dto.MenuTree;
import com.colourfulchina.tianyan.admin.api.entity.SysMenu;
import com.colourfulchina.tianyan.admin.api.vo.MenuVO;
import com.colourfulchina.tianyan.admin.api.vo.TreeUtil;
import com.colourfulchina.tianyan.admin.service.SysMenuService;
import com.colourfulchina.tianyan.common.core.constant.CommonConstant;
import com.colourfulchina.tianyan.common.core.util.R;
import com.colourfulchina.tianyan.common.security.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/menu")
public class MenuController {
	@Autowired
	private SysMenuService sysMenuService;

	/**
	 * 通过角色名称查询用户菜单
	 *
	 * @param role 角色名称
	 * @return 菜单列表
	 */
	@GetMapping("/findMenuByRole/{role}")
	public List<MenuVO> findMenuByRole(@PathVariable String role) {
		return sysMenuService.findMenuByRoleCode(role);
	}

	/**
	 * 返回当前用户的树形菜单集合
	 *
	 * @return 当前用户的树形菜单
	 */
	@GetMapping(value = "/userMenu")
	public List<MenuTree> userMenu() {
		// 获取符合条件得菜单
		Set<MenuVO> all = new HashSet<>();
		SecurityUtils.getRoles().forEach(roleName -> all.addAll(sysMenuService.findMenuByRoleCode(roleName)));
		List<MenuTree> menuTreeList = new ArrayList<>();
		all.forEach(menuVo -> {
			if (CommonConstant.MENU.equals(menuVo.getType())) {
				menuTreeList.add(new MenuTree(menuVo));
			}
		});
		CollUtil.sort(menuTreeList, Comparator.comparingInt(MenuTree::getSort));
		return TreeUtil.bulid(menuTreeList, -1);
	}

	/**
	 * 返回树形菜单集合
	 *
	 * @return 树形菜单
	 */
	@GetMapping(value = "/allTree")
	public List<MenuTree> getTree() {
		SysMenu condition = new SysMenu();
		condition.setDelFlag(CommonConstant.STATUS_NORMAL);
		return TreeUtil.bulidTree(sysMenuService.selectList(new EntityWrapper<>(condition)), -1);
	}

	/**
	 * 返回角色的菜单集合
	 *
	 * @param roleName 角色名称
	 * @return 属性集合
	 */
	@GetMapping("/roleTree/{roleName}")
	public List<Integer> roleTree(@PathVariable String roleName) {
		List<MenuVO> menus = sysMenuService.findMenuByRoleCode(roleName);
		List<Integer> menuList = new ArrayList<>();
		for (MenuVO menuVo : menus) {
			menuList.add(menuVo.getMenuId());
		}
		return menuList;
	}

	/**
	 * 通过ID查询菜单的详细信息
	 *
	 * @param id 菜单ID
	 * @return 菜单详细信息
	 */
	@GetMapping("/{id}")
	public SysMenu menu(@PathVariable Integer id) {
		return sysMenuService.selectById(id);
	}

	/**
	 * 新增菜单
	 *
	 * @param sysMenu 菜单信息
	 * @return success/false
	 */
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_menu_add')")
	public R<Boolean> menu(@RequestBody SysMenu sysMenu) {
		return new R<>(sysMenuService.insert(sysMenu));
	}

	/**
	 * 删除菜单
	 *
	 * @param id 菜单ID
	 * @return success/false
	 * TODO  级联删除下级节点
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_menu_del')")
	public R<Boolean> menuDel(@PathVariable Integer id) {
		return new R<>(sysMenuService.deleteMenu(id));
	}

	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys_menu_edit')")
	public R<Boolean> menuUpdate(@RequestBody SysMenu sysMenu) {
		return new R<>(sysMenuService.updateMenuById(sysMenu));
	}

}

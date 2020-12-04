

package com.colourfulchina.tianyan.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.tianyan.admin.api.entity.SysMenu;
import com.colourfulchina.tianyan.admin.api.vo.MenuVO;
import com.colourfulchina.tianyan.admin.mapper.SysMenuMapper;
import com.colourfulchina.tianyan.admin.service.SysMenuService;
import com.colourfulchina.tianyan.common.core.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
	@Autowired
	private SysMenuMapper sysMenuMapper;

	@Override
	@Cacheable(value = "menu_details", key = "#role  + '_menu'")
	public List<MenuVO> findMenuByRoleCode(String role) {
		return sysMenuMapper.findMenuByRoleCode(role);
	}

	@Override
	@CacheEvict(value = "menu_details", allEntries = true)
	public Boolean deleteMenu(Integer id) {
		// 删除当前节点
		SysMenu condition1 = new SysMenu();
		condition1.setMenuId(id);
		condition1.setDelFlag(CommonConstant.STATUS_DEL);
		this.updateById(condition1);

		// 删除父节点为当前节点的节点
		SysMenu conditon2 = new SysMenu();
		conditon2.setParentId(id);
		SysMenu sysMenu = new SysMenu();
		sysMenu.setDelFlag(CommonConstant.STATUS_DEL);
		return this.update(sysMenu, new EntityWrapper<>(conditon2));
	}

	@Override
	@CacheEvict(value = "menu_details", allEntries = true)
	public Boolean updateMenuById(SysMenu sysMenu) {
		return this.updateById(sysMenu);
	}

	/**
	 * 通过角色ID 查询权限
	 *
	 * @param roleList
	 * @return
	 */
	@Override
	public List<String> findPermissionsByRoleIds(List<Integer> roleList) {
		String roleIds = CollUtil.join(roleList, ",");
		return sysMenuMapper.findPermissionsByRoleIds(roleIds);
	}
}



package com.colourfulchina.tianyan.admin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.tianyan.admin.api.entity.SysUserRole;
import com.colourfulchina.tianyan.admin.mapper.SysUserRoleMapper;
import com.colourfulchina.tianyan.admin.service.SysUserRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

	/**
	 * 根据用户Id删除该用户的角色关系
	 * @param userId 用户ID
	 * @return boolean
	 */
	@Override
	public Boolean deleteByUserId(Integer userId) {
		return baseMapper.deleteByUserId(userId);
	}
}

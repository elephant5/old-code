

package com.colourfulchina.tianyan.admin.service;


import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.tianyan.admin.api.entity.SysUserRole;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 */
public interface SysUserRoleService extends IService<SysUserRole> {

	/**
	 * 根据用户Id删除该用户的角色关系
	 * @param userId 用户ID
	 * @return boolean
	 */
	Boolean deleteByUserId(Integer userId);
}

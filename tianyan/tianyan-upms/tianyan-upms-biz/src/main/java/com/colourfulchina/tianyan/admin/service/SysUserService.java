

package com.colourfulchina.tianyan.admin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.tianyan.admin.api.dto.Query;
import com.colourfulchina.tianyan.admin.api.dto.UserDTO;
import com.colourfulchina.tianyan.admin.api.dto.UserInfo;
import com.colourfulchina.tianyan.admin.api.entity.SysUser;
import com.colourfulchina.tianyan.admin.api.vo.UserVO;
import com.colourfulchina.tianyan.common.core.util.R;

public interface SysUserService extends IService<SysUser> {
	/**
	 * 查询用户信息
	 * @param username 用户名
	 * @return userInfo
	 */
	UserInfo findUserInfo(String username);

	/**
	 * 分页查询用户信息（含有角色信息）
	 *
	 * @param query 查询条件
	 * @return
	 */
	Page selectWithRolePage(Query query);

	/**
	 * 删除用户
	 *
	 * @param sysUser 用户
	 * @return boolean
	 */
	Boolean deleteUserById(SysUser sysUser);

	/**
	 * 更新当前用户基本信息
	 *
	 * @param userDto  用户信息
	 * @param username 用户名
	 * @return Boolean
	 */
	R<Boolean> updateUserInfo(UserDTO userDto, String username);

	/**
	 * 更新指定用户信息
	 *
	 * @param userDto  用户信息
	 * @param username 用户信息
	 * @return
	 */
	Boolean updateUser(UserDTO userDto, String username);

	/**
	 * 通过ID查询用户信息
	 *
	 * @param id 用户ID
	 * @return 用户信息
	 */
	UserVO selectUserVoById(Integer id);
}

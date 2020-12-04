

package com.colourfulchina.tianyan.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.plugins.Page;
import com.colourfulchina.tianyan.admin.api.dto.Query;
import com.colourfulchina.tianyan.admin.api.dto.UserDTO;
import com.colourfulchina.tianyan.admin.api.dto.UserInfo;
import com.colourfulchina.tianyan.admin.api.entity.SysUser;
import com.colourfulchina.tianyan.admin.api.entity.SysUserRole;
import com.colourfulchina.tianyan.admin.api.vo.UserVO;
import com.colourfulchina.tianyan.admin.service.SysUserService;
import com.colourfulchina.tianyan.common.log.annotation.SysLog;
import com.colourfulchina.tianyan.common.core.constant.CommonConstant;
import com.colourfulchina.tianyan.common.core.util.R;
import com.colourfulchina.tianyan.common.security.util.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
	private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
	@Autowired
	private SysUserService userService;

	/**
	 * 获取当前用户信息（角色、权限）
	 * 并且异步初始化用户部门信息
	 *
	 * @param username 用户名
	 * @return 用户名
	 */
	@GetMapping(value = {"/info", "/info/{username}"})
	public R<UserInfo> user(@PathVariable(required = false) String username) {
		//为空时查询当前用户
		if (StrUtil.isBlank(username)) {
			username = SecurityUtils.getUser();
		}
		return new R<>(userService.findUserInfo(username));
	}

	/**
	 * 通过ID查询当前用户信息
	 *
	 * @param id ID
	 * @return 用户信息
	 */
	@GetMapping("/{id}")
	public UserVO user(@PathVariable Integer id) {
		return userService.selectUserVoById(id);
	}

	/**
	 * 删除用户信息
	 *
	 * @param id ID
	 * @return R
	 */
	@SysLog("删除用户信息")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_user_del')")
	public R<Boolean> userDel(@PathVariable Integer id) {
		SysUser sysUser = userService.selectById(id);
		return new R<>(userService.deleteUserById(sysUser));
	}

	/**
	 * 添加用户
	 *
	 * @param userDto 用户信息
	 * @return success/false
	 */
	@SysLog("添加用户")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_user_add')")
	public R<Boolean> user(@RequestBody UserDTO userDto) {
		SysUser sysUser = new SysUser();
		BeanUtils.copyProperties(userDto, sysUser);
		sysUser.setDelFlag(CommonConstant.STATUS_NORMAL);
		sysUser.setPassword(ENCODER.encode(userDto.getNewpassword1()));
		userService.insert(sysUser);

		userDto.getRole().forEach(roleId -> {
			SysUserRole userRole = new SysUserRole();
			userRole.setUserId(sysUser.getUserId());
			userRole.setRoleId(roleId);
			userRole.insert();
		});
		return new R<>(Boolean.TRUE);
	}

	/**
	 * 更新用户信息
	 *
	 * @param userDto 用户信息
	 * @return R
	 */
	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys_user_edit')")
	public R<Boolean> userUpdate(@RequestBody UserDTO userDto) {
		SysUser user = userService.selectById(userDto.getUserId());
		return new R<>(userService.updateUser(userDto, user.getUsername()));
	}

	/**
	 * 分页查询用户
	 *
	 * @param params 参数集
	 * @return 用户集合
	 */
	@RequestMapping("/userPage")
	public Page userPage(@RequestParam Map<String, Object> params) {
		return userService.selectWithRolePage(new Query(params));
	}

	/**
	 * 修改个人信息
	 *
	 * @param userDto userDto
	 * @return success/false
	 */
	@PutMapping("/editInfo")
	public R<Boolean> editInfo(@RequestBody UserDTO userDto) {
		return userService.updateUserInfo(userDto, SecurityUtils.getUser());
	}
}

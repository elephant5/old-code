

package com.colourfulchina.tianyan.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.tianyan.admin.api.dto.Query;
import com.colourfulchina.tianyan.admin.api.entity.SysUser;
import com.colourfulchina.tianyan.admin.api.vo.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
	/**
	 * 通过用户名查询用户信息（含有角色信息）
	 *
	 * @param username 用户名
	 * @return userVo
	 */
	UserVO selectUserVoByUsername(String username);

	/**
	 * 分页查询用户信息（含角色）
	 *
	 * @param query    查询条件
	 * @param username 用户名
	 * @return list
	 */
	List selectUserVoPage(Query query, @Param("username") Object username);

	/**
	 * 通过ID查询用户信息
	 *
	 * @param id 用户ID
	 * @return userVo
	 */
	UserVO selectUserVoById(Integer id);

}



package com.colourfulchina.tianyan.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.tianyan.admin.api.dto.Query;
import com.colourfulchina.tianyan.admin.api.dto.RoleDTO;
import com.colourfulchina.tianyan.admin.api.entity.SysRole;
import com.colourfulchina.tianyan.admin.api.entity.SysRoleDept;
import com.colourfulchina.tianyan.admin.mapper.SysRoleDeptMapper;
import com.colourfulchina.tianyan.admin.mapper.SysRoleMapper;
import com.colourfulchina.tianyan.admin.service.SysRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
	@Autowired
	private SysRoleDeptMapper sysRoleDeptMapper;
	@Autowired
	private SysRoleMapper sysRoleMapper;

	/**
	 * 添加角色
	 *
	 * @param roleDto 角色信息
	 * @return 成功、失败
	 */
	@Override
	public Boolean insertRole(RoleDTO roleDto) {
		SysRole sysRole = new SysRole();
		BeanUtils.copyProperties(roleDto, sysRole);
		sysRoleMapper.insert(sysRole);
		SysRoleDept roleDept = new SysRoleDept();
		roleDept.setRoleId(sysRole.getRoleId());
		roleDept.setDeptId(roleDto.getRoleDeptId());
		sysRoleDeptMapper.insert(roleDept);
		return true;
	}

	/**
	 * 分页查角色列表
	 *
	 * @param query   查询条件
	 * @param wrapper wapper
	 * @return page
	 */
	@Override
	public Page selectwithDeptPage(Query<Object> query, EntityWrapper<Object> wrapper) {
		query.setRecords(sysRoleMapper.selectRolePage(query, query.getCondition()));
		return query;
	}

	/**
	 * 更新角色
	 *
	 * @param roleDto 含有部门信息
	 * @return 成功、失败
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean updateRoleById(RoleDTO roleDto) {
		//删除原有的角色部门关系
		SysRoleDept condition = new SysRoleDept();
		condition.setRoleId(roleDto.getRoleId());
		sysRoleDeptMapper.delete(new EntityWrapper<>(condition));

		//更新角色信息
		SysRole sysRole = new SysRole();
		BeanUtils.copyProperties(roleDto, sysRole);
		sysRoleMapper.updateById(sysRole);

		//维护角色部门关系
		SysRoleDept roleDept = new SysRoleDept();
		roleDept.setRoleId(sysRole.getRoleId());
		roleDept.setDeptId(roleDto.getRoleDeptId());
		sysRoleDeptMapper.insert(roleDept);
		return true;
	}

	/**
	 * 通过部门ID查询角色列表
	 *
	 * @param deptId 部门ID
	 * @return 角色列表
	 */
	@Override
	public List<SysRole> selectListByDeptId(Integer deptId) {
		return sysRoleMapper.selectListByDeptId(deptId);
	}

	/**
	 * 通过用户ID，查询角色信息
	 *
	 * @param userId
	 * @return
	 */
	@Override
	public List<SysRole> findRolesByUserId(Integer userId) {
		return sysRoleMapper.findRolesByUserId(userId);
	}
}

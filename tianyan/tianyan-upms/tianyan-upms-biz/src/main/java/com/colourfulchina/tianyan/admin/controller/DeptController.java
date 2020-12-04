

package com.colourfulchina.tianyan.admin.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.colourfulchina.tianyan.admin.api.dto.DeptTree;
import com.colourfulchina.tianyan.admin.api.entity.SysDept;
import com.colourfulchina.tianyan.admin.service.SysDeptService;
import com.colourfulchina.tianyan.common.core.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 部门管理 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/dept")
public class DeptController {
	@Autowired
	private SysDeptService sysDeptService;

	/**
	 * 通过ID查询
	 *
	 * @param id ID
	 * @return SysDept
	 */
	@GetMapping("/{id}")
	public SysDept get(@PathVariable Integer id) {
		return sysDeptService.selectById(id);
	}


	/**
	 * 返回树形菜单集合
	 *
	 * @return 树形菜单
	 */
	@GetMapping(value = "/tree")
	public List<DeptTree> getTree() {
		SysDept condition = new SysDept();
		condition.setDelFlag(CommonConstant.STATUS_NORMAL);
		return sysDeptService.selectListTree(new EntityWrapper<>(condition));
	}

	/**
	 * 添加
	 *
	 * @param sysDept 实体
	 * @return success/false
	 */
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_dept_add')")
	public Boolean add(@RequestBody SysDept sysDept) {
		return sysDeptService.insertDept(sysDept);
	}

	/**
	 * 删除
	 *
	 * @param id ID
	 * @return success/false
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_dept_del')")
	public Boolean delete(@PathVariable Integer id) {
		return sysDeptService.deleteDeptById(id);
	}

	/**
	 * 编辑
	 *
	 * @param sysDept 实体
	 * @return success/false
	 */
	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys_dept_edit')")
	public Boolean edit(@RequestBody SysDept sysDept) {
		sysDept.setUpdateTime(new Date());
		return sysDeptService.updateDeptById(sysDept);
	}
}

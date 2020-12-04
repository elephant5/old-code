

package com.colourfulchina.tianyan.admin.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.colourfulchina.tianyan.admin.api.dto.Query;
import com.colourfulchina.tianyan.admin.api.entity.SysLog;
import com.colourfulchina.tianyan.admin.service.SysLogService;
import com.colourfulchina.tianyan.common.core.constant.CommonConstant;
import com.colourfulchina.tianyan.common.core.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 日志表 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/log")
public class LogController {
	@Autowired
	private SysLogService sysLogService;

	/**
	 * 分页查询日志信息
	 *
	 * @param params 分页对象
	 * @return 分页对象
	 */
	@RequestMapping("/logPage")
	public Page logPage(@RequestParam Map<String, Object> params) {
		params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
		return sysLogService.selectPage(new Query<>(params), new EntityWrapper<>());
	}

	/**
	 * 根据ID
	 *
	 * @param id ID
	 * @return success/false
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_log_del')")
	public R<Boolean> delete(@PathVariable Long id) {
		return new R<>(sysLogService.updateByLogId(id));
	}

	/**
	 * 插入日志
	 *
	 * @param sysLog 日志实体
	 * @return success/false
	 */
	@PostMapping
	public R<Boolean> save(@RequestBody SysLog sysLog) {
		return new R<>(sysLogService.insert(sysLog));
	}
}

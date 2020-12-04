

package com.colourfulchina.tianyan.admin.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.colourfulchina.tianyan.admin.api.dto.Query;
import com.colourfulchina.tianyan.admin.api.entity.SysOauthClientDetails;
import com.colourfulchina.tianyan.admin.service.SysOauthClientDetailsService;
import com.colourfulchina.tianyan.common.core.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/client")
public class OauthClientDetailsController {
	@Autowired
	private SysOauthClientDetailsService sysOauthClientDetailsService;

	/**
	 * 通过ID查询
	 *
	 * @param id ID
	 * @return SysOauthClientDetails
	 */
	@GetMapping("/{id}")
	public SysOauthClientDetails get(@PathVariable Integer id) {
		return sysOauthClientDetailsService.selectById(id);
	}


	/**
	 * 分页查询信息
	 *
	 * @param params 分页对象
	 * @return 分页对象
	 */
	@RequestMapping("/page")
	public Page page(@RequestParam Map<String, Object> params) {
		return sysOauthClientDetailsService.selectPage(new Query<>(params), new EntityWrapper<>());
	}

	/**
	 * 添加
	 *
	 * @param sysOauthClientDetails 实体
	 * @return success/false
	 */
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_client_add')")
	public R<Boolean> add(@RequestBody SysOauthClientDetails sysOauthClientDetails) {
		return new R<>(sysOauthClientDetailsService.insert(sysOauthClientDetails));
	}

	/**
	 * 删除
	 *
	 * @param id ID
	 * @return success/false
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_client_del')")
	public R<Boolean> delete(@PathVariable String id) {
		SysOauthClientDetails sysOauthClientDetails = new SysOauthClientDetails();
		sysOauthClientDetails.setClientId(id);
		return new R<>(sysOauthClientDetailsService.deleteById(sysOauthClientDetails));
	}

	/**
	 * 编辑
	 *
	 * @param sysOauthClientDetails 实体
	 * @return success/false
	 */
	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys_client_edit')")
	public R<Boolean> edit(@RequestBody SysOauthClientDetails sysOauthClientDetails) {
		return new R<>(sysOauthClientDetailsService.updateById(sysOauthClientDetails));
	}
}

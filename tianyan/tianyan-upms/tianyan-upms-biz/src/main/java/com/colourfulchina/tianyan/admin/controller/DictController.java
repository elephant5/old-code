

package com.colourfulchina.tianyan.admin.controller;


import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.colourfulchina.tianyan.admin.api.dto.Query;
import com.colourfulchina.tianyan.admin.api.entity.SysDict;
import com.colourfulchina.tianyan.admin.api.vo.SysDictVO;
import com.colourfulchina.tianyan.admin.service.SysDictService;
import com.colourfulchina.tianyan.common.core.constant.CommonConstant;
import com.colourfulchina.tianyan.common.core.constant.enums.EnumsReqCode;
import com.colourfulchina.tianyan.common.core.util.R;
import com.colourfulchina.tianyan.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/dict")
@Slf4j
@Api(tags = "字典管理")
public class DictController {
	@Autowired
	private SysDictService sysDictService;

	/**
	 * 通过ID查询字典信息
	 *
	 * @param id ID
	 * @return 字典信息
	 */
	@ApiOperation("通过ID查询字典信息")
	@GetMapping("/{id}")
	public R<SysDict> dict(@PathVariable("id") Integer id) {
		R<SysDict> r = new R<>();
		try {
			SysDict data = sysDictService.selectById(id);
			r.setData(data);
		} catch (Exception e) {
			r.setCode(EnumsReqCode.FAIL.getCode());
			r.setMsg("根据id查找字典信息失败," + e.getMessage());
			log.error("根据id查找字典信息失败,{}", e);
		}
		return r;
	}

	/**
	 * 根据type和label返回唯一的字典对象
	 *
	 * @param sysDict
	 * @return
	 */
	@ApiOperation("根据type和label返回唯一的字典对象")
	@PostMapping("/selectByType")
	public R<SysDict> selectByType(@RequestBody SysDict sysDict) {
		R<SysDict> r = new R<>();
		try {
			Assert.notNull(sysDict.getType(), "type不能为空!!!");
			Assert.notNull(sysDict.getLabel(), "label不能为空!!!");
			sysDict.setDelFlag(CommonConstant.STATUS_NORMAL);
			SysDict data = sysDictService.selectByType(sysDict);
			r.setData(data);
		} catch (Exception e) {
			r.setCode(EnumsReqCode.FAIL.getCode());
			r.setMsg("根据type查找字典信息失败," + e.getMessage());
			log.error("根据type查找字典信息失败,{}", e);
		}
		return r;
	}

	/**
	 * 分页查询字典信息
	 *
	 * @param params 分页对象
	 * @return 分页对象
	 */
	@ApiOperation("分页查询字典信息")
	@RequestMapping("/dictPage")
	public Page dictPage(@RequestParam Map<String, Object> params) {
		params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
		return sysDictService.selectPage(new Query<>(params), new EntityWrapper<>());
	}

	/**
	 * 通过字典类型查找字典
	 *
	 * @param type 类型
	 * @return 同类型字典
	 */
	@ApiOperation("通过字典类型查找字典")
	@GetMapping("/type/{type}")
	@Cacheable(value = "dict_details", key = "#type")
	public R<List<SysDict>> findDictByType(@PathVariable("type") String type) {
		R<List<SysDict>> r = new R<>();
		try {
			SysDict condition = new SysDict();
			condition.setDelFlag(CommonConstant.STATUS_NORMAL);
			condition.setType(type);
			final EntityWrapper<SysDict> entityWrapper = new EntityWrapper<>(condition);
			entityWrapper.orderBy("sort");
			List<SysDict> data = sysDictService.selectList(entityWrapper);
			r.setData(data);
		} catch (Exception e) {
			r.setCode(EnumsReqCode.FAIL.getCode());
			r.setMsg("根据type查找字典列表失败," + e.getMessage());
			log.error("根据type查找字典列表失败,{}", e);
		}
		return r;
	}

	/**
	 * 添加字典
	 *
	 * @param sysDict 字典信息
	 * @return success、false
	 */
	@SysLog("添加字典")
	@PostMapping
	@CacheEvict(value = "dict_details", key = "#sysDict.type")
	@ApiOperation("添加字典")
//	@PreAuthorize("@pms.hasPermission('sys_dict_add')")
	public R<Boolean> dict(@RequestBody SysDict sysDict) {
		R<Boolean> r = new R<>();
		try {
			Boolean data = sysDictService.insert(sysDict);
			r.setData(data);
		} catch (Exception e) {
			r.setCode(EnumsReqCode.FAIL.getCode());
			r.setMsg("根添加字典失败," + e.getMessage());
			log.error("添加字典失败,{}", e);
		}
		return r;
	}


	/**
	 * 删除字典，并且清除字典缓存
	 *
	 * @param id   ID
	 * @param type 类型
	 * @return R
	 */
	@SysLog("删除字典")
	@DeleteMapping("/{id}/{type}")
	@CacheEvict(value = "dict_details", key = "#type")
	@PreAuthorize("@pms.hasPermission('sys_dict_del')")
	public R<Boolean> deleteDict(@PathVariable Integer id, @PathVariable String type) {
		return new R<>(sysDictService.deleteById(id));
	}

	/**
	 * 修改字典
	 *
	 * @param sysDict 字典信息
	 * @return success/false
	 */
	@PutMapping
	@SysLog("修改字典")
	@CacheEvict(value = "dict_details", key = "#sysDict.type")
	@PreAuthorize("@pms.hasPermission('sys_dict_edit')")
	public R<Boolean> editDict(@RequestBody SysDict sysDict) {
		return new R<>(sysDictService.updateById(sysDict));
	}

}

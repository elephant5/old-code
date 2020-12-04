package com.colourfulchina.tianyan.admin.api.feign;

import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.tianyan.admin.api.entity.SysDict;
import com.colourfulchina.tianyan.admin.api.feign.fallback.RemoteDictServiceFallbackImpl;
import com.colourfulchina.tianyan.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/2/27 16:31
 */
@FeignClient(value = ServiceNameConstant.UMPS_SERVICE, fallback = RemoteDictServiceFallbackImpl.class)
public interface RemoteDictService {
	/**
	 * 通过ID查询字典信息
	 *
	 * @param id ID
	 * @return 字典信息
	 */
	@GetMapping("/dict/{id}")
	R<SysDict> dict(@PathVariable("id") Integer id);

	/**
	 * 通过字典类型查找字典
	 *
	 * @param type 类型
	 * @return 同类型字典
	 */
	@GetMapping("/dict/type/{type}")
	R<List<SysDict>> findDictByType(@PathVariable("type") String type);



	/*
	 * 根据type label值查找字典对象
	 * */
	@PostMapping("/dict/selectByType")
	R<SysDict> selectByType(@RequestBody SysDict sysDict);

	/*
	* 添加字典项
	* */
	@PostMapping("/dict")
	R<Boolean> dict(@RequestBody SysDict sysDict);
}

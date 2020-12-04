package com.colourfulchina.tianyan.admin.api.feign.fallback;

import com.colourfulchina.tianyan.admin.api.entity.SysDict;
import com.colourfulchina.tianyan.admin.api.feign.RemoteDictService;
import com.colourfulchina.tianyan.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/2/27 16:32
 */
@Slf4j
@Component
public class RemoteDictServiceFallbackImpl implements RemoteDictService {
	@Override
	public R<SysDict> dict(Integer id) {
		log.error("feign 通过ID查询字典信息失败:{}");
		return null;
	}

	@Override
	public R<List<SysDict>> findDictByType(String type) {
		log.error("feign 通过字典类型查找字典列表失败:{}");
		return null;
	}


	@Override
	public R<SysDict> selectByType(SysDict sysDict) {
		log.error("feign 通过字典类型和标签查找字典信息失败:{}");
		return null;
	}

	@Override
	public R<Boolean> dict(SysDict sysDict) {
		return null;
	}


}

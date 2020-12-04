

package com.colourfulchina.tianyan.admin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.tianyan.admin.api.entity.SysDict;
import com.colourfulchina.tianyan.admin.mapper.SysDictMapper;
import com.colourfulchina.tianyan.admin.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {
	@Autowired
	private SysDictMapper sysDictMapper;

	@Override
	public SysDict selectByType(SysDict sysDict) {
		SysDict result = sysDictMapper.selectByType(sysDict);
		return result;
	}


	@Override
	public Boolean editDictByTypeValue(SysDict sysDict) {
		return sysDictMapper.editDictByTypeValue(sysDict);
	}
}

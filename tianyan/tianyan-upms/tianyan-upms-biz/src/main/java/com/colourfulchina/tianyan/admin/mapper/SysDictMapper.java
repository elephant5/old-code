

package com.colourfulchina.tianyan.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.tianyan.admin.api.entity.SysDict;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 */
public interface SysDictMapper extends BaseMapper<SysDict> {
	SysDict selectByType(SysDict sysDict);

	Boolean editDictByTypeValue(SysDict sysDict);
}



package com.colourfulchina.tianyan.admin.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.tianyan.admin.api.entity.SysDict;

/**
 * <p>
 * 字典表 服务类
 * </p>
 */
public interface SysDictService extends IService<SysDict> {
	SysDict selectByType(SysDict sysDict);

	Boolean editDictByTypeValue(SysDict sysDict);
}

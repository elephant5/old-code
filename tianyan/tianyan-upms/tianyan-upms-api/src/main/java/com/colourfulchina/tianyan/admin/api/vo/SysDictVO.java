package com.colourfulchina.tianyan.admin.api.vo;

import com.colourfulchina.tianyan.admin.api.entity.SysDict;
import lombok.Data;

import java.io.Serializable;

/**
 * 字典项返回对象
 *
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/2/28 14:56
 */
@Data
public class SysDictVO implements Serializable {
	private SysDict sysDict;
	private Integer maxValue;
}

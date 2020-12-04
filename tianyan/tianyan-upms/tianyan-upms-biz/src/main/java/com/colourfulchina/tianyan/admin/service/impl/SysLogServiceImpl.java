

package com.colourfulchina.tianyan.admin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.tianyan.admin.api.entity.SysLog;
import com.colourfulchina.tianyan.admin.mapper.SysLogMapper;
import com.colourfulchina.tianyan.admin.service.SysLogService;
import com.colourfulchina.tianyan.common.core.constant.CommonConstant;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 日志表 服务实现类
 * </p>
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

	@Override
	public Boolean updateByLogId(Long id) {

		SysLog sysLog = new SysLog();
		sysLog.setId(id);
		sysLog.setDelFlag(CommonConstant.STATUS_DEL);
		sysLog.setUpdateTime(new Date());
		return updateById(sysLog);
	}
}

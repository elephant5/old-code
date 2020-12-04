

package com.colourfulchina.tianyan.admin.api.feign.fallback;

import com.colourfulchina.tianyan.admin.api.entity.SysLog;
import com.colourfulchina.tianyan.admin.api.feign.RemoteLogService;
import com.colourfulchina.tianyan.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoteLogServiceFallbackImpl implements RemoteLogService {

	/**
	 * 保存日志
	 *
	 * @param sysLog
	 * @return R
	 */
	@Override
	public R<Boolean> saveLog(SysLog sysLog) {
		log.error("feign 插入日志失败:{}");
		return null;
	}
}

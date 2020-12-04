

package com.colourfulchina.tianyan.admin.api.feign;

import com.colourfulchina.tianyan.admin.api.entity.SysLog;
import com.colourfulchina.tianyan.admin.api.feign.fallback.RemoteLogServiceFallbackImpl;
import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.tianyan.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = ServiceNameConstant.UMPS_SERVICE, fallback = RemoteLogServiceFallbackImpl.class)
public interface RemoteLogService {
	/**
	 * 保存日志
	 *
	 * @param sysLog 日志实体
	 * @return succes、false
	 */
	@PostMapping("/log")
//	@RequestMapping(value = "/log",method = RequestMethod.POST)
//	@RequestLine(value = "POST /log")
	R<Boolean> saveLog(@RequestBody SysLog sysLog);
}

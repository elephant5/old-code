

package com.colourfulchina.god.door.api.aspect;

import com.alibaba.fastjson.JSON;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.event.SysGodDoorLogEvent;
import com.colourfulchina.god.door.api.util.SysGodDoorLogUtils;
import com.colourfulchina.tianyan.common.core.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * 操作日志使用spring event异步入库
 *
 */
@Aspect
@Slf4j
public class SysGodDoorLogAspect {

	@Around("@annotation(sysGodDoorLog)")
	public Object aroundWxApi(ProceedingJoinPoint point, SysGodDoorLog sysGodDoorLog) throws Throwable {
		String strClassName = point.getTarget().getClass().getName();
		String strMethodName = point.getSignature().getName();
		log.debug("[类名]:{},[方法]:{}", strClassName, strMethodName);
		com.colourfulchina.tianyan.admin.api.entity.SysLog logVo = SysGodDoorLogUtils.getSysLog();
		logVo.setTitle(sysGodDoorLog.value());
		// 发送异步日志事件
		Long startTime = System.currentTimeMillis();
		Object obj = point.proceed();
		final Object[] args = point.getArgs();
		if (args != null && args.length>1){
			logVo.setArgs(JSON.toJSONString(args[0]));
		}
		Long endTime = System.currentTimeMillis();
		logVo.setTime(endTime - startTime);
		SpringContextHolder.publishEvent(new SysGodDoorLogEvent(logVo));
		return obj;
	}

}

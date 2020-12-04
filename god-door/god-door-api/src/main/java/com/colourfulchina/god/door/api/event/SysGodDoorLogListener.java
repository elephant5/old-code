

package com.colourfulchina.god.door.api.event;

import com.colourfulchina.tianyan.admin.api.entity.SysLog;
import com.colourfulchina.tianyan.admin.api.feign.RemoteLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;


/**
 *
 * 异步监听日志事件
 */
@Slf4j
@AllArgsConstructor
public class SysGodDoorLogListener {
	private final RemoteLogService remoteLogService;

	@Async
	@Order
	@EventListener(SysGodDoorLogEvent.class)
	public void saveSysLog(SysGodDoorLogEvent event) {
		SysLog sysLog = (SysLog) event.getSource();
		remoteLogService.saveLog(sysLog);
	}
}

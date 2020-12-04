

package com.colourfulchina.god.door.api.event;

import com.colourfulchina.tianyan.admin.api.entity.SysLog;
import org.springframework.context.ApplicationEvent;

/**
 *
 * 系统日志事件
 */
public class SysGodDoorLogEvent extends ApplicationEvent {

	public SysGodDoorLogEvent(SysLog source) {
		super(source);
	}
}

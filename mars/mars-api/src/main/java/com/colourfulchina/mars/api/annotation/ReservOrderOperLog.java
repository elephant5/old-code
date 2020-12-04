

package com.colourfulchina.mars.api.annotation;

import java.lang.annotation.*;

/**
 *
 * 预约单操作日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReservOrderOperLog {
	String value();
}

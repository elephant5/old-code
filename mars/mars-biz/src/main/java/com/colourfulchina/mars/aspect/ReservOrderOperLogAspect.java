package com.colourfulchina.mars.aspect;

import com.alibaba.fastjson.JSONObject;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.god.door.api.util.SysGodDoorLogUtils;
import com.colourfulchina.mars.api.annotation.ReservOrderOperLog;
import com.colourfulchina.mars.api.constant.OrderConstant;
import com.colourfulchina.mars.api.entity.ReservOrderLog;
import com.colourfulchina.mars.mapper.ReservOrderLogMapper;
import com.colourfulchina.mars.service.ReservOrderLogService;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/7/31 15:44
 */
@Aspect
@Slf4j
@Component
public class ReservOrderOperLogAspect {
    @Autowired
    private ReservOrderLogService reservOrderLogService;
    @Around("@annotation(reservOrderOperLog)")
    public Object aroundWxApi(ProceedingJoinPoint point, ReservOrderOperLog reservOrderOperLog) throws Throwable {
        String strClassName = point.getTarget().getClass().getName();
        String strMethodName = point.getSignature().getName();
        log.info("[类名]:{},[方法]:{}", strClassName, strMethodName);
        Object obj = point.proceed();
        try {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(obj));
            //成功才保存日志
            log.info("操作结果code:{}",json.getInteger("code"));
            if(json!=null && json.getInteger("code")==100){
                reservOrderLogService.insertLog(point,reservOrderOperLog,json.getJSONObject("result"));
            }
        }catch (Exception e){
            log.error("日志插入失败",e);
        }
        return obj;
    }
}

package com.colourfulchina.mars.aspect;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.colourfulchina.inf.base.dto.SysOperateLogInfoDto;
import com.colourfulchina.inf.base.enums.SysOperateLogEnums;
import com.colourfulchina.mars.event.SysOperateLogEvent;
import com.colourfulchina.mars.service.SysOperateLogService;
import com.colourfulchina.tianyan.common.core.util.SpringContextHolder;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

//@Component
@Aspect
@Slf4j
public class SysOperateLogAspect {
    private static final Set<String> ASPECT_METHOD_PREFIX=Sets.newHashSet();
    private static final Set<String> EXCLUDE_TABLES=Sets.newHashSet();
    private static final String INSERT="insert";
    private static final String UPDATE="update";
    private static final String DELETE="delete";
    private static final String SELECT="select";
    static {
        ASPECT_METHOD_PREFIX.add(INSERT);
        ASPECT_METHOD_PREFIX.add(UPDATE);
        //因为delete暂时无法获取到操作的表 且系统不存在物理删除 暂不拦截delete
//        ASPECT_METHOD_PREFIX.add(DELETE);
        //暂不拦截查询方法
//        ASPECT_METHOD_PREFIX.add(SELECT);
        EXCLUDE_TABLES.add("sys_operate_log");
        EXCLUDE_TABLES.add("sys_operate_log_info");
        EXCLUDE_TABLES.add("sys_operate_log_detail");
    }
    @Autowired
    private SysOperateLogService sysOperateLogService;

    @Pointcut("execution(* com.baomidou.mybatisplus.mapper.*.*(..))")
    public void logBaseMapperPointcut() {
        log.debug("logBaseMapperPointcut");
    }

    @Pointcut("execution(* com.baomidou.mybatisplus.service.*.*Batch*(..))")
    public void logBaseServicePointcut() {
        log.debug("logBaseServicePointcut");
    }

    @Pointcut("execution(* com.colourfulchina.*.*.mapper.*(..))")
    public void logMapperPointcut() {
        log.debug("logMapperPointcut");
    }

    @Pointcut("execution(* com.colourfulchina.*.*.service.*.*(..))")
    public void logServicePointcut() {
        log.debug("logServicePointcut");
    }

   /* @Pointcut("@annotation(sysOperateLog)")
    public void sysOperateLogPointcut(ProceedingJoinPoint point,SysOperateLog sysOperateLog){
        log.info("sysOperateLogPointcut");
    }*/
    @Pointcut("logBaseMapperPointcut() || logBaseServicePointcut()")
    public void logPointcut() {
        log.debug("logPointcut");
    }

    @Around("logPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        Object result = null;
        try {
            final Signature signature = joinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            final String signatureName = signature.getName();
            log.debug(signatureName);
            if (!containsMethod(signatureName)){
                log.debug("methodName {} is not contains in ASPECT_METHOD_PREFIX");
                return joinPoint.proceed();
            }
            final Object[] joinPointArgs = joinPoint.getArgs();
            //同时insert、update、delete的表只有一个、数据只有一条(不支持非主键操作)
            Object tableNameObject=null;
            Field tableIdField=null;
            SysOperateLogInfoDto operateLogInfoDto=null;
            for (Object arg:joinPointArgs){
                log.debug(arg.toString());
                final Class<?> argClass = arg.getClass();
                final TableName tableName = argClass.getAnnotation(TableName.class);
                if (tableName == null){
                    log.debug("未找到TableName注解");
                }else {
                    String table= tableName.value();
                    if (isExcludeTable(table)){
                        break;
                    }
                    operateLogInfoDto=new SysOperateLogInfoDto();
                    operateLogInfoDto.setTableName(table);
                    tableNameObject=arg;
                    final Field[] fields = argClass.getDeclaredFields();
                    for (Field field:fields){
                        log.debug("field:{}",field.getName());
                        final Annotation[] fieldDeclaredAnnotations = field.getDeclaredAnnotations();
                        /*for (Annotation fieldDeclaredAnnotation:fieldDeclaredAnnotations){
                            log.info("fieldDeclaredAnnotation:{}",fieldDeclaredAnnotation.annotationType());
                            if (fieldDeclaredAnnotation.annotationType().equals(TableId.class)){
                                TableId tableId=(TableId)fieldDeclaredAnnotation;
                                log.info(tableId.value());
                            }else if (fieldDeclaredAnnotation.annotationType().equals(TableField.class)){
                                TableField tableField=(TableField)fieldDeclaredAnnotation;
                                log.info(tableField.value());
                            }
                        }*/
                        final TableId tableId = field.getDeclaredAnnotation(TableId.class);
                        if (tableId != null){
                            tableIdField=field;
                            operateLogInfoDto.setTableId(tableId.value());
                            field.setAccessible(true);
                            final Object fieldValue = field.get(arg);
                            operateLogInfoDto.setRowKey(fieldValue);
                        }
                        log.debug("operateLogInfoDto:{}",JSON.toJSONString(operateLogInfoDto));
                        if (!isInsertMethod(signatureName) && operateLogInfoDto.getTableId() != null && operateLogInfoDto.getRowKey() != null){
                            Map<String, Object> beforeValue = sysOperateLogService.dynamicSelectById(operateLogInfoDto);
                            log.debug(beforeValue.toString());
                            operateLogInfoDto.setBefore(beforeValue);
                            break;
                        }
                    }

                }
            }
            result = joinPoint.proceed();
            if (operateLogInfoDto != null){
                //如果是新增方法 则在操作后查询出主键的值
                operateLogInfoDto.setType(SysOperateLogEnums.Type.getTypeCode(signatureName));
                if (isInsertMethod(signatureName) && null != tableNameObject && null != tableIdField){
                    operateLogInfoDto.setRowKey(tableIdField.get(tableNameObject));
                }else{
                    return result;
                }
                final Map<String, Object> afterValue = sysOperateLogService.dynamicSelectById(operateLogInfoDto);
                log.debug(afterValue.toString());
                operateLogInfoDto.setAfter(afterValue);
                SpringContextHolder.publishEvent(new SysOperateLogEvent(operateLogInfoDto));
            }
        } catch (Throwable throwable) {
            log.error("数据库操作失败:{}",throwable.getMessage(),throwable);
        } finally {
            log.debug("finally");
        }
        return result;
    }

    @Before("logPointcut()")
    public void doBefore(JoinPoint joinPoint) {
        log.debug("doBefore...");
    }

    @After("logPointcut()")
    public void doAfter(JoinPoint joinPoints) {
        log.debug("doAfter...");
    }
    @AfterReturning(returning = "ret", pointcut = "logPointcut()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        log.debug("doAfterReturning: {}" ,ret);
    }
    //后置异常通知
    @AfterThrowing(throwing = "e",pointcut = "logPointcut()")
    public void doAfterThrowing(JoinPoint jp,Exception e){
        log.debug("doAfterThrowing...",e);
    }

    /**
     * 判断调用方法是否为要拦截的方法
     * @param methodName
     * @return
     */
    private static boolean containsMethod(String methodName){
        for (String method:ASPECT_METHOD_PREFIX){
            if (methodName.startsWith(method)){
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为insert方法
     * @param methodName
     * @return
     */
    private static boolean isInsertMethod(String methodName){
        if (methodName.contains(INSERT)){
            return true;
        }
        return false;
    }

    /**
     * 判断是否为要排除的表
     * @param tableName
     * @return
     */
    private static boolean isExcludeTable(String tableName){
        if (EXCLUDE_TABLES.contains(tableName)){
            return true;
        }
        return false;
    }
}
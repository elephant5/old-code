package com.colourfulchina.pangu.taishang.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.util.Properties;

@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
@Slf4j
public class LogInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        log.info("into intercept");
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        log.info("into plugin");
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        log.info("into setProperties");
    }
}

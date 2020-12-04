package com.colourfulchina.god.door.api.interceptor;

import com.alibaba.fastjson.JSON;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.List;

/**
 * 登录认证
 */
@Slf4j
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Value("#{'${sys.sec.ignoreUAList}'.split(',')}")
    private List<String> ignoreUAList;
    @Value("#{'${sys.sec.ignoreOriginList}'.split(',')}")
    private List<String> ignoreOriginList;

    private static final String UA="User-Agent";
    private static final String ORIGIN="Origin";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//    	log.info("===========运行登录拦截器===========");
        final Enumeration<String> headerNames = request.getHeaderNames();
        String userAgent=null;
        String origin=null;
        while (headerNames.hasMoreElements()){
            final String name = headerNames.nextElement();
            final String header = request.getHeader(name);
            log.debug("name:{},header:{}",name,header);
            if (name.equals(UA)){
                userAgent=header;
            }
            if (name.equals(ORIGIN)){
                origin=header;
            }
        }
        log.debug("----------------------------------------------------");
        final Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie:cookies){
                log.debug("name:{},value:{}",cookie.getName(),cookie.getValue());
            }
        }
        final String user = SecurityUtils.getLoginName();
        if(user != null) {
            log.info("user:{}", JSON.toJSONString(user));
            return true;
        }
        if (unlimited(userAgent,origin)){
            return true;
        }
        return false;
    }

    /**
     * 放行拦截
     * @param userAgent
     * @param origin
     * @return
     */
    private boolean unlimited(String userAgent,String origin){
        if (unlimitedUA(userAgent)){
            return true;
        }
        if (unlimitedOrigin(origin)){
            return true;
        }
        return false;
    }

    /**
     * 放行ua拦截
     * @param userAgent
     * @return
     */
    private boolean unlimitedUA(String userAgent){
        if (!CollectionUtils.isEmpty(ignoreUAList) && StringUtils.isNotBlank(userAgent)){
            for (String ua : ignoreUAList){
                if (userAgent.startsWith(ua)){
                    log.debug("userAgent is unlimited:{}", userAgent);
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 放行Origin拦截
     * @param origin
     * @return
     */
    private boolean unlimitedOrigin(String origin){
        if (!CollectionUtils.isEmpty(ignoreOriginList) && StringUtils.isNotBlank(origin)){
            for (String ori : ignoreOriginList){
                if (origin.contains(ori)){
                    log.info("origin is unlimited:{}", origin);
                    return true;
                }
            }
        }
        return false;
    }
}

package com.colourfulchina.mars.utils;

import lombok.val;
import org.springframework.lang.Nullable;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by jill on 2018/3/30.
 */
public class BHttpUtils {	

    public static String getRemoteIp(ServletRequest request) {
        String ip = null;
        if (request instanceof HttpServletRequest) {
            val httpReq = (HttpServletRequest) request;
            ip = httpReq.getHeader("g-remote-ip");
            if (null == ip || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = httpReq.getHeader("X-Forwarded-For");
            }
            if (null == ip || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = httpReq.getHeader("Proxy-Client-IP");
            }
            if (null == ip || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = httpReq.getHeader("WL-Proxy-Client-IP");
            }
            if (null == ip || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = httpReq.getHeader("HTTP_CLIENT_IP");
            }
            if (null == ip || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = httpReq.getHeader("HTTP_X_FORWARDED_FOR");
            }
        }
        if (null == ip || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            return ip.split(",")[0];
        } else {
            return ip;
        }
    }

    @Nullable
    public static String getDeviceTokenCookie(HttpServletRequest request) {
        val cookies = request.getCookies();
        if (null != cookies && cookies.length > 0) {
            for(val cookie : cookies) {
                if (cookie.getName().equals(BHttpConstant.COOKIE_DEVICE_KEY)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static void setDeviceTokenCookie(HttpServletResponse response, String deviceToken) {
        response.setHeader(BHttpConstant.HEAD_X_DEVICE_TOKEN, deviceToken);
        val cookie = new Cookie(BHttpConstant.COOKIE_DEVICE_KEY, deviceToken);
        cookie.setPath("/");
        cookie.setMaxAge(36000000);
        response.addCookie(cookie);
    }

    public static void setLoginTokenCookie(HttpServletResponse response, String token, int expire) {
        val cookie = new Cookie(BHttpConstant.COOKIE_USER_TOKEN_KEY, token);
        cookie.setPath("/");
        cookie.setMaxAge(expire);
        response.addCookie(cookie);
    }

    public static void clearLoginTokenCookie(HttpServletResponse response) {
        val cookie = new Cookie(BHttpConstant.COOKIE_USER_TOKEN_KEY, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}

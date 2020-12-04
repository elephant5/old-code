package com.colourfulchina.god.door.api.util;

import com.alibaba.fastjson.JSON;
import com.colourfulchina.god.door.api.vo.KltSysUser;
import com.colourfulchina.god.door.api.vo.SysToken;
import com.colourfulchina.inf.base.encrypt.EncryptAES;
import com.colourfulchina.inf.base.encrypt.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Name SecurityUtil
 * @Descr 用户上下文对象：设置和获取HttpSession登录的用户
 * @author yanbin
 * @date 2018年04月27日
 */
@Slf4j
@Component
//@AllArgsConstructor
public class SecurityUtils {
    @Autowired
    private ValueOperations<String,String> valueOperations;
    private static ValueOperations<String,String> operations;
    private static String domain;

	@Value("${sys.sec.cookieDomain}")
	private String cookieDomain;

    @PostConstruct
    public void postConstruct(){
        operations=valueOperations;
		domain=cookieDomain;
    }

    private enum Flag{
    	USER_NAME(1),
		LOGIN_NAME(2),
		USER_INFO(3),
		;
		private int value;
		Flag(int value) {
			this.value = value;
		}
	}

	public static final String SECSYSUSER = "secSysUser";

	public static final String SECSYSUSER_NAME = "secSysUserAndName";

	public static final String SECSYSUSER_INFO = "secSysUserInfo";

	public static final String COOKIE_TOKEN="token";
	public static final String COOKIE_REFRESH_TOKEN="refreshToken";
	private static final Integer TIMEOUT=4;
	public static void setTokenAndUser(KltSysUser sysUser,HttpServletRequest request) {
	    if (sysUser != null){
	        String tokenStr= MD5Utils.MD5Encode(UUID.randomUUID().toString());
            final String encryptToken = EncryptAES.encrypt(tokenStr, SECSYSUSER);
            String refreshTokenStr= MD5Utils.MD5Encode(encryptToken);
            Cookie token=new Cookie(COOKIE_TOKEN,tokenStr);
            token.setDomain(domain);
            token.setPath("/");
            getResponse().addCookie(token);
            Cookie refreshToken=new Cookie(COOKIE_REFRESH_TOKEN,refreshTokenStr);
            refreshToken.setDomain(domain);
            refreshToken.setPath("/");
            getResponse().addCookie(refreshToken);
			final String userName = sysUser.getLoginname();
			final String realname = sysUser.getRealname();
			String userAndName=userName;
			if (StringUtils.isNotBlank(realname)){
				userAndName+="-"+realname;
			}
			operations.set(SECSYSUSER+tokenStr, userName,TIMEOUT, TimeUnit.HOURS);
			operations.set(SECSYSUSER_NAME +tokenStr, userAndName,TIMEOUT, TimeUnit.HOURS);
			operations.set(SECSYSUSER_INFO +tokenStr, JSON.toJSONString(sysUser),TIMEOUT, TimeUnit.HOURS);
            operations.set(SECSYSUSER+refreshTokenStr,encryptToken,TIMEOUT, TimeUnit.HOURS);
            getsession(request).setAttribute(SECSYSUSER, sysUser);
        }
	}

	public static void removeTokenAndUser(HttpServletRequest request) {
		final SysToken sysToken = getSysToken(request);
		if (validateToken(sysToken)){
			//由于validateToken为true时 这两个key一定存在 故无需在验证
			operations.getOperations().delete(SECSYSUSER+sysToken.getToken());
			operations.getOperations().delete(SECSYSUSER_NAME +sysToken.getToken());
			operations.getOperations().delete(SECSYSUSER_INFO +sysToken.getToken());
			operations.getOperations().delete(SECSYSUSER+sysToken.getRefreshToken());
			getsession(request).removeAttribute(SECSYSUSER);
		}
	}

	public static KltSysUser getAttributeUser(){
		return getAttributeUser(null);
	}
	public static KltSysUser getAttributeUser(HttpServletRequest request){
		final SysToken sysToken = getSysToken(request);
		final String userInfo = getUserNameOrLoginName(sysToken, Flag.USER_INFO.value);
		KltSysUser kltSysUser = null;
		if (StringUtils.isNotBlank(userInfo)){
			kltSysUser = JSON.parseObject(userInfo, KltSysUser.class);
		}
		return kltSysUser;
	}
	/**
	 * 获取登录名+工号+真实姓名
	 * @return
	 */
	public static String getLoginName() {
        return getLoginName(null);
	}

	public static String getLoginName(HttpServletRequest request) {
		final SysToken sysToken = getSysToken(request);
        return getUserNameOrLoginName(sysToken,Flag.LOGIN_NAME.value);
	}

	/**
	 * 获取登录名
	 * @return
	 */
	public static String getUserName() {
		return getUserName(null);
	}
	public static String getUserName(HttpServletRequest request) {
		final SysToken sysToken = getSysToken(request);

        return getUserNameOrLoginName(sysToken,Flag.USER_NAME.value);
	}

	/**
	 * 获取登录名或登录名+工号
	 * @param sysToken
	 * @param flag com.colourfulchina.god.door.api.util.SecurityUtils.Flag
	 * @return
	 */
	private static String getUserNameOrLoginName(SysToken sysToken,int flag){
		String userName = null;
		String loginName = null;
		String userInfo=null;
		if (validateToken(sysToken)){
			userName = operations.get(SECSYSUSER + sysToken.getToken());
			loginName = operations.get(SECSYSUSER_NAME + sysToken.getToken());
			userInfo = operations.get(SECSYSUSER_INFO + sysToken.getToken());
			//更新有效期
			operations.set(SECSYSUSER+sysToken.getToken(),userName,TIMEOUT, TimeUnit.HOURS);
			operations.set(SECSYSUSER_NAME +sysToken.getToken(),loginName,TIMEOUT, TimeUnit.HOURS);
			operations.set(SECSYSUSER_INFO +sysToken.getToken(),userInfo,TIMEOUT, TimeUnit.HOURS);
			operations.set(SECSYSUSER+sysToken.getRefreshToken(),EncryptAES.encrypt(sysToken.getToken(), SECSYSUSER),TIMEOUT, TimeUnit.HOURS);
		}
		if (flag==Flag.USER_NAME.value){
			return userName;
		}
		if (flag==Flag.LOGIN_NAME.value){
			return loginName;
		}
		if (flag==Flag.USER_INFO.value){
			return userInfo;
		}
		return userName;
	}
	private static boolean validateToken(SysToken sysToken){
		if (sysToken != null && StringUtils.isNotBlank(sysToken.getToken()) && StringUtils.isNotBlank(sysToken.getRefreshToken())){
			final String refreshToken = operations.get(SECSYSUSER+sysToken.getRefreshToken());
			if (StringUtils.isNotBlank(refreshToken)){
				final String decryptToken = EncryptAES.decrypt(refreshToken, SECSYSUSER);
				if (StringUtils.isNotBlank(decryptToken)&&decryptToken.equals(sysToken.getToken())){
					return true;
				}
			}
		}
		return false;
	}

	public static HttpSession getsession(HttpServletRequest request) {
		return getRequest(request).getSession();
	}

	public static HttpServletRequest getRequest(HttpServletRequest request) {
		if (request == null){
			request = getServletRequestAttributes().getRequest();
		}
		final Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()){
			final String name = headerNames.nextElement();
			final String header = request.getHeader(name);
			log.debug("name:{},header:{}",name,header);
		}
		log.debug("----------------------------------------------------");
		final Cookie[] cookies = request.getCookies();
		if (cookies != null){
			for (Cookie cookie:cookies){
				log.debug("name:{},value:{}",cookie.getName(),cookie.getValue());
			}
		}
		return request;
	}
	public static SysToken getSysToken(HttpServletRequest request){
        SysToken sysToken=new SysToken();
		final Cookie[] cookies = getRequest(request).getCookies();
        if (cookies != null){
            for (Cookie cookie:cookies){
                if (cookie.getName().equals(COOKIE_TOKEN)){
                    sysToken.setToken(cookie.getValue());
                }
                if (cookie.getName().equals(COOKIE_REFRESH_TOKEN)){
                    sysToken.setRefreshToken(cookie.getValue());
                }
            }
        }
        return sysToken;
    }
	public static HttpServletResponse getResponse(){
		HttpServletResponse response = getServletRequestAttributes().getResponse();
        return response;
    }
    public static ServletRequestAttributes getServletRequestAttributes(){
		return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	}
}
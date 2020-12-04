package com.colourfulchina.mars.controller;

import com.colourfulchina.mars.utils.BHttpConstant;
import com.colourfulchina.mars.utils.BHttpUtils;
import com.colourfulchina.member.api.res.MemLoginResDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

/**    
 * @ClassName: BaseController  
 * @Description: 客乐芙权益商城-会员登录API 
 * @author Sunny  
 * @date 2018年12月20日14:43:28
 * @company Colourful@copyright(c) 2018
 * @version V1.0    
*/
public class BaseController {
	
	@Autowired(required = false)
    protected HttpServletRequest request;

    protected String getRemoteIp() {
        return BHttpUtils.getRemoteIp(request);
    }
    
    @SuppressWarnings("rawtypes")
	@Autowired
    protected RedisTemplate redisTemplate;

    @SuppressWarnings("unchecked")
	protected MemLoginResDTO getLoginUser() throws Exception {
    	//获取请求头token
    	String token = request.getHeader(BHttpConstant.REQ_ATTR_LOGIN_FRO_MEM);
        if (null == token||"".equals(token)) {
            Cookie[] cookies = request.getCookies();
            if (null != cookies) {
                Optional<Cookie> myCookie = Arrays.stream(cookies).parallel().filter(cookie -> cookie.getName().equals(BHttpConstant.COOKIE_MEM_TOKEN_KEY))
                        .limit(1).findFirst();
                if(myCookie.isPresent()) {
                    token = myCookie.get().getValue();
                }
            }
        }
        if(token!=null && !token.isEmpty()) {
        	// 解密 TOKEN 校验身份
//        	String decrypt = EncryptAES.decrypt(token, IpMacAddressUtil.getMacAddress(HttpUtil.getClientIP(request, null).split(",")[0]));
//	        if(decrypt==null){
//	        	throw new Exception("请重新登录!");
//	        }
        	MemLoginResDTO loginUser = (MemLoginResDTO) redisTemplate.opsForValue().get("MEM_"+token);
        	if(loginUser!=null) {
        		//大于-1
        		if(loginUser.getExpireTime().compareTo(new Date()) > -1) {
        			return loginUser;
        		} else {
        			//清除过期的token
        			redisTemplate.delete("MEM_"+token);
                	throw new Exception("请重新登录!");
                }
        	} else {
            	throw new Exception("请重新登录!");
            }
        }else {
        	throw new Exception("请重新登录!");
        }
    }
	
}

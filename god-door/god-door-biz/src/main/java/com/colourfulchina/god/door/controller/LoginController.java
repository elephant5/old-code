package com.colourfulchina.god.door.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.god.door.api.vo.KltSysFunction;
import com.colourfulchina.god.door.api.vo.KltSysMenus;
import com.colourfulchina.god.door.api.vo.KltSysUser;
import com.colourfulchina.god.door.api.vo.LoginReqVo;
import com.colourfulchina.god.door.config.KltAuthProperties;
import com.colourfulchina.inf.base.encrypt.EncryptUtils;
import com.colourfulchina.inf.base.encrypt.MD5Utils;
import com.colourfulchina.inf.base.utils.HttpUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/login")
@Slf4j
@Api(tags = {"登录接口"})
public class LoginController {
    @Autowired
    private KltAuthProperties kltAuthProperties;

    private static String GET_USER_URL = null;
    private static String GET_MENU_URL = null;
    private static String GET_BUTTON_URL = null;
    public static String SYS_KEY = null;
    public static String S3DES_MENU_KEY = null;
    public static String S3DES_INFO_KEY = null;
    public static Map<String,String> SYS_APP_MAP = null;
    private static final String LOGIN_NAME_PARAM = "loginName";
    private static final String SYS_APP_ID_PARAM = "appId";
    private static final String TIMEDATE = "timestamp";

    @PostConstruct
    public void postConstruct(){
        GET_USER_URL = kltAuthProperties.getBaseUrl()+ kltAuthProperties.getUserUrl();
        GET_MENU_URL = kltAuthProperties.getBaseUrl()+ kltAuthProperties.getMenuUrl();
        GET_BUTTON_URL = kltAuthProperties.getBaseUrl()+ kltAuthProperties.getButtonUrl();
        SYS_KEY = kltAuthProperties.getSysKey();
        S3DES_MENU_KEY = kltAuthProperties.getS3desKeyMenu();
        S3DES_INFO_KEY = kltAuthProperties.getS3desKeyInfo();
        SYS_APP_MAP = kltAuthProperties.getSysAppMap();
    }

    @SysGodDoorLog("登录验证")
    @ApiOperation("登录验证")
    @PostMapping("/login")
    public CommonResultVo<KltSysUser> login(@RequestBody LoginReqVo loginReqVo,HttpServletRequest request){
        CommonResultVo<KltSysUser> result = new CommonResultVo();
        try {
            Assert.notNull(loginReqVo,"参数不能为空");
            Assert.notNull(loginReqVo.getUserName(),"用户名不能为空");
            Assert.notNull(loginReqVo.getPassword(),"密码不能为空");
            //调用开联通接口根据用户名获取用户登录验证
            JSONObject json = new JSONObject();
            json.put(LOGIN_NAME_PARAM,loginReqVo.getUserName());
            String timeDate = DateUtil.format(new Date(),"yyyyMMddHHmmss");
            json.put(TIMEDATE,timeDate );
            //签名串格式：String signstr ="&loginName="+loginName+"&timestamp="+timestamp;
            String signStr = "loginName="+loginReqVo.getUserName()+"&timestamp="+timeDate+"";
            String sign =MD5Utils.MD5Encode(MD5Utils.MD5Encode(signStr)+SYS_KEY);
            json.put("sign",sign);
            log.info("GET_USER_URL:{}",GET_USER_URL);
            String  rest = HttpUtils.sendPost(GET_USER_URL,json.toString());
            log.info("rest:{}",rest);
            JSONObject rowData = JSONObject.parseObject(rest);
            String userStr = EncryptUtils.getDESStr(rowData.get("data")+"",S3DES_INFO_KEY);
            if (StringUtils.isNotBlank(userStr)){
                KltSysUser user = JSON.parseObject(userStr).toJavaObject(KltSysUser.class);
                if (user != null){
                    if (user.getStatus() == 1) {
                        if (StringUtils.isBlank(user.getJobno())) {
                            log.info("用户工号不存在");
                            result.setCode(200);
                            result.setMsg("用户工号不存在");
                        }else {
                            String inPassWord = MD5Utils.MD5Encode(loginReqVo.getPassword());
                            String correctPassWord = user.getPassword();
                            if (!inPassWord.equalsIgnoreCase(correctPassWord)) {
                                log.info("用户{}密码错误", loginReqVo.getUserName());
                                result.setCode(200);
                                result.setMsg("用户密码错误");
                            }else {
                                result.setResult(user);
//                                SecurityUtils.setUser(user);
                                SecurityUtils.setTokenAndUser(user,request);
                            }
                        }
                    }else {
                        log.info("用户{}已失效",loginReqVo.getUserName());
                        result.setCode(200);
                        result.setMsg("用户已失效");
                    }
                }
            }else {
                log.info("用户{}不存在",loginReqVo.getUserName());
                result.setCode(200);
                result.setMsg("用户不存在");
            }
        }catch (Exception e){
            log.error("登录失败！",e);
            result.setCode(200);
            result.setMsg("登录失败!");
        }
        return result;
    }

    @SysGodDoorLog("退出登录")
    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public CommonResultVo<KltSysUser> logout(HttpServletRequest request){
        CommonResultVo<KltSysUser> result = new CommonResultVo();
        try {
            SecurityUtils.removeTokenAndUser(request);
        }catch (Exception e){
            log.error("登录失败！",e);
            result.setCode(200);
            result.setMsg("登录失败!");
        }
        return result;
    }

    @SysGodDoorLog("查询用户菜单")
    @ApiOperation("查询用户菜单")
    @GetMapping("/userMenus/{name}")
    public CommonResultVo<List<KltSysMenus>> userMenus(@PathVariable String name,HttpServletRequest request){
        CommonResultVo<List<KltSysMenus>> result = new CommonResultVo();
        final List<KltSysMenus> list = Lists.newLinkedList();
        try {
            log.info("userMenus sessionId:{}",SecurityUtils.getsession(request).getId());
            log.info("userMenus AttributeUser:{}",JSON.toJSONString(SecurityUtils.getAttributeUser()));
            log.info("userMenus userName2:{}",SecurityUtils.getLoginName(request));
            log.info("userMenus name:{}",name);
            Assert.notEmpty(SYS_APP_MAP,"系统APPID映射为空");
            Assert.isTrue(SYS_APP_MAP.containsKey(name),"系统名称对应的APPID不存在");
            final String appId=SYS_APP_MAP.get(name);
            log.info("map:{}",JSONObject.toJSONString(SYS_APP_MAP));
            log.info("appid:{}",appId);
            String userName = SecurityUtils.getUserName();
            Assert.notNull(userName,"用户名不能为空");
            JSONObject json = new JSONObject();
            json.put(LOGIN_NAME_PARAM, userName);
            json.put(SYS_APP_ID_PARAM, appId);
            String timeDate = DateUtil.format(new Date(),"yyyyMMddHHmmss");
            json.put(TIMEDATE,timeDate );
            //签名串格式：String signstr = "appId="+appId+"&loginName="+loginName+"&timestamp="+timestamp;
            String signStr = "appId="+appId+"&loginName="+userName+"&timestamp="+timeDate+"";
            String sign =MD5Utils.MD5Encode(MD5Utils.MD5Encode(signStr)+SYS_KEY);
            json.put("sign",sign);
            String  rest = HttpUtils.sendPost(GET_MENU_URL,json.toString());
            JSONObject rowData = JSONObject.parseObject(rest);
            String menuStr = EncryptUtils.getDESStr(rowData.get("data")+"",S3DES_MENU_KEY);
            List<KltSysFunction> menus = JSON.parseArray(menuStr,KltSysFunction.class);
            if (!CollectionUtils.isEmpty(menus)){
                for (KltSysFunction menu : menus) {
                    if (menu.getPid() == null){
                        KltSysMenus fath = new KltSysMenus();
                        BeanUtils.copyProperties(menu,fath);
                        list.add(fath);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(list)){
                for (KltSysMenus fa : list) {
                    List<KltSysFunction> sons = Lists.newLinkedList();
                    for (KltSysFunction son : menus) {
                        if (son.getPid() != null && son.getPid().equals(fa.getId())){
                            sons.add(son);
                        }
                    }
                    fa.setSonMenus(sons);
                }
            }
            result.setResult(list);
        }catch (Exception e){
            log.error("用户菜单查询失败",e);
            result.setCode(200);
            result.setMsg("用户菜单查询失败");
        }
        return result;
    }

    @SysGodDoorLog("查询用户菜单按钮")
    @ApiOperation("查询用户菜单按钮")
    @GetMapping("/userButtons/{name}")
    public CommonResultVo<List<KltSysFunction>> userButtons(@PathVariable String name,HttpServletRequest request){
        CommonResultVo<List<KltSysFunction>> result = new CommonResultVo();
        final List<KltSysFunction> list = Lists.newLinkedList();
        try {
            log.info("userButtons sessionId:{}",SecurityUtils.getsession(request).getId());
            log.info("userMenus AttributeUser:{}",JSON.toJSONString(SecurityUtils.getAttributeUser()));
            log.info("userButtons userName2:{}",SecurityUtils.getLoginName(request));
            log.info("userButtons name:{}",name);
            Assert.notEmpty(SYS_APP_MAP,"系统APPID映射为空");
            Assert.isTrue(SYS_APP_MAP.containsKey(name),"系统名称对应的APPID不存在");
            final String appId=SYS_APP_MAP.get(name);
            String userName = SecurityUtils.getUserName();
            Assert.notNull(userName,"用户名不能为空");
            JSONObject json = new JSONObject();
            json.put(LOGIN_NAME_PARAM, userName);
            json.put(SYS_APP_ID_PARAM, appId);
            String timeDate = DateUtil.format(new Date(),"yyyyMMddHHmmss");
            json.put(TIMEDATE,timeDate );
            //签名串格式：String signstr = "appId="+appId+"&loginName="+loginName+"&timestamp="+timestamp;
            String signStr = "appId="+appId+"&loginName="+userName+"&timestamp="+timeDate+"";
            String sign =MD5Utils.MD5Encode(MD5Utils.MD5Encode(signStr)+SYS_KEY);
            json.put("sign",sign);
            String  rest = HttpUtils.sendPost(GET_BUTTON_URL,json.toString());
            JSONObject rowData = JSONObject.parseObject(rest);
            String menuStr = EncryptUtils.getDESStr(rowData.get("data")+"",S3DES_MENU_KEY);
            List<KltSysFunction> menus = JSON.parseArray(menuStr,KltSysFunction.class);
            result.setResult(menus);
        }catch (Exception e){
            log.error("用户菜单按钮查询失败",e);
            result.setCode(200);
            result.setMsg("用户菜单按钮查询失败");
        }
        return result;
    }
}

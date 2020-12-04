package com.colourfulchina.mars.service.impl;

import com.alibaba.fastjson.JSON;
import com.colourfulchina.mars.service.ImassbankService;
import com.colourfulchina.mars.utils.HttpClientUtils;
import com.colourfulchina.mars.utils.ImassbankRSA;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ImassbankServiceImpl implements ImassbankService {

//    private static final String LOGIN_URL = "http://testzwgateway.imassbank.com/api/colourful/unionlogin";
    private static final String LOGIN_URL = "https://zwgateway.imassbank.com/api/colourful/unionlogin";

    /**
     * 登录接口
     * @param mobile
     * @param name
     * @return
     * @throws Exception
     */
    @Override
    public String unionlogin(String mobile, String name) throws Exception {
        if(StringUtils.isEmpty(mobile)) {
            throw new Exception("手机号为空");
        }
        if(StringUtils.isEmpty(name)) {
            throw new Exception("姓名为空");
        }
        // 构造请求参数明文
        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("name", name);
        // 加密请求参数
        Map<String, String> reqMap = new HashMap<>();
        // 加密
        String reqData = ImassbankRSA.encrypt(JSON.toJSONString(map), ImassbankRSA.pulicKey);
        reqMap.put("reqData", reqData);
        // 加签
        reqMap.put("sign", ImassbankRSA.sign(reqData, ImassbankRSA.privateKey));
        String resp = HttpClientUtils.httpPostJson(LOGIN_URL, JSON.toJSONString(reqMap));
        log.info("众网小贷联合登陆回调:{}",resp);
        if (!StringUtils.isEmpty(resp)) {
            JSONObject jObject = new JSONObject(resp);
            // 解析第一层---对象
            if("000000".equals(jObject.getString("rspCode"))){
                // 获取密文结果
                String encryptStr = jObject.getString("respData");
                String signStr = jObject.getString("sign");
                // 验签
                if(!ImassbankRSA.checkSign(encryptStr, signStr, ImassbankRSA.pulicKey)) {
                    throw new Exception("验签失败");
                }
                // 解密
                String result = ImassbankRSA.decrypt(encryptStr, ImassbankRSA.privateKey);
                // 转json对象
                jObject = new JSONObject(result);
                log.info("解密后数据：{}", jObject);
                return jObject.getString("url");
            } else {
                throw new Exception(jObject.getString("rspMsg"));
            }
        } else {
            throw new Exception("服务异常");
        }
    }

}

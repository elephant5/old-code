package com.colourfulchina.inf.base.utils;

import com.alibaba.fastjson.JSON;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Map;

@Slf4j
@UtilityClass
public class SmsContentUtils {
    public static String getContent(String template, Map<String,String> params){
        Assert.hasText(template,"模板不能为空");
        if (CollectionUtils.isEmpty(params)){
            return template;
        }
        String content=template;
        for (Map.Entry<String,String> entry : params.entrySet()){
            content=content.replaceAll("\\$\\{"+entry.getKey()+"\\}",entry.getValue());
        }
        content=content.replaceAll("\\s","");
        log.info("template:{} params:{} content:{}",template,params,content);
//        Assert.isTrue(!content.contains("$"),"缺少参数");
        return content;
    }
}

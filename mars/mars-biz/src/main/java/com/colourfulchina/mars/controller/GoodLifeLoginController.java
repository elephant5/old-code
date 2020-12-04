package com.colourfulchina.mars.controller;

import com.alibaba.fastjson.JSONObject;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.service.GoodLifeLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 罗幸
 * @Description:
 * @Date: 2020/7/23 15:07
 */
@Slf4j
@RestController
@RequestMapping("/goodLifeLogin")
public class GoodLifeLoginController {
    private final static int SUCCESS_CODE = 100;

    private final static int ERROR_CODE = 200;
    @Autowired
    private GoodLifeLoginService goodLifeLoginService;

    @GetMapping("/login/{code}")
    public CommonResultVo<String> login(@PathVariable String code) {
        log.info("美好生活登陆  start ，参数：{}",code);
        CommonResultVo<String> result = new CommonResultVo();
        try {
            String enccifseq = goodLifeLoginService.login(code);
            result.setResult(enccifseq);
        } catch (Exception e) {
            result.setCode(ERROR_CODE);
            result.setMsg(e.getMessage());
            log.error("日志插入异常：" + e.getMessage());
        }
        log.info("美好生活登陆  end ，：{}",JSONObject.toJSONString(result));
        return result;
    }
}

package com.colourfulchina.pangu.taishang.controller.bigan;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/demo")
public class DemoController {

    @RequestMapping(value = "/hello")
    public CommonResultVo<String> testHello(@RequestBody @Valid RequestVo vo){
        CommonResultVo<String> resultVo = new CommonResultVo<String>();
        resultVo.setResult("hello" + vo.getName() + vo.getBirthday());
        return resultVo;
    }
}

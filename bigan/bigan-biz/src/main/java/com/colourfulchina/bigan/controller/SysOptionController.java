package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.SysOption;
import com.colourfulchina.bigan.service.SysOptionService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/sysOption")
public class SysOptionController {

    @Autowired
    private SysOptionService sysOptionService;

    @GetMapping("/get/{name}")
    public CommonResultVo<SysOption> get(@PathVariable String name){
        CommonResultVo<SysOption> resultVo=new CommonResultVo<>();
        try {
            final SysOption sysOption = sysOptionService.selectById(name);
            resultVo.setResult(sysOption);
        }catch (Exception e){
            log.error("查询系统配置出错",e);
        }
        return resultVo;
    }
}

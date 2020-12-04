package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.SysCurrency;
import com.colourfulchina.bigan.service.SysCurrencyService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sysCurrency")
public class SysCurrencyController {
    @Autowired
    private SysCurrencyService sysCurrencyService;

    /**
     * 查询币种列表
     * @return
     */
    @PostMapping("/selectCurrencyList")
    public CommonResultVo<List<SysCurrency>> selectCurrencyList(){
        CommonResultVo<List<SysCurrency>> result = new CommonResultVo<>();
        List<SysCurrency> sysCurrencyList = sysCurrencyService.selectList(null);
        result.setResult(sysCurrencyList);
        return result;
    }
}

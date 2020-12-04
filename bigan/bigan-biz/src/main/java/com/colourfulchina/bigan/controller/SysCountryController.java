package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.SysCountry;
import com.colourfulchina.bigan.service.SysCountryService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sysCountry")
public class SysCountryController {
    @Autowired
    private SysCountryService sysCountryService;

    /**
     * 查询国家列表
     * @return
     */
    @PostMapping("/selectCountryList")
    public CommonResultVo<List<SysCountry>> selectCountryList(){
        CommonResultVo<List<SysCountry>> result = new CommonResultVo<>();
        List<SysCountry> sysCountryList = sysCountryService.selectList(null);
        result.setResult(sysCountryList);
        return result;
    }
}

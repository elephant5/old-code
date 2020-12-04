package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.SysService;
import com.colourfulchina.bigan.service.SysServiceService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sysService")
public class SysServiceController {
    @Autowired
    private SysServiceService sysServiceService;

    /**
     * 查询老系统service列表
     * @return
     */
    @PostMapping("/selectSysServiceList")
    public CommonResultVo<List<SysService>> selectSysServiceList(){
        CommonResultVo<List<SysService>>  result = new CommonResultVo<>();
        List<SysService> sysServiceList = sysServiceService.selectList(null);
        result.setResult(sysServiceList);
        return result;
    }
}

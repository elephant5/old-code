package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.SysCountryArea;
import com.colourfulchina.bigan.service.SysCountryAreaService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sysCountryArea")
public class SysCountryAreaController {
    @Autowired
    private SysCountryAreaService sysCountryAreaService;

    /**
     * 查询区域列表
     * @return
     */
    @PostMapping("/selectAreaList")
    public CommonResultVo<List<SysCountryArea>> selectAreaList(){
        CommonResultVo<List<SysCountryArea>> result = new CommonResultVo<>();
        List<SysCountryArea> sysCountryAreaList = sysCountryAreaService.selectList(null);
        result.setResult(sysCountryAreaList);
        return result;
    }
}

package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.SysBlock;
import com.colourfulchina.bigan.service.SysBlockService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sysBlock")
public class SysBlockController {
    @Autowired
    private SysBlockService sysBlockService;

    /**
     * 查询系统全局block列表
     * @return
     */
    @PostMapping("/selectSysBlockList")
    public CommonResultVo<List<SysBlock>> selectSysBlockList(){
        CommonResultVo<List<SysBlock>> result = new CommonResultVo<>();
        List<SysBlock> sysBlockList = sysBlockService.selectList(null);
        result.setResult(sysBlockList);
        return result;
    }
}

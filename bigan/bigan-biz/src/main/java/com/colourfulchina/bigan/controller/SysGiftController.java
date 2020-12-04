package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.SysGift;
import com.colourfulchina.bigan.service.SysGiftService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sysGift")
public class SysGiftController {
    @Autowired
    private SysGiftService sysGiftService;

    /**
     * 查询老系统gift列表
     * @return
     */
    @PostMapping("/selectSysGiftList")
    public CommonResultVo<List<SysGift>> selectSysGiftList(){
        CommonResultVo<List<SysGift>>  result = new CommonResultVo<>();
        List<SysGift> sysGiftList = sysGiftService.selectList(null);
        result.setResult(sysGiftList);
        return result;
    }
}

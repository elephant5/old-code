package com.colourfulchina.pangu.taishang.controller;

import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.service.GroupProductBlockDateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groupProductBlockDate")
@Slf4j
@Api(tags = {"产品组产品block日期操作"})
public class GroupProductBlockDateController {
    @Autowired
    private GroupProductBlockDateService groupProductBlockDateService;


    /**
     * 生成所有产品组产品的block日期
     * @return
     */
    @SysGodDoorLog("生成所有产品组产品的block日期")
    @ApiOperation("生成所有产品组产品的block日期")
    @PostMapping("/generateBothBlockDate")
    public CommonResultVo generateBothBlockDate(){
        CommonResultVo result = new CommonResultVo();
        try {
            groupProductBlockDateService.generateBothBlockDate();
        }catch (Exception e){
            log.error("生成所有产品组产品的block日期失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}
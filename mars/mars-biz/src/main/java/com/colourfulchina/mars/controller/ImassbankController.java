package com.colourfulchina.mars.controller;

import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.service.ImassbankService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/imassbank")
public class ImassbankController {

    @Autowired
    private ImassbankService imassbankService;

    @SysGodDoorLog("众网小贷联合登陆")
    @ApiOperation("众网小贷联合登陆")
    @PostMapping("/unionlogin")
    public CommonResultVo<String> imassLogin(
            @RequestParam(required = true) String mobile, @RequestParam(required = true) String name){
        CommonResultVo<String> common = new CommonResultVo<>();
        try {
            common.setResult(imassbankService.unionlogin(mobile, name));
        } catch (Exception e) {
            log.error("接口异常：{} ",e);
            common.setCode(200);
            common.setMsg(e.getMessage());
        }
        return common;
    }

}

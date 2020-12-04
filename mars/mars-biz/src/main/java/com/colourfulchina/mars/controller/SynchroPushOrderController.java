package com.colourfulchina.mars.controller;

import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.service.SynchroPushService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/third")
public class SynchroPushOrderController {

    @Autowired
    private SynchroPushService synchroPushService;

    @SysGodDoorLog("同步推送订单至第三方")
    @ApiOperation("同步推送订单至第三方")
    @PostMapping("/synchroPush")
    public CommonResultVo<String> synchroPushOrderThird(@RequestBody Integer reservOderId) throws Exception {
        CommonResultVo<String> commonResultVo = new CommonResultVo<String>();
        try {
            String json = synchroPushService.synchroPush(reservOderId);
            commonResultVo.setCode(100);
            commonResultVo.setMsg("成功");
            commonResultVo.setResult(json);
        } catch (Exception e){
            commonResultVo.setCode(200);
            commonResultVo.setMsg(e.getMessage());
        }
        return commonResultVo;
    }

    @ApiOperation("重复推送失败的订单至第三方")
    @PostMapping("/rePushFailThirdOrder")
    public CommonResultVo<Boolean> rePushFailThirdOrder(){
        CommonResultVo<Boolean> result = new CommonResultVo<>();
        try {
            Boolean b = synchroPushService.rePushFailThirdOrder();
            result.setResult(b);
        }catch (Exception e){
            log.error("重复推送失败的订单至第三方失败",e);
            result.setMsg(e.getMessage());
            result.setCode(200);
            result.setResult(Boolean.FALSE);
        }
        return result;
    }
}

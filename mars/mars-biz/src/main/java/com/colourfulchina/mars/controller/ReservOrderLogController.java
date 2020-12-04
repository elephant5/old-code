package com.colourfulchina.mars.controller;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.ReservOrderLog;
import com.colourfulchina.mars.api.vo.ReservOrderVo;
import com.colourfulchina.mars.service.NuwaInterfaceService;
import com.colourfulchina.mars.service.ReservOrderLogService;
import com.colourfulchina.nuwa.api.entity.SysSmsQueue;
import com.colourfulchina.nuwa.api.sms.enums.SmsStateEnums;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/8/1 12:05
 */
@Slf4j
@RestController
@RequestMapping("/reservOrderLog")
@Api(value = "预约订单操作日志Controller", tags = {"预约订单操作日志Controller"})
public class ReservOrderLogController {
    @Autowired
    private ReservOrderLogService reservOrderLogService;

    @PostMapping("list")
    public CommonResultVo<List<ReservOrderLog>> list(@RequestBody ReservOrderLog reservOrderLog) {
        CommonResultVo<List<ReservOrderLog>> result = new CommonResultVo();
        try {
            List<ReservOrderLog> list = reservOrderLogService.selectList(reservOrderLog);
            result.setResult(list);
        } catch (Exception e) {
            log.error("日志列表查询失败",e);
            result.setCode(200);
            result.setMsg("日志列表查询失败");
        }
        return result;
    }

    @PostMapping("insert")
    public CommonResultVo<ReservOrderLog> insert(@RequestBody ReservOrderLog reservOrderLog) {
        CommonResultVo<ReservOrderLog> result = new CommonResultVo();
        try {
            ReservOrderLog  log= reservOrderLogService.insertManual(reservOrderLog);
            result.setResult(log);
        } catch (Exception e) {
            log.error("日志插入失败",e);
            result.setCode(200);
            result.setMsg("日志插入失败");
        }
        return result;
    }


    @Autowired
    private NuwaInterfaceService nuwaInterfaceService;
    @PostMapping("querySms")
    public void test(@RequestBody ReservOrderVo reservOrderVo){
        List<SysSmsQueue> smsQueueList =nuwaInterfaceService.querySms(reservOrderVo);
        if(smsQueueList!=null&&!smsQueueList.isEmpty()){
            log.info("短信列表:{}",smsQueueList);
            SysSmsQueue sysSmsQueue = smsQueueList.get(0);
            log.info("【smsStatus】:{}",SmsStateEnums.get(sysSmsQueue.getState()).getValue());
        }else{
            log.info("【smsStatus】:{}",SmsStateEnums.INIT.getValue());
        }
    }
}

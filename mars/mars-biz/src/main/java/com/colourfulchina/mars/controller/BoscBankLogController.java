package com.colourfulchina.mars.controller;

import cn.hutool.core.lang.Assert;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.BoscBankLog;
import com.colourfulchina.mars.service.BoscBankLogService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: 罗幸
 * @Description:
 * @Date: 2020/3/17 10:58
 */
@RestController
@RequestMapping("/boscBankLog")
@Slf4j
public class BoscBankLogController {
    @Autowired
    private BoscBankLogService boscBankLogService;

    @SysGodDoorLog("批量插入上海银行操作日志")
    @ApiOperation("批量插入上海银行操作日志")
    @PostMapping("/batchInsertLog")
    public CommonResultVo<List<BoscBankLog>> batchInsertLog(@RequestBody List<BoscBankLog> boscBankLogs){
        CommonResultVo<List<BoscBankLog>> result = new CommonResultVo();
        try {
            Assert.notEmpty(boscBankLogs,"批量插入列表不能为空！！！");
            Boolean flag =  boscBankLogService.batchInsertLog(boscBankLogs);
            Assert.isTrue(flag,"批量插入列表操作失败！！！");
            result.setResult(boscBankLogs);
        }catch (Exception e){
            log.error("批量插入上海银行操作日志失败" ,e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}

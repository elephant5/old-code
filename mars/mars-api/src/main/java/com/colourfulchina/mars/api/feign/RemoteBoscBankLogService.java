package com.colourfulchina.mars.api.feign;

import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.BoscBankLog;
import com.colourfulchina.mars.api.feign.fallback.RemoteBoscBankLogServiceImpl;
import com.colourfulchina.mars.api.feign.fallback.RemoteBoscBankServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author: 罗幸
 * @Description:
 * @Date: 2020/3/17 11:38
 */
@FeignClient(value = ServiceNameConstant.MARS_SERVICE,fallback = RemoteBoscBankLogServiceImpl.class)
public interface RemoteBoscBankLogService {
    @PostMapping("/boscBankLog/batchInsertLog")
     CommonResultVo<List<BoscBankLog>> batchInsertLog(@RequestBody List<BoscBankLog> boscBankLogs);
}

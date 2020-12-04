package com.colourfulchina.mars.api.feign.fallback;

import com.alibaba.fastjson.JSON;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.BoscBankLog;
import com.colourfulchina.mars.api.feign.RemoteBoscBankLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: 罗幸
 * @Description:
 * @Date: 2020/3/17 11:40
 */
@Slf4j
@Component
public class RemoteBoscBankLogServiceImpl implements RemoteBoscBankLogService {
    @Override
    public CommonResultVo<List<BoscBankLog>> batchInsertLog(List<BoscBankLog> boscBankLogs) {
        log.error("batchInsertLog error:{}", JSON.toJSONString(boscBankLogs));
        return null;
    }
}

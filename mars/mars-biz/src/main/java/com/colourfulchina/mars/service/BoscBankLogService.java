package com.colourfulchina.mars.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.mars.api.entity.BoscBankLog;

import java.util.List;

/**
 * @Author: 罗幸
 * @Description:
 * @Date: 2020/3/17 10:56
 */
public interface BoscBankLogService  extends IService<BoscBankLog> {
    Boolean batchInsertLog(List<BoscBankLog> boscBankLogs);
}

package com.colourfulchina.mars.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.mars.api.entity.BoscBankLog;
import com.colourfulchina.mars.mapper.BoscBankLogMapper;
import com.colourfulchina.mars.service.BoscBankLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: 罗幸
 * @Description:
 * @Date: 2020/3/17 10:57
 */
@Service
@Slf4j
public class BoscBankLogServiceImpl extends ServiceImpl<BoscBankLogMapper,BoscBankLog> implements BoscBankLogService {
    @Autowired
    private BoscBankLogMapper boscBankLogMapper;
    @Override
    public Boolean batchInsertLog(List<BoscBankLog> boscBankLogs) {
        return boscBankLogMapper.batchInsert(boscBankLogs)>0;
    }
}

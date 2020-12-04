package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.entity.CheckDataLog;
import com.colourfulchina.pangu.taishang.mapper.CheckDataLogMapper;
import com.colourfulchina.pangu.taishang.service.CheckDataLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CheckDataLogServiceImpl  extends ServiceImpl<CheckDataLogMapper, CheckDataLog> implements CheckDataLogService {

    @Autowired
    CheckDataLogMapper checkDataLogMapper;

    /**
     *新增
     * @param type
     * @param oldValue
     * @param newValue
     * @param keys
     * @param sizeValue
     * @param log
     * @param remark
     */
    @Override
    public void insertCheckDataLog(String type,String oldValue,String newValue,String keys , String sizeValue,String log,String remark){
        CheckDataLog checkDataLog = new CheckDataLog();
        checkDataLog.setType(type);
        checkDataLog.setOldValue(oldValue);
        checkDataLog.setNewValue(newValue);
        checkDataLog.setKeys(keys);
        checkDataLog.setSizeValue(sizeValue);
        checkDataLog.setLog(log);
        checkDataLog.setRemark(remark);
        checkDataLogMapper.insert(checkDataLog);
    }
}

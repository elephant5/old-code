package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.mars.api.entity.EquityCodeDetail;
import com.colourfulchina.pangu.taishang.api.entity.CheckDataLog;

public interface CheckDataLogService   extends IService<CheckDataLog> {

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
    public void insertCheckDataLog(String type,String oldValue,String newValue,String keys , String sizeValue,String log,String remark);
}

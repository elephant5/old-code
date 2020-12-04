package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.SysHolidayConfig;

import java.util.List;

public interface SysHolidayConfigService extends IService<SysHolidayConfig> {
    SysHolidayConfig getSysHolidayConfigByCode(String code);
    List<SysHolidayConfig> getAllSysHolidayConfigList();
}
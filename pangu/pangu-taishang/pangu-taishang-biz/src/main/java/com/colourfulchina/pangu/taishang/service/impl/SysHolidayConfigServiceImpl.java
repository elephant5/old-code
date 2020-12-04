package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.entity.SysHolidayConfig;
import com.colourfulchina.pangu.taishang.mapper.SysHolidayConfigMapper;
import com.colourfulchina.pangu.taishang.service.SysHolidayConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SysHolidayConfigServiceImpl extends ServiceImpl<SysHolidayConfigMapper, SysHolidayConfig> implements SysHolidayConfigService {

    @Autowired
    SysHolidayConfigMapper sysHolidayConfigMapper;

    @Override
    public SysHolidayConfig getSysHolidayConfigByCode(String code) {
        return sysHolidayConfigMapper.selectById(code);
    }

    @Override
    public List<SysHolidayConfig> getAllSysHolidayConfigList() {
        return sysHolidayConfigMapper.selectList(null);
    }
}
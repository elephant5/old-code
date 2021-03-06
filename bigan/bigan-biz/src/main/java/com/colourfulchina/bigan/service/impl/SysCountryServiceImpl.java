package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.SysCountry;
import com.colourfulchina.bigan.mapper.SysCountryMapper;
import com.colourfulchina.bigan.service.SysCountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SysCountryServiceImpl extends ServiceImpl<SysCountryMapper,SysCountry> implements SysCountryService {
    @Autowired
    private SysCountryMapper sysCountryMapper;
}

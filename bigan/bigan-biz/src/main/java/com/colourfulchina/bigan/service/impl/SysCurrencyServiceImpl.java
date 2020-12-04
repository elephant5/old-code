package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.SysCurrency;
import com.colourfulchina.bigan.mapper.SysCurrencyMapper;
import com.colourfulchina.bigan.service.SysCurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SysCurrencyServiceImpl extends ServiceImpl<SysCurrencyMapper,SysCurrency> implements SysCurrencyService {
    @Autowired
    private SysCurrencyMapper sysCurrencyMapper;
}

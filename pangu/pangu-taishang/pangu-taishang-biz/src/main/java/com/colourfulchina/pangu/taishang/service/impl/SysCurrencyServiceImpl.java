package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.entity.SysCurrency;
import com.colourfulchina.pangu.taishang.mapper.SysCurrencyMapper;
import com.colourfulchina.pangu.taishang.service.SysCurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SysCurrencyServiceImpl extends ServiceImpl<SysCurrencyMapper,SysCurrency> implements SysCurrencyService {
    @Autowired
    private SysCurrencyMapper sysCurrencyMapper;
}
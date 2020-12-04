package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.SysService;
import com.colourfulchina.bigan.mapper.SysServiceMapper;
import com.colourfulchina.bigan.service.SysServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class SysServiceServiceImpl extends ServiceImpl<SysServiceMapper,SysService> implements SysServiceService {
}

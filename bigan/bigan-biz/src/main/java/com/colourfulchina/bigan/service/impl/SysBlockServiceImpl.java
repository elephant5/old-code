package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.SysBlock;
import com.colourfulchina.bigan.mapper.SysBlockMapper;
import com.colourfulchina.bigan.service.SysBlockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SysBlockServiceImpl extends ServiceImpl<SysBlockMapper,SysBlock> implements SysBlockService {
    @Autowired
    private SysBlockMapper sysBlockMapper;
}

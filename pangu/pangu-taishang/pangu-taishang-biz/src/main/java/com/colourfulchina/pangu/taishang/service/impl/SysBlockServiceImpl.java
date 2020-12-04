package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.entity.SysBlock;
import com.colourfulchina.pangu.taishang.mapper.SysBlockMapper;
import com.colourfulchina.pangu.taishang.service.SysBlockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SysBlockServiceImpl extends ServiceImpl<SysBlockMapper,SysBlock> implements SysBlockService {
    @Autowired
    private SysBlockMapper sysBlockMapper;
}
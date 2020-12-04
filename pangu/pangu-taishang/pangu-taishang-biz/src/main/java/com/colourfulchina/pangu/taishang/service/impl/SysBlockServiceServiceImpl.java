package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.entity.SysBlockService;
import com.colourfulchina.pangu.taishang.mapper.SysBlockServiceMapper;
import com.colourfulchina.pangu.taishang.service.SysBlockServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SysBlockServiceServiceImpl extends ServiceImpl<SysBlockServiceMapper, SysBlockService> implements SysBlockServiceService {
    @Autowired
    private SysBlockServiceMapper sysBlockServiceMapper;
}
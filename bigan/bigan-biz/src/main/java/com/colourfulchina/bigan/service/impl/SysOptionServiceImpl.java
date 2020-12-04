package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.SysOption;
import com.colourfulchina.bigan.mapper.SysOptionMapper;
import com.colourfulchina.bigan.service.SysOptionService;
import org.springframework.stereotype.Service;

@Service
public class SysOptionServiceImpl extends ServiceImpl<SysOptionMapper, SysOption> implements SysOptionService {
}

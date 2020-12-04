package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.SysCountryArea;
import com.colourfulchina.bigan.api.entity.SysFile;
import com.colourfulchina.bigan.mapper.SysCountryAreaMapper;
import com.colourfulchina.bigan.mapper.SysFileMapper;
import com.colourfulchina.bigan.service.SysCountryAreaService;
import com.colourfulchina.bigan.service.SysFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SysCountryAreaServiceImpl extends ServiceImpl<SysCountryAreaMapper,SysCountryArea> implements SysCountryAreaService {
    @Autowired
    private SysCountryAreaMapper sysCountryAreaMapper;
}

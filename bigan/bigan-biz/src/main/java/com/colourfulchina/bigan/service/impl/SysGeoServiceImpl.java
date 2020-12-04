package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.SysFile;
import com.colourfulchina.bigan.api.entity.SysGeo;
import com.colourfulchina.bigan.mapper.SysFileMapper;
import com.colourfulchina.bigan.mapper.SysGeoMapper;
import com.colourfulchina.bigan.service.SysGeoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SysGeoServiceImpl extends ServiceImpl<SysGeoMapper,SysGeo> implements SysGeoService {
    @Autowired
    private SysGeoMapper sysGeoMapper;
}

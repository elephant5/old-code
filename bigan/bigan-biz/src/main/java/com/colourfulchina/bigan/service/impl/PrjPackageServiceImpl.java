package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.PrjPackage;
import com.colourfulchina.bigan.api.entity.PrjPackageGroups;
import com.colourfulchina.bigan.mapper.PrjPackageGroupsMapper;
import com.colourfulchina.bigan.mapper.PrjPackageMapper;
import com.colourfulchina.bigan.service.PrjPackageGroupsService;
import com.colourfulchina.bigan.service.PrjPackageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class PrjPackageServiceImpl extends ServiceImpl<PrjPackageMapper, PrjPackage> implements PrjPackageService {
}

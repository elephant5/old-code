package com.colourfulchina.mars.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.mars.api.entity.SourceMerchantInfo;
import com.colourfulchina.mars.mapper.SourceMerchantInfoMapper;
import com.colourfulchina.mars.service.SourceMerchantInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SourceMerchantInfoServiceImpl extends ServiceImpl<SourceMerchantInfoMapper, SourceMerchantInfo> implements SourceMerchantInfoService {
    @Autowired
    private SourceMerchantInfoMapper sourceMerchantInfoMapper;
}

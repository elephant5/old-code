package com.colourfulchina.mars.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.mars.api.entity.ThirdReservPushLogInfo;
import com.colourfulchina.mars.mapper.ThirdReservPushLogInfoMapper;
import com.colourfulchina.mars.mapper.ThirdReservPushLogMapper;
import com.colourfulchina.mars.service.ThirdReservPushLogInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ThirdReservPushLogInfoServiceImpl extends ServiceImpl<ThirdReservPushLogInfoMapper, ThirdReservPushLogInfo> implements ThirdReservPushLogInfoService {
    @Autowired
    ThirdReservPushLogInfoMapper thirdReservPushLogInfoMapper;
}

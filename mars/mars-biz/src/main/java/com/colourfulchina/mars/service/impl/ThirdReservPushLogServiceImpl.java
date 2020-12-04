package com.colourfulchina.mars.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.mars.api.entity.ThirdReservPushLog;
import com.colourfulchina.mars.mapper.EquityCodeDetailMapper;
import com.colourfulchina.mars.mapper.ThirdReservPushLogMapper;
import com.colourfulchina.mars.service.ThirdReservPushLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ThirdReservPushLogServiceImpl extends ServiceImpl<ThirdReservPushLogMapper, ThirdReservPushLog> implements ThirdReservPushLogService {
    @Autowired
    ThirdReservPushLogMapper thirdReservPushLogMapper;
}

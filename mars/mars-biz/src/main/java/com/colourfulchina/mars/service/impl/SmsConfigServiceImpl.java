package com.colourfulchina.mars.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.mars.api.entity.ReservTag;
import com.colourfulchina.mars.api.entity.SmsConfig;
import com.colourfulchina.mars.mapper.ReservTagMapper;
import com.colourfulchina.mars.mapper.SmsConfigMapper;
import com.colourfulchina.mars.service.SmsConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class SmsConfigServiceImpl  extends ServiceImpl<SmsConfigMapper, SmsConfig> implements SmsConfigService {

}

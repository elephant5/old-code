package com.colourfulchina.mars.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.mars.api.entity.OrderCode;
import com.colourfulchina.mars.mapper.OrderCodeMapper;
import com.colourfulchina.mars.service.OrderCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderCodeServiceImpl extends ServiceImpl<OrderCodeMapper,OrderCode> implements OrderCodeService {
    @Autowired
    private OrderCodeMapper orderCodeMapper;
}

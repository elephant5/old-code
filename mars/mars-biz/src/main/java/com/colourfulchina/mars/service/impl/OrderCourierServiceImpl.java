package com.colourfulchina.mars.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.mars.api.entity.OrderCourier;
import com.colourfulchina.mars.mapper.OrderCourierMapper;
import com.colourfulchina.mars.mapper.OrderMapper;
import com.colourfulchina.mars.service.OrderCourierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderCourierServiceImpl extends ServiceImpl<OrderCourierMapper,OrderCourier> implements OrderCourierService {
    @Autowired
    private OrderCourierMapper orderCourierMapper;
}

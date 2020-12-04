package com.colourfulchina.mars.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.mars.api.entity.Order;
import com.colourfulchina.mars.mapper.GridMapper;
import com.colourfulchina.mars.mapper.OrderMapper;
import com.colourfulchina.mars.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper,Order> implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
}

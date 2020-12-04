package com.colourfulchina.mars.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.mars.api.entity.OrderActPerson;
import com.colourfulchina.mars.mapper.OrderActPersonMapper;
import com.colourfulchina.mars.mapper.OrderMapper;
import com.colourfulchina.mars.service.OrderActPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderActPersonServiceImpl extends ServiceImpl<OrderActPersonMapper,OrderActPerson> implements OrderActPersonService {
    @Autowired
    private OrderActPersonMapper orderActPersonMapper;
}

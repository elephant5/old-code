package com.colourfulchina.mars.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.mars.api.entity.OrderBatch;
import com.colourfulchina.mars.mapper.OrderBatchMapper;
import com.colourfulchina.mars.mapper.OrderMapper;
import com.colourfulchina.mars.service.OrderBatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderBatchServiceImpl extends ServiceImpl<OrderBatchMapper,OrderBatch> implements OrderBatchService {
    @Autowired
    private OrderBatchMapper orderBatchMapper;
}

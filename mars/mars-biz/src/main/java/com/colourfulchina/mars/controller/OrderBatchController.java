package com.colourfulchina.mars.controller;

import com.colourfulchina.mars.service.OrderBatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orderBatch")
@Slf4j
public class OrderBatchController {
    @Autowired
    private OrderBatchService orderBatchService;
}

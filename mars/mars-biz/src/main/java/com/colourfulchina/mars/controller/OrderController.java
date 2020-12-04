package com.colourfulchina.mars.controller;

import com.colourfulchina.mars.service.OrderService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/order")
@Slf4j
@Api(value = "销售单相关Controller",tags = {"销售相关操作接口"})
public class OrderController {
    @Autowired
    private OrderService orderService;

}

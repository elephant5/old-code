package com.colourfulchina.mars.controller;

import com.colourfulchina.mars.service.OrderCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orderCode")
@Slf4j
public class OrderCodeController {
    @Autowired
    private OrderCodeService orderCodeService;
}

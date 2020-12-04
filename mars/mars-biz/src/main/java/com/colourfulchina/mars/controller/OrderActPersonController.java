package com.colourfulchina.mars.controller;

import com.colourfulchina.mars.service.OrderActPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orderActPerson")
@Slf4j
public class OrderActPersonController {
    @Autowired
    private OrderActPersonService orderActPersonService;
}

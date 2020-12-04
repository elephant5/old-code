package com.colourfulchina.pangu.taishang.controller;

import com.colourfulchina.pangu.taishang.service.GoodsBlockDateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goodsBlockDate")
@Slf4j
public class GoodsBlockDateController {
    @Autowired
    private GoodsBlockDateService goodsBlockDateService;

}
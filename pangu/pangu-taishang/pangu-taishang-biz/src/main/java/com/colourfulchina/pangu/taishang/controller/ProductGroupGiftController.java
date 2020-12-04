package com.colourfulchina.pangu.taishang.controller;

import com.colourfulchina.pangu.taishang.service.ProductGroupGiftService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/productGroupGift")
@Slf4j
@Api(tags = {"产品组和权益关系操作接口"})
public class ProductGroupGiftController {
    @Autowired
    private ProductGroupGiftService productGroupGiftService;
}

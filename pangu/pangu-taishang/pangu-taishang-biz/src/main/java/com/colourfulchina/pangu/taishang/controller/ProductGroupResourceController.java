package com.colourfulchina.pangu.taishang.controller;

import com.colourfulchina.pangu.taishang.service.ProductGroupResourceService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/productGroupResource")
@Slf4j
@Api(tags = {"产品组和资源类型关系操作接口"})
public class ProductGroupResourceController {
    @Autowired
    private ProductGroupResourceService productGroupResourceService;
}

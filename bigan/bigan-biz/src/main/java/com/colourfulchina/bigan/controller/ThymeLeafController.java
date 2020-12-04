package com.colourfulchina.bigan.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.colourfulchina.bigan.api.entity.Hotel;
import com.colourfulchina.bigan.service.HotelService;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/thymeleaf")
public class ThymeLeafController {

    @Autowired
    private HotelService hotelService;

    @GetMapping("/index")
    public String index(ModelMap modelMap){

        modelMap.put("username","hyper.huang");
        return "/bigan/thymeleaf/index";
    }

    @GetMapping("/page")
    public String page(ModelMap modelMap){
        //,Integer pageNo,Integer pageSize
        int pageNo=NumberUtils.toInt(modelMap.get("pageNo")+"",1);
        int pageSize=NumberUtils.toInt(modelMap.get("pageSize")+"",1);

        Page<Hotel> page=new Page<>();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
//        HotelSku
        final Page<Hotel> hotelSkuPage = hotelService.selectPage(page);
        modelMap.put("hotelSkuPage",hotelSkuPage);
        return "/bigan/thymeleaf/page";
    }
}

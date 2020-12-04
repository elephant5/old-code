package com.colourfulchina.pangu.taishang.controller;

import com.colourfulchina.pangu.taishang.service.HotelPortalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotelPortal")
@Slf4j
public class HotelPortalController {
    @Autowired
    private HotelPortalService hotelPortalService;

}
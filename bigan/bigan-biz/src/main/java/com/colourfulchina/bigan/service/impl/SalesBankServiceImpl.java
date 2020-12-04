package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.Hotel;
import com.colourfulchina.bigan.api.entity.SalesBank;
import com.colourfulchina.bigan.mapper.HotelMapper;
import com.colourfulchina.bigan.mapper.SalesBankMapper;
import com.colourfulchina.bigan.service.HotelService;
import com.colourfulchina.bigan.service.SalesBankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class SalesBankServiceImpl extends ServiceImpl<SalesBankMapper,SalesBank> implements SalesBankService {
    @Autowired
    private SalesBankMapper salesBankMapper;


}

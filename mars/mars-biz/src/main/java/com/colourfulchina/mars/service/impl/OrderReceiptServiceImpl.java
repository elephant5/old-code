package com.colourfulchina.mars.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.mars.api.entity.OrderReceipt;
import com.colourfulchina.mars.api.entity.ReservOrderDamage;
import com.colourfulchina.mars.mapper.OrderReceiptServiceMapper;
import com.colourfulchina.mars.mapper.ReservOrderDamageMapper;
import com.colourfulchina.mars.service.OrderReceiptService;
import com.colourfulchina.mars.service.ReservOrderDamageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderReceiptServiceImpl extends ServiceImpl<OrderReceiptServiceMapper, OrderReceipt> implements OrderReceiptService {
}

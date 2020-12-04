package com.colourfulchina.mars.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.mars.api.entity.ReservOrder;
import com.colourfulchina.mars.api.entity.ReservOrderPrice;
import com.colourfulchina.mars.mapper.ReservOrderMapper;
import com.colourfulchina.mars.mapper.ReservOrderPriceMapper;
import com.colourfulchina.mars.service.ReservOrderPriceService;
import com.colourfulchina.mars.service.ReservOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class ReservOrderPriceServiceImpl    extends ServiceImpl<ReservOrderPriceMapper, ReservOrderPrice> implements ReservOrderPriceService {
    @Autowired
    ReservOrderPriceMapper reservOrderPriceMapper;
    @Override
    public void updateReservOrderPrice(ReservOrder reservOrder) {
        EntityWrapper<ReservOrderPrice> local = new EntityWrapper<>();
        local.eq("reserv_order_id",reservOrder.getId());
        List< ReservOrderPrice> prices = reservOrderPriceMapper.selectList(local);
        for (ReservOrderPrice price : prices) {
            price.setPrice(BigDecimal.ZERO);
            reservOrderPriceMapper.updateById(price);
        }
    }
}

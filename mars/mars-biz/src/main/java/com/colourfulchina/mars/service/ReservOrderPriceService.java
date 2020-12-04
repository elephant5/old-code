package com.colourfulchina.mars.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.mars.api.entity.GiftCode;
import com.colourfulchina.mars.api.entity.ReservOrder;
import com.colourfulchina.mars.api.entity.ReservOrderPrice;

import java.util.List;

public interface ReservOrderPriceService  extends IService<ReservOrderPrice> {
    void updateReservOrderPrice(ReservOrder reservOrder);
}

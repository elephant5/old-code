package com.colourfulchina.mars.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.mars.api.entity.ReservCode;

public interface ReservCodeService extends IService<ReservCode> {
    ReservCode selectOneReservCode(Integer id);
}

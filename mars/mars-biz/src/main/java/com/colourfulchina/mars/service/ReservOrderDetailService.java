package com.colourfulchina.mars.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.mars.api.entity.ReservOrderDetail;

import java.util.Date;
import java.util.Map;

public interface ReservOrderDetailService extends IService<ReservOrderDetail> {
    ReservOrderDetail selectOneReservOrderDetail(Integer id);

    Boolean updateStatusByMap(Map<String, Date> map);
}

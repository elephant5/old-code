package com.colourfulchina.mars.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.mars.api.entity.ReservChannel;
import com.colourfulchina.mars.api.entity.ReservOrderDetail;
import com.colourfulchina.mars.mapper.ReservOrderDetailMapper;
import com.colourfulchina.mars.service.ReservOrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
@Slf4j
@Service
public class ReservOrderDetailServiceImpl extends ServiceImpl<ReservOrderDetailMapper, ReservOrderDetail> implements ReservOrderDetailService {


    @Autowired
    ReservOrderDetailMapper reservOrderDetailMapper;

    @Override
    public ReservOrderDetail selectOneReservOrderDetail(Integer id) {


        Wrapper<ReservOrderDetail> local = new Wrapper<ReservOrderDetail>() {
            private static final long serialVersionUID = -3242481417641969340L;

            @Override
            public String getSqlSegment() {
                return "where order_id = " + id;
            }
        };
        List<ReservOrderDetail> result=  reservOrderDetailMapper.selectList(local);
        return  result.size() > 0 ? result.get(0):new ReservOrderDetail();
    }

    @Override
    public Boolean updateStatusByMap(Map<String, Date> map) {
        List<ReservOrderDetail> bps = new ArrayList<>();
        map.forEach((k,v) -> {
            ReservOrderDetail rd = new ReservOrderDetail();
            rd.setOrderId(Integer.valueOf(k));
            rd.setBackAmountStatus(3);
            bps.add(rd);
        });
        return this.updateBatchById(bps);
    }
}

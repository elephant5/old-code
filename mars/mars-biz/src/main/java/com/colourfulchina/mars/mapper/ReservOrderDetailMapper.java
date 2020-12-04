package com.colourfulchina.mars.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.mars.api.entity.ReservOrderDetail;
import com.colourfulchina.mars.api.vo.req.ReservOrderPlaceReq;

public interface ReservOrderDetailMapper extends BaseMapper<ReservOrderDetail> {
    int batchInsertDetail(ReservOrderPlaceReq reservOrderPlaceReq);
}

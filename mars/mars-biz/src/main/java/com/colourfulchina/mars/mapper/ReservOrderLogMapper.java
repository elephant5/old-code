package com.colourfulchina.mars.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.mars.api.entity.ReservOrderLog;

import java.util.List;

public interface ReservOrderLogMapper extends BaseMapper<ReservOrderLog> {
    List<ReservOrderLog> selectList(ReservOrderLog log);
}

package com.colourfulchina.mars.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.mars.api.entity.ReservCode;

public interface ReservCodeMapper extends BaseMapper<ReservCode> {

    void optExpireVarCode(String yesterdayStr);
}

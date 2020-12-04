package com.colourfulchina.mars.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.mars.api.entity.GiftCode;
import com.colourfulchina.mars.api.entity.ReservCode;
import com.colourfulchina.mars.mapper.ReservCodeMapper;
import com.colourfulchina.mars.service.ReservCodeService;
import com.colourfulchina.mars.service.SynchroPushService;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReservCodeServiceImpl   extends ServiceImpl<ReservCodeMapper, ReservCode> implements ReservCodeService {

    @Autowired
    ReservCodeMapper reservCodeMapper;

    @Override
    public ReservCode selectOneReservCode(Integer id) {
        ReservCode local = new ReservCode();
        local.setOrderId(id);
        local.setDelFlag(DelFlagEnums.NORMAL.getCode());
        return reservCodeMapper.selectOne(local);
    }
}

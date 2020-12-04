package com.colourfulchina.pangu.taishang.service;

import com.colourfulchina.bigan.api.entity.Hotel;
import com.colourfulchina.inf.base.vo.CommonResultVo;

import java.util.List;

public interface RemoteHotelService {
    CommonResultVo<List<Hotel>> selectHotelList();
}
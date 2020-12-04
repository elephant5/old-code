package com.colourfulchina.pangu.taishang.service.impl;

import com.colourfulchina.bigan.api.entity.Hotel;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.service.RemoteHotelService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class RemoteHotelServiceImpl implements RemoteHotelService {
    private final com.colourfulchina.bigan.api.feign.RemoteHotelService remoteHotelService;

    @Override
    public CommonResultVo<List<Hotel>> selectHotelList() {
        CommonResultVo<List<Hotel>> result = remoteHotelService.selectHotelList();
        return result;
    }
}
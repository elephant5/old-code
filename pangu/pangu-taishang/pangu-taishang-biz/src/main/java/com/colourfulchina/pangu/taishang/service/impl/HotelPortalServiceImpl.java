package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.entity.HotelPortal;
import com.colourfulchina.pangu.taishang.mapper.HotelPortalMapper;
import com.colourfulchina.pangu.taishang.service.HotelPortalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class HotelPortalServiceImpl extends ServiceImpl<HotelPortalMapper,HotelPortal> implements HotelPortalService {
    @Autowired
    private HotelPortalMapper hotelPortalMapper;

    /**
     * 根据酒店id查询酒店章节列表
     * @param hotelId
     * @return
     */
    @Override
    public List<HotelPortal> selectListByHotelId(Integer hotelId) {
        return hotelPortalMapper.selectListByHotelId(hotelId);
    }
}
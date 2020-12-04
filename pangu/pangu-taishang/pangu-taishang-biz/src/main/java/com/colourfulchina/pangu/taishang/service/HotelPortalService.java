package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.HotelPortal;

import java.util.List;

public interface HotelPortalService extends IService<HotelPortal> {
    /**
     * 根据酒店id查询酒店章节列表
     * @param hotelId
     * @return
     */
    List<HotelPortal> selectListByHotelId(Integer hotelId);
}
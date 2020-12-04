package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.pangu.taishang.api.entity.HotelPortal;

import java.util.List;

public interface HotelPortalMapper extends BaseMapper<HotelPortal> {
    /**
     * 根据酒店id查询酒店章节列表
     * @param hotelId
     * @return
     */
    List<HotelPortal> selectListByHotelId(Integer hotelId);

}
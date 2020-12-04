package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.pangu.taishang.api.entity.HotelPortalImg;

import java.util.List;

public interface HotelPortalImgMapper extends BaseMapper<HotelPortalImg> {
    /**
     * 根据酒店章节id查询酒店章节图片列表
     * @param portalId
     * @return
     */
    List<HotelPortalImg> selectListByPortalId(Integer portalId);
}
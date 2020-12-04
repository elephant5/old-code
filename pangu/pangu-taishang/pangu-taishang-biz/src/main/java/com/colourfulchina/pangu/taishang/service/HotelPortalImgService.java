package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.HotelPortalImg;

import java.util.List;

public interface HotelPortalImgService extends IService<HotelPortalImg> {
    /**
     * 根据酒店章节id查询酒店章节图片列表
     * @param portalId
     * @return
     */
    List<HotelPortalImg> selectListByPortalId(Integer portalId);

    /**
     * 根据酒店章节id删除酒店章节图片
     * @param portalId
     */
    void deleteByPortalId(Integer portalId);
}
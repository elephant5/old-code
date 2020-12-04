package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.entity.HotelPortalImg;
import com.colourfulchina.pangu.taishang.mapper.HotelPortalImgMapper;
import com.colourfulchina.pangu.taishang.service.HotelPortalImgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class HotelPortalImgServiceImpl extends ServiceImpl<HotelPortalImgMapper,HotelPortalImg> implements HotelPortalImgService {
    @Autowired
    private HotelPortalImgMapper hotelPortalImgMapper;

    /**
     * 根据酒店章节id查询酒店章节图片列表
     * @param portalId
     * @return
     */
    @Override
    public List<HotelPortalImg> selectListByPortalId(Integer portalId) {
        return hotelPortalImgMapper.selectListByPortalId(portalId);
    }

    /**
     * 根据酒店章节id删除酒店章节图片
     * @param portalId
     */
    @Override
    public void deleteByPortalId(Integer portalId) {
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where hotel_portal_id ="+portalId;
            }
        };
        hotelPortalImgMapper.delete(wrapper);
    }
}
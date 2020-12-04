package com.colourfulchina.bigan.api.vo;

import com.colourfulchina.bigan.api.entity.Hotel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HotelOldDetailVo implements Serializable {
    private static final long serialVersionUID = 4254445935326964434L;
    /**
     * 酒店信息
     */
    private Hotel hotel;
    /**
     * 酒店章节信息列表
     */
    private List<HotelOldPortalVo> hotelOldPortalVoList;
}

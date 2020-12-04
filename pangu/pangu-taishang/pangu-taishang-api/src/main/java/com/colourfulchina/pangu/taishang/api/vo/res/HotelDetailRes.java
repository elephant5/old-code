package com.colourfulchina.pangu.taishang.api.vo.res;

import com.colourfulchina.pangu.taishang.api.vo.HotelPortalVo;
import com.colourfulchina.pangu.taishang.api.vo.HotelVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 酒店详情返回参数
 */
@Data
public class HotelDetailRes implements Serializable {
    private static final long serialVersionUID = 9008932991328175809L;
    /**
     * 酒店基本信息
     */
    private HotelVo hotel;

    /**
     * 酒店章节列表详情
     */
    private List<HotelPortalVo> hotelPortalVoList;
}

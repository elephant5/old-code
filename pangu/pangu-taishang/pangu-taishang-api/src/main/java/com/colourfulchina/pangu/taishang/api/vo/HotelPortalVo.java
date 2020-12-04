package com.colourfulchina.pangu.taishang.api.vo;

import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.entity.HotelPortal;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 酒店章节详情
 */
@Data
public class HotelPortalVo implements Serializable {
    private static final long serialVersionUID = -5773546485169126183L;
    /**
     * 酒店章节信息
     */
    private HotelPortal hotelPortal;
    /**
     * 酒店章节图片列表
     */
    private List<SysFileDto> sysFileDtoList;
}

package com.colourfulchina.pangu.taishang.api.vo;

import com.colourfulchina.pangu.taishang.api.entity.Hotel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 酒店详情
 */
@Data
public class HotelDetailVo implements Serializable {
    private static final long serialVersionUID = -5408312159137175675L;
    /**
     * 酒店基本信息
     */
    private Hotel hotel;

    /**
     * 账户类型(0商户1酒店2集团） 默认为商户
     */
    private Integer accountType ;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 酒店章节列表详情
     */
    private List<HotelPortalVo> hotelPortalVoList;
}

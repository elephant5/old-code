package com.colourfulchina.pangu.taishang.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 在线预约产品列表
 */
@Data
public class BookProductVo implements Serializable {
    private static final long serialVersionUID = -2214334971500928198L;

    private Integer productGroupProductId;
    private String gift;
    private Integer shopId;
    private String shopPic;
    private String shopName;
    private String hotelName;
    private Integer shopItemId;
    private String serviceName;
    private String shopItemName;
    private String needs;
    private String addon;
    private Integer goodsId;
    private Integer productGroupId;
    private String address;
    private String serviceType;
    private String cityName;
}

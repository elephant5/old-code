package com.colourfulchina.pangu.taishang.api.vo.res.shopItem;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShopItemExportVo implements Serializable {
    private static final long serialVersionUID = 2111248435570490165L;

    private Integer shopItemId;
    private Integer shopId;
    private Integer productId;
    private String hotelName;
    private String shopName;
    private String shopItemName;
    private String needs;
    private String addon;
    private String shopItemBlock;
    private String shopBlock;
    private String cityName;
    private String giftName;
    private String serviceName;
    private String address;
    private String phone;
    private String price;
}

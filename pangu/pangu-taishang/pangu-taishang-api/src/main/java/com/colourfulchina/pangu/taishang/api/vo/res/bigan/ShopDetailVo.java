package com.colourfulchina.pangu.taishang.api.vo.res.bigan;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ShopDetailVo implements Serializable {

    private Shop shop;
    private List<ShopSection> shopSectionList;
    private List<ShopItem> shopItemList;
    private String checkinTime;
    private String checkoutTime;
    private Boolean isOversea;
    private String orderTime;
}

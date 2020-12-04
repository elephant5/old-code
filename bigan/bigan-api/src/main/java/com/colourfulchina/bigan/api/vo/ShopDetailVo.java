package com.colourfulchina.bigan.api.vo;

import com.colourfulchina.bigan.api.entity.Shop;
import com.colourfulchina.bigan.api.entity.ShopItem;
import com.colourfulchina.bigan.api.entity.ShopSection;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ShopDetailVo implements Serializable {
    private Shop shop;
    private List<ShopSection> shopSectionList;
    private List<ShopItem> shopItemList;

    //入住时间
    private String checkinTime;
    //退房时间
    private String checkoutTime;

    //是否为境外酒店：true:是；false:否
    private Boolean isOversea;

    //预约时段:适用spa,健身
    private String orderTime;
}

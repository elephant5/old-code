package com.colourfulchina.bigan.api.dto;

import com.colourfulchina.bigan.api.entity.Shop;
import com.colourfulchina.bigan.api.entity.ShopItem;

import java.util.List;

/**
 * User: Ryan
 * Date: 2018/7/30
 */
public class ShopAndItemInfoDto  {

    private Shop shop;
    private List<ShopItem> shopItemList;

    public Shop getShop() {
        return shop;
    }

    public List<ShopItem> getShopItem() {
        return shopItemList;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public void setShopItem(List<ShopItem> shopItemList) {
        this.shopItemList = shopItemList;
    }
}

package com.colourfulchina.pangu.taishang.api.vo.res.shopItem;

import lombok.Data;

import java.util.List;

@Data
public class ShopItemSetmenuInfo {

    private String name;

    private String menuText;

    private List<String> menuImgList;

    private String openTime;
}

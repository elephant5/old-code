package com.colourfulchina.mars.api.vo.res;

import com.colourfulchina.pangu.taishang.api.entity.Shop;
import com.colourfulchina.pangu.taishang.api.entity.ShopItem;
import lombok.Data;

import java.util.List;

@Data
public class ShopResDto {

    private Shop shop;

    private List<ShopItemResDto> shopItemResDtoList;

//    private List<ShopItem> shopItemList;
}

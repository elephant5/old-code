package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroupProduct;
import com.colourfulchina.pangu.taishang.api.entity.ShopItem;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemNetPriceRule;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopItemPriceRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopItemRes;
import com.colourfulchina.pangu.taishang.api.vo.res.shop.ShopListQueryResVo;
import com.colourfulchina.pangu.taishang.api.vo.res.shopItem.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface ShopItemMapper extends BaseMapper<ShopItem> {


    List<ShopItemPriceRes> selectPriceList(List<Integer> shopIdlist);

    List<ShopItemRes> getShopItemInfo(@Param("shopItemIdList") List<Integer> shopItemIdList);

    GoodsShopItemIdRes getShopItemId(Integer goodsId);

    List<ShopListQueryResVo> getItems(List<Integer> goodsIdList);

    List<ShopItemInfoRes> selectShopItemPrice(@Param("itemsIdList") List<Integer> itemsIdList, @Param("date") Date date);

    ShopItemSetmenuInfo getShopItemSetmenuInfo(Integer shopItemId);

    List<ShopItemConciseRes> selectByItems(List<Integer> items);

    List<ShopItemExportVo> export();

    List<ShopItem> offShopItem();

    void updateShopItemStatus(Integer shopItemId);
    void updateProductStatus(Integer shopItemId);
    void updateGroupProductStatus(Integer productId);

    List<ProductGroupProduct> checkItemIsSale(Integer id);

    List<com.colourfulchina.pangu.taishang.api.vo.res.bigan.ShopItem> selectShopItems(List<Long> list);

    List<com.colourfulchina.pangu.taishang.api.vo.res.bigan.ShopItem> selectShopItemByIds(List<Long> list);

}
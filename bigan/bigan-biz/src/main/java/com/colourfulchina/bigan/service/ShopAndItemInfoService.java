package com.colourfulchina.bigan.service;

import com.colourfulchina.bigan.api.dto.GoodsInfo;
import com.colourfulchina.bigan.api.entity.Shop;
import com.colourfulchina.bigan.api.entity.ShopItem;

import java.util.List;
import java.util.Map;

/**
 * User: Ryan
 * Date: 2018/7/30
 */
public interface ShopAndItemInfoService  {




    /**
     * 定制套餐分析
     * @param map
     * @param items
     * @param shop
     * @param blockMap
     * @return
     */
    public List<ShopItem> analysisSetMenuItem(String serviceCode ,Map map,List<ShopItem> items,Shop shop ,Map blockMap,GoodsInfo vo,Map params);


    /**
     * 分析一下buffet的item
     * @param map
     * @param items
     * @param shop
     * @param goodsVo
     * @return
     */
    List<ShopItem> analysisBuffetItem(String serviceCode ,Map map, List<ShopItem> items, Shop shop,  GoodsInfo goodsVo,Map params);

    /**
     * 住宿分析
     * @param serviceCode
     * @param map
     * @param items
     * @param shop
     * @param blockMap
     * @param goodsVo
     * @param params
     * @return
     */
    List<ShopItem> analysisAccomItem(String serviceCode, Map map, List<ShopItem> items, Shop shop, Map blockMap, GoodsInfo goodsVo, Map params);


    /**
     * 分析单杯饮品的items
     * @param serviceCode
     * @param map
     * @param items
     * @param shop
     * @param goodsVo
     * @param params
     * @return
     */
    List<ShopItem> analysisDrinkItem(String serviceCode, Map map, List<ShopItem> items, Shop shop, GoodsInfo goodsVo, Map params);
}

package com.colourfulchina.bigan.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.bigan.api.dto.GoodsInfo;
import com.colourfulchina.bigan.api.entity.Shop;
import com.colourfulchina.bigan.api.entity.ShopItem;

import java.util.List;

/**
 * User: Ryan
 * Date: 2018/8/1
 */
public interface ShopitemsService  extends IService<ShopItem> {

    /**
     * 根据SHopid或者Items
     * @param shop
     * @return
     */
    public List<ShopItem> getShopitemsByShopId(Shop shop,String serviceCode);


    /**
     * 根据类型查询
     * @param shop
     * @param serviceCode
     * @param goodsVo
     * @return
     */
    public List<ShopItem> getShopitems(Shop shop,String serviceCode,GoodsInfo goodsVo);

    List<ShopItem> selectByItemIdList(List items);

    /**
     * 获取商户规格数据库主键生成
     * @return
     * @throws Exception
     */
    Long selectShopItemSeqNextValue()throws Exception;
}

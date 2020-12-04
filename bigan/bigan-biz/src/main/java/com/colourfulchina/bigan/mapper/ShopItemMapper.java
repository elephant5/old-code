package com.colourfulchina.bigan.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.bigan.api.entity.ShopItem;

import java.util.List;

public interface ShopItemMapper extends BaseMapper<ShopItem> {



    /**
     * 检查商品的权益是否相等
     * @param params （）
     * @return
     */
    ShopItem checkShopItemIsExist(ShopItem params);

    /**
     * 获取序列下一个值
     * @return
     */
    Long selectShopItemSeqNextValue();

    List<ShopItem> selectByItemIdList(List items);
}
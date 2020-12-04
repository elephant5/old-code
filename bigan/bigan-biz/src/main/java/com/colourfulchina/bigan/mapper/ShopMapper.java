package com.colourfulchina.bigan.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.bigan.api.entity.Shop;

import java.util.List;
import java.util.Map;

public interface ShopMapper extends BaseMapper<Shop> {

   // 检查商品是否存在，检查逻辑为酒店+餐厅名+权益项目+餐段完全一致(shop.hotel+shop.name+shop.city

    /**
     *
     * @param shop:shop.hotel+shop.name+shop.city+shop.type
     * @return
     */
    Shop checkShopIsExist(Shop shop);
	
	 /**
     * 根据酒店名、门店名和城市检测门店列表
     * @param shop
     * @return
     */
    public List<Shop> checkShopByNameAndCityAndHotel(Shop shop);

    /**
     * 获取序列中的主键
     * @return
     */
    public long getIdForSeq();
    /**
     * 获取商品序列的下一个值
     * @return
     */
    Long selectShopSeqNextValue();

    Map saveShopInfo(Map vo);
}

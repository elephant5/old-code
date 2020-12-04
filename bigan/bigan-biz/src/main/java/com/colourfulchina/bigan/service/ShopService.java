package com.colourfulchina.bigan.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.bigan.api.dto.GoodsInfo;
import com.colourfulchina.bigan.api.entity.Shop;
import com.colourfulchina.bigan.api.vo.ShopDetailVo;
import com.colourfulchina.bigan.api.vo.ShopSaveInfoVo;

import java.util.List;

public interface ShopService extends IService<Shop> {
    /**
     * 根据酒店名称、门店名称和酒店城市检测是否存在门店
     * @param shop
     * @return
     */
    boolean checkShopByNameAndCityAndHotel(Shop shop);

    /**
     * 获取序列中的主键
     * @return
     */
    long getIdForSeq();

    /**
     * 插入商户
     * @param shop
     * @return
     */
    boolean insert(Shop shop);
    /**
     * 检查酒店是否存在
     * @param tempshop
     * @return
     */

    Shop checkShopIsExist(Shop tempshop);

    /**
     * 获取商店下一个的值
     * @return
     */
    Long selectShopSeqNextValue();

    /**
     * 调用存储过程保存
     * @param vo
     * @return
     */
    Shop saveShopInfo(ShopSaveInfoVo vo);

    /**
     * 查询酒店
     * @param goodsVo
     * @param serviceCode
     * @return
     */
    Shop selectListFor(GoodsInfo goodsVo, String serviceCode);

    ShopDetailVo getShopDetail(Integer id);

    /**
     * 根据酒店id查询商户列表
     * @param hotelId
     * @return
     */
    List<Shop> selectListByHotelId(Integer hotelId);
}

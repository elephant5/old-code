package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroupGift;

import java.util.List;

public interface ProductGroupGiftService extends IService<ProductGroupGift> {

    /**
     * 产品组对应的权益类型存储入库
     * @param gift
     * @param productGroupId
     * @return
     */
    List<ProductGroupGift> storage(String[] gift,Integer productGroupId) throws Exception;

    /**
     * 根据产品组，查询产品在对应的权益类型
     * @param oldproductGroup
     * @return
     */
    List<ProductGroupGift> selectByProductGroupId(Integer oldproductGroup);
}

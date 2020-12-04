package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.ShopType;

import java.util.List;

public interface ShopTypeService extends IService<ShopType> {
    /**
     * 查询商户类型列表
     * @return
     */
    List<ShopType> selectShopTypeList();
}

package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.ShopTypeService;

public interface ShopTypeServiceService extends IService<ShopTypeService> {
    /**
     * 根据服务类型查询商户类型与服务关联
     * @param typeService
     * @return
     */
    ShopTypeService selectByService(String typeService);
}

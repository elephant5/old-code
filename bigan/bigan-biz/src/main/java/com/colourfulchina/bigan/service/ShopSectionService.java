package com.colourfulchina.bigan.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.bigan.api.entity.ShopSection;

public interface ShopSectionService extends IService<ShopSection> {

    /**
     * 根据商户id删除shopSection
     * @param shopId
     */
    void delSection(Integer shopId);
}

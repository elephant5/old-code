package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.Goods;
import com.colourfulchina.pangu.taishang.api.entity.GoodsSetting;

public interface GoodsSettingService extends IService<GoodsSetting> {

    GoodsSetting selectByGoodsId(Integer id);
}
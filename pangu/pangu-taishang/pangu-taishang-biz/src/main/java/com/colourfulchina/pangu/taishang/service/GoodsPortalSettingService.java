package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.GoodsPortalSetting;
import com.colourfulchina.pangu.taishang.api.entity.GoodsSetting;
import com.colourfulchina.pangu.taishang.api.entity.SysBankLogo;

public interface GoodsPortalSettingService extends IService<GoodsPortalSetting> {
    GoodsPortalSetting selectByGoodsId(Integer id);
}
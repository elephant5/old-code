package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.entity.GoodsPortalSetting;
import com.colourfulchina.pangu.taishang.api.entity.GoodsSetting;
import com.colourfulchina.pangu.taishang.mapper.GoodsPortalSettingMapper;
import com.colourfulchina.pangu.taishang.service.GoodsPortalSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoodsPortalSettingServiceImpl extends ServiceImpl<GoodsPortalSettingMapper, GoodsPortalSetting> implements GoodsPortalSettingService {


    @Autowired
    GoodsPortalSettingMapper goodsPortalSettingMapper;
    @Override
    public GoodsPortalSetting selectByGoodsId(Integer id) {
        GoodsPortalSetting goodsPortalSetting = new GoodsPortalSetting();
        goodsPortalSetting.setGoodsId(id);
        return goodsPortalSettingMapper.selectOne(goodsPortalSetting);
    }
}
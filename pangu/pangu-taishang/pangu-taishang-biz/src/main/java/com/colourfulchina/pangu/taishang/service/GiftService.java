package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.Gift;

import java.util.List;

public interface GiftService extends IService<Gift> {

    List<Gift> selectGiftByShopType(String shopType);
}
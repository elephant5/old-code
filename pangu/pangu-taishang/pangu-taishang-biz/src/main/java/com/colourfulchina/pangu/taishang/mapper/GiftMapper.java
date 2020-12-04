package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.pangu.taishang.api.entity.Gift;

import java.util.List;

public interface GiftMapper extends BaseMapper<Gift> {

    List<Gift> selectGiftByShopType(String shopType);
}
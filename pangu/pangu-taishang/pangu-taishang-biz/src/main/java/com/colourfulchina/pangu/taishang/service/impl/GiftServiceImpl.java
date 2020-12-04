package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.entity.Gift;
import com.colourfulchina.pangu.taishang.mapper.GiftMapper;
import com.colourfulchina.pangu.taishang.service.GiftService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GiftServiceImpl extends ServiceImpl<GiftMapper,Gift> implements GiftService {
    @Autowired
    private GiftMapper giftMapper;

    @Override
    public List<Gift> selectGiftByShopType(String shopType) {
        List<Gift> list = giftMapper.selectGiftByShopType(shopType);
        return list;
    }
}
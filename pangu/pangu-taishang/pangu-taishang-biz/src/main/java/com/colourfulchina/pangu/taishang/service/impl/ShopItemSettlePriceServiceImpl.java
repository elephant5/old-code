package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopItemSettlePriceHisRes;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemSettlePrice;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopItemSettlePriceHisReq;
import com.colourfulchina.pangu.taishang.mapper.ShopItemSettlePriceMapper;
import com.colourfulchina.pangu.taishang.service.ShopItemSettlePriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class ShopItemSettlePriceServiceImpl extends ServiceImpl<ShopItemSettlePriceMapper, ShopItemSettlePrice> implements ShopItemSettlePriceService {
    @Resource
    private ShopItemSettlePriceMapper shopItemSettlePriceMapper;
    @Override
    public List<ShopItemSettlePriceHisRes> his(ShopItemSettlePriceHisReq shopItemSettlePriceHisReq) {
        return shopItemSettlePriceMapper.his(shopItemSettlePriceHisReq);
    }
}
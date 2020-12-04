package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopItemSettlePriceHisRes;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemSettlePrice;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopItemSettlePriceHisReq;

import java.util.List;

public interface ShopItemSettlePriceService extends IService<ShopItemSettlePrice> {
    List<ShopItemSettlePriceHisRes> his(ShopItemSettlePriceHisReq shopItemSettlePriceHisReq);
}
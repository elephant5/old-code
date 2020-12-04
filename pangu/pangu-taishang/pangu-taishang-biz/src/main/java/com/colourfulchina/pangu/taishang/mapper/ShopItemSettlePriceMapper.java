package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemSettlePrice;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopItemSettlePriceHisReq;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopItemSettlePriceHisRes;

import java.util.List;

public interface ShopItemSettlePriceMapper extends BaseMapper<ShopItemSettlePrice> {
    List<ShopItemSettlePriceHisRes> his(ShopItemSettlePriceHisReq shopItemSettlePriceHisReq);
}
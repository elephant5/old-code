package com.colourfulchina.pangu.taishang.api.feign.fallback;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemNetPriceRule;
import com.colourfulchina.pangu.taishang.api.feign.RemoteShopItemNetPriceService;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectPriceByTimeReqVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoteShopItemNetPriceServiceFallbackImpl implements RemoteShopItemNetPriceService{


    @Override
    public CommonResultVo<ShopItemNetPriceRule> getPriceByTime(SelectPriceByTimeReqVo reqVo) {
        log.error("查询预约时间对应的净价");
        return null;
    }
}

package com.colourfulchina.pangu.taishang.api.feign;

import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemNetPriceRule;
import com.colourfulchina.pangu.taishang.api.feign.fallback.RemoteShopItemNetPriceServiceFallbackImpl;
import com.colourfulchina.pangu.taishang.api.feign.fallback.RemoteSysFileServiceFallbackImpl;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectPriceByTimeReqVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value =  ServiceNameConstant.PANGU_TAISHANG_SERVICE, fallback = RemoteShopItemNetPriceServiceFallbackImpl.class)
public interface RemoteShopItemNetPriceService {

    /**
     * 查询预约时间对应的净价
     * @param reqVo
     * @return
     */
	@PostMapping("/shopItemNetPriceRule/getPriceByTime")
    public CommonResultVo<ShopItemNetPriceRule> getPriceByTime(@RequestBody SelectPriceByTimeReqVo reqVo);
	
}

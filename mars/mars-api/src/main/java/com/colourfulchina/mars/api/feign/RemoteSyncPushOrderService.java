package com.colourfulchina.mars.api.feign;

import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.feign.fallback.RemoteSyncPushOrderServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = ServiceNameConstant.MARS_SERVICE,fallback = RemoteSyncPushOrderServiceImpl.class)
public interface RemoteSyncPushOrderService {

    /**
     * 同步推送订单至第三方
     * @param reservOderId
     * @return
     */
    @PostMapping("/third/synchroPush")
    CommonResultVo<String> synchroPushOrderThird(@RequestBody Integer reservOderId);

    /**
     * 重复推送失败的订单至第三方
     * @return
     */
    @PostMapping("/third/rePushFailThirdOrder")
    CommonResultVo<Boolean> rePushFailThirdOrder();
}

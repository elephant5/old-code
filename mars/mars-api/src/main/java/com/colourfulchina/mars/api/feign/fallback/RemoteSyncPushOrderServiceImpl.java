package com.colourfulchina.mars.api.feign.fallback;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.feign.RemoteSyncPushOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoteSyncPushOrderServiceImpl implements RemoteSyncPushOrderService {
	@Override
	public CommonResultVo<String> synchroPushOrderThird(Integer reservOderId) {
		log.error("fegin同步推送订单至第三方失败");
		return null;
	}

	@Override
	public CommonResultVo<Boolean> rePushFailThirdOrder() {
		log.error("fegin重复推送失败的订单至第三方失败");
		return null;
	}
}

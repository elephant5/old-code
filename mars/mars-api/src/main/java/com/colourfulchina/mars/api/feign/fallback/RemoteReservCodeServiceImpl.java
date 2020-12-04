package com.colourfulchina.mars.api.feign.fallback;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.ReservCode;
import com.colourfulchina.mars.api.feign.RemoteReservCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName: RemoteGiftTableServiceImpl  
 * @author weibinbin
 * @version V1.0
 */
@Slf4j
@Component
public class RemoteReservCodeServiceImpl implements RemoteReservCodeService {

	@Override
	public CommonResultVo<ReservCode> getReservCode(Integer reservCodeId) {
		log.error("======getReservCode fail");
		return null;
	}

	@Override
	public CommonResultVo<Boolean> optExpireVarCode() {
		log.error("fegin过期核销码失败");
		return null;
	}
}

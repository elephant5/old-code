package com.colourfulchina.mars.api.feign;

import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.ReservCode;
import com.colourfulchina.mars.api.feign.fallback.RemoteReservCodeServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @ClassName: RemoteActivateCodeService  
 * @author weibinbin
 * @version V1.0
 */
@FeignClient(value = ServiceNameConstant.MARS_SERVICE,fallback = RemoteReservCodeServiceImpl.class)
public interface RemoteReservCodeService {

	@PostMapping("/reservCode/getReservCode")
	public CommonResultVo<ReservCode> getReservCode(@RequestBody Integer reservCodeId);

	/**
	 * 核销码过期操作
	 * @return
	 */
	@PostMapping("/reservCode/optExpireVarCode")
	CommonResultVo<Boolean> optExpireVarCode();
}

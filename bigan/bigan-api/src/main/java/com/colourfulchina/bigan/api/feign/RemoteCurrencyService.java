package com.colourfulchina.bigan.api.feign;

import com.colourfulchina.bigan.api.entity.SysCurrency;
import com.colourfulchina.bigan.api.feign.fallback.RemoteCurrencyServiceFallbackImpl;
import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = ServiceNameConstant.BIGAN_SERVICE, fallback = RemoteCurrencyServiceFallbackImpl.class)
public interface RemoteCurrencyService {
	/**
     * fegin查询sqlserver币种列表
	 * @return
     */
	@PostMapping(value = "/sysCurrency/selectCurrencyList")
	CommonResultVo<List<SysCurrency>> selectCurrencyList();
}

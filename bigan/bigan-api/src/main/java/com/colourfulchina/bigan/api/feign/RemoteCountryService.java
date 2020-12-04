package com.colourfulchina.bigan.api.feign;

import com.colourfulchina.bigan.api.entity.SysCountry;
import com.colourfulchina.bigan.api.feign.fallback.RemoteCountryServiceFallbackImpl;
import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = ServiceNameConstant.BIGAN_SERVICE, fallback = RemoteCountryServiceFallbackImpl.class)
public interface RemoteCountryService {
	/**
     * fegin查询sqlserver国家列表
	 * @return
     */
	@PostMapping(value = "/sysCountry/selectCountryList")
	CommonResultVo<List<SysCountry>> selectCountryList();
}

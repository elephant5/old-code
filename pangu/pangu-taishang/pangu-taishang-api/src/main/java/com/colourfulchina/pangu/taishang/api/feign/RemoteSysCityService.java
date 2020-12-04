package com.colourfulchina.pangu.taishang.api.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.City;
import com.colourfulchina.pangu.taishang.api.feign.fallback.RemoteSysCityServiceFallbackImpl;

@FeignClient(value =  ServiceNameConstant.PANGU_TAISHANG_SERVICE, fallback = RemoteSysCityServiceFallbackImpl.class)
public interface RemoteSysCityService {

	@PostMapping("/city/selectCityList")
    public CommonResultVo<List<City>> selectCityList();
	
}

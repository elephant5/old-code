package com.colourfulchina.bigan.api.feign;

import com.colourfulchina.bigan.api.entity.City;
import com.colourfulchina.bigan.api.feign.fallback.RemoteCityServiceFallbackImpl;
import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = ServiceNameConstant.BIGAN_SERVICE, fallback = RemoteCityServiceFallbackImpl.class)
public interface RemoteCityService {
	/**
     * fegin查询sqlserver城市列表
	 * @return
     */
	@PostMapping(value = "/city/selectCityList")
	CommonResultVo<List<City>> selectCityList();
}

package com.colourfulchina.pangu.taishang.api.feign.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.City;
import com.colourfulchina.pangu.taishang.api.feign.RemoteSysCityService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RemoteSysCityServiceFallbackImpl implements RemoteSysCityService{

	@Override
	public CommonResultVo<List<City>> selectCityList() {
		log.error("查询城市信息失败");
		return null;
	}
	
}

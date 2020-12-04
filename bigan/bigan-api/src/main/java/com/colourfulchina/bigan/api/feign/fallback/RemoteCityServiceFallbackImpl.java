package com.colourfulchina.bigan.api.feign.fallback;

import com.colourfulchina.bigan.api.entity.City;
import com.colourfulchina.bigan.api.feign.RemoteCityService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteCityServiceFallbackImpl implements RemoteCityService {
	@Override
	public CommonResultVo<List<City>> selectCityList() {
		log.info("fetgin查询城市失败");
		return null;
	}
}

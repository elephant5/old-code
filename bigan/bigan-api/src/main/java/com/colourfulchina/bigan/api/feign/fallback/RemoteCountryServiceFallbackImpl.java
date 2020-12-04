package com.colourfulchina.bigan.api.feign.fallback;

import com.colourfulchina.bigan.api.entity.SysCountry;
import com.colourfulchina.bigan.api.feign.RemoteCountryService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteCountryServiceFallbackImpl implements RemoteCountryService {
	@Override
	public CommonResultVo<List<SysCountry>> selectCountryList() {
		log.info("fetgin查询国家失败");
		return null;
	}
}

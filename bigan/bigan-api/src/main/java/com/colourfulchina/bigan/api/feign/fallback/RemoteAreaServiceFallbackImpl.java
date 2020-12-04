package com.colourfulchina.bigan.api.feign.fallback;

import com.colourfulchina.bigan.api.entity.SysCountryArea;
import com.colourfulchina.bigan.api.feign.RemoteAreaService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteAreaServiceFallbackImpl implements RemoteAreaService {
	@Override
	public CommonResultVo<List<SysCountryArea>> selectAreaList() {
		log.info("fetgin查询区域失败");
		return null;
	}
}

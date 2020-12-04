package com.colourfulchina.bigan.api.feign.fallback;

import com.colourfulchina.bigan.api.entity.SysGeo;
import com.colourfulchina.bigan.api.feign.RemoteGeoService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteGeoServiceFallbackImpl implements RemoteGeoService {
	@Override
	public CommonResultVo<List<SysGeo>> selectGeoList() {
		log.info("fegin查询酒店定位信息失败");
		return null;
	}

	@Override
	public CommonResultVo<SysGeo> remoteAddGeo(SysGeo sysGeo) {
		log.info("fegin新增酒店定位信息失败");
		return null;
	}

	@Override
	public CommonResultVo<SysGeo> remoteUpdGeo(SysGeo sysGeo) {
		log.info("fegin修改酒店定位信息失败");
		return null;
	}
}

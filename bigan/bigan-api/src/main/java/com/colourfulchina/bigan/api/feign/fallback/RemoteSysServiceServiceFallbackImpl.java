package com.colourfulchina.bigan.api.feign.fallback;

import com.colourfulchina.bigan.api.entity.SysService;
import com.colourfulchina.bigan.api.feign.RemoteSysServiceService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteSysServiceServiceFallbackImpl implements RemoteSysServiceService {

	@Override
	public CommonResultVo<List<SysService>> selectSysServiceList() {
		log.info("fegin查询service列表失败");
		return null;
	}
}

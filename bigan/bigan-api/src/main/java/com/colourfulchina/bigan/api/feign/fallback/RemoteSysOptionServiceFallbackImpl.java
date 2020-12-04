package com.colourfulchina.bigan.api.feign.fallback;

import com.colourfulchina.bigan.api.entity.SysOption;
import com.colourfulchina.bigan.api.entity.SysService;
import com.colourfulchina.bigan.api.feign.RemoteSysOptionService;
import com.colourfulchina.bigan.api.feign.RemoteSysServiceService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteSysOptionServiceFallbackImpl implements RemoteSysOptionService {

	@Override
	public CommonResultVo<SysOption> get(String name) {
		log.info("fegin查询option列表失败");
		return null;
	}
}

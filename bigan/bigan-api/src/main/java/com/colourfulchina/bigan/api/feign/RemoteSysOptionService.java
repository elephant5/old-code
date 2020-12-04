package com.colourfulchina.bigan.api.feign;

import com.colourfulchina.bigan.api.entity.SysOption;
import com.colourfulchina.bigan.api.entity.SysService;
import com.colourfulchina.bigan.api.feign.fallback.RemoteSysOptionServiceFallbackImpl;
import com.colourfulchina.bigan.api.feign.fallback.RemoteSysServiceServiceFallbackImpl;
import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = ServiceNameConstant.BIGAN_SERVICE, fallback = RemoteSysOptionServiceFallbackImpl.class)
public interface RemoteSysOptionService {
	/**
	 * @return
     */
	@GetMapping(value = "/sysOption/get/{name}")
	CommonResultVo<SysOption> get(@PathVariable(value = "name") String name);
}

package com.colourfulchina.bigan.api.feign;

import com.colourfulchina.bigan.api.entity.SysService;
import com.colourfulchina.bigan.api.feign.fallback.RemoteSysServiceServiceFallbackImpl;
import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = ServiceNameConstant.BIGAN_SERVICE, fallback = RemoteSysServiceServiceFallbackImpl.class)
public interface RemoteSysServiceService {
	/**
     * fegin查询sqlserver  service列表
	 * @return
     */
	@PostMapping(value = "/sysService/selectSysServiceList")
	CommonResultVo<List<SysService>> selectSysServiceList();
}

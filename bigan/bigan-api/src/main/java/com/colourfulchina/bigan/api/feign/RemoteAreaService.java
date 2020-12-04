package com.colourfulchina.bigan.api.feign;

import com.colourfulchina.bigan.api.entity.SysCountryArea;
import com.colourfulchina.bigan.api.feign.fallback.RemoteAreaServiceFallbackImpl;
import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = ServiceNameConstant.BIGAN_SERVICE, fallback = RemoteAreaServiceFallbackImpl.class)
public interface RemoteAreaService {
	/**
     * fegin查询sqlserver区域列表
	 * @return
     */
	@PostMapping(value = "/sysCountryArea/selectAreaList")
	CommonResultVo<List<SysCountryArea>> selectAreaList();
}

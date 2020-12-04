package com.colourfulchina.bigan.api.feign;

import com.colourfulchina.bigan.api.entity.SysBlock;
import com.colourfulchina.bigan.api.feign.fallback.RemoteSysBlockServiceFallbackImpl;
import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = ServiceNameConstant.BIGAN_SERVICE, fallback = RemoteSysBlockServiceFallbackImpl.class)
public interface RemoteSysBlockService {
	/**
     * fegin查询sqlserver系统全局block列表
	 * @return
     */
	@PostMapping(value = "/sysBlock/selectSysBlockList")
	CommonResultVo<List<SysBlock>> selectSysBlockList();
}

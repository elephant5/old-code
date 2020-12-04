package com.colourfulchina.bigan.api.feign;

import com.colourfulchina.bigan.api.entity.SysGift;
import com.colourfulchina.bigan.api.feign.fallback.RemoteSysGiftServiceFallbackImpl;
import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = ServiceNameConstant.BIGAN_SERVICE, fallback = RemoteSysGiftServiceFallbackImpl.class)
public interface RemoteSysGiftService {
	/**
     * fegin查询sqlserver  gift列表
	 * @return
     */
	@PostMapping(value = "/sysGift/selectSysGiftList")
	CommonResultVo<List<SysGift>> selectSysGiftList();
}

package com.colourfulchina.bigan.api.feign;

import com.colourfulchina.bigan.api.entity.BookChannel;
import com.colourfulchina.bigan.api.feign.fallback.RemoteBookChannelServiceFallbackImpl;
import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = ServiceNameConstant.BIGAN_SERVICE, fallback = RemoteBookChannelServiceFallbackImpl.class)
public interface RemoteBookChannelService {
	/**
     * fegin查询sqlserver商户资源
	 * @return
     */
	@PostMapping(value = "/bookChannel/selectBookChannelList")
	CommonResultVo<List<BookChannel>> selectBookChannelList();
}

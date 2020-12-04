package com.colourfulchina.pangu.taishang.api.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.feign.fallback.RemoteSysFileServiceFallbackImpl;
import com.colourfulchina.pangu.taishang.api.vo.req.ListSysFileReq;

@FeignClient(value =  ServiceNameConstant.PANGU_TAISHANG_SERVICE, fallback = RemoteSysFileServiceFallbackImpl.class)
public interface RemoteSysFileService {

	@PostMapping("/sysFile/list")
    public CommonResultVo<List<SysFileDto>> list(@RequestBody ListSysFileReq sysFileReq);
	
}

package com.colourfulchina.bigan.api.feign;

import com.colourfulchina.bigan.api.dto.ListSysFileReq;
import com.colourfulchina.bigan.api.dto.SysFileDto;
import com.colourfulchina.bigan.api.feign.fallback.RemoteSysFileServiceFallbackImpl;
import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = ServiceNameConstant.BIGAN_SERVICE, fallback = RemoteSysFileServiceFallbackImpl.class)
public interface RemoteSysFileService {
	@PostMapping(value = "/sysFile/list")
	CommonResultVo<List<SysFileDto>> list(@RequestBody ListSysFileReq sysFileReq);
	@PostMapping("/sysFile/merge")
	CommonResultVo<Boolean> merge(@RequestBody List<SysFileDto> fileDtoList);

	@GetMapping("/sysFile/deleteFileByGuid/{guid}")
	void deleteFileByGuid(@PathVariable(value = "guid",required = false) String guid);
}

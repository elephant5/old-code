package com.colourfulchina.pangu.taishang.api.feign.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.feign.RemoteSysFileService;
import com.colourfulchina.pangu.taishang.api.vo.req.ListSysFileReq;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RemoteSysFileServiceFallbackImpl implements RemoteSysFileService {

	@Override
	public CommonResultVo<List<SysFileDto>> list(ListSysFileReq sysFileReq) {
		log.error("查询图片信息失败");
		return null;
	}

}

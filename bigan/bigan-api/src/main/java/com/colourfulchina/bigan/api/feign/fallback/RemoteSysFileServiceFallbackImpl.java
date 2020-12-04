package com.colourfulchina.bigan.api.feign.fallback;

import com.colourfulchina.bigan.api.dto.ListSysFileReq;
import com.colourfulchina.bigan.api.dto.SysFileDto;
import com.colourfulchina.bigan.api.entity.SysBlock;
import com.colourfulchina.bigan.api.feign.RemoteSysBlockService;
import com.colourfulchina.bigan.api.feign.RemoteSysFileService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Component
public class RemoteSysFileServiceFallbackImpl implements RemoteSysFileService {
	@Override
	public CommonResultVo<List<SysFileDto>> list(ListSysFileReq sysFileReq) {
		log.info("fegin查询文件列表失败");
		return null;
	}

	@Override
	public CommonResultVo<Boolean> merge(List<SysFileDto> fileDtoList) {
		log.info("fegin merge文件列表失败");
		return null;
	}

	@Override
	public void deleteFileByGuid(String guid) {
		log.info("fegin deleteFileByGuid文件列表失败");
	}
}

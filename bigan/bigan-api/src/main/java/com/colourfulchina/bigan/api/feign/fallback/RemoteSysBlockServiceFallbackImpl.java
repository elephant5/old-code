package com.colourfulchina.bigan.api.feign.fallback;

import com.colourfulchina.bigan.api.entity.SysBlock;
import com.colourfulchina.bigan.api.feign.RemoteSysBlockService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteSysBlockServiceFallbackImpl implements RemoteSysBlockService {
	@Override
	public CommonResultVo<List<SysBlock>> selectSysBlockList() {
		log.info("fegin查询系统全局block失败");
		return null;
	}
}

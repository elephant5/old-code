package com.colourfulchina.bigan.api.feign.fallback;

import com.colourfulchina.bigan.api.entity.SysGift;
import com.colourfulchina.bigan.api.feign.RemoteSysGiftService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteSysGiftServiceFallbackImpl implements RemoteSysGiftService {

	@Override
	public CommonResultVo<List<SysGift>> selectSysGiftList() {
		log.info("fegin查询gift列表失败");
		return null;
	}
}

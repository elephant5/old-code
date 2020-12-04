package com.colourfulchina.bigan.api.feign.fallback;

import com.colourfulchina.bigan.api.entity.BookChannel;
import com.colourfulchina.bigan.api.feign.RemoteBookChannelService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteBookChannelServiceFallbackImpl implements RemoteBookChannelService {
	@Override
	public CommonResultVo<List<BookChannel>> selectBookChannelList() {
		log.info("fegin查询老系统商户资源列表失败");
		return null;
	}
}

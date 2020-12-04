package com.colourfulchina.bigan.api.feign.fallback;

import com.colourfulchina.bigan.api.entity.ShopSection;
import com.colourfulchina.bigan.api.feign.RemoteShopSectionService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteShopSectionServiceFallbackImpl implements RemoteShopSectionService {
	@Override
	public CommonResultVo<List<ShopSection>> selectShopSectionList() {
		log.info("fegin查询老系统shopsection失败");
		return null;
	}
}

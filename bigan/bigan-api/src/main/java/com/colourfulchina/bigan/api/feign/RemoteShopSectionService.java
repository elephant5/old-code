package com.colourfulchina.bigan.api.feign;

import com.colourfulchina.bigan.api.entity.ShopSection;
import com.colourfulchina.bigan.api.feign.fallback.RemoteShopSectionServiceFallbackImpl;
import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = ServiceNameConstant.BIGAN_SERVICE, fallback = RemoteShopSectionServiceFallbackImpl.class)
public interface RemoteShopSectionService {
	/**
     * fegin查询sqlserver商户section列表
	 * @return
     */
	@PostMapping(value = "/shopSection/selectShopSectionList")
	CommonResultVo<List<ShopSection>> selectShopSectionList();
}

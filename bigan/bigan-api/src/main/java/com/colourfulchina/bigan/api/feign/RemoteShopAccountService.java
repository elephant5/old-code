package com.colourfulchina.bigan.api.feign;

import com.colourfulchina.bigan.api.entity.ShopAccount;
import com.colourfulchina.bigan.api.feign.fallback.RemoteShopAccountServiceFallbackImpl;
import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = ServiceNameConstant.BIGAN_SERVICE, fallback = RemoteShopAccountServiceFallbackImpl.class)
public interface RemoteShopAccountService {
	/**
     * fegin查询sqlserver  商户中心列表
	 * @return
     */
	@PostMapping(value = "/shopAccount/selectShopAccountList")
	CommonResultVo<List<ShopAccount>> selectShopAccountList();

	/**
	 * 更新老系统商户中心
	 * @return
	 */
	@PostMapping(value = "/shopAccount/remoteAddShopAccount")
	CommonResultVo<ShopAccount> remoteAddShopAccount(@RequestBody ShopAccount shopAccount);
}

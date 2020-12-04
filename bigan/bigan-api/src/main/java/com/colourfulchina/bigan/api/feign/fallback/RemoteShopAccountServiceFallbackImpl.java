package com.colourfulchina.bigan.api.feign.fallback;

import com.colourfulchina.bigan.api.entity.ShopAccount;
import com.colourfulchina.bigan.api.feign.RemoteShopAccountService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteShopAccountServiceFallbackImpl implements RemoteShopAccountService {

	@Override
	public CommonResultVo<List<ShopAccount>> selectShopAccountList() {
		log.info("fegin查询商户中心失败");
		return null;
	}

	/**
	 * 更新老系统商户中心
	 * @return
	 */
	@Override
	public CommonResultVo<ShopAccount> remoteAddShopAccount(ShopAccount shopAccount) {
		log.info("fegin更新老系统商户中心失败");
		return null;
	}
}

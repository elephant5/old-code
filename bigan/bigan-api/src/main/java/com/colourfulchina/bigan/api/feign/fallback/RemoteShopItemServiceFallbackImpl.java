package com.colourfulchina.bigan.api.feign.fallback;

import com.colourfulchina.bigan.api.entity.ShopItem;
import com.colourfulchina.bigan.api.feign.RemoteShopItemService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteShopItemServiceFallbackImpl implements RemoteShopItemService {
	@Override
	public CommonResultVo<List<ShopItem>> selectShopItemList() {
		log.error("fegin查询shopItem列表失败");
		return null;
	}

	@Override
	public CommonResultVo<List<ShopItem>> selectByShopId(Integer shopId) {
		log.error("fegin根据商户id查询商户规格失败");
		return null;
	}

	@Override
	public CommonResultVo<ShopItem> remoteAddShopItem(ShopItem shopItem) {
		log.error("fegin新增老系统商户规格失败");
		return null;
	}

	@Override
	public CommonResultVo<ShopItem> remoteUpdShopItem(ShopItem shopItem) {
		log.error("fegin更新老系统商户规格失败");
		return null;
	}
}

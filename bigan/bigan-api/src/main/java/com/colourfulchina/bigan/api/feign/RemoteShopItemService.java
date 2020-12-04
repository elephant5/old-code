package com.colourfulchina.bigan.api.feign;

import com.colourfulchina.bigan.api.entity.ShopItem;
import com.colourfulchina.bigan.api.feign.fallback.RemoteShopItemServiceFallbackImpl;
import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = ServiceNameConstant.BIGAN_SERVICE, fallback = RemoteShopItemServiceFallbackImpl.class)
public interface RemoteShopItemService {
	/**
     * fegin查询sqlserver商户shopItem列表
	 * @return
     */
	@PostMapping(value = "/shopItem/selectShopItemList")
	CommonResultVo<List<ShopItem>> selectShopItemList();

	/**
	 * 根据商户id查询商户规格
	 * @param shopId
	 * @return
	 */
	@PostMapping(value = "/shopItem/selectByShopId")
	CommonResultVo<List<ShopItem>> selectByShopId(@RequestBody Integer shopId);

	/**
	 * fegin新增老系统商户规格
	 * @return
	 */
	@PostMapping(value = "/shopItem/remoteAddShopItem")
	CommonResultVo<ShopItem> remoteAddShopItem(@RequestBody ShopItem shopItem);

	/**
	 * fegin更新老系统商户规格
	 * @param shopItem
	 * @return
	 */
	@PostMapping(value = "/shopItem/remoteUpdShopItem")
	CommonResultVo<ShopItem> remoteUpdShopItem(@RequestBody ShopItem shopItem);
}

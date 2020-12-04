package com.colourfulchina.bigan.api.feign;

import com.colourfulchina.bigan.api.entity.Shop;
import com.colourfulchina.bigan.api.feign.fallback.RemoteShopServiceFallbackImpl;
import com.colourfulchina.bigan.api.vo.RemoteAddShopVo;
import com.colourfulchina.bigan.api.vo.RemoteUpdShopBaseMsgVo;
import com.colourfulchina.bigan.api.vo.RemoteUpdShopProtocolVo;
import com.colourfulchina.bigan.api.vo.ShopDetailVo;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "bigan", fallback = RemoteShopServiceFallbackImpl.class)
public interface RemoteShopService {
	@GetMapping("/shop/getShopDetail/{id}")
    CommonResultVo<ShopDetailVo> getShopDetail(@PathVariable("id") Integer id);

	@PostMapping("/shop/selectShopList")
	CommonResultVo<List<Shop>> selectShopList();

	@PostMapping("/shop/remoteAddShop")
	CommonResultVo<Shop> remoteAddShop(@RequestBody RemoteAddShopVo remoteAddShopVo);

	@PostMapping("/shop/remoteUpdShopProtocol")
	CommonResultVo<Shop> remoteUpdShopProtocol(@RequestBody RemoteUpdShopProtocolVo remoteUpdShopProtocolVo);

	@PostMapping("/shop/remoteUpdShopBaseMsg")
	CommonResultVo<Shop> remoteUpdShopBaseMsg(@RequestBody RemoteUpdShopBaseMsgVo remoteUpdShopBaseMsgVo);

}

package com.colourfulchina.bigan.api.feign.fallback;

import com.colourfulchina.bigan.api.entity.Shop;
import com.colourfulchina.bigan.api.feign.RemoteShopService;
import com.colourfulchina.bigan.api.vo.RemoteAddShopVo;
import com.colourfulchina.bigan.api.vo.RemoteUpdShopBaseMsgVo;
import com.colourfulchina.bigan.api.vo.RemoteUpdShopProtocolVo;
import com.colourfulchina.bigan.api.vo.ShopDetailVo;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteShopServiceFallbackImpl implements RemoteShopService {

	@Override
	public CommonResultVo<ShopDetailVo> getShopDetail(Integer id) {
		log.error("feign 查询商户信息失败:{}", id);
		return null;
	}

	@Override
	public CommonResultVo<List<Shop>> selectShopList() {
		log.info("fegin查询老系统商户列表失败");
		return null;
	}

	@Override
	public CommonResultVo<Shop> remoteAddShop(RemoteAddShopVo remoteAddShopVo) {
		log.info("fegin新系统商户新增同步到老系统失败");
		return null;
	}

	@Override
	public CommonResultVo<Shop> remoteUpdShopProtocol(RemoteUpdShopProtocolVo remoteUpdShopProtocolVo) {
		log.info("fegin新系统商户新增时协议信息同步老系统失败");
		return null;
	}

	@Override
	public CommonResultVo<Shop> remoteUpdShopBaseMsg(RemoteUpdShopBaseMsgVo remoteUpdShopBaseMsgVo) {
		log.info("fegin新系统商户基本信息新增修改时同步老系统失败");
		return null;
	}
}

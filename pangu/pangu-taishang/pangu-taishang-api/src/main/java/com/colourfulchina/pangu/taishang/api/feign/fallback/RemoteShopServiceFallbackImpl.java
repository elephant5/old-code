package com.colourfulchina.pangu.taishang.api.feign.fallback;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.feign.RemoteShopService;
import com.colourfulchina.pangu.taishang.api.vo.RepeatInfoVo;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopOrderDetailReq;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopDetailRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopItemRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopOrderDetailRes;
import com.colourfulchina.pangu.taishang.api.vo.res.bigan.ShopDetailVo;
import com.colourfulchina.pangu.taishang.api.vo.res.shop.ShopListQueryResVo;
import com.colourfulchina.pangu.taishang.api.vo.res.shopItem.GoodsShopItemIdRes;
import com.colourfulchina.pangu.taishang.api.vo.res.shopItem.ShopItemConciseRes;
import com.colourfulchina.pangu.taishang.api.vo.res.shopItem.ShopItemResVo;
import com.colourfulchina.pangu.taishang.api.vo.res.shopItem.ShopItemSetmenuInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class RemoteShopServiceFallbackImpl implements RemoteShopService {

	@Override
	public CommonResultVo<ShopDetailRes> shopDetail(Integer shopId) {
		log.error("查询商户信息失败");
		return null;
	}

	@Override
	public CommonResultVo<List<ShopDetailRes>> shopDetailList(List<Integer> shopIds) {
		log.error("查询商户信息失败");
		return null;
	}

	@Override
	public CommonResultVo<ShopItemRes> get(Integer id) {
		log.error("查询商户规格信息失败");
		return null;
	}

	@Override
	public CommonResultVo<Shop> selectShopInfoById(Integer shopId) {
		return null;
	}

	@Override
	public CommonResultVo<List<Shop>> selectShops() {
		log.error("查询商户信息失败");
		return null;
	}


	@Override
	public CommonResultVo<ShopOrderDetailRes> selectShopOrderDetail(ShopOrderDetailReq shopOrderDetailReq) {
		log.error("查询商户详细信息失败");
		return null;
	}

	@Override
	public CommonResultVo<Shop> getShopInfoByGoodsId(Integer goodsId) {
		return null;
	}

	@Override
	public CommonResultVo<List<ShopItemRes>> getShopItemInfo(List<Integer> shopItemIdList) {
		return null;
	}

	@Override
	public CommonResultVo<GoodsShopItemIdRes> getShopItemId(Integer goodsId) {
		return null;
	}

	@Override
	public CommonResultVo<List<ShopListQueryResVo>> getItems(List<Integer> goodsIdList) {
		return null;
	}

	@Override
	public CommonResultVo<List<ShopItemResVo>> selectShopItemPrice(List<Integer> itemsIdList,Date date) {
		return null;
	}

	@Override
	public CommonResultVo<Hotel> selectHotelByShopId(Integer shopId) {
		return null;
	}

	@Override
	public CommonResultVo<ShopItemSetmenuInfo> getShopItemSetmenuInfo(Integer shopItemId) {
		return null;
	}

	@Override
	public CommonResultVo<List<ShopChannel>> selectChannelList() {
		return null;
	}

	@Override
	public CommonResultVo<List<ShopItemConciseRes>> selectByItems(List<Integer> items) {
		log.error("fegin根据item ids查询失败");
		return null;
	}

	@Override
	public CommonResultVo<ShopChannel> shopChannelDetail(Integer shopChannelId) {
		log.error("fegin根据shopChannelId查询失败");
		return null;
	}

	@Override
	public CommonResultVo<List<RepeatInfoVo>> getRepeatInfoVo() {
		return null;
	}

	@Override
	public CommonResultVo<ShopDetailVo> getShopDetail(Integer id) {
		return null;
	}

	@Override
	public CommonResultVo<ShopProtocol> findOneShopProtocol(Integer shopId) {
		return null;
	}

	@Override
	public CommonResultVo<Boolean> shopSalesUp(Integer shopId) {
		log.error("fegin商户预约成功销售次数增加失败");
		return null;
	}

	@Override
	public CommonResultVo<Boolean> shopPointUp(Integer shopId) {
		log.error("fegin商户查看点击次数增加失败");
		return null;
	}

	@Override
	public CommonResultVo<List<ShopItem>> getShopItemList() {
		// TODO Auto-generated method stub
		return null;
	}

}

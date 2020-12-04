package com.colourfulchina.pangu.taishang.api.feign;

import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.feign.fallback.RemoteShopServiceFallbackImpl;
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
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@FeignClient(value =  ServiceNameConstant.PANGU_TAISHANG_SERVICE, fallback = RemoteShopServiceFallbackImpl.class)
public interface RemoteShopService {
	/**
	 * 查询商户信息
	 * @param shopId
	 * @return
	 */
	@PostMapping("/shop/shopDetail")
	CommonResultVo<ShopDetailRes> shopDetail(@RequestBody Integer shopId);


	@PostMapping("/shop/shopDetailList")
	CommonResultVo<List<ShopDetailRes>> shopDetailList(@RequestBody List<Integer> shopIds);


	@GetMapping("/shopItem/get/{id}")
	public CommonResultVo<ShopItemRes> get(@PathVariable(value="id") Integer id);

	@PostMapping("/shop/selectShopInfoById")
	public CommonResultVo<Shop> selectShopInfoById(@RequestBody Integer shopId);


	@PostMapping("/shop/selectShops")
	public CommonResultVo<List<Shop>> selectShops();


	@PostMapping("/shopItem/getShopItemList")
	public CommonResultVo<List<ShopItem>> getShopItemList();


	@PostMapping("/shop/selectShopOrderDetail")
	public CommonResultVo<ShopOrderDetailRes> selectShopOrderDetail(@RequestBody ShopOrderDetailReq shopOrderDetailReq);

	/**
	 * @title:getShopInfoByGoodsId
	 * @Description: 根据goodsId查询shop信息
	 * @Param: [goodsId]
	 * @return: com.colourfulchina.inf.base.vo.CommonResultVo<com.colourfulchina.pangu.taishang.api.entity.Shop>
	 * @Auther: 图南
	 * @Date: 2019/6/20 15:40
	 */
	@PostMapping("/shop/getShopInfoByGoodsId")
	CommonResultVo<Shop> getShopInfoByGoodsId(@RequestBody Integer goodsId);

	@PostMapping("/shopItem/getShopItemInfo")
	CommonResultVo<List<ShopItemRes>> getShopItemInfo(@RequestBody List<Integer> shopItemIdList);

	@PostMapping("/shopItem/getShopItemId")
    CommonResultVo<GoodsShopItemIdRes> getShopItemId(@RequestBody Integer goodsId);

	@PostMapping("/shopItem/getItems")
    CommonResultVo<List<ShopListQueryResVo>> getItems(@RequestBody List<Integer> shopItemIdList);

	@PostMapping("/shopItem/selectShopItemPrice")
	CommonResultVo<List<ShopItemResVo>> selectShopItemPrice(@RequestParam("itemsIdList") List<Integer> itemsIdList ,@RequestParam("date") Date date);

	/**
	 * 根据商户id查询酒店信息
	 * @param shopId
	 * @return
	 */
	@PostMapping("/shop/selectHotelByShopId")
	CommonResultVo<Hotel> selectHotelByShopId(@RequestBody Integer shopId);

	/**
	 * @title:getShopItemSetmenuInfo
	 * @Description: 获取商户定制套餐信息
	 * @Param: [shopItemId]
	 * @return: com.colourfulchina.inf.base.vo.CommonResultVo<com.colourfulchina.pangu.taishang.api.vo.res.shopItem.ShopItemSetmenuInfo>
	 * @Auther: 图南
	 * @Date: 2019/6/27 16:28
	 */
	@PostMapping("/shopItem/getShopItemSetmenuInfo")
    CommonResultVo<ShopItemSetmenuInfo> getShopItemSetmenuInfo(@RequestBody  Integer shopItemId);

	@PostMapping("/shopChannel/selectChannelList")
	public CommonResultVo<List<ShopChannel>> selectChannelList();

	@PostMapping("/shopItem/selectByItems")
	CommonResultVo<List<ShopItemConciseRes>> selectByItems(@RequestBody List<Integer> items);

	@PostMapping("/shopChannel/shopChannelDetail")
	public CommonResultVo<ShopChannel> shopChannelDetail(@RequestBody  Integer shopChannelId);


	@GetMapping("/shop/getRepeatInfoVo")
	public CommonResultVo<List<RepeatInfoVo>> getRepeatInfoVo();

	@GetMapping({"/bigan-replace/shop/getShopDetail/{id}"})
	CommonResultVo<ShopDetailVo> getShopDetail(@PathVariable(value = "id") Integer id);

	//根据商户id查询商户协议
	@PostMapping("/shopProtocol/findOneShopProtocol")
	CommonResultVo<ShopProtocol> findOneShopProtocol(@RequestBody Integer shopId);

	//商户预约成功销售次数增加
	@PostMapping("/shop/shopSalesUp")
	CommonResultVo<Boolean> shopSalesUp(@RequestBody Integer shopId);

	//商户查看点击次数增加
	@PostMapping("/shop/shopPointUp")
	CommonResultVo<Boolean> shopPointUp(@RequestBody Integer shopId);
}

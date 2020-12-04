package com.colourfulchina.pangu.taishang.api.feign;

import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.Product;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroup;
import com.colourfulchina.pangu.taishang.api.entity.SysService;
import com.colourfulchina.pangu.taishang.api.feign.fallback.RemoteProductGroupServiceFallbackImpl;
import com.colourfulchina.pangu.taishang.api.vo.req.QueryProductGroupInfoReqVo;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectShopByGroupServiceReq;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopSettleMsgReq;
import com.colourfulchina.pangu.taishang.api.vo.res.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value =  ServiceNameConstant.PANGU_TAISHANG_SERVICE, fallback = RemoteProductGroupServiceFallbackImpl.class)
public interface RemoteProductGroupService {

	/**
	 * 查询商品下面的产品组信息
	 * @param goodsId
	 * @return
	 */
	@PostMapping("/productGroup/selectGoodsGroup")
	CommonResultVo<List<GoodsGroupListRes>> selectGoodsGroup(@RequestBody Integer goodsId);

	/**
	 * 根据产品组ids查询产品组信息
	 * @param groupIds
	 * @return
	 */
	@PostMapping("/productGroup/selectGoodsGroupByIds")
	CommonResultVo<List<GoodsGroupListRes>> selectGoodsGroupByIds(@RequestBody String groupIds);

	/**
	 * 查询产品组的资源类型
	 * @param productGroupId
	 * @return
	 */
	@PostMapping("/productGroup/selectGroupService")
	CommonResultVo<List<SysService>> selectGroupService(@RequestBody Integer productGroupId);



	@ApiOperation("预约获取商户结算信息")
	@PostMapping("/productGroup/shopSettleMsg")
	public CommonResultVo<ShopSettleMsgRes> shopSettleMsg(@RequestBody ShopSettleMsgReq shopSettleMsgReq);

	@ApiOperation("根据id查询产品组")
	@GetMapping("/productGroup/findOneById/{id}")
	public CommonResultVo<GroupQueryOneRes> findOneById(@PathVariable("id") Integer productGroupId);

	@ApiOperation("根据id查询商品产品组信息")
	@PostMapping("/productGroup/selectGoodsGroupByGoodsId")
    CommonResultVo<List<GoodsGroupListRes>> selectGoodsGroupByGoodsId(@RequestBody Integer goodsId);

	@ApiOperation("根据产品组id合资源类型查询产品组产品详情")
	@PostMapping("/productGroup/selectProductByGroupService")
	CommonResultVo<List<SelectProductByGroupServiceRes>> selectProductByGroupService(@RequestBody SelectShopByGroupServiceReq selectShopByGroupServiceReq);


	@PostMapping("/productGroup/selectGoodsGroupById")
	CommonResultVo<List<GoodsGroupListRes>> selectGoodsGroupById(@RequestBody Integer groupById);

	/**
	 * 查询产品
	 * @param p
	 * @return
	 */
	@PostMapping("/product/getProductByShop")
	public CommonResultVo<Product> getProductByShop(@RequestBody Product p);

	@PostMapping("/productGroup/selectProductGroupById")
	public CommonResultVo<List<ProductGroupResVO>> selectProductGroupById(@RequestBody QueryProductGroupInfoReqVo reqVo);

	//根据产品组id列表查询折扣比例
	@PostMapping("/productGroup/selectDiscountByIds")
	CommonResultVo<List<ProductGroup>> selectDiscountByIds(@RequestBody List<Integer> groups);

	//根据Charlie产品组id查询产品组信息
	@PostMapping("/productGroup/selectGroupByOldId")
	CommonResultVo<List<ProductGroup>> selectGroupByOldId(@RequestBody Integer oldId);
}

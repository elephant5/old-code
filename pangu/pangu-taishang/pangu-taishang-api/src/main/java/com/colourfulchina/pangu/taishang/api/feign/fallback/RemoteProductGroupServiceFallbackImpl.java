package com.colourfulchina.pangu.taishang.api.feign.fallback;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.Product;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroup;
import com.colourfulchina.pangu.taishang.api.entity.SysService;
import com.colourfulchina.pangu.taishang.api.feign.RemoteProductGroupService;
import com.colourfulchina.pangu.taishang.api.vo.req.QueryProductGroupInfoReqVo;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectShopByGroupServiceReq;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopSettleMsgReq;
import com.colourfulchina.pangu.taishang.api.vo.res.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteProductGroupServiceFallbackImpl implements RemoteProductGroupService {
	@Override
	public CommonResultVo<List<GoodsGroupListRes>> selectGoodsGroup(Integer goodsId) {
		log.error("fegin查询商品的产品组失败");
		return null;
	}

	@Override
	public CommonResultVo<List<GoodsGroupListRes>> selectGoodsGroupByIds(String groupIds) {
		log.error("fegin根据产品组ids查询产品组失败");
		return null;
	}

	@Override
	public CommonResultVo<List<SysService>> selectGroupService(Integer productGroupId) {
		log.error("fegin查询产品组的资源类型失败");
		return null;
	}

	@Override
	public CommonResultVo<ShopSettleMsgRes> shopSettleMsg(ShopSettleMsgReq shopSettleMsgReq) {
		log.error("fegin预约获取商户结算信息");
		return null;
	}

	@Override
	public CommonResultVo<GroupQueryOneRes> findOneById(Integer productGroupId) {
		return null;
	}

	@Override
	public CommonResultVo<List<GoodsGroupListRes>> selectGoodsGroupByGoodsId(Integer goodsId) {
		return null;
	}

	@Override
	public CommonResultVo<List<SelectProductByGroupServiceRes>> selectProductByGroupService(SelectShopByGroupServiceReq selectShopByGroupServiceReq) {
		log.error("fegin根据产品组id合资源类型查询产品组产品详情失败");
		return null;
	}

	@Override
	public CommonResultVo<List<GoodsGroupListRes>> selectGoodsGroupById(Integer groupById) {
		return null;
	}

	/**
	 * 查询产品
	 *
	 * @param p
	 * @return
	 */
	@Override
	public CommonResultVo<Product> getProductByShop(Product p) {
		log.error("fegin查询产品失败");
		return null;
	}

	@Override
	public CommonResultVo<List<ProductGroupResVO>> selectProductGroupById(QueryProductGroupInfoReqVo reqVo) {
		return null;
	}

	@Override
	public CommonResultVo<List<ProductGroup>> selectDiscountByIds(List<Integer> groups) {
		log.error("fegin根据产品组id列表查询折扣比例失败");
		return null;
	}

	@Override
	public CommonResultVo<List<ProductGroup>> selectGroupByOldId(Integer oldId) {
		log.error("fegin根据Charlie系统产品组id查询产品组失败");
		return null;
	}


}

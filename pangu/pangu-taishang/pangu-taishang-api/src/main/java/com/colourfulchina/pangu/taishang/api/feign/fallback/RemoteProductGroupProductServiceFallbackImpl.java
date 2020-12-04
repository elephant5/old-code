package com.colourfulchina.pangu.taishang.api.feign.fallback;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroupProduct;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemNetPriceRule;
import com.colourfulchina.pangu.taishang.api.feign.RemoteProductGroupProductService;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectBookPayReq;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectBookProductReq;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectBookShopItemReq;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopListPageQueryReq;
import com.colourfulchina.pangu.taishang.api.vo.res.*;
import com.colourfulchina.pangu.taishang.api.vo.res.bookBasePayment.BookBasePaymentRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteProductGroupProductServiceFallbackImpl implements RemoteProductGroupProductService {
	@Override
	public CommonResultVo<GroupProductDetailRes> selectProductDetail(Integer productGroupProductId) {
		log.error("feign查询产品组下面产品详情失败");
		return null;
	}

	@Override
	public CommonResultVo<List<ShopItemNetPriceRule>> selectProductPrices(Integer productGroupProductId) {
		log.error("fegin查询产品组产品的价格失败");
		return null;
	}

	@Override
	public CommonResultVo<List<SelectBookProductRes>> selectBookProduct(SelectBookProductReq selectBookProductReq) {
		log.error("feign查询产品组下面产品列表失败");
		return null;
	}

	@Override
	public CommonResultVo<PageVo<SelectBookProductRes>> selectBookProductPaging(PageVo<SelectBookShopItemReq> pageVo) {
		log.error("feign分页查询产品组下面产品列表失败");
		return null;
	}

	@Override
	public CommonResultVo<List<SelectBookProductRes>> selectBookProductNew(SelectBookProductReq selectBookProductReq) {
		log.error("fegin查询产品组产品列表失败");
		return null;
	}

	@Override
	public CommonResultVo<PageVo<ShopListQueryRes>> selectGoodsListByGroupId(PageVo<ShopListPageQueryReq> pageVo) {
		log.error("fegin查询产品组下面的产品信息（商区）失败");
		return null;
	}

	@Override
	public CommonResultVo<List<BookBasePaymentRes>> selectBookPay(SelectBookPayReq selectBookPayReq) {
		log.error("fegin查询产品预约支付金额失败");
		return null;
	}

	@Override
	public CommonResultVo<ProductGroupProduct> findById(Integer id) {
		log.error("fegin查询产品信息失败:{}",id);
		return null;
	}

	@Override
	public CommonResultVo generateBothBlockDate() {
		log.error("fegin生成产品组产品block日期失败");
		return null;
	}

	@Override
	public CommonResultVo<List<BookBasePaymentRes>> selectBookPayList(List<Integer> productGroupProductIdList) {
		return null;
	}

	@Override
	public CommonResultVo<GroupDetailRes> groupDetail(Integer id) {
		return null;
	}

}

package com.colourfulchina.pangu.taishang.api.feign;

import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroupProduct;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemNetPriceRule;
import com.colourfulchina.pangu.taishang.api.feign.fallback.RemoteProductGroupProductServiceFallbackImpl;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectBookPayReq;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectBookProductReq;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectBookShopItemReq;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopListPageQueryReq;
import com.colourfulchina.pangu.taishang.api.vo.res.GroupDetailRes;
import com.colourfulchina.pangu.taishang.api.vo.res.GroupProductDetailRes;
import com.colourfulchina.pangu.taishang.api.vo.res.SelectBookProductRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopListQueryRes;
import com.colourfulchina.pangu.taishang.api.vo.res.bookBasePayment.BookBasePaymentRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value =  ServiceNameConstant.PANGU_TAISHANG_SERVICE, fallback = RemoteProductGroupProductServiceFallbackImpl.class)
public interface RemoteProductGroupProductService {

	/**
	 * 查询产品组下面产品的详情
	 * @param productGroupProductId
	 * @return
	 */
	@PostMapping("/productGroupProduct/selectProductDetail")
	CommonResultVo<GroupProductDetailRes> selectProductDetail(@RequestBody Integer productGroupProductId);

	/**
	 * 查询产品组产品的价格
	 * @param productGroupProductId
	 * @return
	 */
	@PostMapping("/productGroupProduct/selectProductPrices")
	CommonResultVo<List<ShopItemNetPriceRule>> selectProductPrices(@RequestBody Integer productGroupProductId);

	/**
	 * 查询产品组下的产品列表
	 * @param selectBookProductReq
	 * @return
	 */
	@PostMapping("/productGroupProduct/selectBookProduct")
	CommonResultVo<List<SelectBookProductRes>> selectBookProduct(@RequestBody SelectBookProductReq selectBookProductReq);
	/**
	 * 查询产品组下的产品列表 分页
	 * @param selectBookProductReq
	 * @return
	 */
	@PostMapping("/productGroupProduct/selectBookProductPaging")
	CommonResultVo<PageVo<SelectBookProductRes>> selectBookProductPaging(@RequestBody PageVo<SelectBookShopItemReq> pageVo);

	/**
	 * 查询产品列表(优化)
	 *
	 * @param selectBookProductReq
	 * @return
	 */
	@PostMapping("/productGroupProduct/selectBookProductNew")
	CommonResultVo<List<SelectBookProductRes>> selectBookProductNew(@RequestBody SelectBookProductReq selectBookProductReq);

	/**
	 * 查询产品组下面的产品信息（商区）
	 * @param pageVo
	 * @return
	 */
	@PostMapping("/productGroupProduct/selectGoodsListByGroupId")
	CommonResultVo<PageVo<ShopListQueryRes>> selectGoodsListByGroupId(@RequestBody PageVo<ShopListPageQueryReq> pageVo);

	/**
	 * 查询产品预约支付金额
	 * @param selectBookPayReq
	 * @return
	 */
	@PostMapping("/bookBasePayment/selectBookPay")
	CommonResultVo<List<BookBasePaymentRes>> selectBookPay(@RequestBody SelectBookPayReq selectBookPayReq);

	@PostMapping("/productGroupProduct/findById")
	CommonResultVo<ProductGroupProduct> findById(@RequestBody Integer id);

	/**
	 * 生成所有产品组产品的block日期
	 * @return
	 */
	@PostMapping("/groupProductBlockDate/generateBothBlockDate")
	CommonResultVo generateBothBlockDate();

	/**
	 * @title:selectBookPayList
	 * @Description: 批量查询预约支付金额
	 * @Param: [productGroupProductIdList]
	 * @return: com.colourfulchina.inf.base.vo.CommonResultVo<java.util.List<com.colourfulchina.pangu.taishang.api.vo.res.bookBasePayment.BookBasePaymentRes>>
	 * @Auther: 图南
	 * @Date: 2019/9/26 16:11
	 */
	@PostMapping("/bookBasePayment/selectBookPayList")
	CommonResultVo<List<BookBasePaymentRes>> selectBookPayList(@RequestBody List<Integer> productGroupProductIdList);

	@PostMapping("/productGroup/groupDetail")
	CommonResultVo<GroupDetailRes> groupDetail(@RequestBody Integer id);
}

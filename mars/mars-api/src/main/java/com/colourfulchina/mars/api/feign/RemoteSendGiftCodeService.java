package com.colourfulchina.mars.api.feign;

import com.colourfulchina.colourfulCoupon.api.entity.CpnThirdCode;
import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.GiftCode;
import com.colourfulchina.mars.api.feign.fallback.RemoteSendGiftCodeServiceImpl;
import com.colourfulchina.mars.api.vo.req.*;
import com.colourfulchina.mars.api.vo.res.GiftOrderInfoResVo;
import com.colourfulchina.mars.api.vo.res.GiftOrderResVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = ServiceNameConstant.MARS_SERVICE, fallback = RemoteSendGiftCodeServiceImpl.class)
public interface RemoteSendGiftCodeService {

	@PostMapping("/orderApi/createOrder")
	public CommonResultVo<GiftOrderResVo> createOrder(@RequestBody GiftOrderReqVo reqVo);

	@PostMapping("/orderApi/updateOrder")
	public CommonResultVo<GiftOrderResVo> updateOrder(@RequestBody GiftOrderPayReqVo reqVo);

	@PostMapping("/orderApi/getOrder")
	public CommonResultVo<GiftOrderInfoResVo> getOrder(@RequestBody String oderId);

	@PostMapping("/orderApi/refundOrder")
	public CommonResultVo<Integer> refundOrder(@RequestBody String orderId);

	@PostMapping("/orderApi/getOrderDetail")
	public CommonResultVo<GiftOrderInfoResVo> getOrderDetail(@RequestBody GiftOrderQueryReqVo reqVo);

	@PostMapping("/orderApi/updateOrderId")
	public CommonResultVo updateOrderId(@RequestBody GiftOrderIdReqVo reqVo);

	@PostMapping("/goods/check")
	public boolean checkGiftByid(@RequestBody GiftCode id);

	@PostMapping("/coupons/receiveCoupons")
	public CommonResultVo<CpnThirdCode> getBoscCardList(@RequestBody CouponThirdCodeReqVO reqVO);
}

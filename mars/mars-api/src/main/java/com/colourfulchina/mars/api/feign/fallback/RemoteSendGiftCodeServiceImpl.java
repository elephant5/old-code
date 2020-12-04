package com.colourfulchina.mars.api.feign.fallback;

import com.colourfulchina.colourfulCoupon.api.entity.CpnThirdCode;
import com.colourfulchina.mars.api.entity.GiftCode;
import com.colourfulchina.mars.api.vo.req.*;
import org.springframework.stereotype.Component;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.feign.RemoteSendGiftCodeService;
import com.colourfulchina.mars.api.vo.res.GiftOrderInfoResVo;
import com.colourfulchina.mars.api.vo.res.GiftOrderResVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RemoteSendGiftCodeServiceImpl implements RemoteSendGiftCodeService {
	
	@Override
	public CommonResultVo<GiftOrderResVo> createOrder(GiftOrderReqVo reqVo) {
		log.error("fegin createOrder异常");
		return null;
	}
	
	@Override
	public CommonResultVo<GiftOrderResVo> updateOrder(GiftOrderPayReqVo reqVo) {
		log.error("fegin updateOrder异常");
		return null;
	}

	@Override
	public CommonResultVo<GiftOrderInfoResVo> getOrder(String oderId) {
		log.error("fegin getOrder异常");
		return null;
	}

	@Override
	public CommonResultVo<Integer> refundOrder(String orderId) {
		log.error("fegin refundOrder异常");
		return null;
	}

	@Override
	public CommonResultVo<GiftOrderInfoResVo> getOrderDetail(GiftOrderQueryReqVo reqVo) {
		log.error("fegin getOrderDetail异常");
		return null;
	}

	@Override
	public CommonResultVo updateOrderId(GiftOrderIdReqVo reqVo) {
		log.error("fegin updateOrderId异常");
		return null;
	}

    @Override
    public boolean checkGiftByid(GiftCode id) {
		log.error("fegin checkGiftByid异常");
		return false;
    }

    @Override
    public CommonResultVo<CpnThirdCode> getBoscCardList(CouponThirdCodeReqVO reqVO) {
        log.error("fegin getBoscCardList异常");
		return null;
    }

}

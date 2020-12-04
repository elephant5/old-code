package com.colourfulchina.mars.controller;

import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.vo.req.GiftOrderIdReqVo;
import com.colourfulchina.mars.api.vo.req.GiftOrderPayReqVo;
import com.colourfulchina.mars.api.vo.req.GiftOrderQueryReqVo;
import com.colourfulchina.mars.api.vo.req.GiftOrderReqVo;
import com.colourfulchina.mars.api.vo.res.GiftOrderInfoResVo;
import com.colourfulchina.mars.api.vo.res.GiftOrderResVo;
import com.colourfulchina.mars.service.GiftOrderApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/orderApi")
public class GiftOrderApiController {

	private final static int SUCCESS_CODE = 100;
	private final static int ERROR_CODE = 200;

	@Autowired
	private GiftOrderApiService giftOrderApiService;

	@SysGodDoorLog("激活订单下单")
	@PostMapping("/createOrder")
	public CommonResultVo<GiftOrderResVo> createOrder(@RequestBody GiftOrderReqVo reqVo) throws Exception {
		CommonResultVo<GiftOrderResVo> commResultVo = new CommonResultVo<GiftOrderResVo>();
		try {
			GiftOrderResVo order = giftOrderApiService.createOrder(reqVo);
			commResultVo.setCode(SUCCESS_CODE);
			commResultVo.setResult(order);
		} catch (Exception e) {
			log.error("激活订单下单", e);
			commResultVo.setCode(ERROR_CODE);
			commResultVo.setMsg(e.getMessage());
		}
		return commResultVo;
	}
	
	@SysGodDoorLog("激活订单更新")
	@PostMapping("/updateOrder")
	public CommonResultVo<GiftOrderResVo> updateOrder(@RequestBody GiftOrderPayReqVo reqVo) throws Exception {
		CommonResultVo<GiftOrderResVo> commResultVo = new CommonResultVo<GiftOrderResVo>();
		try {
			GiftOrderResVo order = giftOrderApiService.updateOrder(reqVo);
			commResultVo.setCode(SUCCESS_CODE);
			commResultVo.setResult(order);
		} catch (Exception e) {
			log.error("激活订单下单", e);
			commResultVo.setCode(ERROR_CODE);
			commResultVo.setMsg(e.getMessage());
		}
		return commResultVo;
	}


	@SysGodDoorLog("异步通知商户")
	@PostMapping("/noticeOrder")
	public CommonResultVo<GiftOrderResVo> noticeOrder(@RequestBody GiftOrderReqVo reqVo) throws Exception {
		CommonResultVo<GiftOrderResVo> commonResultVo = new CommonResultVo<GiftOrderResVo>();
		try{
			GiftOrderResVo notice = giftOrderApiService.noticeOrder(reqVo);
			commonResultVo.setCode(SUCCESS_CODE);
			commonResultVo.setResult(notice);
		}catch (Exception e){
			log.error("异步通知商户失败", e);
			commonResultVo.setCode(ERROR_CODE);
			commonResultVo.setMsg(e.getMessage());
		}
		return commonResultVo;
	}


	@SysGodDoorLog("查询订单信息")
	@PostMapping("/getOrder")
	public CommonResultVo<GiftOrderInfoResVo> getOrder(@RequestBody String  orderId) throws Exception {
		CommonResultVo<GiftOrderInfoResVo> commonResultVo = new CommonResultVo<GiftOrderInfoResVo>();
		try{
			GiftOrderInfoResVo notice = giftOrderApiService.getOrder(orderId);
			commonResultVo.setCode(SUCCESS_CODE);
			commonResultVo.setResult(notice);
		}catch (Exception e){
			log.error("异步通知商户失败", e);
			commonResultVo.setCode(ERROR_CODE);
			commonResultVo.setMsg(e.getMessage());
		}
		return commonResultVo;
	}
	
	@SysGodDoorLog("订单退货")
	@PostMapping("/refundOrder")
	public CommonResultVo<Integer> refundOrder(@RequestBody String  orderId) throws Exception {
		CommonResultVo<Integer> commonResultVo = new CommonResultVo<Integer>();
		try{
			Integer cnt = giftOrderApiService.refundOrder(orderId);
			commonResultVo.setCode(SUCCESS_CODE);
			commonResultVo.setResult(cnt);
		}catch (Exception e){
			log.error("订单退货户失败", e);
			commonResultVo.setCode(ERROR_CODE);
			commonResultVo.setMsg(e.getMessage());
		}
		return commonResultVo;
	}

    @SysGodDoorLog("查询订单信息")
    @PostMapping("/getOrderDetail")
    public CommonResultVo<GiftOrderInfoResVo> getOrderDetail(@RequestBody GiftOrderQueryReqVo reqVo) throws Exception {
        CommonResultVo<GiftOrderInfoResVo> commonResultVo = new CommonResultVo<GiftOrderInfoResVo>();
        try {
            GiftOrderInfoResVo notice = giftOrderApiService.getOrderDetail(reqVo);
            commonResultVo.setCode(SUCCESS_CODE);
            commonResultVo.setResult(notice);
        } catch (Exception e) {
            commonResultVo.setCode(ERROR_CODE);
            commonResultVo.setMsg(e.getMessage());
        }
        return commonResultVo;
    }

    @SysGodDoorLog("更新订单ID")
    @PostMapping("/updateOrderId")
    public CommonResultVo updateOrderId(@RequestBody GiftOrderIdReqVo reqVo) throws Exception {
        CommonResultVo commResultVo = new CommonResultVo();
        try {
            giftOrderApiService.updateOrderId(reqVo);
            commResultVo.setCode(SUCCESS_CODE);
        } catch (Exception e) {
            log.error("更新订单ID异常", e);
            commResultVo.setCode(ERROR_CODE);
            commResultVo.setMsg(e.getMessage());
        }
        return commResultVo;
    }

}

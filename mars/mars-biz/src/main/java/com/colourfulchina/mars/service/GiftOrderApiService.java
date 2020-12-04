package com.colourfulchina.mars.service;

import com.colourfulchina.mars.api.entity.GiftOrderCapital;
import com.colourfulchina.mars.api.vo.req.GiftOrderIdReqVo;
import com.colourfulchina.mars.api.vo.req.GiftOrderPayReqVo;
import com.colourfulchina.mars.api.vo.req.GiftOrderQueryReqVo;
import com.colourfulchina.mars.api.vo.req.GiftOrderReqVo;
import com.colourfulchina.mars.api.vo.res.GiftOrderInfoResVo;
import com.colourfulchina.mars.api.vo.res.GiftOrderResVo;

import java.util.List;

public interface GiftOrderApiService {

	/**
	 * @throws
	 * @Title: createOrder
	 * @Description: 下单
	 * @author: sunny.wang
	 * @date: 2019年7月29日 下午2:29:47
	 * @param: @param reqVo
	 * @param: @return
	 * @param: @throws Exception
	 * @return: GiftOrderResVo
	 */
	public GiftOrderResVo createOrder(GiftOrderReqVo reqVo) throws Exception;

	/**
	 * @throws
	 * @Title: updateOrder
	 * @Description: 更新
	 * @author: sunny.wang
	 * @date: 2019年7月29日 下午2:29:50
	 * @param: @param reqVo
	 * @param: @return
	 * @param: @throws Exception
	 * @return: GiftOrderResVo
	 */
	public GiftOrderResVo updateOrder(GiftOrderPayReqVo reqVo) throws Exception;

	public GiftOrderResVo noticeOrder(GiftOrderReqVo reqVo) throws Exception;

	public GiftOrderInfoResVo getOrder(String orderId) throws Exception;

	public Integer refundOrder(String orderId) throws Exception;

	public GiftOrderInfoResVo getOrderDetail(GiftOrderQueryReqVo reqVo) throws Exception;

	public void updateOrderId(GiftOrderIdReqVo reqVo) throws Exception;

	public String getOrderInfoByGiftCode(Integer actCodeId) throws Exception;
}

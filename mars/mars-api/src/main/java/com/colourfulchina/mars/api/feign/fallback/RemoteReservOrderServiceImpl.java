package com.colourfulchina.mars.api.feign.fallback;

import com.alibaba.fastjson.JSON;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.BookOrderDetail;
import com.colourfulchina.mars.api.entity.ReservCode;
import com.colourfulchina.mars.api.entity.ReservOrder;
import com.colourfulchina.mars.api.feign.RemoteReservOrderService;
import com.colourfulchina.mars.api.vo.BookOrderReq;
import com.colourfulchina.mars.api.vo.ReservOrderComInfoVo;
import com.colourfulchina.mars.api.vo.ReservOrderVo;
import com.colourfulchina.mars.api.vo.req.AlipayBookOrderReq;
import com.colourfulchina.mars.api.vo.req.NewReservOrderPlaceReq;
import com.colourfulchina.mars.api.vo.req.QueryReserveOrderDateReqVO;
import com.colourfulchina.mars.api.vo.res.ReservOrderResVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Sunny
 * @version V1.0
 * @ClassName: RemoteGiftTableServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2018年10月8日
 * @company Colourful@copyright(c) 2018
 */
@Slf4j
@Component
public class RemoteReservOrderServiceImpl implements RemoteReservOrderService {

  @Override
  public CommonResultVo<List<ReservOrder>> selectPayTimeOutReservOrderList(
      ReservOrderVo reservOrderVo) {
    log.error("feign RemoteReservOrderService selectPayTimeOutReservOrderList error:{}",
        JSON.toJSONString(reservOrderVo));
    return null;
  }

  @Override
  public CommonResultVo<ReservOrderVo> reservCancel(ReservOrderVo reservOrderVo) {
    log.error("feign RemoteReservOrderService oreservCancel error:{}",
        JSON.toJSONString(reservOrderVo));
    return null;
  }

  @Override
  public CommonResultVo<ReservOrderVo> reservFail(ReservOrderVo reservOrderVo) {
    log.error("feign RemoteReservOrderService reservFail error:{}",
        JSON.toJSONString(reservOrderVo));
    return null;
  }

  @Override
  public CommonResultVo<ReservOrderVo> reservSuccess(ReservOrderVo reservOrderVo) {
    log.error("feign RemoteReservOrderService reservSuccess error:{}",
        JSON.toJSONString(reservOrderVo));
    return null;
  }

  /**
   * 预订成功
   */
  @Override
  public CommonResultVo<ReservOrderVo> updateCouponsReservOrder(ReservOrderVo reservOrderVo) {
    log.error("feign RemoteReservOrderService updateCouponsReservOrder error:{}",
        JSON.toJSONString(reservOrderVo));
    return null;
  }

  @Override
  public CommonResultVo<ReservOrder> success(HttpServletRequest httpRequest) {
    log.error("feign RemoteReservOrderService success error:{}", JSON.toJSONString(httpRequest));
    return null;
  }

  @Override
  public CommonResultVo<String> getReserveOrderDate(QueryReserveOrderDateReqVO reqVO) {
    return null;
  }

  @Override
  public CommonResultVo<List<ReservOrder>> getOrderList(Integer giftCodeId) {
    log.error("feign list error:{}", giftCodeId);
    return null;
  }

  @Override
  public CommonResultVo<ReservCode> getReservCode(Integer reservCodeId) {
    log.error("feign getReservCode error:{}", reservCodeId);
    return null;
  }

  @Override
  public CommonResultVo<List<BookOrderDetail>> getBookOrderDetail(BookOrderReq req) {
    log.error("feign getBookOrderDetail error:{}", JSON.toJSONString(req));
    return null;
  }

  @Override
  public CommonResultVo<List<Map<String, Object>>> getgiftConut(Map<String, Object> req) {
    log.error("feign getgiftConut error:{}", JSON.toJSONString(req));
    return null;
  }

  @Override
  public CommonResultVo<ReservOrderVo> getbookOrderCancel(Map<String, Object> req) {
    log.error("feign getbookOrderCancel error:{}", req);
    return null;
  }

  @Override
  public CommonResultVo<ReservOrderResVO> openPlaceOrder(
      NewReservOrderPlaceReq newreservOrderPlaceReq) {
    log.error("fegin openPlaceOrder error:{}", newreservOrderPlaceReq);
    return null;
  }

  @Override
  public CommonResultVo<List<ReservOrder>> getReservOrders(AlipayBookOrderReq req) {
    return null;
  }

  @Override
  public CommonResultVo<ReservOrderVo> getReservOrder(Integer id) {
    return null;
  }

  public CommonResultVo<ReservOrderVo> reservUpdate(ReservOrderVo reservOrderVo){
    return null;
  }

  @Override
  public CommonResultVo<List<ReservOrder>> getOrderListByContion(ReservOrder order) {
    log.error("fegin invoke error");
    return null;
  }

  @Override
  public CommonResultVo<Boolean> changeOrderPayStatus(ReservOrder reservOrder) {
    log.error("fegin改变预约单支付状态失败");
    return null;
  }

	@Override
	public CommonResultVo<List<ReservOrderComInfoVo>> getComInfoByOrderIds(@RequestBody List<Integer> ids){
		log.error("fegin getComInfoByOrderIds error:{}",ids);
		return null;
	}

	@Override
	public CommonResultVo<Boolean> updateComInfoByMap(@RequestBody Map<String, Date> map){
		log.error("fegin updateComInfoByOrderIds error:{}",map);
		return null;
	}

    @Override
    @PostMapping("/reservOrder/getReservOrderBySalesOrderId")
    public CommonResultVo<ReservOrder> getReservOrderBySalesOrderId(@RequestBody String salesOrderId){
        log.error("fegin getReservOrderBySalesOrderId error:{}",salesOrderId);
        return null;
    }

  @Override
  public CommonResultVo<List<ReservOrder>> selectAlipayFreeList() {
    log.error("fegin查询支付宝钻铂免费项目取消/失败的已支付的预约单失败");
    return null;
  }

  /**
   * 医疗的预约成功的数据
   *
   * @param reservOrderVo
   * @return
   */
  @Override
  public CommonResultVo<List<ReservOrderVo>> selectSuceessReservOrderList(ReservOrderVo reservOrderVo) {
    log.error("fegin查询医疗的预约成功的数据失败");
    return null;
  }

}

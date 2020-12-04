package com.colourfulchina.mars.api.feign;

import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.BookOrderDetail;
import com.colourfulchina.mars.api.entity.ReservCode;
import com.colourfulchina.mars.api.entity.ReservOrder;
import com.colourfulchina.mars.api.feign.fallback.RemoteReservOrderServiceImpl;
import com.colourfulchina.mars.api.vo.BookOrderReq;
import com.colourfulchina.mars.api.vo.ReservOrderComInfoVo;
import com.colourfulchina.mars.api.vo.ReservOrderVo;
import com.colourfulchina.mars.api.vo.req.AlipayBookOrderReq;
import com.colourfulchina.mars.api.vo.req.NewReservOrderPlaceReq;
import com.colourfulchina.mars.api.vo.req.QueryReserveOrderDateReqVO;
import com.colourfulchina.mars.api.vo.res.ReservOrderResVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Sunny
 * @version V1.0
 * @ClassName: RemoteActivateCodeService
 * @date 2018年10月8日
 * @company Colourful@copyright(c) 2018
 */
@FeignClient(value = ServiceNameConstant.MARS_SERVICE, fallback = RemoteReservOrderServiceImpl.class)
public interface RemoteReservOrderService {

    /**
     * 查询支付超时订单
     */
    @PostMapping("/reservOrder/selectPayTimeOutReservOrderList")
    CommonResultVo<List<ReservOrder>> selectPayTimeOutReservOrderList(
            @RequestBody ReservOrderVo reservOrderVo);

    /**
     * 预订取消
     */
    @PostMapping("/reservOrder/reservCancel")
    CommonResultVo<ReservOrderVo> reservCancel(@RequestBody ReservOrderVo reservOrderVo);

    /**
     * 预订失败
     */
    @PostMapping("/reservOrder/reservFail")
    CommonResultVo<ReservOrderVo> reservFail(@RequestBody ReservOrderVo reservOrderVo);

    /**
     * 预订成功
     */
    @PostMapping("/reservOrder/reservSuccess")
    CommonResultVo<ReservOrderVo> reservSuccess(@RequestBody ReservOrderVo reservOrderVo);

    /**
     * 预订成功
     */
    @PostMapping("/reservOrder/updateCouponsReservOrder")
    CommonResultVo<ReservOrderVo> updateCouponsReservOrder(@RequestBody ReservOrderVo reservOrderVo);


    @ApiOperation("预约单支付成功接口")
    @PostMapping("/pay/payStatus/success")
    public CommonResultVo<ReservOrder> success(HttpServletRequest httpRequest);


    @PostMapping("/reservOrder/getReserveOrderDate")
    public CommonResultVo<String> getReserveOrderDate(@RequestBody QueryReserveOrderDateReqVO reqVO);

    @PostMapping("/reservOrder/getOrderList")
    CommonResultVo<List<ReservOrder>> getOrderList(@RequestBody Integer giftCodeId);

    @PostMapping("/reservCode/getReservCode")
    public CommonResultVo<ReservCode> getReservCode(@RequestBody Integer reservCodeId);


    @PostMapping("/reservOrder/bookOrderDetail")
    CommonResultVo<List<BookOrderDetail>> getBookOrderDetail(@RequestBody BookOrderReq req);

    @PostMapping("/reservOrder/giftConut")
    CommonResultVo<List<Map<String, Object>>> getgiftConut(@RequestBody Map<String, Object> req);

    @PostMapping("/reservOrder/bookOrderCancel")
    CommonResultVo<ReservOrderVo> getbookOrderCancel(@RequestBody Map<String, Object> req);

    /*
     * 对外下预约单
     */
    @PostMapping("/reservOrderAttach/openPlaceOrder")
    CommonResultVo<ReservOrderResVO> openPlaceOrder(
            @RequestBody NewReservOrderPlaceReq newreservOrderPlaceReq);

    @PostMapping("/reservOrder/getOrders")
    CommonResultVo<List<ReservOrder>> getReservOrders(@RequestBody AlipayBookOrderReq req);

    @GetMapping({"/reservOrder/getOrder/{id}"})
    CommonResultVo<ReservOrderVo> getReservOrder(@PathVariable("id") Integer id);

    @PostMapping("/reservOrder/reservUpdate")
    public CommonResultVo<ReservOrderVo> reservUpdate(@RequestBody ReservOrderVo reservOrderVo);

    @PostMapping("/reservOrder/getOrderListByContion")
    public CommonResultVo<List<ReservOrder>> getOrderListByContion(@RequestBody ReservOrder order);

    //改变预约单支付状态
    @PostMapping("/reservOrder/changeOrderPayStatus")
    CommonResultVo<Boolean> changeOrderPayStatus(@RequestBody ReservOrder reservOrder);

    @PostMapping("/reservOrder/getComInfoByOrderIds")
    CommonResultVo<List<ReservOrderComInfoVo>> getComInfoByOrderIds(@RequestBody List<Integer> ids);

    @PostMapping("/reservOrder/updateComInfoByMap")
    CommonResultVo<Boolean> updateComInfoByMap(@RequestBody Map<String, Date> map);

    @PostMapping("/reservOrder/getReservOrderBySalesOrderId")
    CommonResultVo<ReservOrder> getReservOrderBySalesOrderId(@RequestBody String salesOrderId);

    //查询支付宝钻铂免费项目取消/失败的已支付的预约单
    @PostMapping("/reservOrder/selectAlipayFreeList")
    CommonResultVo<List<ReservOrder>> selectAlipayFreeList();


    /**
     * 医疗的预约成功的数据
     * @param reservOrderVo
     * @return
     */
    @PostMapping("/selectSuceessReservOrderList")
    public CommonResultVo<List<ReservOrderVo>> selectSuceessReservOrderList(
            @RequestBody ReservOrderVo reservOrderVo);
}


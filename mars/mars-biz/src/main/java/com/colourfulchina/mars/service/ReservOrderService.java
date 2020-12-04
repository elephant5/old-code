package com.colourfulchina.mars.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.god.door.api.vo.KltSysUser;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.mars.api.entity.BookOrderDetail;
import com.colourfulchina.mars.api.entity.ReservOrder;
import com.colourfulchina.mars.api.vo.*;
import com.colourfulchina.mars.api.vo.req.BookPayReq;
import com.colourfulchina.mars.api.vo.req.QueryReserveOrderDateReqVO;
import com.colourfulchina.mars.api.vo.req.ResrrvOrderReqVo;
import com.colourfulchina.member.api.res.MemLoginResDTO;
import com.colourfulchina.pangu.taishang.api.vo.res.bookBasePayment.BookBasePaymentRes;

import javax.servlet.ServletOutputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ReservOrderService  extends IService<ReservOrder> {
    /**
     * 根据激活码或者手机号查询客户匹配的权益（所有购买的权益列表）
     * @param equityListVo
     * @return
     */
    List<EquityListVo> selectEquityList(EquityListVo equityListVo,Boolean isMobile);

    /*
     * 获取预定的预约单
     * */
    ReservOrderProductVo getReservOrder(Integer id,MemLoginResDTO memInfo) throws Exception;

    /**
     * 来电录单：新增预约订单
     * @param reservOrderVo
     * @return
     */
    ReservOrderVo insertReservOrder(ReservOrderVo reservOrderVo) throws ParseException, Exception;

    /**
     * 预约单分页查询
     * @param pageVoReq
     * @return
     */
    PageVo<ReservOrderVo> selectReservOrderPageList(PageVo<ReservOrderVo> pageVoReq)throws Exception;

    /**
     * 订单详细信息查询
     * @param id
     * @return
     */
    ReservOrderVo selectReservOrderById(Integer id);

    /**
     * 变更预约订单信息
     * @param reservOrderVo
     * @return
     */
    ReservOrderVo updateReservOrder(ReservOrderVo reservOrderVo);

    void insertPriceInfo(ReservOrderVo reservOrderVo, ReservOrder reservOrder);

    /**
     * 预订成功
     * @param reservOrderVo
     * @return
     */
    ReservOrderVo reservSuccess(ReservOrderVo reservOrderVo) throws Exception;

    /**
     * 预订失败
     * @param reservOrderVo
     * @return
     */
    ReservOrderVo reservFail(ReservOrderVo reservOrderVo)throws Exception;

    /**
     * 预约单取消
     * @param reservOrderVo
     * @return
     */
    ReservOrderVo reservCancel(ReservOrderVo reservOrderVo)throws Exception;

    /**
     * 修正
     * @param reservOrderVo
     * @return
     */
    ReservOrderVo reservUpdateInfo(ReservOrderVo reservOrderVo);

    /**
     * 导出预约单
     * @param pageVoReq
     * @return
     */
    String exportReservOrder(PageVo<ReservOrderVo> pageVoReq)throws Exception;

    /**
     * 导出
     * @param out
     * @param historyOrderResBeanList
     */
    void exportExcel(ServletOutputStream out, List<ReservOrderVo> historyOrderResBeanList);


    /**
     * 检查同一时段有相同预约单
     * @param reservOrderVo
     * @return
     */
    ReservOrder selectReservOrderVoIsExsist(ReservOrderVo reservOrderVo) throws Exception;

    /**
     *
     * @param reservOrderVo
     * @return
     */
    ReservOrder selectReservOrderVoIsSusess(ReservOrderVo reservOrderVo);

    /**
     * 根据手机号码查询权益。
     * @param members
     * @return
     */
    List<EquityListVo> selectEquityListByMembers(List<Long> members);

    /**
     *
     * @param reservOrderVo
     * @return
     * @throws Exception
     */
    List<ReservOrder> selectReservOrderVoByPhoneIsExsist(ReservOrderVo reservOrderVo) throws Exception;

    boolean updateIdByOldOrderId(ReservOrder reservOrder);

    void getRedisReservOrder(Integer id);

    void deleteRedisReservOrder(Integer id);

	String exportReservOrderNew(PageVo<ReservOrderVo> pageVoReq, KltSysUser sysUser) throws Exception;

    /**
     * 查询该权益预约成功后并使用的最早时间
     * @param reqVO
     * @return
     */
    String getReserveOrderSucessDate(QueryReserveOrderDateReqVO reqVO) throws Exception;

    PageVo<ReservOrderProductVo> selectReservOrderList(PageVo<ReservOrder> pageVo);

	List<ReservOrder> getOrderList(Integer giftCodeId) throws Exception;

    List<ReservOrder> getOrderListByCondition(ReservOrder order);

//<<<<<<< HEAD
    List<BookOrderDetail> getBookOrderDetail(BookOrderReq req) throws Exception;

    List<Map<String,Object>> getgiftConut(Map<String, Object> req) throws Exception;

    ReservOrder getReservOrderById(Integer reservOderId) throws Exception;

    ReservOrderProductVo getReservOrderProById(Integer reservOderId) throws Exception;
//=======
    ReservOrderVo updateCouponsReservOrder(ReservOrderVo reservOrderVo);
//>>>>>>> repair-bug

    List<ReservOrderComInfoVo> getReservOrderComInfoByOrderIds(List<Integer> orderIds);

    Boolean updateReservOrderStatusByMap(Map<String, Date> map);

    ReservOrder getReservOrderBySalesOrderId(String saleOrderId);

    /**
     * 判断是否可下此预约单
     * @param reqVo
     * @return
     */
    Boolean checkResrrvOrderNum(ResrrvOrderReqVo reqVo);

    public  List<BookBasePaymentRes> selectBookPay(BookPayReq req);

    /**
     * 绿通医疗发送邮件
     * @param reservOrderVo
     */
    void sendMailforMedicalOrderData(ReservOrderVo reservOrderVo);
}

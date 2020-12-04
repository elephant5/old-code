package com.colourfulchina.mars.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.mars.api.entity.BookOrderDetail;
import com.colourfulchina.mars.api.entity.EquityCodeDetail;
import com.colourfulchina.mars.api.entity.ReservOrder;
import com.colourfulchina.mars.api.vo.BookOrderReq;
import com.colourfulchina.mars.api.vo.EquityListVo;
import com.colourfulchina.mars.api.vo.ReservOrderProductVo;
import com.colourfulchina.mars.api.vo.ReservOrderVo;
import com.colourfulchina.mars.api.vo.req.QueryReserveOrderDateReqVO;
import com.colourfulchina.mars.api.vo.req.ReservOrderPlaceReq;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ReservOrderMapper extends BaseMapper<ReservOrder> {


    List<EquityListVo> selectEquityList(List<String> codeList);


    List<ReservOrderVo> selectReservOrderPageList(PageVo<ReservOrderVo> pageVoReq, Map<String, Object> params);

    List<ReservOrderVo> exportReservOrderPageList(PageVo<ReservOrderVo> pageVoReq, Map<String, Object> params);

    ReservOrderProductVo getReservOrder(@Param("id") Integer id);

    ReservOrderVo selectReservOrderById(@Param("id")Integer id);

    List<EquityListVo> selectEquityListByMembers(List<Long> members);


    int countSameTimeOrderNum(ReservOrderPlaceReq req);

    int countFinishNum(ReservOrderPlaceReq req);

    int updateIdByOldOrderId(ReservOrder reservOrder);

    List<ReservOrder> selectReserveOrderSucessDate(QueryReserveOrderDateReqVO reqVO);

    List<ReservOrderProductVo> selectReservOrderListPage(PageVo<ReservOrder> pageVo, Map<String,Object> condition);

    List<BookOrderDetail> getBookOrderDetail(BookOrderReq req);

    Map<String,Object> getgiftCount(Map<String, Object> req);

    List<EquityCodeDetail> getequityCodeDetail(@Param("giftCodeId") Integer giftCodeId);

    List<ReservOrder> selectReserveOrdersByIds(List<Integer> ids);

    ReservOrder getReservOrderBySalesOrderId(@Param("saleOrderId") String saleOrderId);
}

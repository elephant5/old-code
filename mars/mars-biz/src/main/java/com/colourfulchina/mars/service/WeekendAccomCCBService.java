package com.colourfulchina.mars.service;

import com.colourfulchina.mars.api.vo.ZwBookOrder;
import com.colourfulchina.mars.api.vo.ZwSalesOrder;
import com.colourfulchina.mars.api.vo.ZwSalesOrderBack;
import com.colourfulchina.mars.api.vo.res.reservOrder.ReservOrderReportRes;

import java.util.List;
import java.util.Map;

public interface WeekendAccomCCBService {

    List<ReservOrderReportRes> selectWeekendAccomDayList(Map map)throws Exception;

    List<ReservOrderReportRes> selectWeekendAccomWeekList(Map map)throws Exception;

    List<ZwSalesOrder> selectZWList(Map map) throws Exception;

    List<ZwSalesOrderBack> selectZWBackList(Map map) throws Exception;

    List<ZwBookOrder> selectZWOrderList(Map map) throws Exception;
}

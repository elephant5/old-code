package com.colourfulchina.mars.mapper;

import com.colourfulchina.mars.api.vo.ZwBookOrder;
import com.colourfulchina.mars.api.vo.ZwSalesOrder;
import com.colourfulchina.mars.api.vo.ZwSalesOrderBack;
import com.colourfulchina.mars.api.vo.res.reservOrder.ReservOrderReportRes;

import java.util.List;
import java.util.Map;

public interface ReservOrderReportMapper{

    List<ReservOrderReportRes> selectOrderReport(Map map);

    List<ZwSalesOrder> selectZWReport(Map map);

    List<ZwSalesOrderBack> selectZWBackReport(Map map);

    List<ZwBookOrder> selectZWOrderList(Map map);
}

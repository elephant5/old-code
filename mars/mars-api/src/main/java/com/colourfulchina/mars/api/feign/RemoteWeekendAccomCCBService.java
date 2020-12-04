package com.colourfulchina.mars.api.feign;

import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.feign.fallback.RemoteWeekendAccomCCBServiceImpl;
import com.colourfulchina.mars.api.vo.ZwBookOrder;
import com.colourfulchina.mars.api.vo.ZwSalesOrder;
import com.colourfulchina.mars.api.vo.ZwSalesOrderBack;
import com.colourfulchina.mars.api.vo.res.reservOrder.ReservOrderReportRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(value = ServiceNameConstant.MARS_SERVICE,fallback = RemoteWeekendAccomCCBServiceImpl.class)
public interface RemoteWeekendAccomCCBService {


    /**
     * 日数据
     * @param map
     * @return
     */
    @PostMapping("/weekendAccom/selectDayList")
    CommonResultVo<List<ReservOrderReportRes>> selectDayList(@RequestBody Map map);

    /**
     * 建行客户补贴销售结算周数据（结算组数据–周推送）查询
     * @return
     */
    @PostMapping("/weekendAccom/selectWeekList")
    CommonResultVo<List<ReservOrderReportRes>> selectWeekList(@RequestBody Map map);

    /**
     * 众网小贷 推送日报给物流，时间维度下单日期，抄送Tammy和Annie查询
     * @return
     */
    @PostMapping("/weekendAccom/selectZWList")
    CommonResultVo<List<ZwSalesOrder>> selectZWList(@RequestBody Map map);

    @PostMapping("/weekendAccom/selectZWBackList")
    CommonResultVo<List<ZwSalesOrderBack>> selectZWBackList(@RequestBody Map map);

    @PostMapping("/weekendAccom/selectZWOrderList")
    CommonResultVo<List<ZwBookOrder>> selectZWOrderList(@RequestBody Map map1);
}

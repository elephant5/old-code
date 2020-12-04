package com.colourfulchina.mars.controller;

import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.vo.ZwBookOrder;
import com.colourfulchina.mars.api.vo.ZwSalesOrderBack;
import com.colourfulchina.mars.api.vo.res.reservOrder.ReservOrderReportRes;
import com.colourfulchina.mars.service.WeekendAccomCCBService;
import com.colourfulchina.mars.api.vo.ZwSalesOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/weekendAccom")
@AllArgsConstructor
@Slf4j
@Api(tags = {"建行周末住宿接口"})
public class WeekendAccomCCBController {
    @Autowired
    private WeekendAccomCCBService weekendAccomCCBService;

    /**
     * 建行周末住宿 客户补贴销售物流日数据（物流组数据–日推送）查询
     * @return
     */
    @SysGodDoorLog("建行周末住宿 客户补贴销售物流日数据（物流组数据–日推送）查询")
    @ApiOperation("建行周末住宿 客户补贴销售物流日数据（物流组数据–日推送）查询")
    @PostMapping("/selectDayList")
    public CommonResultVo<List<ReservOrderReportRes>> selectDayList(@RequestBody Map map){
        CommonResultVo<List<ReservOrderReportRes>> result = new CommonResultVo<>();
        try {
            List<ReservOrderReportRes> list = weekendAccomCCBService.selectWeekendAccomDayList(map);
            result.setResult(list);
        }catch (Exception e){
            log.error("建行周末住宿日数据查询失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 建行客户补贴销售结算周数据（结算组数据–周推送）查询
     * @return
     */
    @SysGodDoorLog("建行客户补贴销售结算周数据（结算组数据–周推送）查询")
    @ApiOperation("建行客户补贴销售结算周数据（结算组数据–周推送）查询")
    @PostMapping("/selectWeekList")
    public CommonResultVo<List<ReservOrderReportRes>> selectWeekList(@RequestBody Map map){
        CommonResultVo<List<ReservOrderReportRes>> result = new CommonResultVo<>();
        try {
            List<ReservOrderReportRes> list = weekendAccomCCBService.selectWeekendAccomWeekList(map);
            result.setResult(list);
        }catch (Exception e){
            log.error("建行周末住宿周数据查询失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 众网小贷 推送日报给物流，时间维度下单日期，抄送Tammy和Annie
     * @return
     */
    @SysGodDoorLog("众网小贷 推送日报给物流，时间维度下单日期，抄送Tammy和Annie查询")
    @ApiOperation("众网小贷 推送日报给物流，时间维度下单日期，抄送Tammy和Annie查询")
    @PostMapping("/selectZWList")
    public CommonResultVo<List<ZwSalesOrder>> selectZWList(@RequestBody Map map){
        CommonResultVo<List<ZwSalesOrder>> result = new CommonResultVo<>();
        try {
            List<ZwSalesOrder> list = weekendAccomCCBService.selectZWList(map);
            result.setResult(list);
        }catch (Exception e){
            log.error("众网小贷数据查询失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 众网小贷 推送日报给物流，时间维度下单日期，抄送Tammy和Annie
     * @return
     */
    @SysGodDoorLog("众网小贷 推送日报给物流，时间维度下单日期，抄送Tammy和Annie查询")
    @ApiOperation("众网小贷 推送日报给物流，时间维度下单日期，抄送Tammy和Annie查询")
    @PostMapping("/selectZWBackList")
    public CommonResultVo<List<ZwSalesOrderBack>> selectZWBackList(@RequestBody Map map){
        CommonResultVo<List<ZwSalesOrderBack>> result = new CommonResultVo<>();
        try {
            List<ZwSalesOrderBack> list = weekendAccomCCBService.selectZWBackList(map);
            result.setResult(list);
        }catch (Exception e){
            log.error("众网小贷数据查询失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }


    /**
     * 推送物流数据(表头详见附件)：频次：日报；已使用客户数据；时间维度使用日期，抄送Tammy和Annie

     * @return
     */
    @SysGodDoorLog("推送物流数据(表头详见附件)：频次：日报；已使用客户数据；时间维度使用日期，抄送Tammy和Annie")
    @ApiOperation("推送物流数据(表头详见附件)：频次：日报；已使用客户数据；时间维度使用日期，抄送Tammy和Annie")
    @PostMapping("/selectZWOrderList")
    public CommonResultVo<List<ZwBookOrder>> selectZWOrderList(@RequestBody Map map){
        CommonResultVo<List<ZwBookOrder>> result = new CommonResultVo<>();
        try {
            List<ZwBookOrder> list = weekendAccomCCBService.selectZWOrderList(map);
            result.setResult(list);
        }catch (Exception e){
            log.error("众网小贷数据查询失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

}

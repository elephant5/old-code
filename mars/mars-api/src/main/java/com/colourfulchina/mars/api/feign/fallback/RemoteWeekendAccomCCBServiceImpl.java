package com.colourfulchina.mars.api.feign.fallback;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.feign.RemoteWeekendAccomCCBService;
import com.colourfulchina.mars.api.vo.ZwBookOrder;
import com.colourfulchina.mars.api.vo.ZwSalesOrder;
import com.colourfulchina.mars.api.vo.ZwSalesOrderBack;
import com.colourfulchina.mars.api.vo.res.reservOrder.ReservOrderReportRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class RemoteWeekendAccomCCBServiceImpl implements RemoteWeekendAccomCCBService {
	@Override
	public CommonResultVo<List<ReservOrderReportRes>> selectDayList(Map map) {
		log.error("fegin查询建行周末住宿天数据失败");
		return null;
	}

	@Override
	public CommonResultVo<List<ReservOrderReportRes>> selectWeekList(Map map) {
		log.error("fegin查询建行周末住宿周数据失败");
		return null;
	}

    @Override
    public CommonResultVo<List<ZwSalesOrder>> selectZWList(Map map) {
		log.error("fegin查询众网小贷数据失败");
		return null;
    }

	@Override
	public CommonResultVo<List<ZwSalesOrderBack>> selectZWBackList(Map map) {
		log.error("fegin查询众网小贷退货数据失败");
		return null;
	}

    @Override
    public CommonResultVo<List<ZwBookOrder>> selectZWOrderList(Map map1) {
		log.error("fegin查询众网小贷预约单失败");
        return null;
    }
}

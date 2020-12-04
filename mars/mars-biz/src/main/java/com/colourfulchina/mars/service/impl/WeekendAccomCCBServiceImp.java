package com.colourfulchina.mars.service.impl;

import cn.hutool.core.date.DateUtil;
import com.colourfulchina.mars.api.enums.PayOrderStatusEnum;
import com.colourfulchina.mars.api.vo.ZwBookOrder;
import com.colourfulchina.mars.api.vo.ZwSalesOrder;
import com.colourfulchina.mars.api.vo.ZwSalesOrderBack;
import com.colourfulchina.mars.api.vo.res.reservOrder.ReservOrderReportRes;
import com.colourfulchina.mars.mapper.ReservOrderReportMapper;
import com.colourfulchina.mars.service.PanguInterfaceService;
import com.colourfulchina.mars.service.WeekendAccomCCBService;
import com.colourfulchina.pangu.taishang.api.entity.Goods;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsChannelRes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class WeekendAccomCCBServiceImp implements WeekendAccomCCBService {
	@Autowired
	private ReservOrderReportMapper reservOrderReportMapper;
	@Autowired
	private PanguInterfaceService panguInterfaceService;

	/**
	 * 周末住宿日数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<ReservOrderReportRes> selectWeekendAccomDayList(Map map) throws Exception {
		List<ReservOrderReportRes> list = reservOrderReportMapper.selectOrderReport(map);
		if (!CollectionUtils.isEmpty(list)){
			//获取商品列表
			List<Goods> goodsList = panguInterfaceService.selectGoodsList();
			Map<Integer,Goods> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getId, goods -> goods));
			//获取销售渠道信息
			List<GoodsChannelRes> salesChannels = panguInterfaceService.selectSalesChannelList();
			Map<Integer,GoodsChannelRes> salesChannelsMap = salesChannels.stream().collect(Collectors.toMap(GoodsChannelRes::getId,goodsChannelRes -> goodsChannelRes));
			for (ReservOrderReportRes reservOrderReportRes : list) {
				reservOrderReportRes.setSalesBankName(reservOrderReportRes.getSalesChannelId() != null ? salesChannelsMap.get(reservOrderReportRes.getSalesChannelId()).getBankName() : null);
				reservOrderReportRes.setSalesChannelName(reservOrderReportRes.getSalesChannelId() != null ? salesChannelsMap.get(reservOrderReportRes.getSalesChannelId()).getSalesChannelName() : null);
				reservOrderReportRes.setSalesWayName(reservOrderReportRes.getSalesChannelId() != null ? salesChannelsMap.get(reservOrderReportRes.getSalesChannelId()).getSalesWayName() : null);
				reservOrderReportRes.setGoodsShortName(reservOrderReportRes.getGoodsId() != null ? goodsMap.get(reservOrderReportRes.getGoodsId()).getShortName() : null);
//				reservOrderReportRes.setVarStatus(StringUtils.isNotBlank(reservOrderReportRes.getVarStatus()) ? HxCodeStatusEnum.HxCodeStatus.findNameByIndex(Integer.valueOf(reservOrderReportRes.getVarStatus())) : null);
				reservOrderReportRes.setVarStatus("已使用");
				reservOrderReportRes.setNum(1);
				reservOrderReportRes.setTotalAmount(reservOrderReportRes.getPayAmoney());
				reservOrderReportRes.setDiscountAmount(null);
				reservOrderReportRes.setCreateDate(DateUtil.format(reservOrderReportRes.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			}
		}
		return list;
	}

	/**
	 * 周末住宿周数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<ReservOrderReportRes> selectWeekendAccomWeekList(Map map) throws Exception {
		List<ReservOrderReportRes> list = reservOrderReportMapper.selectOrderReport(map);
		if (!CollectionUtils.isEmpty(list)){
			//获取商品列表
			List<Goods> goodsList = panguInterfaceService.selectGoodsList();
			Map<Integer,Goods> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getId, goods -> goods));
			//获取销售渠道信息
			List<GoodsChannelRes> salesChannels = panguInterfaceService.selectSalesChannelList();
			Map<Integer,GoodsChannelRes> salesChannelsMap = salesChannels.stream().collect(Collectors.toMap(GoodsChannelRes::getId,goodsChannelRes -> goodsChannelRes));
			for (ReservOrderReportRes reservOrderReportRes : list) {
				reservOrderReportRes.setSalesBankName(reservOrderReportRes.getSalesChannelId() != null ? salesChannelsMap.get(reservOrderReportRes.getSalesChannelId()).getBankName() : null);
				reservOrderReportRes.setSalesChannelName(reservOrderReportRes.getSalesChannelId() != null ? salesChannelsMap.get(reservOrderReportRes.getSalesChannelId()).getSalesChannelName() : null);
				reservOrderReportRes.setSalesWayName(reservOrderReportRes.getSalesChannelId() != null ? salesChannelsMap.get(reservOrderReportRes.getSalesChannelId()).getSalesWayName() : null);
				reservOrderReportRes.setGoodsShortName(reservOrderReportRes.getGoodsId() != null ? goodsMap.get(reservOrderReportRes.getGoodsId()).getShortName() : null);
				reservOrderReportRes.setPaySuccessTimeStr(reservOrderReportRes.getPaySuccessTime() != null ? DateUtil.format(reservOrderReportRes.getPaySuccessTime(),"yyyy-MM-dd HH:mm:ss") : null);
				reservOrderReportRes.setBackAmountDateStr(reservOrderReportRes.getBackAmountDate() != null ? DateUtil.format(reservOrderReportRes.getBackAmountDate(),"yyyy-MM-dd HH:mm:ss") : null);
				reservOrderReportRes.setPayStatus(StringUtils.isNotBlank(reservOrderReportRes.getPayStatus()) ? PayOrderStatusEnum.getNameByCode(Integer.valueOf(reservOrderReportRes.getPayStatus())) : null);
				if (StringUtils.isNotBlank(reservOrderReportRes.getGiftDate())){
					Date giftDate = DateUtil.parse(reservOrderReportRes.getGiftDate(),"yyyy-MM-dd");
					Date now = DateUtil.parse(DateUtil.format(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd");
					if (giftDate.before(now)){
						reservOrderReportRes.setVarStatus("已使用");
					}else {
						reservOrderReportRes.setVarStatus("未使用");
					}
				}
				reservOrderReportRes.setNum(1);
				reservOrderReportRes.setDiscountAmount(null);
				reservOrderReportRes.setCreateDate(DateUtil.format(reservOrderReportRes.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			}
		}
		return list;
	}

	@Override
	public List<ZwSalesOrder> selectZWList(Map map) throws Exception {
		List<ZwSalesOrder> list = reservOrderReportMapper.selectZWReport(map);
		if (!CollectionUtils.isEmpty(list)){
			//获取商品列表
			List<Goods> goodsList = panguInterfaceService.selectGoodsList();
			Map<Integer,Goods> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getId, goods -> goods));
			//获取销售渠道信息
			List<GoodsChannelRes> salesChannels = panguInterfaceService.selectSalesChannelList();
			Map<Integer,GoodsChannelRes> salesChannelsMap = salesChannels.stream().collect(Collectors.toMap(GoodsChannelRes::getId,goodsChannelRes -> goodsChannelRes));
			int i=0;
			for (ZwSalesOrder zwSalesOrder : list) {
				zwSalesOrder.setSalesBankName(zwSalesOrder.getProjectChannelId() != null ? salesChannelsMap.get(zwSalesOrder.getProjectChannelId()).getBankName() : null);
				Integer id = null;
				if(zwSalesOrder.getProjectId()!=null){
					id = Integer.valueOf(zwSalesOrder.getProjectId());
				}
				zwSalesOrder.setProjectShortName(zwSalesOrder.getProjectId() != null ? goodsMap.get(id).getShortName() : null);
				zwSalesOrder.setProjectName(zwSalesOrder.getProjectId() != null ? goodsMap.get(id).getName() : null);
				i++;
				zwSalesOrder.setNum(i+"");
				zwSalesOrder.setGoodsNum(1+"");
			}
		}
		return list;
	}

	@Override
	public List<ZwSalesOrderBack> selectZWBackList(Map map) throws Exception {
		List<ZwSalesOrderBack> list = reservOrderReportMapper.selectZWBackReport(map);
		if (!CollectionUtils.isEmpty(list)){
			//获取商品列表
			List<Goods> goodsList = panguInterfaceService.selectGoodsList();
			Map<Integer,Goods> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getId, goods -> goods));
			//获取销售渠道信息
			List<GoodsChannelRes> salesChannels = panguInterfaceService.selectSalesChannelList();
			Map<Integer,GoodsChannelRes> salesChannelsMap = salesChannels.stream().collect(Collectors.toMap(GoodsChannelRes::getId,goodsChannelRes -> goodsChannelRes));
			int i=0;
			for (ZwSalesOrderBack zwSalesOrderBack : list) {
				zwSalesOrderBack.setSalesBankName(zwSalesOrderBack.getProjectChannelId() != null ? salesChannelsMap.get(zwSalesOrderBack.getProjectChannelId()).getBankName() : null);
				Integer id = null;
				if(zwSalesOrderBack.getProjectId()!=null){
					id = Integer.valueOf(zwSalesOrderBack.getProjectId());
				}
				zwSalesOrderBack.setProjectShortName(zwSalesOrderBack.getProjectId() != null ? goodsMap.get(id).getShortName() : null);
				zwSalesOrderBack.setProjectName(zwSalesOrderBack.getProjectId() != null ? goodsMap.get(id).getName() : null);
				i++;
				zwSalesOrderBack.setNum(i+"");
				zwSalesOrderBack.setGoodsNum(1+"");
			}
		}
		return list;
	}

	@Override
	public List<ZwBookOrder> selectZWOrderList(Map map) throws Exception {
		List<ZwBookOrder> list = reservOrderReportMapper.selectZWOrderList(map);
		if (!CollectionUtils.isEmpty(list)){
			//获取商品列表
			List<Goods> goodsList = panguInterfaceService.selectGoodsList();
			Map<Integer,Goods> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getId, goods -> goods));
			//获取销售渠道信息
			List<GoodsChannelRes> salesChannels = panguInterfaceService.selectSalesChannelList();
			Map<Integer,GoodsChannelRes> salesChannelsMap = salesChannels.stream().collect(Collectors.toMap(GoodsChannelRes::getId,goodsChannelRes -> goodsChannelRes));
			int i=0;
			for (ZwBookOrder zwBookOrder : list) {
				zwBookOrder.setSalesBankName(zwBookOrder.getProjectChannelId() != null ? salesChannelsMap.get(zwBookOrder.getProjectChannelId()).getBankName() : null);
				Integer id = null;
				if(zwBookOrder.getProjectId()!=null){
					id = Integer.valueOf(zwBookOrder.getProjectId());
				}
				zwBookOrder.setProjectShortName(zwBookOrder.getProjectId() != null ? goodsMap.get(id).getShortName() : null);
				i++;
				zwBookOrder.setNum(i+"");
				zwBookOrder.setGoodsNum(1+"");
			}
		}
		return list;
	}
}

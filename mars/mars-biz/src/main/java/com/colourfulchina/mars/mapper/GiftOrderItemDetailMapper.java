package com.colourfulchina.mars.mapper;

import com.colourfulchina.mars.api.entity.GiftOrderCapital;
import com.colourfulchina.mars.api.vo.req.GiftOrderQueryReqVo;
import com.colourfulchina.mars.api.vo.res.GiftOrderDetailVo;
import com.colourfulchina.mars.api.vo.res.GiftOrderResVo;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.mars.api.entity.GiftOrderItemDetail;

public interface GiftOrderItemDetailMapper extends BaseMapper<GiftOrderItemDetail>{

	public Integer getCountByChannel(@Param("acChannel") String acChannel);

	public Integer getCountByGoodsId(@Param("goodsId") Integer goodsId);

	public GiftOrderCapital getNoticeOrder(@Param("capitalOrderId") String capitalOrderId);

	public GiftOrderDetailVo getOrderDetail(GiftOrderQueryReqVo reqVo);
}

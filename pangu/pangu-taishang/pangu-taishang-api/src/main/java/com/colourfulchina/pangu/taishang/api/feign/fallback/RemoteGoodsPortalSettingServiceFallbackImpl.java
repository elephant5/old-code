package com.colourfulchina.pangu.taishang.api.feign.fallback;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.dto.GoodsPortalSettingDto;
import com.colourfulchina.pangu.taishang.api.entity.Goods;
import com.colourfulchina.pangu.taishang.api.entity.GoodsClause;
import com.colourfulchina.pangu.taishang.api.entity.SalesChannel;
import com.colourfulchina.pangu.taishang.api.feign.RemoteGoodsPortalSettingService;
import com.colourfulchina.pangu.taishang.api.feign.RemoteGoodsService;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsExpiryDateReq;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsChannelRes;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsClauseRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteGoodsPortalSettingServiceFallbackImpl implements RemoteGoodsPortalSettingService {
	@Override
	public CommonResultVo<GoodsPortalSettingDto> get(Integer goodsId) {
		log.error("fegin根据商品id:{}查询商品前端设置失败",goodsId);
		return null;
	}

	@Override
	public CommonResultVo<GoodsPortalSettingDto> getByCode(String code) {
		log.error("fegin根据code:{}查询商品前端设置失败",code);
		return null;
	}
}

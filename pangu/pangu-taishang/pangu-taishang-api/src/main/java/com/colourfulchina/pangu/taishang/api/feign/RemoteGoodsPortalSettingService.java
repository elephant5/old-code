package com.colourfulchina.pangu.taishang.api.feign;

import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.dto.GoodsPortalSettingDto;
import com.colourfulchina.pangu.taishang.api.entity.Goods;
import com.colourfulchina.pangu.taishang.api.entity.GoodsClause;
import com.colourfulchina.pangu.taishang.api.entity.SalesChannel;
import com.colourfulchina.pangu.taishang.api.feign.fallback.RemoteGoodsPortalSettingServiceFallbackImpl;
import com.colourfulchina.pangu.taishang.api.feign.fallback.RemoteGoodsServiceFallbackImpl;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsExpiryDateReq;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsChannelRes;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsClauseRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = ServiceNameConstant.PANGU_TAISHANG_SERVICE, fallback = RemoteGoodsPortalSettingServiceFallbackImpl.class)
public interface RemoteGoodsPortalSettingService {

	/**
	 * 根据Id查找商品
	 * @param goodsId
	 * @return
	 */
	@GetMapping("/GoodsPortalSetting/get/{goodsId}")
	CommonResultVo<GoodsPortalSettingDto> get(@PathVariable(value = "goodsId") Integer goodsId);
	/**
	 * 根据code查找商品
	 * @param code
	 * @return
	 */
	@GetMapping("/GoodsPortalSetting/getByCode/{code}")
	CommonResultVo<GoodsPortalSettingDto> getByCode(@PathVariable(value = "code") String code);
}

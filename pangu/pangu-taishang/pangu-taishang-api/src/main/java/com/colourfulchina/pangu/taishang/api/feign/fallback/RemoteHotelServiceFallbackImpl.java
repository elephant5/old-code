package com.colourfulchina.pangu.taishang.api.feign.fallback;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.City;
import com.colourfulchina.pangu.taishang.api.feign.RemoteHotelService;
import com.colourfulchina.pangu.taishang.api.vo.res.city.CityRes;
import com.colourfulchina.pangu.taishang.api.vo.res.hotel.HotelInfoQueryRes;
import com.colourfulchina.pangu.taishang.api.vo.res.shopProtocol.ShopProtocolRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteHotelServiceFallbackImpl implements RemoteHotelService {
	@Override
	public City get(Long id) {
		log.error("feign 查询酒店信息失败:{}", id);
		return null;
	}

	@Override
	public CommonResultVo<List<ShopProtocolRes>> selectShopProtocol(Integer shopId) {
		return null;
	}

	@Override
	public CommonResultVo<HotelInfoQueryRes> selectHotelByShopId(Integer shopId) {
		return null;
	}

	@Override
	public CommonResultVo<List<CityRes>> selectCityInfoList(Integer goodsId) {
		log.error("fegin查询商品城市列表失败");
		return null;
	}
}

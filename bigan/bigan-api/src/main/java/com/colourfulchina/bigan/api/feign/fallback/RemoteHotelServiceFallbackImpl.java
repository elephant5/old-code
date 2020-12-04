package com.colourfulchina.bigan.api.feign.fallback;

import com.colourfulchina.bigan.api.entity.Hotel;
import com.colourfulchina.bigan.api.feign.RemoteHotelService;
import com.colourfulchina.bigan.api.vo.HotelOldDetailVo;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteHotelServiceFallbackImpl implements RemoteHotelService {
	@Override
	public CommonResultVo<List<Hotel>> selectHotelList() {
		log.info("fegin查询酒店列表失败");
		return null;
	}

	@Override
	public CommonResultVo<Hotel> addHotel(Hotel hotel) {
		log.info("新增酒店失败{}",hotel.toString());
		return null;
	}

	@Override
	public CommonResultVo<Hotel> updHotel(HotelOldDetailVo hotelOldDetailVo) {
		log.info("更新酒店失败{}",hotelOldDetailVo.toString());
		return null;
	}
}

package com.colourfulchina.bigan.api.feign;

import com.colourfulchina.bigan.api.entity.Hotel;
import com.colourfulchina.bigan.api.feign.fallback.RemoteHotelServiceFallbackImpl;
import com.colourfulchina.bigan.api.vo.HotelOldDetailVo;
import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = ServiceNameConstant.BIGAN_SERVICE, fallback = RemoteHotelServiceFallbackImpl.class)
public interface RemoteHotelService {
	/**
	 * fegin查询sqlserver酒店列表
	 * @return
	 */
	@PostMapping(value = "/hotel/selectHotelList")
	CommonResultVo<List<Hotel>> selectHotelList();

	/**
	 * 新增酒店
	 * @param hotel
	 * @return
	 */
	@PostMapping(value = "/hotel/addHotel")
	CommonResultVo<Hotel> addHotel(@RequestBody Hotel hotel);

	/**
	 * 酒店更新
	 * @param hotelOldDetailVo
	 * @return
	 */
	@PostMapping(value = "/hotel/updHotel")
	CommonResultVo<Hotel> updHotel(@RequestBody HotelOldDetailVo hotelOldDetailVo);
}

package com.colourfulchina.pangu.taishang.api.feign;

import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.City;
import com.colourfulchina.pangu.taishang.api.feign.fallback.RemoteHotelServiceFallbackImpl;
import com.colourfulchina.pangu.taishang.api.vo.res.city.CityRes;
import com.colourfulchina.pangu.taishang.api.vo.res.hotel.HotelInfoQueryRes;
import com.colourfulchina.pangu.taishang.api.vo.res.shopProtocol.ShopProtocolRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value =  ServiceNameConstant.PANGU_TAISHANG_SERVICE, fallback = RemoteHotelServiceFallbackImpl.class)
public interface RemoteHotelService {

	@GetMapping("/City/get/{id}")
	City get(@PathVariable(value = "id") Long id);

	/**
	 * @title:selectShopProtocol
	 * @Description: 根据shopId获取商户扩展信息异常
	 * @Param: [shopId]
	 * @return: com.colourfulchina.inf.base.vo.CommonResultVo<java.util.List<com.colourfulchina.pangu.taishang.api.vo.res.shopProtocol.ShopProtocolRes>>
	 * @Auther: 图南
	 * @Date: 2019/6/20 15:37
	 */
	@PostMapping("/shopProtocol/selectShopProtocol")
	CommonResultVo<List<ShopProtocolRes>> selectShopProtocol(@RequestBody Integer shopId);

	/**
	 * @title:selectHotelByShopId
	 * @Description: 根据商户ID查询酒店详细信息
	 * @Param: [shopId]
	 * @return: com.colourfulchina.inf.base.vo.CommonResultVo<com.colourfulchina.pangu.taishang.api.vo.res.hotel.HotelInfoQueryRes>
	 * @Auther: 图南
	 * @Date: 2019/6/20 15:39
	 */
	@PostMapping("/hotel/selectHotelByShopId")
    CommonResultVo<HotelInfoQueryRes> selectHotelByShopId(@RequestBody Integer shopId);

    /**
     * @title:selectCityInfoList
     * @Description: 根据项目id获取城市的详细信息
     * @Param: [projectId]
     * @return: com.colourfulchina.inf.base.vo.CommonResultVo<java.util.List<com.colourfulchina.pangu.taishang.api.vo.res.city.CityRes>>
     * @Auther: 图南
     * @Date: 2019/6/20 15:40
     */
	@PostMapping("/city/selectCityInfoList")
	CommonResultVo<List<CityRes>> selectCityInfoList(@RequestBody Integer goodsId);
}

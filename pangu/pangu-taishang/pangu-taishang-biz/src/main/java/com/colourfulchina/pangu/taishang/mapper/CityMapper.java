package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.pangu.taishang.api.entity.City;
import com.colourfulchina.pangu.taishang.api.vo.CityVo;
import com.colourfulchina.pangu.taishang.api.vo.res.city.CityRes;

import java.util.List;

public interface CityMapper extends BaseMapper<City> {

    List<CityVo> selectByCountryId(String countryId);

    List<CityRes> selectCityInfoList(Integer goodsId);
}

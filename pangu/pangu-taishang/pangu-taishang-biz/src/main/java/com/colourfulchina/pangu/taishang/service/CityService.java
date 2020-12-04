package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.City;
import com.colourfulchina.pangu.taishang.api.vo.CityVo;
import com.colourfulchina.pangu.taishang.api.vo.res.city.CityRes;

import java.util.List;

public interface CityService extends IService<City> {
    /**
     * 根据老系统城市id查询新系统城市信息
     * @param oldId
     * @return
     */
    City selectByOldId(Integer oldId);

    /**
     * 根据国家id查询城市列表
     * @param countryId
     * @return
     */
    List<CityVo> selectByCountryId(String countryId);

    List<CityRes> selectCityInfoList(Integer goodsId);
}

package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.entity.City;
import com.colourfulchina.pangu.taishang.api.vo.CityVo;
import com.colourfulchina.pangu.taishang.api.vo.res.city.CityRes;
import com.colourfulchina.pangu.taishang.mapper.CityMapper;
import com.colourfulchina.pangu.taishang.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CityServiceImpl extends ServiceImpl<CityMapper,City> implements CityService {
    @Autowired
    private CityMapper cityMapper;


    /**
     * 根据老系统城市id查询新系统城市信息
     * @param oldId
     * @return
     */
    @Override
    public City selectByOldId(Integer oldId) {
        Wrapper<City> wrapper = new Wrapper<City>() {
            @Override
            public String getSqlSegment() {
                return "where old_city_id = "+oldId;
            }
        };
        return cityMapper.selectList(wrapper).get(0);
    }

    /**
     * 根据国家id查询城市列表
     * @param countryId
     * @return
     */
    @Override
    public List<CityVo> selectByCountryId(String countryId) {
        return cityMapper.selectByCountryId(countryId);
    }

    @Override
    public List<CityRes> selectCityInfoList(Integer goodsId) {
        List<CityRes> cityResList = cityMapper.selectCityInfoList(goodsId);
        return cityResList;
    }
}

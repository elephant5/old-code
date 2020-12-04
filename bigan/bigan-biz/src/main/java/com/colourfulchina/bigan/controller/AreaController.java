package com.colourfulchina.bigan.controller;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.bigan.api.entity.City;
import com.colourfulchina.bigan.api.entity.Country;
import com.colourfulchina.bigan.service.CityService;
import com.colourfulchina.bigan.service.CountryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/area")
public class AreaController {
    @Autowired
    private CountryService countryService;

    @Autowired
    private CityService cityService;


    @GetMapping("/getCityList/{code}")
    public List<City> getCityList(String code){
        Wrapper<City> cityWrapper=new Wrapper<City>() {
            @Override
            public String getSqlSegment() {
                return StringUtils.isBlank(code)?null:"where country='"+code+"'";
            }
        };
        return cityService.selectList(cityWrapper);
    }

    @GetMapping("/getCountryList/{area}")
    public List<Country> getCountryList(String area){
        Wrapper<Country> countryWrapper=new Wrapper<Country>() {
            @Override
            public String getSqlSegment() {
                return StringUtils.isBlank(area)?null:"where area='"+area+"'";
            }
        };
        return countryService.selectList(countryWrapper);
    }

}

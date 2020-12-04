package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.City;
import com.colourfulchina.bigan.service.CityService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CityController {
    @Autowired
    private CityService cityService;

    /**
     * 查询城市列表
     * @return
     */
    @PostMapping("/selectCityList")
    public CommonResultVo<List<City>> selectCityList(){
        CommonResultVo<List<City>> result = new CommonResultVo<>();
        List<City> cityList = cityService.selectList(null);
        result.setResult(cityList);
        return result;
    }
}

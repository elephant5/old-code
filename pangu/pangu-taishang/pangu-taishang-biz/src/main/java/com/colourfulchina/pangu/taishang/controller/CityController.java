package com.colourfulchina.pangu.taishang.controller;

import com.colourfulchina.bigan.api.feign.RemoteCityService;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.City;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.api.vo.res.city.CityRes;
import com.colourfulchina.pangu.taishang.service.CityService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/city")
@AllArgsConstructor
@Slf4j
@Api(tags = {"城市操作接口"})
public class CityController {
    @Autowired
    private CityService cityService;
    private final RemoteCityService remoteCityService;

    @SysGodDoorLog("根据城市id查询城市")
    @ApiOperation("根据城市id查询城市")
    @GetMapping("/get/{id}")
    @Cacheable(value = "City",key = "'get_'+#id",unless = "#result == null")
    public City get(@PathVariable Long id){
        return cityService.selectById(id);
    }

    /**
     * 城市列表查询
     * @return
     */
    @SysGodDoorLog("城市列表")
    @ApiOperation("城市列表")
    @PostMapping("/selectCityList")
    @Cacheable(value = "City",key = "'selectCityList'",unless = "#result == null")
    public CommonResultVo<List<City>> selectCityList(){
        CommonResultVo<List<City>> result = new CommonResultVo<>();
        List<City> cityList = cityService.selectList(null);
        result.setResult(cityList);
        return result;
    }

    /**
     * 城市详情查询
     * @param id
     * @return
     */
    @SysGodDoorLog("城市详情")
    @ApiOperation("城市详情")
    @PostMapping("/cityDetail")
    @Cacheable(value = "City",key = "'cityDetail_'+#id",unless = "#result == null")
    public CommonResultVo<City> cityDetail(@RequestBody Integer id){
        CommonResultVo<City> result = new CommonResultVo<>();
        City city = cityService.selectById(id);
        result.setResult(city);
        return result;
    }

    /**
     * 初始化老系统中城市到mysql中
     * @return
     */
    @SysGodDoorLog("初始化老系统中城市到mysql中")
    @ApiOperation("初始化老系统中城市到mysql中")
    @PostMapping("/syncCityList")
    @Transactional(rollbackFor=Exception.class)
    public CommonResultVo<List<City>> syncCityList(){
        CommonResultVo<List<City>> result = new CommonResultVo<>();
        List<City> cityList = Lists.newLinkedList();
        CommonResultVo<List<com.colourfulchina.bigan.api.entity.City>> remoteResult = remoteCityService.selectCityList();
        if (!CollectionUtils.isEmpty(remoteResult.getResult())){
            for (com.colourfulchina.bigan.api.entity.City remoteCity : remoteResult.getResult()) {
                City city = new City();
                city.setNameCh(remoteCity.getName());
                city.setHot(remoteCity.getHot());
                city.setOversea(remoteCity.getOversea());
                city.setNamePy(remoteCity.getPy());
                city.setCountryId(remoteCity.getCountry());
                city.setNameEn(remoteCity.getEn());
                city.setOldCityId(Integer.valueOf(remoteCity.getId()+""));
                city.setSort(remoteCity.getSort());
                city.setCreateTime(new Date());
                city.setCreateUser(SecurityUtils.getLoginName());
                city.setDelFlag(DelFlagEnums.NORMAL.getCode());
                cityList.add(city);
            }
        }
        cityService.insertBatch(cityList);
        result.setResult(cityList);
        return result;
    }




    @ApiOperation("根据项目id获取城市的详细信息")
    @PostMapping("/selectCityInfoList")
    @Cacheable(value = "City",key = "'selectCityInfoList_'+#goodsId",unless = "#result == null")
    public CommonResultVo<List<CityRes>> selectCityInfoList(@RequestBody Integer goodsId){
        CommonResultVo<List<CityRes>> commonResultVo = new CommonResultVo<List<CityRes>>();
        try {
            Assert.notNull(goodsId,"商品id不能为空");
            List<CityRes> cityResList = cityService.selectCityInfoList(goodsId);
            commonResultVo.setResult(cityResList);
        }catch (Exception e){
            log.error("根据项目id获取城市的详细信息异常:",e);
            commonResultVo.setCode(200);
            commonResultVo.setMsg("根据项目id获取城市的详细信息异常");
        }
        return commonResultVo;
    }
}

package com.colourfulchina.pangu.taishang.controller;

import com.colourfulchina.bigan.api.entity.SysCountry;
import com.colourfulchina.bigan.api.feign.RemoteCountryService;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.Country;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.api.vo.CityVo;
import com.colourfulchina.pangu.taishang.api.vo.CountryCityVo;
import com.colourfulchina.pangu.taishang.service.CityService;
import com.colourfulchina.pangu.taishang.service.CountryService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/country")
@AllArgsConstructor
@Slf4j
@Api(tags = {"国家操作接口"})
public class CountryController {
    @Autowired
    private CountryService countryService;
    @Autowired
    private CityService cityService;
    private final RemoteCountryService remoteCountryService;

    /**
     * 国家列表查询
     * @return
     */
    @SysGodDoorLog("国家列表")
    @ApiOperation("国家列表")
    @PostMapping("/selectCountryList")
    public CommonResultVo<List<Country>> selectCountryList(){
        CommonResultVo<List<Country>> result = new CommonResultVo<>();
        List<Country> countryList = countryService.selectList(null);
        result.setResult(countryList);
        return result;
    }

    /**
     * 国家城市联动列表
     * @return
     */
    @SysGodDoorLog("国家城市联动列表")
    @ApiOperation("国家城市联动列表")
    @PostMapping("/selectCountryCity")
    public CommonResultVo<List<CountryCityVo>> selectCountryCity(){
        CommonResultVo<List<CountryCityVo>> result = new CommonResultVo<>();
        List<CountryCityVo> countryCityVos = Lists.newLinkedList();
        List<Country> countryList = countryService.selectList(null);
        for (Country country : countryList) {
            CountryCityVo countryCityVo = new CountryCityVo();
            countryCityVo.setCountryId(country.getId());
            countryCityVo.setCountryName(country.getNameCh());
            List<CityVo> cityVoList = cityService.selectByCountryId(country.getId());
            countryCityVo.setCityDetail(cityVoList);
            countryCityVos.add(countryCityVo);
        }
        result.setResult(countryCityVos);
        return result;
    }

    /**
     * 国家详情查询
     * @param id
     * @return
     */
    @SysGodDoorLog("国家详情")
    @ApiOperation("国家详情")
    @PostMapping("/countryDetail")
    public CommonResultVo<Country> countryDetail(@RequestBody String id){
        CommonResultVo<Country> result = new CommonResultVo<>();
        Country country = countryService.selectById(id);
        result.setResult(country);
        return result;
    }

    /**
     * 初始化老系统中国家到mysql中
     * @return
     */
    @SysGodDoorLog("初始化老系统中国家到mysql中")
    @ApiOperation("初始化老系统中国家到mysql中")
    @PostMapping("/syncCountryList")
    @Transactional(rollbackFor=Exception.class)
    public CommonResultVo<List<Country>> syncCountryList(){
        CommonResultVo<List<Country>> result = new CommonResultVo<>();
        List<Country> countryList = Lists.newLinkedList();
        CommonResultVo<List<SysCountry>> remoteResult = remoteCountryService.selectCountryList();
        if (!CollectionUtils.isEmpty(remoteResult.getResult())){
            for (SysCountry sysCountry : remoteResult.getResult()) {
                Country country = new Country();
                country.setId(sysCountry.getCode());
                country.setNameCh(sysCountry.getNameCn());
                country.setNameEn(sysCountry.getNameEn());
                country.setNamePy(sysCountry.getNamePy());
                country.setFlag(sysCountry.getFlag());
                country.setAlias(sysCountry.getAlias());
                country.setAreaId(sysCountry.getArea());
                country.setCreateTime(new Date());
                country.setCreateUser(SecurityUtils.getLoginName());
                country.setDelFlag(DelFlagEnums.NORMAL.getCode());
                countryList.add(country);
            }
        }
        countryService.insertBatch(countryList);
        result.setResult(countryList);
        return result;
    }
}
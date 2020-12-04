package com.colourfulchina.pangu.taishang.controller;

import com.colourfulchina.bigan.api.entity.SysCountryArea;
import com.colourfulchina.bigan.api.feign.RemoteAreaService;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.CountryArea;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.service.CountryAreaService;
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

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/countryArea")
@Api(tags = {"地区操作接口"})
public class CountryAreaController {
    @Autowired
    private CountryAreaService countryAreaService;
    private final RemoteAreaService remoteAreaService;

    /**
     * 地区列表查询
     * @return
     */
    @SysGodDoorLog("地区列表")
    @ApiOperation("地区列表")
    @PostMapping("/selectAreaList")
    public CommonResultVo<List<CountryArea>> selectAreaList(){
        CommonResultVo<List<CountryArea>> result = new CommonResultVo<>();
        List<CountryArea> areaList = countryAreaService.selectList(null);
        result.setResult(areaList);
        return result;
    }

    /**
     * 地区详情
     * @param id
     * @return
     */
    @SysGodDoorLog("地区详情")
    @ApiOperation("地区详情")
    @PostMapping("/areaDetail")
    public CommonResultVo<CountryArea> areaDetail(@RequestBody String id){
        CommonResultVo<CountryArea> result = new CommonResultVo<>();
        CountryArea countryArea = countryAreaService.selectById(id);
        result.setResult(countryArea);
        return result;
    }

    /**
     * 初始化老系统中的区域到mysql中
     * @return
     */
    @SysGodDoorLog("初始化老系统中的区域到mysql中")
    @ApiOperation("初始化老系统中的区域到mysql中")
    @PostMapping("/syncAreaList")
    @Transactional(rollbackFor=Exception.class)
    public CommonResultVo<List<CountryArea>> syncAreaList(){
        CommonResultVo<List<CountryArea>> result = new CommonResultVo<>();
        List<CountryArea> countryAreaList = Lists.newLinkedList();
        CommonResultVo<List<SysCountryArea>> areaList = remoteAreaService.selectAreaList();
        if (!CollectionUtils.isEmpty(areaList.getResult())){
            for (SysCountryArea sysCountryArea : areaList.getResult()) {
                CountryArea countryArea = new CountryArea();
                countryArea.setId(sysCountryArea.getCode());
                countryArea.setName(sysCountryArea.getName());
                countryArea.setSort(sysCountryArea.getSort());
                countryArea.setDelFlag(DelFlagEnums.NORMAL.getCode());
                countryArea.setCreateTime(new Date());
                countryArea.setCreateUser(SecurityUtils.getLoginName());
                countryAreaList.add(countryArea);
            }
        }
        countryAreaService.insertBatch(countryAreaList);
        result.setResult(countryAreaList);
        return result;
    }

}
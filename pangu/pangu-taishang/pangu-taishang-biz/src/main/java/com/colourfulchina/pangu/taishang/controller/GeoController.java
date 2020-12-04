package com.colourfulchina.pangu.taishang.controller;

import com.colourfulchina.bigan.api.entity.SysGeo;
import com.colourfulchina.bigan.api.feign.RemoteGeoService;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.Geo;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.service.GeoService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/geo")
@Slf4j
@AllArgsConstructor
@Api(tags = {"定位接口"})
public class GeoController {
    @Autowired
    private GeoService geoService;
    private final RemoteGeoService remoteGeoService;

    /**
     * 初始化老系统中酒店定位到mysql中
     * @return
     */
    @SysGodDoorLog("初始化老系统中酒店定位到mysql中")
    @ApiOperation("初始化老系统中酒店定位到mysql中")
    @PostMapping("/syncGeoList")
    @Transactional(rollbackFor=Exception.class)
    public CommonResultVo<List<Geo>> syncGeoList(){
        CommonResultVo<List<Geo>> result = new CommonResultVo<>();
        List<Geo> geoList = Lists.newLinkedList();
        CommonResultVo<List<SysGeo>> remoteResult = remoteGeoService.selectGeoList();
        if (!CollectionUtils.isEmpty(remoteResult.getResult())){
            for (SysGeo sysGeo : remoteResult.getResult()) {
                Geo geo = new Geo();
                geo.setOldId(sysGeo.getId());
                geo.setAddressCh(sysGeo.getAddress());
                geo.setAddressEn(sysGeo.getAddressEn());
                geo.setPoint(sysGeo.getPoint());
                geo.setLat(sysGeo.getLat());
                geo.setLng(sysGeo.getLng());
                geo.setZoom(sysGeo.getZoom());
                geo.setCountry(sysGeo.getCountry());
                geo.setProvince(sysGeo.getProvince());
                geo.setCity(sysGeo.getCity());
                geo.setDelFlag(DelFlagEnums.NORMAL.getCode());
                geo.setCreateTime(new Date());
                geo.setCreateUser(SecurityUtils.getLoginName());
                geoList.add(geo);
            }
        }
        geoService.insertBatch(geoList);
        result.setResult(geoList);
        return result;
    }
}
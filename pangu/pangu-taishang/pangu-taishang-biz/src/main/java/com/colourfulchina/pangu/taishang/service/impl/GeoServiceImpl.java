package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.SysGeo;
import com.colourfulchina.bigan.api.feign.RemoteGeoService;
import com.colourfulchina.bigan.api.feign.RemoteHotelService;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.Geo;
import com.colourfulchina.pangu.taishang.mapper.GeoMapper;
import com.colourfulchina.pangu.taishang.service.GeoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class GeoServiceImpl extends ServiceImpl<GeoMapper,Geo> implements GeoService {
    @Autowired
    private GeoMapper geoMapper;
    private final RemoteGeoService remoteGeoService;

    /**
     * 添加定位信息
     * @param geo
     * @return
     * @throws Exception
     */
    @Override
    public Geo addGeo(Geo geo) throws Exception {
        //远程老系统定位信息同步新增
        SysGeo sysGeo = new SysGeo();
        sysGeo.setAddress(geo.getAddressCh());
        sysGeo.setAddressEn(geo.getAddressEn());
        sysGeo.setLat(geo.getLat());
        sysGeo.setLng(geo.getLng());
        sysGeo.setZoom(geo.getZoom());
        sysGeo.setCountry(geo.getCountry());
        sysGeo.setProvince(geo.getProvince());
        sysGeo.setCity(geo.getCity());
        CommonResultVo<SysGeo> remoteGeo = remoteGeoService.remoteAddGeo(sysGeo);
        //新系统定位信息洗新增
        if (remoteGeo != null && remoteGeo.getCode() == 100){
            geo.setOldId(remoteGeo.getResult().getId());
            geo.setCreateUser(SecurityUtils.getLoginName());
            geoMapper.insert(geo);
        }
        return geo;
    }

    /**
     * 更新定位信息
     * @param geo
     * @return
     * @throws Exception
     */
    @Override
    public Geo updGeo(Geo geo) throws Exception {
        //远程老系统定位信息同步修改
        SysGeo sysGeo = new SysGeo();
        sysGeo.setId(geo.getOldId());
        sysGeo.setAddress(geo.getAddressCh());
        sysGeo.setAddressEn(geo.getAddressEn());
        sysGeo.setLat(geo.getLat());
        sysGeo.setLng(geo.getLng());
        sysGeo.setZoom(geo.getZoom());
        sysGeo.setCountry(geo.getCountry());
        sysGeo.setProvince(geo.getProvince());
        sysGeo.setCity(geo.getCity());
        CommonResultVo<SysGeo> remoteGeo = remoteGeoService.remoteUpdGeo(sysGeo);
        //新系统定位信息修改
        geo.setUpdateUser(SecurityUtils.getLoginName());
        geoMapper.updateById(geo);
        return geo;
    }
}
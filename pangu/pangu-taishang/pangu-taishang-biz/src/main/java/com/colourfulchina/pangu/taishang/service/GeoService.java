package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.Geo;

public interface GeoService extends IService<Geo> {
    /**
     * 添加定位信息
     * @param geo
     * @return
     * @throws Exception
     */
    Geo addGeo(Geo geo)throws Exception;

    /**
     * 更新定位信息
     * @param geo
     * @return
     * @throws Exception
     */
    Geo updGeo(Geo geo)throws Exception;
}
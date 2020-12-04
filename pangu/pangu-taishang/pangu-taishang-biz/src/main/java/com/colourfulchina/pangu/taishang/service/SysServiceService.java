package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.SysService;

import java.util.List;

public interface SysServiceService extends IService<SysService> {

    /**
     * 服务类型列表查询
     * @return
     */
    List<SysService> selectSysServiceList();

    List<SysService> selectListByShopType(String shopType);
}
package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.pangu.taishang.api.entity.SysService;

import java.util.List;

public interface SysServiceMapper extends BaseMapper<SysService> {
    List<SysService> selectListByShopType(String shopType);
}
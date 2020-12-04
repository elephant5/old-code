package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.ServiceGift;

import java.util.List;

public interface ServiceGiftService extends IService<ServiceGift> {

    List<ServiceGift> selectByCode(String code);
}
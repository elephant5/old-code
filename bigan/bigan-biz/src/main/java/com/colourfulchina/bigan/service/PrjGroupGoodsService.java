package com.colourfulchina.bigan.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.bigan.api.entity.PrjGroupGoods;

import java.util.List;

public interface PrjGroupGoodsService extends IService<PrjGroupGoods> {
    List<PrjGroupGoods> findListByProjectIdAndGroupId(PrjGroupGoods prjGroupGoods);
}

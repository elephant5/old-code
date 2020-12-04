package com.colourfulchina.bigan.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.bigan.api.entity.PrjGroupGoods;

import java.util.List;


public interface PrjGroupGoodsMapper extends BaseMapper<PrjGroupGoods> {

    List<PrjGroupGoods> findListByProjectIdAndGroupId(PrjGroupGoods prjGroupGoods);
}
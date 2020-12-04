package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.PrjGroupGoods;
import com.colourfulchina.bigan.mapper.PrjGroupGoodsMapper;
import com.colourfulchina.bigan.service.PrjGroupGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class PrjGroupGoodsServiceImpl extends ServiceImpl<PrjGroupGoodsMapper,PrjGroupGoods> implements PrjGroupGoodsService {
    @Autowired
    private PrjGroupGoodsMapper prjGroupGoodsMapper;

    @Override
    public List<PrjGroupGoods> findListByProjectIdAndGroupId(PrjGroupGoods prjGroupGoods) {
        return prjGroupGoodsMapper.findListByProjectIdAndGroupId(prjGroupGoods);
    }
}

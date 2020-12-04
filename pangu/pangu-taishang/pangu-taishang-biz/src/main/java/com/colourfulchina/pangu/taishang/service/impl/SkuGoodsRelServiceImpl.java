package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.entity.SkuGoodsRel;
import com.colourfulchina.pangu.taishang.api.vo.req.SkuGoodsReqVo;
import com.colourfulchina.pangu.taishang.mapper.SkuGoodsRelMapper;
import com.colourfulchina.pangu.taishang.service.SkuGoodsRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;

@Slf4j
@Service
public class SkuGoodsRelServiceImpl extends ServiceImpl<SkuGoodsRelMapper, SkuGoodsRel> implements SkuGoodsRelService{
    @Autowired
    private SkuGoodsRelMapper skuGoodsRelMapper;


    @Override
    public SkuGoodsRel getGoodsInfoBySku(SkuGoodsReqVo reqVo) {

        return skuGoodsRelMapper.selectGoodsInfoBySku(reqVo);
    }
}

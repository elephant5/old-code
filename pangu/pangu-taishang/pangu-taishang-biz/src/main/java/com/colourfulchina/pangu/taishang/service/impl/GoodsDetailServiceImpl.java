package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.entity.GoodsClause;
import com.colourfulchina.pangu.taishang.api.entity.GoodsDetail;
import com.colourfulchina.pangu.taishang.api.entity.GoodsPrice;
import com.colourfulchina.pangu.taishang.mapper.GoodsDetailMapper;
import com.colourfulchina.pangu.taishang.service.GoodsDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GoodsDetailServiceImpl extends ServiceImpl<GoodsDetailMapper, GoodsDetail> implements GoodsDetailService {


    @Autowired
    GoodsDetailMapper goodsDetailMapper;
    /**
     * 根据商品ID查询商品详情数据
     *
     * @param goodsId
     * @return
     */
    @Override
    public List<GoodsDetail> selectByGoodsId(Integer goodsId) {
        Wrapper<GoodsDetail> localWrapper = new Wrapper<GoodsDetail>() {
            @Override
            public String getSqlSegment() {
                return " where goods_id  = " +  goodsId;
            }
        };
        return goodsDetailMapper.selectList(localWrapper);
    }
}
package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.entity.GoodsClause;
import com.colourfulchina.pangu.taishang.api.entity.GoodsPrice;
import com.colourfulchina.pangu.taishang.mapper.GoodsPriceMapper;
import com.colourfulchina.pangu.taishang.service.GoodsPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GoodsPriceServiceImpl extends ServiceImpl<GoodsPriceMapper, GoodsPrice> implements GoodsPriceService {

    @Autowired
    GoodsPriceMapper goodsPriceMapper;

    /**
     * 根据商品ID查询商品价格数据
     *
     * @param goodsId
     * @return
     */
    @Override
    public List<GoodsPrice> selectByGoodsId(Integer goodsId) {
        Wrapper<GoodsPrice> localWrapper = new Wrapper<GoodsPrice>() {
            @Override
            public String getSqlSegment() {
                return " where goods_id  = " +  goodsId;
            }
        };
        return goodsPriceMapper.selectList(localWrapper);
    }

    /**
     * 删除goods下的价格
     *
     * @param goodsId
     */
    @Override
    public void deleteByGoodsId(Integer goodsId) {
        Wrapper<GoodsPrice> localWrapper = new Wrapper<GoodsPrice>() {
            @Override
            public String getSqlSegment() {
                return " where goods_id  = " +  goodsId;
            }
        };
        goodsPriceMapper.delete(localWrapper);
    }
}
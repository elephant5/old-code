package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.entity.GoodsClause;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.colourfulchina.pangu.taishang.mapper.GoodsClauseMapper;
import com.colourfulchina.pangu.taishang.service.GoodsClauseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GoodsClauseServiceImpl extends ServiceImpl<GoodsClauseMapper, GoodsClause> implements GoodsClauseService {

    @Autowired
    GoodsClauseMapper goodsClauseMapper;
    /**
     * 根据商品ID查商品详情
     *
     * @param goodsId
     * @return
     */
    @Override
    public List<GoodsClause> selectByGoodsId(Integer goodsId) {
        Wrapper<GoodsClause> localWrapper = new Wrapper<GoodsClause>() {
            @Override
            public String getSqlSegment() {
                return " where goods_id = " + goodsId;
            }
        };
        return goodsClauseMapper.selectList(localWrapper);
    }

    /**
     * 根据商品ID删除数据
     *
     * @param goodsId
     */
    @Override
    public void deleteByGoodsId(Integer goodsId) {
        Wrapper<GoodsClause> localWrapper = new Wrapper<GoodsClause>() {
            @Override
            public String getSqlSegment() {
                return " where goods_id = " + goodsId;
            }
        };
         goodsClauseMapper.delete(localWrapper);
    }

    @Override
    public List<GoodsClause> selectGoodsClauseById(Integer goodsId) {
        List<GoodsClause> goodsBaseVoList =  goodsClauseMapper.selectGoodsClauseById(goodsId);
        return goodsBaseVoList;
    }
}
package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.GoodsClause;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;

import java.util.List;

public interface GoodsClauseService extends IService<GoodsClause> {

    /**
     * 根据商品ID查商品详情
     * @param goodsId
     * @return
     */
    List<GoodsClause> selectByGoodsId(Integer goodsId);

    /**
     * 根据商品ID删除数据
     * @param id
     */
    void deleteByGoodsId(Integer id);

    /**
     * @title:selectGoodsClauseById
     * @Description: 根据goodsId 获取商品扩展信息
     * @Param: [goodsId]
     * @return: com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo
     * @Auther: 图南
     * @Date: 2019/6/26 16:07
     */
    List<GoodsClause> selectGoodsClauseById(Integer goodsId);
}
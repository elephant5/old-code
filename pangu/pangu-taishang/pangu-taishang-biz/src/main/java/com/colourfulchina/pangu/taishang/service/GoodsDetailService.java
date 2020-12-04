package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.GoodsClause;
import com.colourfulchina.pangu.taishang.api.entity.GoodsDetail;

import java.util.List;

public interface GoodsDetailService extends IService<GoodsDetail> {

    /**
     * 根据商品ID查询商品详情数据
     * @param goodsId
     * @return
     */
    List<GoodsDetail> selectByGoodsId(Integer goodsId);
}
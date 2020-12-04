package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.GoodsClause;
import com.colourfulchina.pangu.taishang.api.entity.GoodsPrice;

import java.util.List;

public interface GoodsPriceService extends IService<GoodsPrice> {

    /**
     * 根据商品ID查询商品价格数据
     * @param goodsId
     * @return
     */
    List<GoodsPrice> selectByGoodsId(Integer goodsId);

    /**
     * 删除goods下的价格
     * @param id
     */
    void deleteByGoodsId(Integer id);
}
package com.colourfulchina.bigan.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.bigan.api.dto.GoodsInfo;
import com.colourfulchina.bigan.api.entity.Goods;
import com.colourfulchina.bigan.api.entity.PrjGroupGoods;
import com.colourfulchina.bigan.api.entity.Shop;
import com.colourfulchina.bigan.api.entity.ShopItem;
import com.colourfulchina.bigan.api.vo.GoodsDetailVo;
import com.colourfulchina.bigan.api.vo.GoodsSaveVo;

import java.util.List;
import java.util.Map;

public interface GoodsService extends IService<Goods> {
    /**
     * 调用存储过程保存Goods信息
     * @param shop 商户
     * @param items 可为空
     * @param map Excel上的Row
     * @param goodsVo 临时对象 可为空
     * @return
     */
    public Map saveGoodsInfoVo(Shop shop, List<ShopItem> items, Map map, GoodsSaveVo goodsVo ,Integer projectId ,String serviceCode,GoodsInfo vo);

    /**
     * 根据PrjGroupGoods中的goodsID列表查询商品详情
     * @param prjGroupGoodsList
     * @return
     */
    List<GoodsDetailVo> queryGoodsDetail(List<PrjGroupGoods> prjGroupGoodsList);

    /**
     * 根据goodsID查询商品详情
     * @return
     */
    GoodsDetailVo getGoodsDetail(String goodsId);

    /**
     * 导入查询goods
     * @param goodsVo
     * @param serviceCode
     * @return
     */
    List<Goods> selectForList(GoodsInfo goodsVo, String serviceCode,Shop shop);
}

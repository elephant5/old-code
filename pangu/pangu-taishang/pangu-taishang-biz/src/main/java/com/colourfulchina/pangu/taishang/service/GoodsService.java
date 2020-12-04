package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.Goods;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.colourfulchina.pangu.taishang.api.vo.req.QueryOrderExpireTimeReq;
import com.colourfulchina.tianyan.admin.api.entity.SysDict;

import java.util.List;

public interface GoodsService extends IService<Goods> {

    /**
     * 分页查找商品
     * @param pageVoReq
     * @return
     */
    PageVo<GoodsBaseVo> findPageList(PageVo<GoodsBaseVo> pageVoReq);

    /**
     * 复制商品的相关数据
     * @param newGoods
     * @param goodsId
     */
    void copyGoodsDate(Goods newGoods ,Integer goodsId);

    /**
     * 复制商品的产品组相关数据
     * @param newGoods
     * @param goodsId
     */
    void copyGoodsAndProductDate(Goods newGoods,Integer goodsId)throws Exception;

    /**
     * 增加或者更新商品的其他数据
     * @param goods
     */

    void insertOrUpdateAnotherData(GoodsBaseVo goods);
    /**
     * 增加或者更新商品的其他数据
     * @param goods
     * @param channelId
     */
    void insertOrUpdateAnotherData(GoodsBaseVo goods,Integer channelId);

    /**
     * 根据商品id列表查询商品名称
     * @param ids
     * @return
     */
    List<Goods> selectNameByIds(List<Integer> ids);


    List<String> getGoodsGiftType(String type);

    /**
     * 查询客户权益的到期时间
     * @param queryOrderExpireTimeReq
     * @return
     * @throws Exception
     */
    GoodsBaseVo queryOrderExpireTime(QueryOrderExpireTimeReq queryOrderExpireTimeReq)throws Exception;

    List<SysDict> selectSysDict(String type);

    /**
     * 保存goods和goods setting
     * @param goods
     */
    void insertGoodsAndSetting(GoodsBaseVo goods);

    void insertSyncGoodsCode(Integer projectId, int targetGoodsId, Integer id, Integer salesChannelId, Integer charlieChannel);
}
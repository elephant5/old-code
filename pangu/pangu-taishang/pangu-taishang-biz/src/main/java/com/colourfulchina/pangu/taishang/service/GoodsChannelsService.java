package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.GoodsChannels;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopListQueryReq;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsChannelRes;
import com.colourfulchina.pangu.taishang.api.vo.res.SalesChannelRes;

import java.util.*;

public interface GoodsChannelsService extends IService<GoodsChannels> {

    /**
     * 根据goodsId查询渠道信息
     * @param goodsId
     * @return
     */
    List<GoodsChannels> selectGoodsChannelsByGoodsId(Integer goodsId);

    /**
     * 根据商品ID和渠道ID查询
     * @param id
     * @param id1
     * @return
     */
    GoodsChannels selectByGoodsIdAndChannelId(Integer id, Integer id1);

    /**
     * 根据商品id查询商品渠道详情
     * @param goodsId
     * @return
     */
    List<GoodsChannelRes> selectGoodsChannel(Integer goodsId)throws Exception;

    /**
     * @title:selectGoodsChannel
     * @Description: 根据渠道id查询渠道信息
     * @Param: [channelId]
     * @return: com.colourfulchina.pangu.taishang.api.vo.res.SalesChannelRes
     * @Auther: 图南
     * @Date: 2019/6/19 9:33
     */
    SalesChannelRes selectGoodsChannel(String channelId);

}
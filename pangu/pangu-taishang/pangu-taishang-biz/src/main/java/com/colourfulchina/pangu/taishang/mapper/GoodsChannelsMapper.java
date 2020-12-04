package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.pangu.taishang.api.entity.GoodsChannels;
import com.colourfulchina.pangu.taishang.api.entity.SalesChannel;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopListQueryReq;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsChannelRes;
import com.colourfulchina.pangu.taishang.api.vo.res.SalesChannelRes;
import com.colourfulchina.pangu.taishang.api.vo.res.bigan.ProjectChannel;

import java.util.List;

public interface GoodsChannelsMapper extends BaseMapper<GoodsChannels> {

    /**
     * 根据商品id查询渠道详情
     * @param goodsId
     * @return
     */
    List<GoodsChannelRes> selectGoodsChannel(Integer goodsId);

    /**
     * @title:selectGoodsChannel
     * @Description: 根据渠道id查询渠道信息
     * @Param: [channelId]
     * @return: com.colourfulchina.pangu.taishang.api.vo.res.SalesChannelRes
     * @Auther: 图南
     * @Date: 2019/6/19 9:35
     */
    SalesChannelRes selectGoodsChannelByChannelId(String channelId);

    /**
     * 根据商品ID查询商品渠道信息
     * @param goodsId
     * @return
     */
    List<ProjectChannel> selectGoodsChannelByGoodsId(Integer goodsId);
}

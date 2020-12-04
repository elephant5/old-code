package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.ShopChannel;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopChannelPageListReq;

import java.util.List;
import java.util.Map;

public interface ShopChannelMapper extends BaseMapper<ShopChannel> {
    /**
     * 商戶渠道資源列表
     * @param pageReq
     * @param map
     * @return
     */
    List<ShopChannel> selectShopChannelPageList(PageVo<ShopChannelPageListReq> pageReq, Map map);

    /**
     * 檢查商戶資源渠道是否已經存在（根據渠道名稱）
     * @param shopChannel
     * @return
     */
    List<ShopChannel> checkChannelIsExist(ShopChannel shopChannel);
}
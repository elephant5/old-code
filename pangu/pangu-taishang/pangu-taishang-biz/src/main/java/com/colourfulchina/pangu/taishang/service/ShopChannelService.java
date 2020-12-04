package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.ShopChannel;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopChannelPageListReq;

import java.util.List;

public interface ShopChannelService extends IService<ShopChannel> {
    /**
     * 查询商户资源列表
     * @return
     */
    PageVo<ShopChannel> selectShopChannelPageList(PageVo<ShopChannelPageListReq> pageReq);

    /**
     * 根据老系统商户渠道id查询新系统商户渠道信息
     * @param oldChannelId
     * @return
     */
    ShopChannel selectByOldChannel(Integer oldChannelId);

    /**
     * 檢查商戶資源渠道是否已經存在（根據渠道名稱）
     * @return
     */
    List<ShopChannel> checkChannelIsExist(ShopChannel shopChannel);
}
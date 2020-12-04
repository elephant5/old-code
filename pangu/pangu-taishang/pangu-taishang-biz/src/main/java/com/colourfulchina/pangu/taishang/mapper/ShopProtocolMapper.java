package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.pangu.taishang.api.entity.ShopChannel;
import com.colourfulchina.pangu.taishang.api.entity.ShopProtocol;
import com.colourfulchina.pangu.taishang.api.vo.ShopProtocolMsgVo;
import com.colourfulchina.pangu.taishang.api.vo.res.shopProtocol.ShopProtocolRes;

import java.util.List;

public interface ShopProtocolMapper extends BaseMapper<ShopProtocol> {
    /**
     * 查询商户协议信息
     * @param shopId
     * @return
     */
    ShopProtocolMsgVo selectProtocolMsg(Integer shopId);

    /**
     * 查询商户渠道信息
     * @param shopId
     * @return
     */
    ShopChannel selectShopChannel(Integer shopId);

    List<ShopProtocolRes> selectShopProtocol(Integer shopId);
}
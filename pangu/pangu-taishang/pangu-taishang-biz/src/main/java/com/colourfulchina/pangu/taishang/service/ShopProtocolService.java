package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.ShopChannel;
import com.colourfulchina.pangu.taishang.api.entity.ShopProtocol;
import com.colourfulchina.pangu.taishang.api.vo.ShopProtocolMsgVo;
import com.colourfulchina.pangu.taishang.api.vo.res.shopProtocol.ShopProtocolRes;

import java.util.List;

public interface ShopProtocolService extends IService<ShopProtocol> {

    /**
     * 添加商户协议信息
     * @param shopProtocol
     * @return
     */
    ShopProtocol addShopProtocol(ShopProtocol shopProtocol);

    /**
     * 修改商户协议信息
     * @param shopProtocol
     * @return
     */
    ShopProtocol updShopProtocol(ShopProtocol shopProtocol);

    /**
     * 查询商户协议信息
     * @param shopId
     * @return
     */
    ShopProtocolMsgVo selectProtocolMsg(Integer shopId);

    /**
     * 查询商户的渠道信息
     * @param shopId
     * @return
     * @throws Exception
     */
    ShopChannel selectShopChannel(Integer shopId) throws Exception;

    /**
     * @title:selectShopProtocol
     * @Description: 获取商户扩展信息
     * @Param: [shopId]
     * @return: java.util.List<com.colourfulchina.pangu.taishang.api.vo.res.shopProtocol.ShopProtocolRes>
     * @Auther: 图南
     * @Date: 2019/6/20 19:05
     */
    List<ShopProtocolRes> selectShopProtocol(Integer shopId);
}
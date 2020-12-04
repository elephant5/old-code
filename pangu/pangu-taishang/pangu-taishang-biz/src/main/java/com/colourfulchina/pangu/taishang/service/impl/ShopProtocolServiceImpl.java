package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.Shop;
import com.colourfulchina.bigan.api.feign.RemoteShopService;
import com.colourfulchina.bigan.api.vo.RemoteUpdShopProtocolVo;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import com.colourfulchina.pangu.taishang.api.entity.ShopChannel;
import com.colourfulchina.pangu.taishang.api.entity.ShopProtocol;
import com.colourfulchina.pangu.taishang.api.enums.ShopTypeEnums;
import com.colourfulchina.pangu.taishang.api.enums.ThirdChannelEnums;
import com.colourfulchina.pangu.taishang.api.vo.ShopProtocolMsgVo;
import com.colourfulchina.pangu.taishang.api.vo.res.shopProtocol.ShopProtocolRes;
import com.colourfulchina.pangu.taishang.mapper.ShopProtocolMapper;
import com.colourfulchina.pangu.taishang.service.BlockRuleService;
import com.colourfulchina.pangu.taishang.service.ShopChannelService;
import com.colourfulchina.pangu.taishang.service.ShopProtocolService;
import com.colourfulchina.pangu.taishang.service.ShopService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ShopProtocolServiceImpl extends ServiceImpl<ShopProtocolMapper, ShopProtocol> implements ShopProtocolService {
    @Autowired
    private ShopProtocolMapper shopProtocolMapper;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopChannelService shopChannelService;
    @Autowired
    private BlockRuleService blockRuleService;
    private final RemoteShopService remoteShopService;

    /**
     * 添加商户协议信息
     * @param shopProtocol
     * @return
     */
    @Override
    public ShopProtocol addShopProtocol(ShopProtocol shopProtocol) {
        Shop shop = remoteUpdShopProtocol(shopProtocol);
        shopProtocolMapper.insert(shopProtocol);
        return shopProtocol;
    }

    /**
     * 修改商户协议信息
     * @param shopProtocol
     * @return
     */
    @Override
    public ShopProtocol updShopProtocol(ShopProtocol shopProtocol) {
        Shop shop = remoteUpdShopProtocol(shopProtocol);
        shopProtocolMapper.updateById(shopProtocol);
        return shopProtocol;
    }

    /**
     * 查询商户协议信息
     * @param shopId
     * @return
     */
    @Override
    public ShopProtocolMsgVo selectProtocolMsg(Integer shopId) {
        ShopProtocolMsgVo shopProtocolMsgVo = shopProtocolMapper.selectProtocolMsg(shopId);
        if (StringUtils.isNotEmpty(shopProtocolMsgVo.getBlockRule())){
            List<BlockRule> blockRuleList = blockRuleService.blockRuleStr2list(shopProtocolMsgVo.getBlockRule());
            shopProtocolMsgVo.setBlockRuleList(blockRuleList);
        }
        return shopProtocolMsgVo;
    }

    /**
     * 查询商户的渠道信息
     * @param shopId
     * @return
     * @throws Exception
     */
    @Override
    public ShopChannel selectShopChannel(Integer shopId) throws Exception {
        return shopProtocolMapper.selectShopChannel(shopId);
    }

    /**
     * @title:selectShopProtocol
     * @Description: 获取商户扩展信息
     * @Param: [shopId]
     * @return: java.util.List<com.colourfulchina.pangu.taishang.api.vo.res.shopProtocol.ShopProtocolRes>
     * @Auther: 图南
     * @Date: 2019/6/20 19:06
     */
    @Override
    public List<ShopProtocolRes> selectShopProtocol(Integer shopId) {
        List<ShopProtocolRes> shopProtocolResList = shopProtocolMapper.selectShopProtocol(shopId);
        return shopProtocolResList;
    }

    /**
     * 同步新增商户时商户协议到老系统shop表中
     * @param shopProtocol
     * @return
     */
    public Shop remoteUpdShopProtocol(ShopProtocol shopProtocol){
        //根据商户协议id查询商户信息
        com.colourfulchina.pangu.taishang.api.entity.Shop shop = shopService.selectById(shopProtocol.getId());
        //商户类型为兑换券或者出行的不同步
        if (!ShopTypeEnums.COUPON.getCode().equals(shop.getShopType())
                && !ShopTypeEnums.TRIP.getCode().equals(shop.getShopType())
                && !ShopTypeEnums.OTHER.getCode().equals(shop.getShopType())){
            //根据渠道id查询渠道信息
            ShopChannel shopChannel = shopChannelService.selectById(shopProtocol.getChannelId());
            RemoteUpdShopProtocolVo remoteUpdShopProtocolVo = new RemoteUpdShopProtocolVo();
            remoteUpdShopProtocolVo.setShopId(shop.getOldShopId());
            if (shopChannel.getId() == ThirdChannelEnums.THIRD_CHANNEL.getCode()){
                remoteUpdShopProtocolVo.setChannelId(null);
            }else {
                remoteUpdShopProtocolVo.setChannelId(shopChannel.getOldId());
            }
            remoteUpdShopProtocolVo.setCurrency(shopProtocol.getCurrency());
            remoteUpdShopProtocolVo.setSettleMethod(shopProtocol.getSettleMethod());
            remoteUpdShopProtocolVo.setDecimal(shopProtocol.getDecimal());
            remoteUpdShopProtocolVo.setPrincipal(shopProtocol.getPrincipal());
            remoteUpdShopProtocolVo.setImprest(shopProtocol.getImprest());
            remoteUpdShopProtocolVo.setDeposit(shopProtocol.getDeposit());
            remoteUpdShopProtocolVo.setContractStart(shopProtocol.getContractStart());
            remoteUpdShopProtocolVo.setContractExpiry(shopProtocol.getContractExpiry());
            remoteUpdShopProtocolVo.setBlockRule(shopProtocol.getBlockRule());
            remoteUpdShopProtocolVo.setParking(shopProtocol.getParking());
            remoteUpdShopProtocolVo.setChildren(shopProtocol.getChildren());
            remoteUpdShopProtocolVo.setNotice(shopProtocol.getNotice());
            CommonResultVo<Shop> remoteResult = remoteShopService.remoteUpdShopProtocol(remoteUpdShopProtocolVo);
            return remoteResult.getResult();
        }
        return null;
    }
}
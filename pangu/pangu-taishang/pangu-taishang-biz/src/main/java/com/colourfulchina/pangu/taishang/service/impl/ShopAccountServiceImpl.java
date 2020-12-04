package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.feign.RemoteShopAccountService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.Shop;
import com.colourfulchina.pangu.taishang.api.entity.ShopAccount;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.api.enums.ShopAccountTypeEnums;
import com.colourfulchina.pangu.taishang.api.enums.ShopTypeEnums;
import com.colourfulchina.pangu.taishang.mapper.ShopAccountMapper;
import com.colourfulchina.pangu.taishang.service.ShopAccountService;
import com.colourfulchina.pangu.taishang.service.ShopService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class ShopAccountServiceImpl extends ServiceImpl<ShopAccountMapper,ShopAccount> implements ShopAccountService {
    @Autowired
    private ShopAccountMapper shopAccountMapper;
    @Autowired
    private ShopService shopService;
    private final RemoteShopAccountService remoteShopAccountService;

    /**
     * 商户中心添加
     * @param shopAccount
     * @return
     */
    @Override
    public ShopAccount addShopAccount(ShopAccount shopAccount) {
        com.colourfulchina.bigan.api.entity.ShopAccount remoteShopAccount = remoteAddShopAccount(shopAccount);
        shopAccountMapper.insert(shopAccount);
        return shopAccount;
    }

    /**
     * 商户中心修改
     * @param shopAccount
     * @return
     */
    @Override
    public ShopAccount updShopAccount(ShopAccount shopAccount) {
        com.colourfulchina.bigan.api.entity.ShopAccount remoteShopAccount = remoteAddShopAccount(shopAccount);
        shopAccountMapper.updateById(shopAccount);
        return shopAccount;
    }

    /**
     * 获取酒店的账户
     *
     * @param id
     * @return
     */
    @Override
    public ShopAccount getHotelAccount(Integer id) {

        ShopAccount local = new ShopAccount();
        local.setHotelId(id);
        local.setAccountType(ShopAccountTypeEnums.HOTEL.getCode());
        local.setDelFlag(DelFlagEnums.NORMAL.getCode());
        return shopAccountMapper.selectOne(local);
    }

    @Override
    public ShopAccount getShopAccount(Integer id) {

        ShopAccount local = new ShopAccount();
        local.setShopId(id);
        local.setAccountType(ShopAccountTypeEnums.SHOP.getCode());
        local.setDelFlag(DelFlagEnums.NORMAL.getCode());
        return shopAccountMapper.selectOne(local);
    }
    public List<ShopAccount> getShopsAccount(Integer id) {

        EntityWrapper local = new EntityWrapper();
        local.eq("hotel_id",id);
        local.eq("account_type",ShopAccountTypeEnums.SHOP.getCode());
        return shopAccountMapper.selectList(local);
    }
    /**
     * 商户中心添加或修改时同步更新老系统的商户中心
     * @param shopAccount
     * @return
     */
    public com.colourfulchina.bigan.api.entity.ShopAccount remoteAddShopAccount(ShopAccount shopAccount){
        //根据商户id查询商户信息
        Shop shop = shopService.selectById(shopAccount.getShopId());
        //出行和兑换券类型的商户不同步到老系统
        if (!ShopTypeEnums.COUPON.getCode().equals(shop.getShopType())
                && !ShopTypeEnums.TRIP.getCode().equals(shop.getShopType())
                && !ShopTypeEnums.OTHER.getCode().equals(shop.getShopType())){
            com.colourfulchina.bigan.api.entity.ShopAccount oldShopAccount = new com.colourfulchina.bigan.api.entity.ShopAccount();
            oldShopAccount.setShopId(shop.getOldShopId().longValue());
            oldShopAccount.setAccount(shopAccount.getUsername());
            oldShopAccount.setPwd(shopAccount.getPassword());
            CommonResultVo<com.colourfulchina.bigan.api.entity.ShopAccount> remoteResult = remoteShopAccountService.remoteAddShopAccount(oldShopAccount);
            return remoteResult.getResult();
        }
        return null;
    }
}
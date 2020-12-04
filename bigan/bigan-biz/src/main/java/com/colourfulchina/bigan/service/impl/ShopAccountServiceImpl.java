package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.ShopAccount;
import com.colourfulchina.bigan.mapper.ShopAccountMapper;
import com.colourfulchina.bigan.mapper.ShopMapper;
import com.colourfulchina.bigan.service.ShopAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class ShopAccountServiceImpl extends ServiceImpl<ShopAccountMapper,ShopAccount> implements ShopAccountService {
    @Autowired
    private ShopAccountMapper shopAccountMapper;

    /**
     * 根据shipId检测账户是否存在
     * @param shopAccount
     * @return flag(true:存在,false:不存在)
     */
    @Override
    public boolean checkAccountByShopId(ShopAccount shopAccount) {
        boolean flag = true;
        shopAccount = shopAccountMapper.checkAccountByShopId(shopAccount);
        if (shopAccount == null){
            flag = false;
        }
        return flag;
    }
}

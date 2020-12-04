package com.colourfulchina.bigan.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.bigan.api.entity.ShopAccount;

public interface ShopAccountService extends IService<ShopAccount> {
    boolean checkAccountByShopId(ShopAccount shopAccount);
}

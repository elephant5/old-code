package com.colourfulchina.bigan.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.bigan.api.entity.ShopAccount;

public interface ShopAccountMapper extends BaseMapper<ShopAccount> {
    /**
     * 根据shopId检测是否存在此账户
     * @param shopAccount
     * @return
     */
    public ShopAccount checkAccountByShopId(ShopAccount shopAccount);
}

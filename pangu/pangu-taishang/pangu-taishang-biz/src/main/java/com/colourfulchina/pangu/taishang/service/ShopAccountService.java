package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.ShopAccount;

import java.util.List;

public interface ShopAccountService extends IService<ShopAccount> {
    /**
     * 商户中心添加
     * @param shopAccount
     * @return
     */
    ShopAccount addShopAccount(ShopAccount shopAccount);

    /**
     * 商户中心修改
     * @param shopAccount
     * @return
     */
    ShopAccount updShopAccount(ShopAccount shopAccount);

    /**
     * 获取酒店的账户
     * @param id
     * @return
     */
    ShopAccount getHotelAccount(Integer id);


    /**
     * 获取酒店下的子商户账户
     * @param id
     * @return
     */
    public List<ShopAccount> getShopsAccount(Integer id);

    ShopAccount getShopAccount(Integer shopId);
}
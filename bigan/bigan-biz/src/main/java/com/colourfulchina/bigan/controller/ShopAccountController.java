package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.ShopAccount;
import com.colourfulchina.bigan.service.ShopAccountService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shopAccount")
public class ShopAccountController {
    @Autowired
    ShopAccountService shopAccountService;

    @GetMapping("/get/{id}")
    public ShopAccount get(@PathVariable Long id){
        return shopAccountService.selectById(id);
    }

    /**
     * 根据shopId检测账户是否存在
     * @param shopAccount
     * @return flag(true:存在,false:不存在)
     */
    @GetMapping(value = "/check")
    public boolean checkAccountByShopId(ShopAccount shopAccount){
        boolean flag = shopAccountService.checkAccountByShopId(shopAccount);
        return flag;
    }

    /**
     * 查询商户中心列表
     * @return
     */
    @PostMapping("/selectShopAccountList")
    public CommonResultVo<List<ShopAccount>> selectShopAccountList(){
        CommonResultVo<List<ShopAccount>> result = new CommonResultVo<>();
        List<ShopAccount> shopAccountList = shopAccountService.selectList(null);
        result.setResult(shopAccountList);
        return result;
    }

    /**
     * 更新商户中心信息
     * @param shopAccount
     * @return
     */
    @PostMapping("/remoteAddShopAccount")
    public CommonResultVo<ShopAccount> remoteAddShopAccount(@RequestBody ShopAccount shopAccount){
        CommonResultVo<ShopAccount> result = new CommonResultVo<>();
        //根据商户id查询商户中心信息
        ShopAccount oldShopAccount = shopAccountService.selectById(shopAccount.getShopId());
        if (oldShopAccount == null){
            shopAccountService.insert(shopAccount);
        }else {
            shopAccountService.updateById(shopAccount);
        }
        result.setResult(shopAccount);
        return result;
    }
}

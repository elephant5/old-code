package com.colourfulchina.pangu.taishang.controller;

import com.colourfulchina.bigan.api.feign.RemoteShopAccountService;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.Shop;
import com.colourfulchina.pangu.taishang.api.entity.ShopAccount;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.api.enums.ShopAccountTypeEnums;
import com.colourfulchina.pangu.taishang.service.ShopAccountService;
import com.colourfulchina.pangu.taishang.service.ShopService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/shopAccount")
@AllArgsConstructor
@Slf4j
@Api(tags = {"商户账号操作接口"})
public class ShopAccountController {
    @Autowired
    private ShopAccountService shopAccountService;
    @Autowired
    private ShopService shopService;
    private final RemoteShopAccountService remoteShopAccountService;

    /**
     * 同步老系统商户中心到新系统中
     * @return
     */
    @SysGodDoorLog("同步老系统商户中心到新系统")
    @ApiOperation("同步老系统商户中心到新系统")
    @PostMapping("/syncShopAccountList")
    @Transactional(rollbackFor=Exception.class)
    public CommonResultVo<List<ShopAccount>> syncShopAccountList(){
        log.info("同步开始");
        CommonResultVo<List<ShopAccount>> result = new CommonResultVo<>();
        List<ShopAccount> shopAccountList = Lists.newLinkedList();
        CommonResultVo<List<com.colourfulchina.bigan.api.entity.ShopAccount>> remoteResult = remoteShopAccountService.selectShopAccountList();
        for (com.colourfulchina.bigan.api.entity.ShopAccount remoteShopAccount : remoteResult.getResult()) {
            ShopAccount shopAccount = new ShopAccount();
            //根据老系统商户id查询新系统商户id(排除礼品兑换的测试商户)
            if (remoteShopAccount.getShopId().intValue() != 1246){
                Shop shop = shopService.selectByOldId(remoteShopAccount.getShopId().intValue());
//                shopAccount.setId(shop.getId());
                shopAccount.setShopId(remoteShopAccount.getShopId().intValue());
                shopAccount.setAccountType(ShopAccountTypeEnums.SHOP.getCode());
                shopAccount.setUsername(remoteShopAccount.getAccount());
                shopAccount.setPassword(remoteShopAccount.getPwd());
                shopAccount.setCreateTime(new Date());

                shopAccount.setCreateUser(SecurityUtils.getLoginName());
                shopAccount.setUpdateTime(new Date());
                shopAccount.setUpdateUser(SecurityUtils.getLoginName());
                shopAccount.setDelFlag(DelFlagEnums.NORMAL.getCode());
                shopAccountList.add(shopAccount);
            }
        }
        shopAccountService.insertBatch(shopAccountList);
        result.setResult(shopAccountList);
        log.info("同步结束");
        return result;
    }
}
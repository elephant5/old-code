package com.colourfulchina.pangu.taishang.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.bigan.api.entity.SysGift;
import com.colourfulchina.bigan.api.feign.RemoteSysGiftService;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.Gift;
import com.colourfulchina.pangu.taishang.api.entity.ServiceGift;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.service.GiftService;
import com.colourfulchina.pangu.taishang.service.ServiceGiftService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/gift")
@AllArgsConstructor
@Slf4j
@Api(tags = {"权益类型操作"})
public class GiftController {
    @Autowired
    private GiftService giftService;
    @Autowired
    private ServiceGiftService serviceGiftService;
    private final RemoteSysGiftService remoteSysGiftService;

    /**
     * 查询权益类型列表
     * @return
     */
    @SysGodDoorLog("查询权益类型列表")
    @ApiOperation("查询权益类型列表")
    @PostMapping("/selectGiftList")
    public CommonResultVo<List<Gift>> selectGiftList(){
        CommonResultVo<List<Gift>> result = new CommonResultVo<>();
        try {
            Wrapper wrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0";
                }
            };
            List<Gift> list = giftService.selectList(wrapper);
            result.setResult(list);
        }catch (Exception e){
            log.error("权益类型查询失败",e);
            result.setCode(200);
            result.setMsg("权益类型查询失败");
        }
        return result;
    }

    /**
     * 根据商户类型查询权益类型
     * @return
     */
    @SysGodDoorLog("根据商户类型查询权益类型")
    @ApiOperation("根据商户类型查询权益类型")
    @PostMapping("/selectGiftByShopType")
    public CommonResultVo<List<Gift>> selectGiftByShopType(@RequestBody String shopType){
        CommonResultVo<List<Gift>> result = new CommonResultVo<>();
        try {
            List<Gift> list = giftService.selectGiftByShopType(shopType);
            result.setResult(list);
        }catch (Exception e){
            log.error("权益类型查询失败",e);
            result.setCode(200);
            result.setMsg("权益类型查询失败");
        }
        return result;
    }

    /**
     * 同步老系统中gift列表
     * @return
     */
    @SysGodDoorLog("同步老系统gift表")
    @ApiOperation("同步老系统gift表")
    @PostMapping("/syncGiftList")
    @Transactional(rollbackFor=Exception.class)
    public CommonResultVo<List<Gift>> syncGiftList(){
        CommonResultVo<List<Gift>> result = new CommonResultVo<>();
        List<Gift> giftList = Lists.newLinkedList();
        CommonResultVo<List<SysGift>> remoteResult = remoteSysGiftService.selectSysGiftList();
        for (SysGift remoteGift : remoteResult.getResult()) {
            Gift gift = new Gift();
            gift.setCode(remoteGift.getCode());
            gift.setName(remoteGift.getName());
            gift.setShortName(remoteGift.getShortName());
            gift.setCreateTime(new Date());

            gift.setCreateUser(com.colourfulchina.god.door.api.util.SecurityUtils.getLoginName());
            gift.setUpdateTime(new Date());
            gift.setUpdateUser(SecurityUtils.getLoginName());
            gift.setDelFlag(DelFlagEnums.NORMAL.getCode());
            giftService.insert(gift);
            giftList.add(gift);

            List<ServiceGift> serviceGiftList = Lists.newLinkedList();
            String target = remoteGift.getTarget();
            JSONArray jsonArray = JSON.parseArray(target);
            for (Object o : jsonArray) {
                ServiceGift serviceGift = new ServiceGift();
                serviceGift.setGiftId(gift.getCode());
                serviceGift.setServiceId(o.toString());
                serviceGift.setCreateTime(new Date());

                serviceGift.setCreateUser(com.colourfulchina.god.door.api.util.SecurityUtils.getLoginName());
                serviceGift.setUpdateTime(new Date());
                serviceGift.setUpdateUser(SecurityUtils.getLoginName());
                serviceGift.setDelFlag(DelFlagEnums.NORMAL.getCode());
                serviceGiftList.add(serviceGift);
            }
            serviceGiftService.insertBatch(serviceGiftList);
        }
        result.setResult(giftList);
        return result;
    }
}
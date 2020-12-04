package com.colourfulchina.pangu.taishang.controller;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.bigan.api.entity.BookChannel;
import com.colourfulchina.bigan.api.feign.RemoteBookChannelService;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.ShopChannel;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.api.enums.ResourceTypeEnums;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopChannelPageListReq;
import com.colourfulchina.pangu.taishang.service.ShopChannelService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/shopChannel")
@AllArgsConstructor
@Slf4j
@Api(value = "商户资源渠道controller",tags = {"商户资源渠道操作接口"})
public class ShopChannelController {
    @Autowired
    private ShopChannelService shopChannelService;
    private final RemoteBookChannelService remoteBookChannelService;

    /**
     * 分页查询商户资源列表
     * @return
     */
    @SysGodDoorLog("分页查询商户渠道列表")
    @ApiOperation("分页查询商户渠道列表")
    @PostMapping("/selectShopChannelPageList")
    public CommonResultVo<PageVo<ShopChannel>> selectShopChannelPageList(@RequestBody PageVo<ShopChannelPageListReq> pageReq){
        CommonResultVo<PageVo<ShopChannel>> result = new CommonResultVo<>();
        PageVo<ShopChannel> pageRes = shopChannelService.selectShopChannelPageList(pageReq);
        result.setResult(pageRes);
        return result;
    }

    /**
     * 商户渠道列表
     * @return
     */
    @SysGodDoorLog("商户渠道列表")
    @ApiOperation("商户渠道列表")
    @PostMapping("/selectChannelList")
    @Cacheable(value = "ShopChannel",key = "'selectChannelList'",unless = "#result == null")
    public CommonResultVo<List<ShopChannel>> selectChannelList(){
        CommonResultVo<List<ShopChannel>> result = new CommonResultVo<>();
        try {
            List<ShopChannel> shopChannelList = shopChannelService.selectList(null);
            result.setResult(shopChannelList);
        }catch (Exception e){
            log.error("查询商户渠道列表失败",e);
            result.setMsg(e.getMessage());
            result.setCode(200);
        }
        return result;
    }

    /**
     * 根据资源类型查询商户渠道列表
     * @return
     */
    @SysGodDoorLog("商户渠道列表")
    @ApiOperation("商户渠道列表")
    @PostMapping("/selectChannelListByServerType")
    @Cacheable(value = "ShopChannel",key = "'selectChannelListByServerType_'+#shopChannel.service",unless = "#result == null")
    public CommonResultVo<List<ShopChannel>> selectChannelListByServerType(@RequestBody ShopChannel shopChannel){
        CommonResultVo<List<ShopChannel>> result = new CommonResultVo<>();
        try {
            Wrapper<ShopChannel> local = new Wrapper<ShopChannel>() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0  and service like '%" + ResourceTypeEnums.findByName(shopChannel.getService()).getCode() +"%'";
                }
            };
            List<ShopChannel> shopChannelList = shopChannelService.selectList(local);
            result.setResult(shopChannelList);
        }catch (Exception e){
            log.error("查询商户渠道列表失败",e);
            result.setMsg(e.getMessage());
            result.setCode(200);
        }
        return result;
    }

    /**
     * 商户资源详情
     * @param shopChannelId
     * @return
     */
    @SysGodDoorLog("商户资源详情")
    @ApiOperation("商户资源详情")
    @PostMapping("/shopChannelDetail")
    @Cacheable(value = "ShopChannel",key = "'shopChannelDetail_'+#shopChannelId",unless = "#result == null")
    public CommonResultVo<ShopChannel> shopChannelDetail(@RequestBody Integer shopChannelId){
        CommonResultVo<ShopChannel> result = new CommonResultVo<>();
        ShopChannel shopChannel = shopChannelService.selectById(shopChannelId);
        result.setResult(shopChannel);
        return result;
    }

    /**
     * 商戶資源渠道新增
     * @param shopChannel
     * @return
     */
    @SysGodDoorLog("商戶資源渠道新增")
    @ApiOperation("商戶資源渠道新增")
    @PostMapping("/addShopChannel")
    @CacheEvict(value = {"ShopChannel","Shop"},allEntries = true)
    public CommonResultVo<ShopChannel> addShopChannel(@RequestBody ShopChannel shopChannel){
        CommonResultVo<ShopChannel> result = new CommonResultVo<>();
        List<ShopChannel> shopChannelList = shopChannelService.checkChannelIsExist(shopChannel);
        if (CollectionUtils.isEmpty(shopChannelList)){
            result.setCode(200);
            result.setMsg("渠道名称重复，无法添加");
            return result;
        }
        shopChannel.setCreateUser(SecurityUtils.getLoginName());
        shopChannel.setCreateTime(new Date());
        shopChannelService.insert(shopChannel);
        result.setResult(shopChannel);
        return result;
    }

    /**
     * 同步老系统商户资源类型
     * @return
     */
    @SysGodDoorLog("同步老系统商户资源类型")
    @ApiOperation("同步老系统商户资源类型")
    @PostMapping("/syncShopChannel")
    @Transactional(rollbackFor=Exception.class)
    public CommonResultVo<List<ShopChannel>> syncShopChannel(){
        CommonResultVo<List<ShopChannel>> result = new CommonResultVo<>();
        List<ShopChannel> shopChannelList = Lists.newLinkedList();
        CommonResultVo<List<BookChannel>> remoteResult = remoteBookChannelService.selectBookChannelList();
        if (!CollectionUtils.isEmpty(remoteResult.getResult())){
            for (BookChannel bookChannel : remoteResult.getResult()) {
                ShopChannel shopChannel = new ShopChannel();
                shopChannel.setOldId(bookChannel.getId());
                shopChannel.setName(bookChannel.getName());
                shopChannel.setInternal(bookChannel.getInternal());
                shopChannel.setSettleMethod(bookChannel.getSettleMethod());
                shopChannel.setCurrency(bookChannel.getCurrency());
                shopChannel.setCreateTime(new Date());
                shopChannel.setCreateUser(SecurityUtils.getLoginName());
//                if (bookChannel.getActive() == 0){
//                    shopChannel.setDelFlag(DelFlagEnums.DELETE.getCode());
//                }else if (bookChannel.getActive() == 1){
                    shopChannel.setDelFlag(DelFlagEnums.NORMAL.getCode());
//                }
                shopChannelList.add(shopChannel);
            }
        }
        shopChannelService.insertBatch(shopChannelList);
        result.setResult(shopChannelList);
        return result;
    }

}
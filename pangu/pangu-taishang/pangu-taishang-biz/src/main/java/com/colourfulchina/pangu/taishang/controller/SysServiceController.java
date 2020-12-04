package com.colourfulchina.pangu.taishang.controller;

import com.colourfulchina.bigan.api.feign.RemoteSysServiceService;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.SysService;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.service.SysServiceService;
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
@RequestMapping("/sysService")
@AllArgsConstructor
@Slf4j
@Api(tags = {"服务类型操作接口"})
public class SysServiceController {
    @Autowired
    private SysServiceService sysServiceService;
    private final RemoteSysServiceService remoteSysServiceService;

    /**
     * 服务类型列表查询接口
     * @return
     */
    @SysGodDoorLog("服务类型列表查询接口")
    @PostMapping("/selectSysServiceList")
    public CommonResultVo<List<SysService>> selectSysServiceList(){
        CommonResultVo<List<SysService>> result = new CommonResultVo<>();
        List<SysService> sysServiceList = sysServiceService.selectSysServiceList();
        result.setResult(sysServiceList);
        return result;
    }

    /**
     * 根据商户类型查询资源类型
     * @return
     */
    @SysGodDoorLog("根据商户类型查询资源类型")
    @PostMapping("/selectListByShopType")
    public CommonResultVo<List<SysService>> selectListByShopType(@RequestBody String shopType){
        CommonResultVo<List<SysService>> result = new CommonResultVo<>();
        try {
            List<SysService> sysServiceList = sysServiceService.selectListByShopType(shopType);
            result.setResult(sysServiceList);
        }catch (Exception e){
            log.error("根据商户类型查询资源类型失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 服务类型同步老系统接口
     * @return
     */
    @SysGodDoorLog("服务类型同步老系统接口")
    @ApiOperation("服务类型同步老系统接口")
    @PostMapping("/syncSysService")
    @Transactional(rollbackFor=Exception.class)
    public CommonResultVo<List<SysService>> syncSysService(){
        CommonResultVo<List<SysService>> result = new CommonResultVo<>();
        List<SysService> sysServiceList = Lists.newLinkedList();
        CommonResultVo<List<com.colourfulchina.bigan.api.entity.SysService>> remoteResult = remoteSysServiceService.selectSysServiceList();
        for (com.colourfulchina.bigan.api.entity.SysService remoteService : remoteResult.getResult()) {
            SysService sysService = new SysService();
            sysService.setCode(remoteService.getCode());
            sysService.setName(remoteService.getName());
            sysService.setCreateTime(new Date());

            sysService.setCreateUser(SecurityUtils.getLoginName());
            sysService.setUpdateTime(new Date());
            sysService.setUpdateUser(SecurityUtils.getLoginName());
            sysService.setDelFlag(DelFlagEnums.NORMAL.getCode());
            sysServiceList.add(sysService);
        }
        sysServiceService.insertBatch(sysServiceList);
        result.setResult(sysServiceList);
        return result;
    }
}
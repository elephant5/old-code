package com.colourfulchina.pangu.taishang.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.colourfulchina.bigan.api.feign.RemoteSysBlockService;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.SysBlock;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.service.SysBlockService;
import com.colourfulchina.pangu.taishang.service.SysBlockServiceService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sysBlock")
@AllArgsConstructor
@Slf4j
@Api("全局block操作接口")
public class SysBlockController {
    @Autowired
    private SysBlockService sysBlockService;
    @Autowired
    private SysBlockServiceService sysBlockServiceService;
    private final RemoteSysBlockService remoteSysBlockService;

    /**
     * 同步老系统系统全局block到新系统中
     * @return
     */
    @SysGodDoorLog("同步老系统系统全局block到新系统中")
    @ApiOperation("同步老系统系统全局block到新系统中")
    @PostMapping("/syncBlockList")
    @Transactional(rollbackFor=Exception.class)
    public CommonResultVo<List<SysBlock>> syncSysBlockList(){
        CommonResultVo<List<SysBlock>> result = new CommonResultVo<>();
        List<SysBlock> sysBlockList = Lists.newLinkedList();
        CommonResultVo<List<com.colourfulchina.bigan.api.entity.SysBlock>> remoteResult = remoteSysBlockService.selectSysBlockList();
        for (com.colourfulchina.bigan.api.entity.SysBlock remoteBlock : remoteResult.getResult()) {
            //系统全局block表插入
            SysBlock sysBlock = new SysBlock();
            if (StringUtils.isNotEmpty(remoteBlock.getMore())){
                JSONObject jsonObject = JSON.parseObject(remoteBlock.getMore());
                System.out.println(jsonObject);
                JSONArray jsonExceptProject = jsonObject.getJSONArray("exceptProject");
                JSONArray jsonExceptGroup = jsonObject.getJSONArray("exceptGroup");
                sysBlock.setExceptProject(StringUtils.join(jsonExceptProject,","));
                sysBlock.setExceptGroup(StringUtils.join(jsonExceptGroup,","));
            }
            sysBlock.setBlock(remoteBlock.getCode());
            sysBlock.setNotes(remoteBlock.getNotes());
            sysBlock.setCreateTime(new Date());

            sysBlock.setCreateUser(SecurityUtils.getLoginName());
            sysBlock.setDelFlag(DelFlagEnums.NORMAL.getCode());
            sysBlockService.insert(sysBlock);
            sysBlockList.add(sysBlock);

            //系统全局block与服务类型关系表插入
            if (StringUtils.isNotEmpty(remoteBlock.getTarget())){
                JSONArray jsonArray = JSONObject.parseArray(remoteBlock.getTarget());
                for (Object o : jsonArray) {
                    com.colourfulchina.pangu.taishang.api.entity.SysBlockService sysBlockService = new com.colourfulchina.pangu.taishang.api.entity.SysBlockService();
                    sysBlockService.setBlockId(sysBlock.getId());
                    sysBlockService.setServiceId(o.toString());
                    sysBlockService.setCreateTime(new Date());
                    sysBlockService.setCreateUser(SecurityUtils.getLoginName());
                    sysBlockServiceService.insert(sysBlockService);
                }
            }
        }
        result.setResult(sysBlockList);
        return result;
    }
}
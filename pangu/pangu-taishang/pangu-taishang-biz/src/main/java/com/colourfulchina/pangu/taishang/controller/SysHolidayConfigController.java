package com.colourfulchina.pangu.taishang.controller;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import com.colourfulchina.pangu.taishang.api.entity.SysHolidayConfig;
import com.colourfulchina.pangu.taishang.service.SysHolidayConfigService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/SysHolidayConfig")
@Api(tags = {"节假日操作接口"})
public class SysHolidayConfigController {
    @Resource
    private SysHolidayConfigService sysHolidayConfigService;

    /**
     *
     * @param sysHolidayConfig
     * @return
     */
    @SysGodDoorLog("查询节假日列表")
    @ApiOperation("查询节假日列表")
    @PostMapping("/list")
    public CommonResultVo<List<SysHolidayConfig>> list(@RequestBody SysHolidayConfig sysHolidayConfig){
        CommonResultVo<List<SysHolidayConfig>> resultVo=new CommonResultVo<>();
        try {
            Wrapper<SysHolidayConfig> configWrapper=new Wrapper<SysHolidayConfig>() {
                @Override
                public String getSqlSegment() {
                    if (sysHolidayConfig == null){
                        return null;
                    }
                    StringBuffer sql=new StringBuffer("where 1=1");
                    if (StringUtils.isNotBlank(sysHolidayConfig.getCode())){
                        sql.append(" and ").append("code = '").append(sysHolidayConfig.getCode()).append("'");
                    }
                    if (StringUtils.isNotBlank(sysHolidayConfig.getName())){
                        sql.append(" and ").append("name = '").append(sysHolidayConfig.getName()).append("'");
                    }
                    if (sysHolidayConfig.getEnable() != null){
                        sql.append(" and ").append("enable = ").append(sysHolidayConfig.getEnable());
                    }
                    return sql.toString();
                }
            };
            final List<SysHolidayConfig> sysHolidayConfigs = sysHolidayConfigService.selectList(configWrapper);
            resultVo.setResult(sysHolidayConfigs);
        }catch (Exception e){
            log.error("查询节假日列表失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }

        return resultVo;
    }

    /**
     * 节假日转block列表
     * @return
     */
    @SysGodDoorLog("节假日转block列表")
    @PostMapping("/holiday2BlockRule")
    @ApiOperation("节假日转block列表")
    public CommonResultVo<List<BlockRule>> holiday2BlockRule(){
        CommonResultVo<List<BlockRule>> result = new CommonResultVo<>();
        try {
            List<BlockRule> blockRuleList = Lists.newLinkedList();
            Wrapper wrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where enable = '1'";
                }
            };
            List<SysHolidayConfig> sysHolidayConfigList = sysHolidayConfigService.selectList(wrapper);
            for (SysHolidayConfig sysHolidayConfig : sysHolidayConfigList) {
                BlockRule blockRule = new BlockRule();
                blockRule.setClose(true);
                blockRule.setType(2);
                blockRule.setRule(sysHolidayConfig.getCode());
                blockRule.setNatural(sysHolidayConfig.getName());
                blockRuleList.add(blockRule);
            }
            result.setResult(blockRuleList);
        }catch (Exception e){
            log.error("节假日转block列表失败",e);
            result.setMsg(e.getMessage());
            result.setCode(200);
        }
        return result;
    }

    @SysGodDoorLog("根据code查询节假日")
    @ApiOperation("根据code查询节假日")
    @GetMapping("/get/{code}")
    public CommonResultVo<SysHolidayConfig> get(@PathVariable String code){
        CommonResultVo<SysHolidayConfig> resultVo=new CommonResultVo<>();
        try {
            final SysHolidayConfig sysHolidayConfig = sysHolidayConfigService.selectById(code);
            resultVo.setResult(sysHolidayConfig);
        }catch (Exception e){
            log.error("查询节假日失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("更新节假日")
    @ApiOperation("更新节假日")
    @PostMapping("/update")
    public CommonResultVo<SysHolidayConfig> update(@RequestBody SysHolidayConfig sysHolidayConfig){
        CommonResultVo<SysHolidayConfig> resultVo=new CommonResultVo<>();
        try {
            if (sysHolidayConfig == null){
                throw new Exception("参数不能为空");
            }
            if (StringUtils.isBlank(sysHolidayConfig.getCode())){
                throw new Exception("参数code不能为空");
            }
            final boolean updateById = sysHolidayConfigService.updateById(sysHolidayConfig);
            if (!updateById){
                throw new Exception("更新节假日失败");
            }
            resultVo.setResult(sysHolidayConfig);
        }catch (Exception e){
            log.error("更新节假日失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("添加节假日")
    @ApiOperation("添加节假日")
    @PostMapping("/add")
    public CommonResultVo<SysHolidayConfig> add(@RequestBody SysHolidayConfig sysHolidayConfig){
        CommonResultVo<SysHolidayConfig> resultVo=new CommonResultVo<>();
        try {
            final boolean insert = sysHolidayConfigService.insert(sysHolidayConfig);
            if (!insert){
                throw new Exception("添加节假日失败");
            }
            resultVo.setResult(sysHolidayConfig);
        }catch (Exception e){
            log.error("添加节假日失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

}

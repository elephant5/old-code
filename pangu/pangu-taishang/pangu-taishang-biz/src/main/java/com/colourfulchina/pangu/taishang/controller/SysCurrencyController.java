package com.colourfulchina.pangu.taishang.controller;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.bigan.api.feign.RemoteCurrencyService;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.SysCurrency;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.service.SysCurrencyService;
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
@RequestMapping("/sysCurrency")
@AllArgsConstructor
@Slf4j
@Api(value = "货币类型controller",tags = {"货币操作接口"})
public class SysCurrencyController {
    @Autowired
    private SysCurrencyService sysCurrencyService;
    private final RemoteCurrencyService remoteCurrencyService;

    /**
     * 货币类型列表接口
     * @return
     */
    @SysGodDoorLog("货币类型列表接口")
    @ApiOperation("货币类型列表接口")
    @PostMapping("/selectCurrencyList")
    public CommonResultVo<List<SysCurrency>> selectCurrencyList(){
        CommonResultVo<List<SysCurrency>> result = new CommonResultVo<>();
        try {
            Wrapper wrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0";
                }
            };
            List<SysCurrency> sysCurrencyList = sysCurrencyService.selectList(wrapper);
            result.setResult(sysCurrencyList);
        }catch (Exception e){
            log.error("货币类型查询失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 同步老系统币种到新系统中
     * @return
     */
    @SysGodDoorLog("同步老系统币种到新系统接口")
    @ApiOperation("同步老系统币种到新系统接口")
    @PostMapping("/syncCurrencyList")
    @Transactional(rollbackFor=Exception.class)
    public CommonResultVo<List<SysCurrency>> syncCurrencyList(){
        CommonResultVo<List<SysCurrency>> result = new CommonResultVo<>();
        List<SysCurrency> sysCurrencyList = Lists.newLinkedList();
        CommonResultVo<List<com.colourfulchina.bigan.api.entity.SysCurrency>> remoteResult = remoteCurrencyService.selectCurrencyList();
        for (com.colourfulchina.bigan.api.entity.SysCurrency remoteCurrency : remoteResult.getResult()) {
            SysCurrency sysCurrency = new SysCurrency();
            sysCurrency.setCode(remoteCurrency.getCode());
            sysCurrency.setName(remoteCurrency.getName());
            sysCurrency.setSymbol(remoteCurrency.getSymbol());
            sysCurrency.setCreateTime(new Date());

            sysCurrency.setCreateUser(SecurityUtils.getLoginName());
            sysCurrency.setUpdateTime(new Date());
            sysCurrency.setUpdateUser(SecurityUtils.getLoginName());
            sysCurrency.setDelFlag(DelFlagEnums.NORMAL.getCode());
            sysCurrencyList.add(sysCurrency);
        }
        sysCurrencyService.insertBatch(sysCurrencyList);
        result.setResult(sysCurrencyList);
        return result;
    }
}
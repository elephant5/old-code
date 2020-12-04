package com.colourfulchina.pangu.taishang.controller;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.SysSettleExpressItem;
import com.colourfulchina.pangu.taishang.service.SysSettleExpressItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/SysSettleExpressItem")
@Api(tags = {"结算公式项(结算公式所需变量)操作接口"})
public class SysSettleExpressItemController {
    @Resource
    private SysSettleExpressItemService sysSettleExpressItemService;

    /**
     * @param settleExpressItem
     * @return
     */
    @SysGodDoorLog("查询结算公式项列表")
    @ApiOperation("查询结算公式项列表")
    @PostMapping("/list")
    public CommonResultVo<List<SysSettleExpressItem>> list(@RequestBody SysSettleExpressItem settleExpressItem){
        CommonResultVo<List<SysSettleExpressItem>> resultVo=new CommonResultVo<>();
        try {
            Wrapper<SysSettleExpressItem> settleExpressWrapper=new Wrapper<SysSettleExpressItem>() {
                @Override
                public String getSqlSegment() {
                    if (settleExpressItem == null){
                        return null;
                    }
                    StringBuffer sql=new StringBuffer("where 1=1");
                    if (settleExpressItem.getType() != null){
                        sql.append(" and ").append("type in ( 0,").append(settleExpressItem.getType()).append(" )");
                    }
                    return sql.toString();
                }
            };
            final List<SysSettleExpressItem> settleExpressList = sysSettleExpressItemService.selectList(settleExpressWrapper);
            resultVo.setResult(settleExpressList);
        }catch (Exception e){
            log.error("查询结算公式项列表失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("根据id查询结算公式项")
    @ApiOperation("根据id查询结算公式项")
    @GetMapping("/get/{id}")
    @Cacheable(value = "SysSettleExpressItem",key = "'get_'+#id",unless = "#result == null")
    public CommonResultVo<SysSettleExpressItem> get(@PathVariable String id){
        CommonResultVo<SysSettleExpressItem> resultVo=new CommonResultVo<>();
        try {
            final SysSettleExpressItem settleExpressItem = sysSettleExpressItemService.selectById(id);
            resultVo.setResult(settleExpressItem);
        }catch (Exception e){
            log.error("查询结算公式项失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("更新结算公式项")
    @ApiOperation("更新结算公式项")
    @PostMapping("/update")
    @CacheEvict(value = {"SysSettleExpressItem","Shop","Goods","ProductGroup","ProductGroupProduct"},allEntries = true)
    public CommonResultVo<SysSettleExpressItem> update(@RequestBody SysSettleExpressItem settleExpressItem){
        CommonResultVo<SysSettleExpressItem> resultVo=new CommonResultVo<>();
        try {
            Assert.notNull(settleExpressItem,"参数不能为空");
            Assert.notNull(settleExpressItem.getId(),"参数id不能为空");
            settleExpressItem.setUpdateUser(SecurityUtils.getLoginName());
            settleExpressItem.setUpdateTime(new Date());
            final boolean updateById = sysSettleExpressItemService.updateById(settleExpressItem);
            if (!updateById){
                throw new Exception("更新结算公式项失败");
            }
            resultVo.setResult(settleExpressItem);
        }catch (Exception e){
            log.error("更新结算公式项失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("添加结算公式项")
    @ApiOperation("添加结算公式项")
    @PostMapping("/add")
    @CacheEvict(value = {"SysSettleExpressItem","Shop","Goods","ProductGroup","ProductGroupProduct"},allEntries = true)
    public CommonResultVo<SysSettleExpressItem> add(@RequestBody SysSettleExpressItem settleExpressItem){
        CommonResultVo<SysSettleExpressItem> resultVo=new CommonResultVo<>();
        try {
            Assert.notNull(settleExpressItem,"参数不能为空");
            settleExpressItem.setCreateUser(SecurityUtils.getLoginName());
            settleExpressItem.setCreateTime(new Date());
            settleExpressItem.setUpdateUser(SecurityUtils.getLoginName());
            settleExpressItem.setUpdateTime(new Date());
            final boolean insert = sysSettleExpressItemService.insert(settleExpressItem);
            if (!insert){
                throw new Exception("添加结算公式项失败");
            }
            resultVo.setResult(settleExpressItem);
        }catch (Exception e){
            log.error("添加结算公式项失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

}

package com.colourfulchina.pangu.taishang.controller;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.ShopType;
import com.colourfulchina.pangu.taishang.api.entity.ShopType;
import com.colourfulchina.pangu.taishang.service.ShopTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shopType")
@Slf4j
@Api(tags = {"商户类型操作接口"})
public class ShopTypeController {
    @Autowired
    private ShopTypeService shopTypeService;

    /**
     *
     * @param shopType
     * @return
     */
    @SysGodDoorLog("查询商户类型列表")
    @ApiOperation("查询商户类型列表")
    @PostMapping("/list")
    public CommonResultVo<List<ShopType>> list(@RequestBody ShopType shopType){
        CommonResultVo<List<ShopType>> resultVo=new CommonResultVo<>();
        try {
            Wrapper<ShopType> configWrapper=new Wrapper<ShopType>() {
                @Override
                public String getSqlSegment() {
                    if (shopType == null){
                        return null;
                    }
                    StringBuffer sql=new StringBuffer("where 1=1");
                    if (StringUtils.isNotBlank(shopType.getCode())){
                        sql.append(" and ").append("code = '").append(shopType.getCode()).append("'");
                    }
                    if (StringUtils.isNotBlank(shopType.getName())){
                        sql.append(" and ").append("name = '").append(shopType.getName()).append("'");
                    }
                    return sql.toString();
                }
            };
            final List<ShopType> sysHolidayConfigs = shopTypeService.selectList(configWrapper);
            resultVo.setResult(sysHolidayConfigs);
        }catch (Exception e){
            log.error("查询商户类型列表失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }

        return resultVo;
    }

    @SysGodDoorLog("根据code查询商户类型")
    @ApiOperation("根据code查询商户类型")
    @GetMapping("/get/{code}")
    @Cacheable(value = "ShopType",key = "'get_'+#code",unless = "#result == null")
    public CommonResultVo<ShopType> get(@PathVariable String code){
        CommonResultVo<ShopType> resultVo=new CommonResultVo<>();
        try {
            final ShopType shopType = shopTypeService.selectById(code);
            resultVo.setResult(shopType);
        }catch (Exception e){
            log.error("查询商户类型失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }
    @ApiOperation("更新商户类型")
    @PostMapping("/update")
    @CacheEvict(value = {"ShopTypeService","ShopType","Shop","Goods","ProductGroup","ProductGroupProduct"},allEntries = true)
    public CommonResultVo<ShopType> update(@RequestBody ShopType shopType){
        CommonResultVo<ShopType> resultVo=new CommonResultVo<>();
        try {
            if (shopType == null){
                throw new Exception("参数不能为空");
            }
            if (StringUtils.isBlank(shopType.getCode())){
                throw new Exception("参数code不能为空");
            }
            final boolean updateById = shopTypeService.updateById(shopType);
            if (!updateById){
                throw new Exception("更新商户类型失败");
            }
            resultVo.setResult(shopType);
        }catch (Exception e){
            log.error("更新商户类型失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }
    @SysGodDoorLog("添加商户类型")
    @ApiOperation("添加商户类型")
    @PostMapping("/add")
    @CacheEvict(value = {"ShopTypeService","ShopType","Shop","Goods","ProductGroup","ProductGroupProduct"},allEntries = true)
    public CommonResultVo<ShopType> add(@RequestBody ShopType shopType){
        CommonResultVo<ShopType> resultVo=new CommonResultVo<>();
        try {
            final boolean insert = shopTypeService.insert(shopType);
            if (!insert){
                throw new Exception("添加商户类型失败");
            }
            resultVo.setResult(shopType);
        }catch (Exception e){
            log.error("添加商户类型失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }
}
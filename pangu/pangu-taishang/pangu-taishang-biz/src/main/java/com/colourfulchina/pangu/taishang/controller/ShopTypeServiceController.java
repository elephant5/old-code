package com.colourfulchina.pangu.taishang.controller;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.ShopTypeService;
import com.colourfulchina.pangu.taishang.service.ShopTypeServiceService;
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
@RequestMapping("/shopTypeService")
@Slf4j
@Api(tags = {"商户资源类型关联操作接口"})
public class ShopTypeServiceController {
    @Autowired
    private ShopTypeServiceService shopTypeServiceService;

    /**
     *
     * @param shopTypeService
     * @return
     */
    @SysGodDoorLog("查询商户资源类型列表")
    @ApiOperation("查询商户资源类型列表")
    @PostMapping("/list")
    public CommonResultVo<List<ShopTypeService>> list(@RequestBody ShopTypeService shopTypeService){
        CommonResultVo<List<ShopTypeService>> resultVo=new CommonResultVo<>();
        try {
            Wrapper<ShopTypeService> configWrapper=new Wrapper<ShopTypeService>() {
                @Override
                public String getSqlSegment() {
                    if (shopTypeService == null){
                        return null;
                    }
                    StringBuffer sql=new StringBuffer("where 1=1");
                    if (StringUtils.isNotBlank(shopTypeService.getCode())){
                        sql.append(" and ").append("code = '").append(shopTypeService.getCode()).append("'");
                    }
                    if (StringUtils.isNotBlank(shopTypeService.getShopType())){
                        sql.append(" and ").append("shopType = '").append(shopTypeService.getShopType()).append("'");
                    }
                    if (StringUtils.isNotBlank(shopTypeService.getName())){
                        sql.append(" and ").append("name = '").append(shopTypeService.getName()).append("'");
                    }
                    return sql.toString();
                }
            };
            final List<ShopTypeService> sysHolidayConfigs = shopTypeServiceService.selectList(configWrapper);
            resultVo.setResult(sysHolidayConfigs);
        }catch (Exception e){
            log.error("查询商户类型服务列表失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }

        return resultVo;
    }

    @SysGodDoorLog("根据code查询商户资源类型")
    @ApiOperation("根据code查询商户资源类型")
    @GetMapping("/get/{code}")
    @Cacheable(value = "ShopTypeService",key = "'get_'+#code",unless = "#result == null")
    public CommonResultVo<ShopTypeService> get(@PathVariable String code){
        CommonResultVo<ShopTypeService> resultVo=new CommonResultVo<>();
        try {
            final ShopTypeService shopTypeService = shopTypeServiceService.selectById(code);
            resultVo.setResult(shopTypeService);
        }catch (Exception e){
            log.error("查询商户类型服务失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("根据code查询商户资源类型")
    @ApiOperation("根据code查询商户资源类型")
    @PostMapping("/getShopTypeService")
    @Cacheable(value = "ShopTypeService",key = "'getShopTypeService_'+#shopType",unless = "#result == null")
    public CommonResultVo<List<ShopTypeService>> getShopTypeService(@RequestBody String shopType){
        CommonResultVo<List<ShopTypeService>> resultVo=new CommonResultVo<>();
        try {
            Wrapper<ShopTypeService> wrapper = new Wrapper<ShopTypeService>() {
                @Override
                public String getSqlSegment() {
                    return "where shop_type = '"+shopType+"'";
                }
            };
            List<ShopTypeService> shopTypeService = shopTypeServiceService.selectList(wrapper);
            resultVo.setResult(shopTypeService);
        }catch (Exception e){
            log.error("查询商户类型服务失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("更新商户资源类型")
    @ApiOperation("更新商户资源类型")
    @PostMapping("/update")
    @CacheEvict(value = {"ShopTypeService","ShopType","Shop","Goods","ProductGroup","ProductGroupProduct"},allEntries = true)
    public CommonResultVo<ShopTypeService> update(@RequestBody ShopTypeService shopTypeService){
        CommonResultVo<ShopTypeService> resultVo=new CommonResultVo<>();
        try {
            if (shopTypeService == null){
                throw new Exception("参数不能为空");
            }
            if (StringUtils.isBlank(shopTypeService.getCode())){
                throw new Exception("参数code不能为空");
            }
            final boolean updateById = shopTypeServiceService.updateById(shopTypeService);
            if (!updateById){
                throw new Exception("更新商户类型服务失败");
            }
            resultVo.setResult(shopTypeService);
        }catch (Exception e){
            log.error("更新商户类型服务失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("添加商户资源类型")
    @ApiOperation("添加商户资源类型")
    @PostMapping("/add")
    @CacheEvict(value = {"ShopTypeService","ShopType","Shop","Goods","ProductGroup","ProductGroupProduct"},allEntries = true)
    public CommonResultVo<ShopTypeService> add(@RequestBody ShopTypeService shopTypeService){
        CommonResultVo<ShopTypeService> resultVo=new CommonResultVo<>();
        try {
            final boolean insert = shopTypeServiceService.insert(shopTypeService);
            if (!insert){
                throw new Exception("添加商户类型服务失败");
            }
            resultVo.setResult(shopTypeService);
        }catch (Exception e){
            log.error("添加商户类型服务失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }
}
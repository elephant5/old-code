package com.colourfulchina.pangu.taishang.controller;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemSettleExpress;
import com.colourfulchina.pangu.taishang.service.ShopItemSettleExpressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shopItemSettleExpress")
@Slf4j
@Api(tags = {"商品结算规则"})
public class ShopItemSettleExpressController {
    @Autowired
    private ShopItemSettleExpressService shopItemSettleExpressService;

    @SysGodDoorLog("查询规格结算规则详情列表")
    @ApiOperation("查询规格结算规则详情列表")
    @PostMapping("/list")
    public CommonResultVo<List<ShopItemSettleExpress>> list(@RequestBody ShopItemSettleExpress settleExpress){
        CommonResultVo<List<ShopItemSettleExpress>> resultVo=new CommonResultVo<>();
        try {
            Wrapper<ShopItemSettleExpress> priceRuleWrapper=new Wrapper<ShopItemSettleExpress>() {
                @Override
                public String getSqlSegment() {
                    if (settleExpress == null){
                        return null;
                    }
                    StringBuffer sql=new StringBuffer("where 1=1");
                    if (settleExpress.getShopId() != null){
                        sql.append(" and ").append("shop_id = ").append(settleExpress.getShopId());
                    }
                    if (settleExpress.getShopItemId() != null){
                        sql.append(" and ").append("shop_item_id = ").append(settleExpress.getShopItemId());
                    }
                    return sql.toString();
                }
            };
            final List<ShopItemSettleExpress> shopItemNetPriceRules = shopItemSettleExpressService.selectList(priceRuleWrapper);
            resultVo.setResult(shopItemNetPriceRules);
        }catch (Exception e){
            log.error("查询商户资源结算规则详情失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("添加规格结算规则详情")
    @ApiOperation("添加规格结算规则详情")
    @PostMapping("/add")
    @CacheEvict(value = {"ShopItemSettlePriceRule","ShopItemSettleExpress","ShopItemNetPriceRule","Shop","Goods","ProductGroup","ProductGroupProduct"},allEntries = true)
    public CommonResultVo<ShopItemSettleExpress> add(@RequestBody ShopItemSettleExpress settleExpress){
        CommonResultVo<ShopItemSettleExpress> resultVo=new CommonResultVo<>();
        try {
            if (settleExpress == null){
                throw new Exception("参数不能为空");
            }
            final boolean insert = shopItemSettleExpressService.insert(settleExpress);
            if (!insert){
                throw new Exception("添加商户资源结算规则详情列表失败");
            }
            resultVo.setResult(settleExpress);
        }catch (Exception e){
            log.error("添加商户资源结算规则详情列表失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("更新规格结算规则详情")
    @ApiOperation("更新规格结算规则详情")
    @PostMapping("/update")
    @CacheEvict(value = {"ShopItemSettlePriceRule","ShopItemSettleExpress","ShopItemNetPriceRule","Shop","Goods","ProductGroup","ProductGroupProduct"},allEntries = true)
    public CommonResultVo<ShopItemSettleExpress> update(@RequestBody ShopItemSettleExpress settleExpress){
        CommonResultVo<ShopItemSettleExpress> resultVo=new CommonResultVo<>();
        try {
            if (settleExpress == null){
                throw new Exception("参数不能为空");
            }
            if (settleExpress.getId() == null){
                throw new Exception("参数id不能为空");
            }
            final boolean update = shopItemSettleExpressService.updateById(settleExpress);
            if (!update){
                throw new Exception("更新商户资源结算规则详情失败");
            }
            resultVo.setResult(settleExpress);
        }catch (Exception e){
            log.error("更新商户资源结算规则详情失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("根据id规格结算规则详情")
    @ApiOperation("根据id规格结算规则详情")
    @PostMapping("/get/{id}")
    @Cacheable(value = "ShopItemSettleExpress",key = "'get_'+#id",unless = "#result == null")
    public CommonResultVo<ShopItemSettleExpress> get(@PathVariable Integer id){
        CommonResultVo<ShopItemSettleExpress> resultVo=new CommonResultVo<>();
        try {
            if (id == null){
                throw new Exception("参数不能为空");
            }
            final ShopItemSettleExpress settleExpress = shopItemSettleExpressService.selectById(id);
            resultVo.setResult(settleExpress);
        }catch (Exception e){
            log.error("查询商户资源结算规则详情失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }
}
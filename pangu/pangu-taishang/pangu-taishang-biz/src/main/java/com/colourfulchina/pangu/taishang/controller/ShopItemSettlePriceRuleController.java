package com.colourfulchina.pangu.taishang.controller;

import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemSettlePriceRule;
import com.colourfulchina.pangu.taishang.api.enums.FileTypeEnums;
import com.colourfulchina.pangu.taishang.api.vo.SettleExpressTranslateVo;
import com.colourfulchina.pangu.taishang.api.vo.req.ListSysFileReq;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopItemSettleRuleOptReq;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopItemSettleRulesListRes;
import com.colourfulchina.pangu.taishang.service.ProductService;
import com.colourfulchina.pangu.taishang.service.ShopItemSettlePriceRuleService;
import com.colourfulchina.pangu.taishang.service.SysFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/shopItemSettlePriceRule")
@Slf4j
@Api(tags = {"规格结算规则操作接口"})
public class ShopItemSettlePriceRuleController {
    @Autowired
    private ShopItemSettlePriceRuleService shopItemSettlePriceRuleService;
    @Autowired
    private ProductService productService;
    @Autowired
    SysFileService sysFileService;


    @SysGodDoorLog("查询规格结算规则列表")
    @ApiOperation("查询规格结算规则列表")
    @PostMapping("/list")
    public CommonResultVo<List<ShopItemSettleRulesListRes>> list(@RequestBody ShopItemSettlePriceRule priceInfo){
        CommonResultVo<List<ShopItemSettleRulesListRes>> resultVo=new CommonResultVo<>();
        try {
            Assert.notNull(priceInfo.getShopId(),"商户id不能为空");
            Assert.notNull(priceInfo.getShopItemId(),"商户规格id不能为空");
            List<ShopItemSettleRulesListRes> list = shopItemSettlePriceRuleService.selectSettleRuleList(priceInfo);
            if(!list.isEmpty()){
                for(ShopItemSettleRulesListRes rule : list){
                    ListSysFileReq sysFileReq = new ListSysFileReq();
                    sysFileReq.setObjId(rule.getId());
                    sysFileReq.setType(FileTypeEnums.SHOP_ITEM_SETTLE_CONTRACT.getCode());
                    List<SysFileDto> fileDtoList = sysFileService.listSysFileDto(sysFileReq);
                    if(!fileDtoList.isEmpty()){
                        rule.setFiles(fileDtoList);
                    }
                }
            }
            resultVo.setResult(list);
        }catch (Exception e){
            log.error("查询规格结算规则列表失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("添加规格结算规则详情")
    @ApiOperation("添加规格结算规则详情")
    @PostMapping("/add")
    @CacheEvict(value = {"ShopItemSettlePriceRule","ShopItemSettleExpress","ShopItemNetPriceRule","Shop","Goods","ProductGroup","ProductGroupProduct"},allEntries = true)
    public CommonResultVo<List<ShopItemSettlePriceRule>> add(@RequestBody ShopItemSettleRuleOptReq shopItemSettleRuleOptReq,HttpServletRequest request){
        CommonResultVo<List<ShopItemSettlePriceRule>> resultVo=new CommonResultVo<>();
        try {
            Assert.notNull(shopItemSettleRuleOptReq,"参数不能为空");
            Assert.notNull(shopItemSettleRuleOptReq.getShopId(),"商户id不能为空");
            Assert.notNull(shopItemSettleRuleOptReq.getShopItemId(),"规格id不能为空");
            List<ShopItemSettlePriceRule> list = shopItemSettlePriceRuleService.add(shopItemSettleRuleOptReq);
            //生成产品
            productService.generateProduct(shopItemSettleRuleOptReq.getShopItemId(),request);
            resultVo.setResult(list);
        }catch (Exception e){
            log.error("添加规格结算规则详情失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("结算公式中文翻译")
    @ApiOperation("结算公式中文翻译")
    @PostMapping("/translateSettleExpress")
    public CommonResultVo<SettleExpressTranslateVo> translateSettleExpress(@RequestBody SettleExpressTranslateVo settleExpressTranslateVo){
        CommonResultVo<SettleExpressTranslateVo> result = new CommonResultVo<>();
        try {
            settleExpressTranslateVo = shopItemSettlePriceRuleService.translateSettleExpress(settleExpressTranslateVo);
            result.setResult(settleExpressTranslateVo);
        }catch (Exception e){
            log.error("结算公式中文翻译失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("结算规则删除")
    @ApiOperation("结算规则删除")
    @PostMapping("delete")
    @CacheEvict(value = {"ShopItemSettlePriceRule","ShopItemSettleExpress","ShopItemNetPriceRule","Shop","Goods","ProductGroup","ProductGroupProduct"},allEntries = true)
    public CommonResultVo<ShopItemSettlePriceRule> delete(@RequestBody Integer id, HttpServletRequest request){
        CommonResultVo<ShopItemSettlePriceRule> result = new CommonResultVo<>();
        try {
            ShopItemSettlePriceRule shopItemSettlePriceRule = shopItemSettlePriceRuleService.delSettle(id);
            //生成产品
            productService.generateProduct(shopItemSettlePriceRule.getShopItemId(),request);
            result.setResult(shopItemSettlePriceRule);
        }catch (Exception e){
            log.error("结算规则删除失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("更新规格净价详情")
    @ApiOperation("更新规格净价详情")
    @PostMapping("/update")
    @CacheEvict(value = {"ShopItemSettlePriceRule","ShopItemSettleExpress","ShopItemNetPriceRule","Shop","Goods","ProductGroup","ProductGroupProduct"},allEntries = true)
    public CommonResultVo<ShopItemSettlePriceRule> update(@RequestBody ShopItemSettlePriceRule priceInfo){
        CommonResultVo<ShopItemSettlePriceRule> resultVo=new CommonResultVo<>();
        try {
            if (priceInfo == null){
                throw new Exception("参数不能为空");
            }
            if (priceInfo.getId() == null){
                throw new Exception("参数id不能为空");
            }
            final boolean update = shopItemSettlePriceRuleService.updateById(priceInfo);
            if (!update){
                throw new Exception("更新商户资源价格详情失败");
            }
            resultVo.setResult(priceInfo);
        }catch (Exception e){
            log.error("更新商户资源价格详情失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("根据id规格净价详情")
    @ApiOperation("根据id规格净价详情")
    @PostMapping("/get/{id}")
    @Cacheable(value = "ShopItemSettlePriceRule",key = "'get_'+#id",unless = "#result == null")
    public CommonResultVo<ShopItemSettlePriceRule> get(@PathVariable Integer id){
        CommonResultVo<ShopItemSettlePriceRule> resultVo=new CommonResultVo<>();
        try {
            if (id == null){
                throw new Exception("参数不能为空");
            }
            final ShopItemSettlePriceRule priceInfo = shopItemSettlePriceRuleService.selectById(id);
            resultVo.setResult(priceInfo);
        }catch (Exception e){
            log.error("查询商户资源价格详情失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }
}
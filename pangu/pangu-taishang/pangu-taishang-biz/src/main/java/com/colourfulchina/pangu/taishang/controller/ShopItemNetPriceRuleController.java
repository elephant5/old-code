package com.colourfulchina.pangu.taishang.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.entity.Product;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroupProduct;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemNetPriceRule;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.api.enums.FileTypeEnums;
import com.colourfulchina.pangu.taishang.api.vo.req.ListSysFileReq;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectPriceByTimeReqVo;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopItemNetPriceRuleOptReq;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopItemNetPriceRuleQueryReq;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopItemNetPriceRuleQueryRes;
import com.colourfulchina.pangu.taishang.service.ProductGroupProductService;
import com.colourfulchina.pangu.taishang.service.ProductService;
import com.colourfulchina.pangu.taishang.service.ShopItemNetPriceRuleService;
import com.colourfulchina.pangu.taishang.service.SysFileService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/shopItemNetPriceRule")
@Slf4j
@Api(tags = {"规格价格操作接口"})
public class ShopItemNetPriceRuleController {
    @Autowired
    private ShopItemNetPriceRuleService shopItemNetPriceRuleService;
    @Autowired
    SysFileService sysFileService;

    @Autowired
    private ProductGroupProductService productGroupProductService;

    @Autowired
    private ProductService productService;

    @SysGodDoorLog("查询规格净价规则列表")
    @ApiOperation("查询规格净价规则列表")
    @PostMapping("/list")
    public CommonResultVo<List<ShopItemNetPriceRuleQueryRes>> list(@RequestBody ShopItemNetPriceRuleQueryReq shopItemNetPriceRuleQueryReq){
        CommonResultVo<List<ShopItemNetPriceRuleQueryRes>> resultVo=new CommonResultVo<>();
        List<ShopItemNetPriceRuleQueryRes> list = Lists.newLinkedList();
        try {
            Wrapper priceRuleWrapper=new Wrapper() {
                @Override
                public String getSqlSegment() {
                    if (shopItemNetPriceRuleQueryReq == null){
                        return null;
                    }
                    StringBuffer sql=new StringBuffer("where del_flag=0");
                    if (shopItemNetPriceRuleQueryReq.getShopId() != null){
                        sql.append(" and ").append("shop_id = ").append(shopItemNetPriceRuleQueryReq.getShopId());
                    }
                    if (shopItemNetPriceRuleQueryReq.getShopItemId() != null){
                        sql.append(" and ").append("shop_item_id = ").append(shopItemNetPriceRuleQueryReq.getShopItemId());
                    }
                    return sql.toString();
                }
            };
            List<ShopItemNetPriceRule> shopItemNetPriceRules = shopItemNetPriceRuleService.selectList(priceRuleWrapper);
            for (ShopItemNetPriceRule shopItemNetPriceRule : shopItemNetPriceRules) {
                ShopItemNetPriceRuleQueryRes queryRes = new ShopItemNetPriceRuleQueryRes();
                BeanUtils.copyProperties(shopItemNetPriceRule,queryRes);
                list.add(queryRes);
            }
            //组装价格白话文
            if (!CollectionUtils.isEmpty(list)){
                for (ShopItemNetPriceRuleQueryRes res : list) {
                    List<String> weeks = Lists.newLinkedList();
                    StringBuffer timeString = new StringBuffer();
                    StringBuffer priceString = new StringBuffer();
                    timeString.append(DateUtil.format(res.getStartDate(),"yyyy-MM-dd"));
                    timeString.append("~");
                    timeString.append(DateUtil.format(res.getEndDate(),"yyyy-MM-dd"));
                    timeString.append("：");
                    if (res.getMonday()==1){
                        weeks.add("周一");
                    }
                    if (res.getTuesday()==1){
                        weeks.add("周二");
                    }
                    if (res.getWednesday()==1){
                        weeks.add("周三");
                    }
                    if (res.getThursday()==1){
                        weeks.add("周四");
                    }
                    if (res.getFriday()==1){
                        weeks.add("周五");
                    }
                    if (res.getSaturday()==1){
                        weeks.add("周六");
                    }
                    if (res.getSunday()==1){
                        weeks.add("周日");
                    }
                    if (!CollectionUtils.isEmpty(weeks)){
                        timeString.append(StringUtils.join(weeks,"、"));
                        res.setTimeString(timeString.toString());
                    }
                    priceString.append("【");
                    if (res.getNetPrice() != null){
                        priceString.append(res.getNetPrice().toString());
                    }
                    if (res.getServiceRate()!=null){
                        priceString.append("+");
                        priceString.append(res.getServiceRate().multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString());
                        priceString.append("%服务费");
                    }
                    if (res.getTaxRate() != null){
                        priceString.append("+");
                        priceString.append(res.getTaxRate().multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString());
                        priceString.append("%增值税");
                    }
                    priceString.append("】");
                    res.setPriceString(priceString.toString());
                    ListSysFileReq sysFileReq = new ListSysFileReq();
                    sysFileReq.setObjId(res.getId());
                    sysFileReq.setType(FileTypeEnums.SHOP_ITEM_PRICE_CONTRACT.getCode());
                    List<SysFileDto> fileDtoList = sysFileService.listSysFileDto(sysFileReq);
                    if(!fileDtoList.isEmpty()){
                        res.setFiles(fileDtoList);
                    }
                }
            }
            resultVo.setResult(list);
        }catch (Exception e){
            log.error("查询商户资源价格失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("添加/修改规格净价规则")
    @ApiOperation("添加/修改规格净价规则")
    @PostMapping("/add")
    @CacheEvict(value = {"ShopItemSettlePriceRule","ShopItemSettleExpress","ShopItemNetPriceRule","Shop","Goods","ProductGroup","ProductGroupProduct"},allEntries = true)
    public CommonResultVo<List<ShopItemNetPriceRule>> add(@RequestBody ShopItemNetPriceRuleOptReq shopItemNetPriceRuleReq, HttpServletRequest request){
        CommonResultVo<List<ShopItemNetPriceRule>> resultVo=new CommonResultVo<>();
        try {
            Assert.notNull(shopItemNetPriceRuleReq,"参数不能为空");
            List<ShopItemNetPriceRule> res = shopItemNetPriceRuleService.add(shopItemNetPriceRuleReq);
            productService.generateProduct(shopItemNetPriceRuleReq.getShopItemId(),request);
            resultVo.setResult(res);
        }catch (Exception e){
            log.error("添加/修改商户资源价格失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("更新规格净价规则")
    @ApiOperation("更新规格净价规则")
    @PostMapping("/update")
    @CacheEvict(value = {"ShopItemSettlePriceRule","ShopItemSettleExpress","ShopItemNetPriceRule","Shop","Goods","ProductGroup","ProductGroupProduct"},allEntries = true)
    public CommonResultVo<List<ShopItemNetPriceRule>> update(@RequestBody ShopItemNetPriceRuleOptReq shopItemNetPriceRuleReq,HttpServletRequest request){
        CommonResultVo<List<ShopItemNetPriceRule>> resultVo=new CommonResultVo<>();
        try {
            Assert.notNull(shopItemNetPriceRuleReq,"参数不能为空");
            Assert.notNull(shopItemNetPriceRuleReq.getId(),"参数id不能为空");
            List<ShopItemNetPriceRule> res = shopItemNetPriceRuleService.upd(shopItemNetPriceRuleReq);
            productService.generateProduct(shopItemNetPriceRuleReq.getShopItemId(),request);
            resultVo.setResult(res);
        }catch (Exception e){
            log.error("更新商户资源价格失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("根据id查询规格净价规则")
    @ApiOperation("根据id查询规格净价规则")
    @PostMapping("/get/{id}")
    @Cacheable(value = "ShopItemNetPriceRule",key = "'get_'+#id",unless = "#result == null")
    public CommonResultVo<ShopItemNetPriceRule> get(@PathVariable Integer id){
        CommonResultVo<ShopItemNetPriceRule> resultVo=new CommonResultVo<>();
        try {
            if (id == null){
                throw new Exception("参数不能为空");
            }
            final ShopItemNetPriceRule priceRule = shopItemNetPriceRuleService.selectById(id);
            resultVo.setResult(priceRule);
        }catch (Exception e){
            log.error("查询商户资源价格失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("删除规格价格")
    @ApiOperation("删除规格价格")
    @PostMapping("/delete")
    @CacheEvict(value = {"ShopItemSettlePriceRule","ShopItemSettleExpress","ShopItemNetPriceRule","Shop","Goods","ProductGroup","ProductGroupProduct"},allEntries = true)
    public CommonResultVo<ShopItemNetPriceRule> delete(@RequestBody Integer id, HttpServletRequest request){
        CommonResultVo<ShopItemNetPriceRule> result = new CommonResultVo<>();
        try {
            Assert.notNull(id,"id不能为空");
            ShopItemNetPriceRule shopItemNetPriceRule = shopItemNetPriceRuleService.selectById(id);
            shopItemNetPriceRule.setDelFlag(DelFlagEnums.DELETE.getCode());
            shopItemNetPriceRuleService.updateById(shopItemNetPriceRule);
            productService.generateProduct(shopItemNetPriceRule.getShopItemId(),request);
        }catch (Exception e){
            log.error("删除商户资源价格失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("查询预约时间对应的净价")
    @ApiOperation("查询预约时间对应的净价")
    @PostMapping("/getPriceByTime")
    public CommonResultVo<ShopItemNetPriceRule> getPriceByTime(@RequestBody SelectPriceByTimeReqVo reqVo){
        CommonResultVo<ShopItemNetPriceRule> result = new CommonResultVo<>();
        try {
            Assert.notNull(reqVo, "参数不能为空");
            //获取产品组下的产品信息
            ProductGroupProduct productGroupProduct = productGroupProductService.selectById(reqVo.getProductGroupProductId());
            //获取产品信息
            Product product = productService.selectById(productGroupProduct.getProductId());
            //查询规格价格
            Wrapper priceWrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0 and shop_item_id =" + product.getShopItemId();
                }
            };
            List<ShopItemNetPriceRule> priceRules = shopItemNetPriceRuleService.selectList(priceWrapper);
            ShopItemNetPriceRule shopItemNetPriceRule = shopItemNetPriceRuleService.foundPriceByTime(reqVo.getBookDate(), priceRules);
            result.setCode(100);
            result.setMsg("成功");
            result.setResult(shopItemNetPriceRule);
        }catch (Exception e){
            log.error("查询预约时间对应的净价失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}
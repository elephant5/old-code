package com.colourfulchina.pangu.taishang.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.dto.GiftPriceDto;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.enums.CarryRuleEnums;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopItemSettlePriceHisReq;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopItemSettlePriceReq;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopItemSettlePriceHisRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopItemSettlePriceRes;
import com.colourfulchina.pangu.taishang.service.*;
import com.colourfulchina.pangu.taishang.utils.DateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/ShopItemSettlePrice")
@Slf4j
@Api(tags = {"规格历史结算价操作接口"})
public class ShopItemSettlePriceController {
    @Autowired
    private ShopItemSettlePriceService shopItemSettlePriceService;
    @Autowired
    private ShopProtocolService shopProtocolService;
    @Autowired
    private ShopItemService shopItemService;
    @Autowired
    private BlockRuleService blockRuleService;
    @Autowired
    private ShopItemSettlePriceRuleService shopItemSettlePriceRuleService;
    @Autowired
    private ShopItemNetPriceRuleService shopItemNetPriceRuleService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private ShopItemSettleExpressService shopItemSettleExpressService;

    @SysGodDoorLog("查询规格历史结算价列表")
    @ApiOperation("查询规格历史结算价列表")
    @PostMapping("/list")
    public CommonResultVo<List<ShopItemSettlePrice>> list(@RequestBody ShopItemSettlePrice settlePrice){
        CommonResultVo<List<ShopItemSettlePrice>> resultVo=new CommonResultVo<>();
        try {
            Wrapper<ShopItemSettlePrice> priceWrapper=new Wrapper<ShopItemSettlePrice>() {
                @Override
                public String getSqlSegment() {
                    if (settlePrice == null){
                        return null;
                    }
                    StringBuffer sql=new StringBuffer("where 1=1");
                    if (settlePrice.getShopId() != null){
                        sql.append(" and ").append("shop_id = ").append(settlePrice.getShopId());
                    }
                    if (settlePrice.getShopItemId() != null){
                        sql.append(" and ").append("shop_item_id = ").append(settlePrice.getShopItemId());
                    }
                    if (settlePrice.getBookDate() != null){
                        sql.append(" and ").append("book_date = str_to_date('").append(DateUtil.format(settlePrice.getBookDate(),"yyyy-MM-dd")).append("','%Y-%m-%d')");
                    }
                    return sql.toString();
                }
            };
            priceWrapper.orderBy("book_date");
            final List<ShopItemSettlePrice> shopItemBlockRules = shopItemSettlePriceService.selectList(priceWrapper);
            resultVo.setResult(shopItemBlockRules);
        }catch (Exception e){
            log.error("查询商户规格历史结算价列表失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }

        return resultVo;
    }

    @SysGodDoorLog("查询规格历史结算价列表")
    @ApiOperation("查询规格历史结算价列表")
    @PostMapping("/his")
    public CommonResultVo<List<ShopItemSettlePriceHisRes>> his(@RequestBody ShopItemSettlePriceHisReq settlePrice){
        CommonResultVo<List<ShopItemSettlePriceHisRes>> resultVo=new CommonResultVo<>();
        try {
            final List<ShopItemSettlePriceHisRes> his = shopItemSettlePriceService.his(settlePrice);
            resultVo.setResult(his);
        }catch (Exception e){
            log.error("查询商户规格历史结算价列表失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }

        return resultVo;
    }

    @SysGodDoorLog("添加规格历史结算价")
    @ApiOperation("添加规格历史结算价")
    @PostMapping("/add")
    public CommonResultVo<ShopItemSettlePrice> add(@RequestBody ShopItemSettlePrice settlePrice){
        CommonResultVo<ShopItemSettlePrice> resultVo=new CommonResultVo<>();
        try {
            if (settlePrice == null){
                throw new Exception("参数不能为空");
            }
            final boolean insert = shopItemSettlePriceService.insert(settlePrice);
            if (!insert){
                throw new Exception("添加商户规格历史结算价失败");
            }
            resultVo.setResult(settlePrice);
        }catch (Exception e){
            log.error("添加商户规格历史结算价失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("更新规格历史结算价")
    @ApiOperation("更新规格历史结算价")
    @PostMapping("/update")
    public CommonResultVo<ShopItemSettlePrice> update(@RequestBody ShopItemSettlePrice settlePrice){
        CommonResultVo<ShopItemSettlePrice> resultVo=new CommonResultVo<>();
        try {
            if (settlePrice == null){
                throw new Exception("参数不能为空");
            }
            if (settlePrice.getId() == null){
                throw new Exception("参数id不能为空");
            }
            final boolean update = shopItemSettlePriceService.updateById(settlePrice);
            if (!update){
                throw new Exception("更新商户规格历史结算价失败");
            }
            resultVo.setResult(settlePrice);
        }catch (Exception e){
            log.error("更新商户规格历史结算价失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("根据id查询规格历史结算价")
    @ApiOperation("根据id查询规格历史结算价")
    @GetMapping("/get/{id}")
    public CommonResultVo<ShopItemSettlePrice> get(@PathVariable Integer id){
        CommonResultVo<ShopItemSettlePrice> resultVo=new CommonResultVo<>();
        try {
            if (id == null){
                throw new Exception("参数不能为空");
            }
            final ShopItemSettlePrice shopItemSettlePrice = shopItemSettlePriceService.selectById(id);
            resultVo.setResult(shopItemSettlePrice);
        }catch (Exception e){
            log.error("查询商户规格历史结算价失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("查询结算价日历")
    @ApiOperation("查询结算价日历")
    @PostMapping("/listShopItemSettlePrice")
    public CommonResultVo<List<ShopItemSettlePriceRes>> listShopItemSettlePrice(@RequestBody ShopItemSettlePriceReq settlePriceReq){
        CommonResultVo<List<ShopItemSettlePriceRes>> result=new CommonResultVo<>();
        List<ShopItemSettlePriceRes> list= Lists.newArrayList();
        try {
            Assert.notNull(settlePriceReq,"参数不能为空");
            Assert.notNull(settlePriceReq.getShopId(),"参数shopId不能为空");
            Assert.notNull(settlePriceReq.getShopItemId(),"参数shopItemId不能为空");
            Assert.notNull(settlePriceReq.getYear(),"参数year不能为空");
            Assert.notNull(settlePriceReq.getMonth(),"参数month不能为空");
            //找到时间列表
            Date startTime = DateUtil.parse(settlePriceReq.getYear()+"/"+settlePriceReq.getMonth()+"/1","yyyy/MM/dd");
            String nextMinTime = settlePriceReq.getMonth().compareTo(12) == 0 ? (settlePriceReq.getYear()+1) +"/1/1" : settlePriceReq.getYear() +"/"+(settlePriceReq.getMonth()+1)+"/1";
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtil.parse(nextMinTime,"yyyy/MM/dd"));
            calendar.add(Calendar.DAY_OF_MONTH,-1);
            Date endTime = calendar.getTime();
            List<Date> dateList = DateUtils.containDateList(startTime,endTime,null);
            //找到时间列表中每天的block状态
            List<String> allBlockList = Lists.newLinkedList();
            String blockRule = null;
            ShopProtocol shopProtocol = shopProtocolService.selectById(settlePriceReq.getShopId());
            ShopItem shopItem = shopItemService.selectById(settlePriceReq.getShopItemId());
            if (StringUtils.isNotBlank(shopProtocol.getBlockRule())){
                allBlockList.add(shopProtocol.getBlockRule());
            }
            if (StringUtils.isNotBlank(shopItem.getBlockRule())){
                allBlockList.add(shopItem.getBlockRule());
            }
            if (!CollectionUtils.isEmpty(allBlockList)){
                blockRule = StringUtils.join(allBlockList,", ");
            }
            //列表中开放的日期
            List<Date> openList = blockRuleService.generateBookDate(startTime,endTime,blockRule);
            for (Date date : dateList) {
                boolean flag = true;
                ShopItemSettlePriceRes shopItemSettlePriceRes = new ShopItemSettlePriceRes();
                shopItemSettlePriceRes.setCalendarDate(date);
                for (Date open : openList) {
                    if (date.compareTo(open) == 0){
                        flag = false;
                        break;
                    }
                }
                shopItemSettlePriceRes.setIsBlock(flag);
                list.add(shopItemSettlePriceRes);
            }
            //列表中权益类型及结算价
            //价格信息
            Wrapper priceWrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0 and shop_item_id ="+settlePriceReq.getShopItemId();
                }
            };
            List<ShopItemNetPriceRule> priceList = shopItemNetPriceRuleService.selectList(priceWrapper);
            //结算规则信息
            Wrapper giftWrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0 and shop_item_id ="+settlePriceReq.getShopItemId();
                }
            };
            List<ShopItemSettlePriceRule> giftList = shopItemSettlePriceRuleService.selectList(giftWrapper);
            HashSet<String> giftSet = Sets.newHashSet();
            for (ShopItemSettlePriceRule rule : giftList) {
                if (StringUtils.isNotBlank(rule.getGift())){
                    giftSet.add(rule.getGift());
                }else {
                    giftSet.add("null");
                }
            }
            Map<String,List<ShopItemSettlePriceRule>> ruleMap = Maps.newHashMap();
            if (!CollectionUtils.isEmpty(giftSet)){
                for (String gift : giftSet) {
                    Wrapper giftSetWrapper = new Wrapper() {
                        @Override
                        public String getSqlSegment() {
                            StringBuffer stringBuffer = new StringBuffer();
                            stringBuffer.append("where del_flag = 0 and shop_item_id ="+settlePriceReq.getShopItemId());
                            if ("null".equals(gift)){
                                stringBuffer.append(" and (gift IS NULL OR gift = '')");
                            }else {
                                stringBuffer.append(" and gift ='"+gift+"'");
                            }
                            return stringBuffer.toString();
                        }
                    };
                    List<ShopItemSettlePriceRule> priceRules = shopItemSettlePriceRuleService.selectList(giftSetWrapper);
                    ruleMap.put(gift,priceRules);
                }
            }
            log.debug("listShopItemSettlePrice priceList:{}", JSON.toJSONString(priceList));
            log.debug("listShopItemSettlePrice ruleMap:{}", JSON.toJSONString(ruleMap));
            for (ShopItemSettlePriceRes priceRes : list) {
                List<GiftPriceDto> giftPrices = Lists.newLinkedList();
                for (String gift : giftSet) {
                    GiftPriceDto giftPrice = new GiftPriceDto();
                    if ("null".equals(gift)){
                        giftPrice.setGift(shopItem.getServiceType());
                    }else {
                        giftPrice.setGift(gift);
                    }
                    ShopItemNetPriceRule price = shopItemNetPriceRuleService.foundPriceByTime(priceRes.getCalendarDate(),priceList);
                    ShopItemSettlePriceRule rule = productGroupService.foundRuleByTime(priceRes.getCalendarDate(),ruleMap.get(gift));
                    log.debug("listShopItemSettlePrice date:{} price:{}", DateUtil.format(priceRes.getCalendarDate(),"yyyy-MM-dd"),JSON.toJSONString(price));
                    log.debug("listShopItemSettlePrice date:{} rule:{}:", DateUtil.format(priceRes.getCalendarDate(),"yyyy-MM-dd"),JSON.toJSONString(rule));
                    if (rule != null){
                        if (rule.getSettleExpressId() != null){
                            ShopItemSettleExpress shopItemSettleExpress = shopItemSettleExpressService.selectById(rule.getSettleExpressId());
                            BigDecimal calPrice = shopItemSettlePriceRuleService.calProtocolPrice(price,shopItemSettleExpress);
                            if (calPrice != null){
                                Integer decimal = 2;
                                if (shopProtocol.getDecimal() != null){
                                    decimal = shopProtocol.getDecimal();
                                }
                                if (CarryRuleEnums.ROUND_HALF_UP.getCode().equals(shopProtocol.getRoundup())){
                                    calPrice = calPrice.setScale(decimal,BigDecimal.ROUND_HALF_UP);
                                }else if (CarryRuleEnums.ROUND_UP.getCode().equals(shopProtocol.getRoundup())){
                                    calPrice = calPrice.setScale(decimal,BigDecimal.ROUND_UP);
                                }else if (CarryRuleEnums.ROUND_DOWN.getCode().equals(shopProtocol.getRoundup())){
                                    calPrice = calPrice.setScale(decimal,BigDecimal.ROUND_DOWN);
                                }else {
                                    calPrice = calPrice.setScale(decimal,BigDecimal.ROUND_HALF_UP);
                                }
                            }
                            giftPrice.setSettlePrice(calPrice == null ? null : calPrice.setScale(2,BigDecimal.ROUND_HALF_UP));
                        }else {
                            giftPrice.setSettlePrice(new BigDecimal(0).setScale(2));
                        }
                    }
                    giftPrices.add(giftPrice);
                }
                priceRes.setPrices(giftPrices);
            }
            log.debug("日历计算{}",JSONObject.toJSONString(list));
            result.setResult(list);
        }catch (Exception e){
            log.error("查询结算价日历失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}
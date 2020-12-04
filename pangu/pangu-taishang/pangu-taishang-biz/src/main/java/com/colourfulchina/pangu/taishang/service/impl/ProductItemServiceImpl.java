package com.colourfulchina.pangu.taishang.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.mapper.ProductItemMapper;
import com.colourfulchina.pangu.taishang.mapper.ProductMapper;
import com.colourfulchina.pangu.taishang.service.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class ProductItemServiceImpl extends ServiceImpl<ProductItemMapper,ProductItem> implements ProductItemService {
    @Autowired
    private ProductItemMapper productItemMapper;
//    @Autowired
//    private ProductService productService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ShopItemService shopItemService;
    @Autowired
    private ShopItemNetPriceRuleService shopItemNetPriceRuleService;
    @Autowired
    private ShopItemSettlePriceRuleService shopItemSettlePriceRuleService;
    @Autowired
    private ShopItemSettleExpressService shopItemSettleExpressService;

    /**
     * 生成产品子项
     * @param productId
     * @return
     * @throws Exception
     */
    @Override
    public List<ProductItem> generateProductItem(Integer productId,HttpServletRequest request) throws Exception {
        log.info("生成产品子项，产品id为{}",productId);
        List<ProductItem> result = Lists.newLinkedList();
        //获取产品信息
        Product product = productMapper.selectById(productId);
        //获取规格信息
        ShopItem shopItem = shopItemService.selectById(product.getShopItemId());
        //获取价格信息
        Wrapper<ShopItemNetPriceRule> wrapperPrice = new Wrapper<ShopItemNetPriceRule>() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and shop_item_id ="+shopItem.getId();
            }
        };
        List<ShopItemNetPriceRule> priceList = shopItemNetPriceRuleService.selectList(wrapperPrice);
        //获取结算规则信息
        Wrapper<ShopItemSettlePriceRule> ruleWrapper = new Wrapper<ShopItemSettlePriceRule>() {
            @Override
            public String getSqlSegment() {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("where del_flag = 0 and shop_item_id ="+shopItem.getId()+"");
                if (StringUtils.isNotBlank(product.getGift())){
                    stringBuffer.append(" and gift ='"+product.getGift()+"'");
                }else {
                    stringBuffer.append(" and (gift IS NULL OR gift = '')");
                }
                return stringBuffer.toString();
            }
        };
        List<ShopItemSettlePriceRule> ruleList = shopItemSettlePriceRuleService.selectList(ruleWrapper);
        //删除旧的产品子项
        Wrapper<ProductItem> wrapperDelProductItem = new Wrapper<ProductItem>() {
            @Override
            public String getSqlSegment() {
                return "where product_id ="+productId;
            }
        };
        productItemMapper.delete(wrapperDelProductItem);
        //组装适用时间，成本,生成产品子项
        if (!CollectionUtils.isEmpty(ruleList)){
            //判断该产品是否生成成本子项
            boolean haveItem = false;
            for (ShopItemSettlePriceRule rule : ruleList) {
                if (!CollectionUtils.isEmpty(priceList)){
                    for (ShopItemNetPriceRule price : priceList) {
                        Date ruleStartDate = rule.getStartDate();
                        Date ruleEndDate = rule.getEndDate();
                        Date priceStartDate = price.getStartDate();
                        Date priceEndDate = price.getEndDate();
                        long ruleStart = ruleStartDate.getTime();
                        long ruleEnd = ruleEndDate.getTime();
                        long priceStart = priceStartDate.getTime();
                        long priceEnd = priceEndDate.getTime();
                        //获取年月日时间段
                        String ymd = null;
                        Date startTime = null;
                        Date endTime = null;
                        //价格的起始时间在规则起始时间之间,价格结束时间在规则起始时间之外
                        if (priceStart >= ruleStart && priceStart <= ruleEnd && priceEnd > ruleEnd){
                            if (priceStart == ruleEnd){
                                ymd = DateUtil.format(ruleEndDate,"yyyy-MM-dd");
                                startTime = ruleEndDate;
                                endTime = ruleEndDate;
                            }else {
                                ymd = DateUtil.format(priceStartDate,"yyyy-MM-dd")+"~"+DateUtil.format(ruleEndDate,"yyyy-MM-dd");
                                startTime = priceStartDate;
                                endTime = ruleEndDate;
                            }
                        }
                        //价格的结束时间在规则起始时间之间,价格起始时间在规则起始时间之外
                        else if (priceEnd >= ruleStart && priceEnd <= ruleEnd && priceStart < ruleStart){
                            if (priceEnd == ruleStart){
                                ymd = DateUtil.format(ruleStartDate,"yyyy-MM-dd");
                                startTime = ruleStartDate;
                                endTime = ruleStartDate;
                            }else {
                                ymd = DateUtil.format(ruleStartDate,"yyyy-MM-dd")+"~"+DateUtil.format(priceEndDate,"yyyy-MM-dd");
                                startTime = ruleStartDate;
                                endTime = priceEndDate;
                            }
                        }
                        //价格起始时间和结束时间都在结算规则内
                        else if (priceStart >= ruleStart && priceEnd <= ruleEnd){
                            ymd = DateUtil.format(priceStartDate,"yyyy-MM-dd")+"~"+DateUtil.format(priceEndDate,"yyyy-MM-dd");
                            startTime = priceStartDate;
                            endTime = priceEndDate;
                        }
                        //价格的起始时间小于结算规则的起始时间，价格的终止时间大于结算规则的终止时间
                        else if (priceStart < ruleStart && priceEnd > ruleEnd){
                            ymd = DateUtil.format(ruleStartDate,"yyyy-MM-dd")+"~"+DateUtil.format(ruleEndDate,"yyyy-MM-dd");
                            startTime = ruleStartDate;
                            endTime = ruleEndDate;
                        }
                        //查询此时间段内相同的星期
                        List<String> weeks = queryBothWeek(rule,price);
                        //计算此价格和结算规则组合的成本价
                        BigDecimal cost;
                        if (rule.getSettleExpressId() != null){
                            ShopItemSettleExpress shopItemSettleExpress = shopItemSettleExpressService.selectById(rule.getSettleExpressId());
                            cost = shopItemSettlePriceRuleService.calProtocolPrice(price,shopItemSettleExpress);
                        }else {
                            cost = new BigDecimal(0);
                        }
                        //存在相同的星期,并且时间段存在交集
                        if (StringUtils.isNotBlank(ymd) && !CollectionUtils.isEmpty(weeks)){
                            ProductItem productItem = new ProductItem();
                            productItem.setProductId(productId);
                            productItem.setName(shopItem.getName());
                            productItem.setService(shopItem.getServiceType());
                            productItem.setGift(product.getGift());
                            productItem.setApplyTime(ymd+":"+StringUtils.join(weeks,"、"));
                            productItem.setStartDate(startTime);
                            productItem.setEndDate(endTime);
                            if (weeks.contains("星期一")){
                                productItem.setMonday(1);
                            }
                            if (weeks.contains("星期二")){
                                productItem.setTuesday(1);
                            }
                            if (weeks.contains("星期三")){
                                productItem.setWednesday(1);
                            }
                            if (weeks.contains("星期四")){
                                productItem.setThursday(1);
                            }
                            if (weeks.contains("星期五")){
                                productItem.setFriday(1);
                            }
                            if (weeks.contains("星期六")){
                                productItem.setSaturday(1);
                            }
                            if (weeks.contains("星期日")){
                                productItem.setSunday(1);
                            }
                            productItem.setCost(cost);
                            productItem.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            productItem.setCreateUser(SecurityUtils.getLoginName(request));
                            productItemMapper.insert(productItem);
                            result.add(productItem);
                            haveItem = true;
                        }
                    }
                }else {
                    Date ruleStartDate = rule.getStartDate();
                    Date ruleEndDate = rule.getEndDate();
                    //获取年月日时间段
                    String ymd = null;
                    Date startTime = null;
                    Date endTime = null;
                    ymd = DateUtil.format(ruleStartDate,"yyyy-MM-dd")+"~"+DateUtil.format(ruleEndDate,"yyyy-MM-dd");
                    startTime = ruleStartDate;
                    endTime = ruleEndDate;
                    //计算此价格和结算规则组合的成本价
                    BigDecimal cost;
                    if (rule.getSettleExpressId() != null){
                        ShopItemSettleExpress shopItemSettleExpress = shopItemSettleExpressService.selectById(rule.getSettleExpressId());
                        cost = shopItemSettlePriceRuleService.calProtocolPrice(null,shopItemSettleExpress);
                    }else {
                        cost = new BigDecimal(0);
                    }
                    if (cost != null){
                        //查询此规则的星期
                        List<String> weeks = queryRuleWeek(rule);
                        ProductItem productItem = new ProductItem();
                        productItem.setProductId(productId);
                        productItem.setName(shopItem.getName());
                        productItem.setService(shopItem.getServiceType());
                        productItem.setGift(product.getGift());
                        productItem.setApplyTime(ymd+":"+StringUtils.join(weeks,"、"));
                        productItem.setStartDate(startTime);
                        productItem.setEndDate(endTime);
                        productItem.setMonday(rule.getMonday());
                        productItem.setTuesday(rule.getTuesday());
                        productItem.setWednesday(rule.getWednesday());
                        productItem.setThursday(rule.getThursday());
                        productItem.setFriday(rule.getFriday());
                        productItem.setSaturday(rule.getSaturday());
                        productItem.setSunday(rule.getSunday());
                        productItem.setCost(cost);
                        productItem.setDelFlag(DelFlagEnums.NORMAL.getCode());
                        productItem.setCreateUser(SecurityUtils.getLoginName(request));
                        productItemMapper.insert(productItem);
                        result.add(productItem);
                        haveItem = true;
                    }
                }
            }
            if (!haveItem){
                ProductItem productItem = new ProductItem();
                productItem.setProductId(productId);
                productItem.setName(shopItem.getName());
                productItem.setService(shopItem.getServiceType());
                productItem.setGift(product.getGift());
                productItem.setApplyTime("暂无");
                productItem.setDelFlag(DelFlagEnums.NORMAL.getCode());
                productItem.setCreateUser(SecurityUtils.getLoginName(request));
                productItemMapper.insert(productItem);
                result.add(productItem);
            }
        }else {
            ProductItem productItem = new ProductItem();
            productItem.setProductId(productId);
            productItem.setName(shopItem.getName());
            productItem.setService(shopItem.getServiceType());
            productItem.setGift(product.getGift());
            productItem.setApplyTime("暂无");
            productItem.setDelFlag(DelFlagEnums.NORMAL.getCode());
            productItem.setCreateUser(SecurityUtils.getLoginName(request));
            productItemMapper.insert(productItem);
            result.add(productItem);
        }
        return result;
    }

    /**
     * 合并产品子项，生成block
     * @param productItemList
     * @return
     */
    @Override
    public List<ProductItem> margeItem(List<ProductItem> productItemList) throws Exception{
        List<ProductItem> resList = Lists.newLinkedList();
        if (!CollectionUtils.isEmpty(productItemList)){
            //block的范围
            List<ProductItem> blockItem = Lists.newLinkedList();
            //最小的开始时间
            Date minDate = getMinDate(productItemList);
            //最小开始时间-1年
            Date minDownYearDate = null;
            //最大的结束时间
            Date maxDate = getMaxDate(productItemList);
            //最大结束时间+1年
            Date maxUpYearDate = null;
            for (ProductItem tempItem : productItemList) {
                Calendar calendar = Calendar.getInstance();
                if (tempItem.getCost() != null){
                    Date tempStartDate = DateUtil.parse(DateUtil.format(tempItem.getStartDate(),"yyyy-MM-dd"),"yyyy-MM-dd");
                    Date tempEndDate = DateUtil.parse(DateUtil.format(tempItem.getEndDate(),"yyyy-MM-dd"),"yyyy-MM-dd");
                    if (CollectionUtils.isEmpty(blockItem)){
                        //无限小
                        ProductItem leftItem = new ProductItem();
                        //无限大
                        ProductItem rightItem = new ProductItem();
                        //生成无限小到开始时间的blockItem
                        Date startDate = tempItem.getStartDate();
                        //默认无限小为列表中最小时间-1年
                        calendar.setTime(minDate);
                        calendar.add(Calendar.YEAR,-1);
                        minDownYearDate = calendar.getTime();
                        leftItem.setStartDate(calendar.getTime());
                        calendar.setTime(startDate);
                        calendar.add(Calendar.DAY_OF_MONTH,-1);
                        leftItem.setEndDate(calendar.getTime());
                        leftItem.setMonday(1);
                        leftItem.setTuesday(1);
                        leftItem.setWednesday(1);
                        leftItem.setThursday(1);
                        leftItem.setFriday(1);
                        leftItem.setSaturday(1);
                        leftItem.setSunday(1);
                        blockItem.add(leftItem);
                        //生成结束时间到无限大blockItem
                        Date endDate = tempItem.getEndDate();
                        //默认无限大为列表中最大时间+1年
                        calendar.setTime(endDate);
                        calendar.add(Calendar.DAY_OF_MONTH,1);
                        rightItem.setStartDate(calendar.getTime());
                        calendar.setTime(maxDate);
                        calendar.add(Calendar.YEAR,1);
                        maxUpYearDate = calendar.getTime();
                        rightItem.setEndDate(calendar.getTime());
                        rightItem.setMonday(1);
                        rightItem.setTuesday(1);
                        rightItem.setWednesday(1);
                        rightItem.setThursday(1);
                        rightItem.setFriday(1);
                        rightItem.setSaturday(1);
                        rightItem.setSunday(1);
                        blockItem.add(rightItem);
                        //非适用区间blockItem
                        //判断区间内是否每天都适用
                        boolean allIsOk = allIsOk(tempItem);
                        if (!allIsOk){
                            ProductItem productItem = new ProductItem();
                            productItem.setStartDate(tempItem.getStartDate());
                            productItem.setEndDate(tempItem.getEndDate());
                            productItem.setMonday(tempItem.getMonday() == 0 ? 1 : 0);
                            productItem.setTuesday(tempItem.getTuesday() == 0 ? 1 : 0);
                            productItem.setWednesday(tempItem.getWednesday() == 0 ? 1 : 0);
                            productItem.setThursday(tempItem.getThursday() == 0 ? 1 : 0);
                            productItem.setFriday(tempItem.getFriday() == 0 ? 1 : 0);
                            productItem.setSaturday(tempItem.getSaturday() == 0 ? 1 : 0);
                            productItem.setSunday(tempItem.getSunday() == 0 ? 1 : 0);
                            blockItem.add(productItem);
                        }
                    }else {
                        List<ProductItem> tempBlockList = Lists.newLinkedList();
                        for (ProductItem productItem : blockItem) {
                            long tempStart = tempStartDate.getTime();
                            long tempEnd = tempEndDate.getTime();
                            Date blockStartDate = productItem.getStartDate();
                            Date blockEndDate = productItem.getEndDate();
                            long blockStart = blockStartDate.getTime();
                            long blockEnd = blockEndDate.getTime();
                            //temp的起始时间在block起止时间之间,temp结束时间在block起止时间之外
                            if (tempStart >= blockStart && tempStart <= blockEnd && tempEnd > blockEnd){
                                //没有相同的星期
                                if (isNoWeekCommon(tempItem,productItem)){
                                    //所有block存入列表
                                    tempBlockList.add(productItem);
                                }else {
                                    //block所有的星期都在temp中存在
                                    if (isBothWeekCommon(productItem,tempItem)){
                                        if (blockStart == tempStart){
                                            //此时不处理
                                        }else {
                                            //block结束时间更新为temp开始时间-1
                                            calendar.setTime(tempStartDate);
                                            calendar.add(Calendar.DAY_OF_MONTH,-1);
                                            productItem.setEndDate(calendar.getTime());
                                            //余下的block存入列表
                                            tempBlockList.add(productItem);
                                        }
                                    }else {//block部分星期在temp中存在
                                        List<Integer> weeks = findCommonWeek(productItem,tempItem);
                                        if (blockStart == tempStart){
                                            productItem = transItem(weeks,productItem);
                                            tempBlockList.add(productItem);
                                        }else {
                                            //block结束时间更新为temp开始时间-1
                                            ProductItem leftItem = new ProductItem();
                                            ProductItem rightItem = new ProductItem();
                                            BeanUtils.copyProperties(productItem,leftItem);
                                            BeanUtils.copyProperties(productItem,rightItem);
                                            calendar.setTime(tempStartDate);
                                            calendar.add(Calendar.DAY_OF_MONTH,-1);
                                            leftItem.setEndDate(calendar.getTime());
                                            tempBlockList.add(leftItem);
                                            rightItem.setStartDate(tempStartDate);
                                            rightItem = transItem(weeks,rightItem);
                                            tempBlockList.add(rightItem);
                                        }
                                    }
                                }
                            }
                            //temp的结束时间在block的起止时间之间，temp的起始时间在blcok的起止时间之外
                            else if (tempEnd >= blockStart && tempEnd <= blockEnd && tempStart < blockStart){
                                //没有相同的星期
                                if (isNoWeekCommon(tempItem,productItem)){
                                    //所有block存入列表
                                    tempBlockList.add(productItem);
                                }else {
                                    //block所有的星期都在temp中存在
                                    if (isBothWeekCommon(productItem,tempItem)){
                                        if (blockEnd == tempEnd){
                                            //此时不处理
                                        }else {
                                            //block开始时间更新为temp结束时间+1
                                            calendar.setTime(tempEndDate);
                                            calendar.add(Calendar.DAY_OF_MONTH,1);
                                            productItem.setStartDate(calendar.getTime());
                                            tempBlockList.add(productItem);
                                        }
                                    }else {//block部分星期在temp中存在
                                        List<Integer> weeks = findCommonWeek(productItem,tempItem);
                                        if (blockEnd == tempEnd){
                                            productItem = transItem(weeks,productItem);
                                            tempBlockList.add(productItem);
                                        }else {
                                            //block开始时间更新为temp结束时间+1
                                            ProductItem leftItem = new ProductItem();
                                            ProductItem rightItem = new ProductItem();
                                            BeanUtils.copyProperties(productItem,leftItem);
                                            BeanUtils.copyProperties(productItem,rightItem);
                                            calendar.setTime(tempEndDate);
                                            calendar.add(Calendar.DAY_OF_MONTH,1);
                                            rightItem.setStartDate(calendar.getTime());
                                            tempBlockList.add(rightItem);
                                            leftItem.setEndDate(tempEndDate);
                                            leftItem = transItem(weeks,leftItem);
                                            tempBlockList.add(leftItem);
                                        }
                                    }
                                }
                            }
                            //temp起止时间都在block起止时间之间
                            else if (tempStart >= blockStart && tempEnd <= blockEnd){
                                //没有相同的星期
                                if (isNoWeekCommon(productItem,tempItem)){
                                    tempBlockList.add(productItem);
                                }else {
                                    //block所有的星期都在temp中存在
                                    if (isBothWeekCommon(productItem,tempItem)){
                                        if (tempStart == blockStart && tempEnd < blockEnd){
                                            //block开始时间更新为temp结束时间+1
                                            calendar.setTime(tempEndDate);
                                            calendar.add(Calendar.DAY_OF_MONTH,1);
                                            productItem.setStartDate(calendar.getTime());
                                            tempBlockList.add(productItem);
                                        }else if (tempStart > blockStart && tempEnd == blockEnd){
                                            //block结束时间更新为temp开始时间-1
                                            calendar.setTime(tempStartDate);
                                            calendar.add(Calendar.DAY_OF_MONTH,-1);
                                            productItem.setEndDate(calendar.getTime());
                                            tempBlockList.add(productItem);
                                        }else if (tempStart == blockStart && tempEnd == blockEnd){
                                            //此时不处理
                                        }else if (tempStart > blockStart && tempEnd < blockEnd){
                                            ProductItem leftItem = new ProductItem();
                                            ProductItem rightItem = new ProductItem();
                                            BeanUtils.copyProperties(productItem,leftItem);
                                            BeanUtils.copyProperties(productItem,rightItem);
                                            //左边bolck插入
                                            calendar.setTime(tempStartDate);
                                            calendar.add(Calendar.DAY_OF_MONTH,-1);
                                            leftItem.setEndDate(calendar.getTime());
                                            tempBlockList.add(leftItem);
                                            //右边block插入
                                            calendar.setTime(tempEndDate);
                                            calendar.add(Calendar.DAY_OF_MONTH,1);
                                            rightItem.setStartDate(calendar.getTime());
                                            tempBlockList.add(rightItem);
                                        }
                                    }else {//block部分星期在temp中存在
                                        List<Integer> weeks = findCommonWeek(productItem,tempItem);
                                        if (tempStart == blockStart && tempEnd < blockEnd){
                                            ProductItem leftItem = new ProductItem();
                                            ProductItem rightItem = new ProductItem();
                                            BeanUtils.copyProperties(productItem,leftItem);
                                            BeanUtils.copyProperties(productItem,rightItem);
                                            //block开始时间更新为temp结束时间+1
                                            calendar.setTime(tempEndDate);
                                            calendar.add(Calendar.DAY_OF_MONTH,1);
                                            rightItem.setStartDate(calendar.getTime());
                                            tempBlockList.add(rightItem);
                                            //重合时间段block处理
                                            leftItem.setEndDate(tempEndDate);
                                            leftItem = transItem(weeks,leftItem);
                                            tempBlockList.add(leftItem);
                                        }else if (tempStart > blockStart && tempEnd == blockEnd){
                                            ProductItem leftItem = new ProductItem();
                                            ProductItem rightItem = new ProductItem();
                                            BeanUtils.copyProperties(productItem,leftItem);
                                            BeanUtils.copyProperties(productItem,rightItem);
                                            //block结束时间更新为temp开始时间-1
                                            calendar.setTime(tempStartDate);
                                            calendar.add(Calendar.DAY_OF_MONTH,-1);
                                            leftItem.setEndDate(calendar.getTime());
                                            tempBlockList.add(leftItem);
                                            //重合时间段block处理
                                            rightItem.setStartDate(tempStartDate);
                                            rightItem = transItem(weeks,rightItem);
                                            tempBlockList.add(rightItem);
                                        }else if (tempStart == blockStart && tempEnd == blockEnd){
                                            productItem = transItem(weeks,productItem);
                                            tempBlockList.add(productItem);
                                        }else if (tempStart > blockStart && tempEnd < blockEnd){
                                            ProductItem leftItem = new ProductItem();
                                            ProductItem rightItem = new ProductItem();
                                            BeanUtils.copyProperties(productItem,leftItem);
                                            BeanUtils.copyProperties(productItem,rightItem);
                                            //左边block插入
                                            calendar.setTime(tempStartDate);
                                            calendar.add(Calendar.DAY_OF_MONTH,-1);
                                            leftItem.setEndDate(calendar.getTime());
                                            tempBlockList.add(leftItem);
                                            //右边block插入
                                            calendar.setTime(tempEndDate);
                                            calendar.add(Calendar.DAY_OF_MONTH,1);
                                            rightItem.setStartDate(calendar.getTime());
                                            tempBlockList.add(rightItem);
                                            //重合时间段价格处理
                                            productItem.setStartDate(tempStartDate);
                                            productItem.setEndDate(tempEndDate);
                                            productItem = transItem(weeks,productItem);
                                            tempBlockList.add(productItem);
                                        }
                                    }
                                }
                            }
                            //block开始时间大于temp的起始时间，block的结束时间小于temp的结束时间
                            else if (tempStart < blockStart && tempEnd > blockEnd){
                                //没有相同的星期
                                if (isNoWeekCommon(productItem,tempItem)){
                                    tempBlockList.add(productItem);
                                }else {
                                    //block所有的星期都在temp中存在
                                    if (isBothWeekCommon(productItem,tempItem)){
                                        //此时不处理
                                    }else {//block部分星期在temp中存在
                                        List<Integer> weeks = findCommonWeek(productItem,tempItem);
                                        productItem = transItem(weeks,productItem);
                                        tempBlockList.add(productItem);
                                    }
                                }
                            }else {
                                tempBlockList.add(productItem);
                            }
                        }
                        blockItem = tempBlockList;
                    }
                }
            }
            if (!CollectionUtils.isEmpty(blockItem)){
                //去掉头尾多余的block
                for (ProductItem tempItem : blockItem) {
                    if (tempItem.getStartDate().compareTo(minDownYearDate) != 0 && tempItem.getEndDate().compareTo(maxUpYearDate) != 0){
                        resList.add(tempItem);
                    }
                }
            }
        }
        return resList;
    }

    /**
     * 子项列表转换为block
     * @param blockItem
     * @return
     */
    @Override
    public String item2Block(List<ProductItem> blockItem)throws Exception {
        String block = null;
        List<String> blockList = Lists.newLinkedList();
        for (ProductItem productItem : blockItem) {
            //是否存在星期
            if (isExistWeek(productItem)){
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(DateUtil.format(productItem.getStartDate(),"yyyy/MM/dd"));
                stringBuffer.append("-");
                stringBuffer.append(DateUtil.format(productItem.getEndDate(),"yyyy/MM/dd"));
                if (productItem.getMonday() == 1){
                    stringBuffer.append("/W1");
                }
                if (productItem.getTuesday() == 1){
                    stringBuffer.append("/W2");
                }
                if (productItem.getWednesday() == 1){
                    stringBuffer.append("/W3");
                }
                if (productItem.getThursday() == 1){
                    stringBuffer.append("/W4");
                }
                if (productItem.getFriday() == 1){
                    stringBuffer.append("/W5");
                }
                if (productItem.getSaturday() == 1){
                    stringBuffer.append("/W6");
                }
                if (productItem.getSunday() == 1){
                    stringBuffer.append("/W7");
                }
                blockList.add(stringBuffer.toString());
            }
        }
        if (!CollectionUtils.isEmpty(blockList)){
            block = StringUtils.join(blockList,", ");
        }
        return block;
    }

    /**
     * 判断子项中是否存在星期
     * @param productItem
     * @return
     */
    private boolean isExistWeek(ProductItem productItem) {
        boolean flag = false;
        if (productItem.getMonday() == 1){
            flag = true;
        }
        if (productItem.getTuesday() == 1){
            flag = true;
        }
        if (productItem.getWednesday() == 1){
            flag = true;
        }
        if (productItem.getThursday() == 1){
            flag = true;
        }
        if (productItem.getFriday() == 1){
            flag = true;
        }
        if (productItem.getSaturday() == 1){
            flag = true;
        }
        if (productItem.getSunday() == 1){
            flag = true;
        }
        return flag;
    }

    /**
     * 根据星期列表转换子项
     * @param weeks
     * @param productItem
     * @return
     */
    private ProductItem transItem(List<Integer> weeks,ProductItem productItem){
        if (weeks.contains(1)){
            productItem.setMonday(1);
        }else {
            productItem.setMonday(0);
        }
        if (weeks.contains(2)){
            productItem.setTuesday(1);
        }else {
            productItem.setTuesday(0);
        }
        if (weeks.contains(3)){
            productItem.setWednesday(1);
        }else {
            productItem.setWednesday(0);
        }
        if (weeks.contains(4)){
            productItem.setThursday(1);
        }else {
            productItem.setThursday(0);
        }
        if (weeks.contains(5)){
            productItem.setFriday(1);
        }else {
            productItem.setFriday(0);
        }
        if (weeks.contains(6)){
            productItem.setSaturday(1);
        }else {
            productItem.setSaturday(0);
        }
        if (weeks.contains(7)){
            productItem.setSunday(1);
        }else {
            productItem.setSunday(0);
        }
        return productItem;
    }

    /**
     * 查找第一个子项存在而第二个子项不存在的星期列表
     * @param rule1
     * @param rule2
     * @return
     */
    private List<Integer> findCommonWeek(ProductItem rule1,ProductItem rule2){
        List<Integer> list = Lists.newLinkedList();
        if (rule1.getMonday() == 1 && rule1.getMonday() != rule2.getMonday()){
            list.add(1);
        }
        if (rule1.getTuesday() == 1 && rule1.getTuesday() != rule2.getTuesday()){
            list.add(2);
        }
        if (rule1.getWednesday() == 1 && rule1.getWednesday() != rule2.getWednesday()){
            list.add(3);
        }
        if (rule1.getThursday() == 1 && rule1.getThursday() != rule2.getThursday()){
            list.add(4);
        }
        if (rule1.getFriday() == 1 && rule1.getFriday() != rule2.getFriday()){
            list.add(5);
        }
        if (rule1.getSaturday() == 1 && rule1.getSaturday() != rule2.getSaturday()){
            list.add(6);
        }
        if (rule1.getSunday() == 1 && rule1.getSunday() != rule2.getSunday()){
            list.add(7);
        }
        return list;
    }

    /**
     * 判断第一个子项是否全部在第二个子项中存在相同的星期
     * @param rule1
     * @param rule2
     * @return
     */
    private boolean isBothWeekCommon(ProductItem rule1,ProductItem rule2){
        boolean flag = true;
        if (rule1.getMonday() == 1 && rule1.getMonday() != rule2.getMonday()){
            flag = false;
        }
        if (rule1.getTuesday() == 1 && rule1.getTuesday() != rule2.getTuesday()){
            flag = false;
        }
        if (rule1.getWednesday() == 1 && rule1.getWednesday() != rule2.getWednesday()){
            flag = false;
        }
        if (rule1.getThursday() == 1 && rule1.getThursday() != rule2.getThursday()){
            flag = false;
        }
        if (rule1.getFriday() == 1 && rule1.getFriday() != rule2.getFriday()){
            flag = false;
        }
        if (rule1.getSaturday() == 1 && rule1.getSaturday() != rule2.getSaturday()){
            flag = false;
        }
        if (rule1.getSunday() == 1 && rule1.getSunday() != rule2.getSunday()){
            flag = false;
        }
        return flag;
    }

    /**
     * 判断两个产品子项是否没有相同的星期
     * @param rule1
     * @param rule2
     * @return
     */
    private boolean isNoWeekCommon(ProductItem rule1,ProductItem rule2){
        boolean flag = true;
        if (rule1.getMonday() == 1 && rule1.getMonday() == rule2.getMonday()){
            flag = false;
        }
        if (rule1.getTuesday() == 1 && rule1.getTuesday() == rule2.getTuesday()){
            flag = false;
        }
        if (rule1.getWednesday() == 1 && rule1.getWednesday() == rule2.getWednesday()){
            flag = false;
        }
        if (rule1.getThursday() == 1 && rule1.getThursday() == rule2.getThursday()){
            flag = false;
        }
        if (rule1.getFriday() == 1 && rule1.getFriday() == rule2.getFriday()){
            flag = false;
        }
        if (rule1.getSaturday() == 1 && rule1.getSaturday() == rule2.getSaturday()){
            flag = false;
        }
        if (rule1.getSunday() == 1 && rule1.getSunday() == rule2.getSunday()){
            flag = false;
        }
        return flag;
    }

    /**
     * 获取产品子项列表中最大的结束时间
     * @param productItemList
     * @return
     */
    private Date getMaxDate(List<ProductItem> productItemList) {
        Date maxDate = null;
        if (!CollectionUtils.isEmpty(productItemList)){
            for (ProductItem productItem : productItemList) {
                if (maxDate == null){
                    maxDate = productItem.getEndDate();
                }else {
                    if (productItem.getEndDate() != null){
                        if (maxDate.compareTo(productItem.getEndDate()) < 0){
                            maxDate = productItem.getEndDate();
                        }
                    }
                }
            }
        }
        return maxDate;
    }

    /**
     * 获取产品子项列表中最小的开始时间
     * @param productItemList
     * @return
     */
    private Date getMinDate(List<ProductItem> productItemList) {
        Date minDate = null;
        if (!CollectionUtils.isEmpty(productItemList)){
            for (ProductItem productItem : productItemList) {
                if (minDate == null){
                    minDate = productItem.getStartDate();
                }else {
                    if (productItem.getStartDate() != null){
                        if (minDate.compareTo(productItem.getStartDate()) > 0){
                            minDate = productItem.getStartDate();
                        }
                    }
                }
            }
        }
        return minDate;
    }

    /**
     * 判断区间内是否每天都可用
     * @param productItem
     * @return
     * @throws Exception
     */
    public boolean allIsOk(ProductItem productItem)throws Exception{
        boolean flag = true;
        if (productItem.getMonday() == 0){
            flag = false;
        }
        if (productItem.getTuesday() == 0){
            flag = false;
        }
        if (productItem.getWednesday() == 0){
            flag = false;
        }
        if (productItem.getThursday() == 0){
            flag = false;
        }
        if (productItem.getFriday() == 0){
            flag = false;
        }
        if (productItem.getSaturday() == 0){
            flag = false;
        }
        if (productItem.getSunday() == 0){
            flag = false;
        }
        return flag;
    }

    /**
     * 查询价格和结算规则中相同的星期
     * @param rule
     * @param price
     * @return
     */
    public List<String> queryBothWeek(ShopItemSettlePriceRule rule,ShopItemNetPriceRule price){
        List<String> weeks = Lists.newLinkedList();
        if (rule.getMonday() == 1 && rule.getMonday() == price.getMonday()){
            weeks.add("星期一");
        }
        if (rule.getTuesday() == 1 && rule.getTuesday() == price.getTuesday()){
            weeks.add("星期二");
        }
        if (rule.getWednesday() == 1 && rule.getWednesday() == price.getWednesday()){
            weeks.add("星期三");
        }
        if (rule.getThursday() == 1 && rule.getThursday() == price.getThursday()){
            weeks.add("星期四");
        }
        if (rule.getFriday() == 1 && rule.getFriday() == price.getFriday()){
            weeks.add("星期五");
        }
        if (rule.getSaturday() == 1 && rule.getSaturday() == price.getSaturday()){
            weeks.add("星期六");
        }
        if (rule.getSunday() == 1 && rule.getSunday() == price.getSunday()){
            weeks.add("星期日");
        }
        return weeks;
    }

    /**
     * 查询结算规则中的星期
     * @param rule
     * @return
     */
    public List<String> queryRuleWeek(ShopItemSettlePriceRule rule){
        List<String> weeks = Lists.newLinkedList();
        if (rule.getMonday() == 1){
            weeks.add("星期一");
        }
        if (rule.getTuesday() == 1){
            weeks.add("星期二");
        }
        if (rule.getWednesday() == 1){
            weeks.add("星期三");
        }
        if (rule.getThursday() == 1){
            weeks.add("星期四");
        }
        if (rule.getFriday() == 1){
            weeks.add("星期五");
        }
        if (rule.getSaturday() == 1){
            weeks.add("星期六");
        }
        if (rule.getSunday() == 1){
            weeks.add("星期日");
        }
        return weeks;
    }
}

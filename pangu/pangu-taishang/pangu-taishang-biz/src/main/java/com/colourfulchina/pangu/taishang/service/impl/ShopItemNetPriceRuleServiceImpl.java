package com.colourfulchina.pangu.taishang.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemNetPriceRule;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopItemNetPriceRuleOptReq;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopItemNetPriceRuleQueryRes;
import com.colourfulchina.pangu.taishang.mapper.ShopItemNetPriceRuleMapper;
import com.colourfulchina.pangu.taishang.service.ShopItemNetPriceRuleService;
import com.colourfulchina.pangu.taishang.utils.DateUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ShopItemNetPriceRuleServiceImpl extends ServiceImpl<ShopItemNetPriceRuleMapper,ShopItemNetPriceRule> implements ShopItemNetPriceRuleService {
    @Autowired
    private ShopItemNetPriceRuleMapper shopItemNetPriceRuleMapper;
    /**
     * 规格价格添加、修改
     * @param shopItemNetPriceRuleOptReq
     * @return
     * @throws Exception
     */
    @Override
    public List<ShopItemNetPriceRule> add(ShopItemNetPriceRuleOptReq shopItemNetPriceRuleOptReq) throws Exception {
        List<ShopItemNetPriceRule> result = Lists.newLinkedList();
        ShopItemNetPriceRule shopItemNetPriceRule = convert(shopItemNetPriceRuleOptReq);
        if (shopItemNetPriceRule.getId() != null){
            shopItemNetPriceRule.setDelFlag(DelFlagEnums.DELETE.getCode());
            shopItemNetPriceRuleMapper.updateById(shopItemNetPriceRule);
            shopItemNetPriceRule.setId(null);
        }
        //查询已存在的价格列表
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and shop_item_id ="+shopItemNetPriceRuleOptReq.getShopItemId();
            }
        };
        List<ShopItemNetPriceRule> list = shopItemNetPriceRuleMapper.selectList(wrapper);
        result = optPrice(shopItemNetPriceRule,list);
        return result;
    }

    /**
     * 规格价格修改
     * @param shopItemNetPriceRuleOptReq
     * @return
     * @throws Exception
     */
    @Override
    public List<ShopItemNetPriceRule> upd(ShopItemNetPriceRuleOptReq shopItemNetPriceRuleOptReq) throws Exception {
        List<ShopItemNetPriceRule> result = Lists.newLinkedList();
        ShopItemNetPriceRule shopItemNetPriceRule = convert(shopItemNetPriceRuleOptReq);
        shopItemNetPriceRule.setDelFlag(DelFlagEnums.DELETE.getCode());
        shopItemNetPriceRuleMapper.updateById(shopItemNetPriceRule);
        shopItemNetPriceRule.setId(null);
        //查询已存在的价格列表
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and shop_item_id ="+shopItemNetPriceRuleOptReq.getShopItemId();
            }
        };
        List<ShopItemNetPriceRule> list = shopItemNetPriceRuleMapper.selectList(wrapper);
        result = optPrice(shopItemNetPriceRule,list);
        return result;
    }

    @Override
    public List<ShopItemNetPriceRuleQueryRes> translatePrice(List<ShopItemNetPriceRule> priceRules) throws Exception {
        List<ShopItemNetPriceRuleQueryRes> list = Lists.newLinkedList();
        for (ShopItemNetPriceRule shopItemNetPriceRule : priceRules) {
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
            }
        }
        return list;
    }

    /**
     * 预约时间对应的价格
     * @param bookDate
     * @param priceList
     * @return
     */
    @Override
    public ShopItemNetPriceRule foundPriceByTime(Date bookDate,List<ShopItemNetPriceRule> priceList)throws Exception{
        ShopItemNetPriceRule res = null;
        Integer week = DateUtils.dateForWeek(bookDate);
        log.debug("ShopItemNetPriceRule week:{} bookDate:{}",week, DateUtil.format(bookDate,"yyyy-MM-dd"));
        if (!CollectionUtils.isEmpty(priceList)){
            for (ShopItemNetPriceRule shopItemNetPriceRule : priceList) {
                boolean dateFlag = DateUtils.belongCalendar(bookDate,shopItemNetPriceRule.getStartDate(),shopItemNetPriceRule.getEndDate());
                if (dateFlag){
                    if (week.compareTo(2) == 0){
                        if (shopItemNetPriceRule.getMonday() == 1){
                            res = shopItemNetPriceRule;
                            break;
                        }
                    }
                    if (week.compareTo(3) == 0){
                        if (shopItemNetPriceRule.getTuesday() == 1){
                            res = shopItemNetPriceRule;
                            break;
                        }
                    }
                    if (week.compareTo(4) == 0){
                        if (shopItemNetPriceRule.getWednesday() == 1){
                            res = shopItemNetPriceRule;
                            break;
                        }
                    }
                    if (week.compareTo(5) == 0){
                        if (shopItemNetPriceRule.getThursday() == 1){
                            res = shopItemNetPriceRule;
                            break;
                        }
                    }
                    if (week.compareTo(6) == 0){
                        if (shopItemNetPriceRule.getFriday() == 1){
                            res = shopItemNetPriceRule;
                            break;
                        }
                    }
                    if (week.compareTo(7) == 0){
                        if (shopItemNetPriceRule.getSaturday() == 1){
                            res = shopItemNetPriceRule;
                            break;
                        }
                    }
                    if (week.compareTo(1) == 0){
                        if (shopItemNetPriceRule.getSunday() == 1){
                            res = shopItemNetPriceRule;
                            break;
                        }
                    }
                }
            }
        }
        return res;
    }

    /**
     * 价格新增.修改操作(按相同的时间段和星期覆盖替换)
     * @param price
     * @param rules
     */
    private List<ShopItemNetPriceRule> optPrice(ShopItemNetPriceRule price,List<ShopItemNetPriceRule> rules){
        List<ShopItemNetPriceRule> result = Lists.newLinkedList();
        Date newStartDate = DateUtil.parse(DateUtil.format(price.getStartDate(),"yyyy-MM-dd"),"yyyy-MM-dd");
        Date newEndDate = DateUtil.parse(DateUtil.format(price.getEndDate(),"yyyy-MM-dd"),"yyyy-MM-dd");
        List<List<ShopItemNetPriceRule>> remainList = Lists.newLinkedList();
        int i = 0;
        boolean flag = false;
        if (!CollectionUtils.isEmpty(rules)){
            for (ShopItemNetPriceRule rule : rules) {
                i++;
                long newStart = newStartDate.getTime();
                long newEnd = newEndDate.getTime();
                Date oldStartDate = rule.getStartDate();
                Date oldEndDate = rule.getEndDate();
                long oldStart = oldStartDate.getTime();
                long oldEnd = oldEndDate.getTime();
                Calendar calendar = Calendar.getInstance();
                //新价格的起始时间在老价格起止时间之间,新价格结束时间在老价格起止时间之外
                if (newStart >= oldStart && newStart <= oldEnd && newEnd > oldEnd){
                    //没有相同的星期
                    if (isNoWeekCommon(rule,price)){
                        //新价格插入
                        ShopItemNetPriceRule priceRule = new ShopItemNetPriceRule();
                        BeanUtils.copyProperties(price,priceRule);
                        priceRule.setStartDate(newStartDate);
                        priceRule.setEndDate(oldEndDate);
                        priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                        priceRule = insertPrice(priceRule);
                        if (priceRule != null){
                            result.add(priceRule);
                        }
                        //剩下的区间塞进列表中
                        List<ShopItemNetPriceRule> list = Lists.newLinkedList();
                        calendar.setTime(oldEndDate);
                        calendar.add(Calendar.DAY_OF_MONTH,1);
                        ShopItemNetPriceRule remainRule = new ShopItemNetPriceRule();
                        BeanUtils.copyProperties(price,remainRule);
                        remainRule.setStartDate(calendar.getTime());
                        remainRule.setEndDate(newEndDate);
                        list.add(remainRule);
                        remainList.add(list);
                    }else {
                        //老价格所有的星期都在新价格中存在
                        if (isBothWeekCommon(rule,price)){
                            if (oldStart == newStart){
                                rule.setDelFlag(DelFlagEnums.DELETE.getCode());
                            }else {
                                //老价格结束时间更新为新价格开始时间-1
                                calendar.setTime(newStartDate);
                                calendar.add(Calendar.DAY_OF_MONTH,-1);
                                rule.setEndDate(calendar.getTime());
                            }
                            shopItemNetPriceRuleMapper.updateById(rule);
                            //新价格插入
                            ShopItemNetPriceRule priceRule = new ShopItemNetPriceRule();
                            BeanUtils.copyProperties(price,priceRule);
                            priceRule.setStartDate(newStartDate);
                            priceRule.setEndDate(oldEndDate);
                            priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            priceRule = insertPrice(priceRule);
                            if (priceRule != null){
                                result.add(priceRule);
                            }
                            //剩下的区间塞进列表中
                            List<ShopItemNetPriceRule> list = Lists.newLinkedList();
                            calendar.setTime(oldEndDate);
                            calendar.add(Calendar.DAY_OF_MONTH,1);
                            ShopItemNetPriceRule remainRule = new ShopItemNetPriceRule();
                            BeanUtils.copyProperties(price,remainRule);
                            remainRule.setStartDate(calendar.getTime());
                            remainRule.setEndDate(newEndDate);
                            list.add(remainRule);
                            remainList.add(list);
                        }else {//老价格部分星期在新价格中存在
                            List<Integer> weeks = findCommonWeek(rule,price);
                            if (oldStart == newStart){
                                rule.setDelFlag(DelFlagEnums.DELETE.getCode());
                            }else {
                                //老价格结束时间更新为新价格开始时间-1
                                calendar.setTime(newStartDate);
                                calendar.add(Calendar.DAY_OF_MONTH,-1);
                                rule.setEndDate(calendar.getTime());
                            }
                            shopItemNetPriceRuleMapper.updateById(rule);
                            //老价格存在而新价格不存在的星期价格插入
                            ShopItemNetPriceRule oldPrice = new ShopItemNetPriceRule();
                            BeanUtils.copyProperties(rule,oldPrice);
                            oldPrice.setId(null);
                            oldPrice.setStartDate(newStartDate);
                            oldPrice.setEndDate(oldEndDate);
                            oldPrice.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            if (weeks.contains(1)){
                                oldPrice.setMonday(1);
                            }else {
                                oldPrice.setMonday(0);
                            }
                            if (weeks.contains(2)){
                                oldPrice.setTuesday(1);
                            }else {
                                oldPrice.setTuesday(0);
                            }
                            if (weeks.contains(3)){
                                oldPrice.setWednesday(1);
                            }else {
                                oldPrice.setWednesday(0);
                            }
                            if (weeks.contains(4)){
                                oldPrice.setThursday(1);
                            }else {
                                oldPrice.setThursday(0);
                            }
                            if (weeks.contains(5)){
                                oldPrice.setFriday(1);
                            }else {
                                oldPrice.setFriday(0);
                            }
                            if (weeks.contains(6)){
                                oldPrice.setSaturday(1);
                            }else {
                                oldPrice.setSaturday(0);
                            }
                            if (weeks.contains(7)){
                                oldPrice.setSunday(1);
                            }else {
                                oldPrice.setSunday(0);
                            }
                            oldPrice = insertPrice(oldPrice);
                            if (oldPrice != null){
                                result.add(oldPrice);
                            }
                            //新价格插入
                            ShopItemNetPriceRule priceRule = new ShopItemNetPriceRule();
                            BeanUtils.copyProperties(price,priceRule);
                            priceRule.setStartDate(newStartDate);
                            priceRule.setEndDate(oldEndDate);
                            priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            priceRule = insertPrice(priceRule);
                            if (priceRule != null){
                                result.add(priceRule);
                            }
                            //剩下的区间塞进列表中
                            List<ShopItemNetPriceRule> list = Lists.newLinkedList();
                            calendar.setTime(oldEndDate);
                            calendar.add(Calendar.DAY_OF_MONTH,1);
                            ShopItemNetPriceRule remainRule = new ShopItemNetPriceRule();
                            BeanUtils.copyProperties(price,remainRule);
                            remainRule.setStartDate(calendar.getTime());
                            remainRule.setEndDate(newEndDate);
                            list.add(remainRule);
                            remainList.add(list);
                        }
                    }
                }
                //新价格的结束时间在老价格的起止时间之间，新价格的起始时间在老价格的起止时间之外
                else if (newEnd >= oldStart && newEnd <= oldEnd && newStart < oldStart){
                    //没有相同的星期
                    if (isNoWeekCommon(rule,price)){
                        //新价格插入
                        ShopItemNetPriceRule priceRule = new ShopItemNetPriceRule();
                        BeanUtils.copyProperties(price,priceRule);
                        priceRule.setStartDate(oldStartDate);
                        priceRule.setEndDate(newEndDate);
                        priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                        priceRule = insertPrice(priceRule);
                        if (priceRule != null){
                            result.add(priceRule);
                        }
                        //剩下的区间塞进列表中
                        List<ShopItemNetPriceRule> list = Lists.newLinkedList();
                        calendar.setTime(oldStartDate);
                        calendar.add(Calendar.DAY_OF_MONTH,-1);
                        ShopItemNetPriceRule remainRule = new ShopItemNetPriceRule();
                        BeanUtils.copyProperties(price,remainRule);
                        remainRule.setStartDate(newStartDate);
                        remainRule.setEndDate(calendar.getTime());
                        list.add(remainRule);
                        remainList.add(list);
                    }else {
                        //老价格所有的星期都在新价格中存在
                        if (isBothWeekCommon(rule,price)){
                            if (oldEnd == newEnd){
                                rule.setDelFlag(DelFlagEnums.DELETE.getCode());
                            }else {
                                //老价格开始时间更新为新价格结束时间+1
                                calendar.setTime(newEndDate);
                                calendar.add(Calendar.DAY_OF_MONTH,1);
                                rule.setStartDate(calendar.getTime());
                            }
                            shopItemNetPriceRuleMapper.updateById(rule);
                            //新价格插入
                            ShopItemNetPriceRule priceRule = new ShopItemNetPriceRule();
                            BeanUtils.copyProperties(price,priceRule);
                            priceRule.setStartDate(oldStartDate);
                            priceRule.setEndDate(newEndDate);
                            priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            priceRule = insertPrice(priceRule);
                            if (priceRule != null){
                                result.add(priceRule);
                            }
                            //剩下的区间塞进列表中
                            List<ShopItemNetPriceRule> list = Lists.newLinkedList();
                            calendar.setTime(oldStartDate);
                            calendar.add(Calendar.DAY_OF_MONTH,-1);
                            ShopItemNetPriceRule remainRule = new ShopItemNetPriceRule();
                            BeanUtils.copyProperties(price,remainRule);
                            remainRule.setStartDate(newStartDate);
                            remainRule.setEndDate(calendar.getTime());
                            list.add(remainRule);
                            remainList.add(list);
                        }else {//老价格部分星期在新价格中存在
                            List<Integer> weeks = findCommonWeek(rule,price);
                            if (oldEnd == newEnd){
                                rule.setDelFlag(DelFlagEnums.DELETE.getCode());
                            }else {
                                //老价格开始时间更新为新价格结束时间+1
                                calendar.setTime(newEndDate);
                                calendar.add(Calendar.DAY_OF_MONTH,1);
                                rule.setStartDate(calendar.getTime());
                            }
                            shopItemNetPriceRuleMapper.updateById(rule);
                            //老价格存在而新价格不存在的星期价格插入
                            ShopItemNetPriceRule oldPrice = new ShopItemNetPriceRule();
                            BeanUtils.copyProperties(rule,oldPrice);
                            oldPrice.setId(null);
                            oldPrice.setStartDate(oldStartDate);
                            oldPrice.setEndDate(newEndDate);
                            oldPrice.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            if (weeks.contains(1)){
                                oldPrice.setMonday(1);
                            }else {
                                oldPrice.setMonday(0);
                            }
                            if (weeks.contains(2)){
                                oldPrice.setTuesday(1);
                            }else {
                                oldPrice.setTuesday(0);
                            }
                            if (weeks.contains(3)){
                                oldPrice.setWednesday(1);
                            }else {
                                oldPrice.setWednesday(0);
                            }
                            if (weeks.contains(4)){
                                oldPrice.setThursday(1);
                            }else {
                                oldPrice.setThursday(0);
                            }
                            if (weeks.contains(5)){
                                oldPrice.setFriday(1);
                            }else {
                                oldPrice.setFriday(0);
                            }
                            if (weeks.contains(6)){
                                oldPrice.setSaturday(1);
                            }else {
                                oldPrice.setSaturday(0);
                            }
                            if (weeks.contains(7)){
                                oldPrice.setSunday(1);
                            }else {
                                oldPrice.setSunday(0);
                            }
                            oldPrice = insertPrice(oldPrice);
                            if (oldPrice != null){
                                result.add(oldPrice);
                            }
                            //新价格插入
                            ShopItemNetPriceRule priceRule = new ShopItemNetPriceRule();
                            BeanUtils.copyProperties(price,priceRule);
                            priceRule.setStartDate(oldStartDate);
                            priceRule.setEndDate(newEndDate);
                            priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            priceRule = insertPrice(priceRule);
                            if (priceRule != null){
                                result.add(priceRule);
                            }
                            //剩下的区间塞进列表中
                            List<ShopItemNetPriceRule> list = Lists.newLinkedList();
                            calendar.setTime(oldStartDate);
                            calendar.add(Calendar.DAY_OF_MONTH,-1);
                            ShopItemNetPriceRule remainRule = new ShopItemNetPriceRule();
                            BeanUtils.copyProperties(price,remainRule);
                            remainRule.setStartDate(newStartDate);
                            remainRule.setEndDate(calendar.getTime());
                            list.add(remainRule);
                            remainList.add(list);
                        }
                    }
                }
                //新价格起止时间都在老价格起止时间之间
                else if (newStart >= oldStart && newEnd <= oldEnd){
                    //没有相同的星期
                    if (isNoWeekCommon(rule,price)){
                        //新价格插入
                        ShopItemNetPriceRule priceRule = new ShopItemNetPriceRule();
                        BeanUtils.copyProperties(price,priceRule);
                        priceRule.setStartDate(newStartDate);
                        priceRule.setEndDate(newEndDate);
                        priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                        priceRule = insertPrice(priceRule);
                        if (priceRule != null){
                            result.add(priceRule);
                        }
                        flag = true;
                    }else {
                        //老价格所有的星期都在新价格中存在
                        if (isBothWeekCommon(rule,price)){
                            if (newStart == oldStart && newEnd < oldEnd){
                                //老价格开始时间更新为新价格结束时间+1
                                calendar.setTime(newEndDate);
                                calendar.add(Calendar.DAY_OF_MONTH,1);
                                rule.setStartDate(calendar.getTime());
                                shopItemNetPriceRuleMapper.updateById(rule);
                            }else if (newStart > oldStart && newEnd == oldEnd){
                                //老价格结束时间更新为新价格开始时间-1
                                calendar.setTime(newStartDate);
                                calendar.add(Calendar.DAY_OF_MONTH,-1);
                                rule.setEndDate(calendar.getTime());
                                shopItemNetPriceRuleMapper.updateById(rule);
                            }else if (newStart == oldStart && newEnd == oldEnd){
                                rule.setDelFlag(DelFlagEnums.DELETE.getCode());
                                shopItemNetPriceRuleMapper.updateById(rule);
                            }else if (newStart > oldStart && newEnd < oldEnd){
                                ShopItemNetPriceRule leftPrice = new ShopItemNetPriceRule();
                                ShopItemNetPriceRule rightPrice = new ShopItemNetPriceRule();
                                BeanUtils.copyProperties(rule,leftPrice);
                                BeanUtils.copyProperties(rule,rightPrice);
                                //左边价格插入
                                calendar.setTime(newStartDate);
                                calendar.add(Calendar.DAY_OF_MONTH,-1);
                                leftPrice.setId(null);
                                leftPrice.setStartDate(oldStartDate);
                                leftPrice.setEndDate(calendar.getTime());
                                //右边价格插入
                                calendar.setTime(newEndDate);
                                calendar.add(Calendar.DAY_OF_MONTH,1);
                                rightPrice.setId(null);
                                rightPrice.setStartDate(calendar.getTime());
                                rightPrice.setEndDate(oldEndDate);
                                //老价格删除
                                rule.setDelFlag(DelFlagEnums.DELETE.getCode());
                                leftPrice = insertPrice(leftPrice);
                                rightPrice = insertPrice(rightPrice);
                                if (leftPrice != null){
                                    result.add(leftPrice);
                                }
                                if (rightPrice != null){
                                    result.add(rightPrice);
                                }
                                shopItemNetPriceRuleMapper.updateById(rule);
                            }
                            //新价格插入
                            ShopItemNetPriceRule priceRule = new ShopItemNetPriceRule();
                            BeanUtils.copyProperties(price,priceRule);
                            priceRule.setStartDate(newStartDate);
                            priceRule.setEndDate(newEndDate);
                            priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            priceRule = insertPrice(priceRule);
                            if (priceRule != null){
                                result.add(priceRule);
                            }
                            flag = true;
                        }else {//老价格部分星期在新价格中存在
                            List<Integer> weeks = findCommonWeek(rule,price);
                            if (newStart == oldStart && newEnd < oldEnd){
                                //老价格开始时间更新为新价格结束时间+1
                                calendar.setTime(newEndDate);
                                calendar.add(Calendar.DAY_OF_MONTH,1);
                                rule.setStartDate(calendar.getTime());
                                shopItemNetPriceRuleMapper.updateById(rule);
                            }else if (newStart > oldStart && newEnd == oldEnd){
                                //老价格结束时间更新为新价格开始时间-1
                                calendar.setTime(newStartDate);
                                calendar.add(Calendar.DAY_OF_MONTH,-1);
                                rule.setEndDate(calendar.getTime());
                                shopItemNetPriceRuleMapper.updateById(rule);
                            }else if (newStart == oldStart && newEnd == oldEnd){
                                rule.setDelFlag(DelFlagEnums.DELETE.getCode());
                                shopItemNetPriceRuleMapper.updateById(rule);
                            }else if (newStart > oldStart && newEnd < oldEnd){
                                ShopItemNetPriceRule leftPrice = new ShopItemNetPriceRule();
                                ShopItemNetPriceRule rightPrice = new ShopItemNetPriceRule();
                                BeanUtils.copyProperties(rule,leftPrice);
                                BeanUtils.copyProperties(rule,rightPrice);
                                //左边价格插入
                                calendar.setTime(newStartDate);
                                calendar.add(Calendar.DAY_OF_MONTH,-1);
                                leftPrice.setId(null);
                                leftPrice.setStartDate(oldStartDate);
                                leftPrice.setEndDate(calendar.getTime());
                                //右边价格插入
                                calendar.setTime(newEndDate);
                                calendar.add(Calendar.DAY_OF_MONTH,1);
                                rightPrice.setId(null);
                                rightPrice.setStartDate(calendar.getTime());
                                rightPrice.setEndDate(oldEndDate);
                                //老价格删除
                                rule.setDelFlag(DelFlagEnums.DELETE.getCode());
                                leftPrice = insertPrice(leftPrice);
                                rightPrice = insertPrice(rightPrice);
                                if (leftPrice != null){
                                    result.add(leftPrice);
                                }
                                if (rightPrice != null){
                                    result.add(rightPrice);
                                }
                                shopItemNetPriceRuleMapper.updateById(rule);
                            }
                            //老价格存在而新价格不存在的星期价格插入
                            ShopItemNetPriceRule oldPrice = new ShopItemNetPriceRule();
                            BeanUtils.copyProperties(rule,oldPrice);
                            oldPrice.setId(null);
                            oldPrice.setStartDate(newStartDate);
                            oldPrice.setEndDate(newEndDate);
                            oldPrice.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            if (weeks.contains(1)){
                                oldPrice.setMonday(1);
                            }else {
                                oldPrice.setMonday(0);
                            }
                            if (weeks.contains(2)){
                                oldPrice.setTuesday(1);
                            }else {
                                oldPrice.setTuesday(0);
                            }
                            if (weeks.contains(3)){
                                oldPrice.setWednesday(1);
                            }else {
                                oldPrice.setWednesday(0);
                            }
                            if (weeks.contains(4)){
                                oldPrice.setThursday(1);
                            }else {
                                oldPrice.setThursday(0);
                            }
                            if (weeks.contains(5)){
                                oldPrice.setFriday(1);
                            }else {
                                oldPrice.setFriday(0);
                            }
                            if (weeks.contains(6)){
                                oldPrice.setSaturday(1);
                            }else {
                                oldPrice.setSaturday(0);
                            }
                            if (weeks.contains(7)){
                                oldPrice.setSunday(1);
                            }else {
                                oldPrice.setSunday(0);
                            }
                            oldPrice = insertPrice(oldPrice);
                            if (oldPrice != null){
                                result.add(oldPrice);
                            }
                            //新价格插入
                            ShopItemNetPriceRule priceRule = new ShopItemNetPriceRule();
                            BeanUtils.copyProperties(price,priceRule);
                            priceRule.setStartDate(newStartDate);
                            priceRule.setEndDate(newEndDate);
                            priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            priceRule = insertPrice(priceRule);
                            if (priceRule != null){
                                result.add(priceRule);
                            }
                            flag = true;
                        }
                    }
                }
                //老价格开始时间大于新价格的起始时间，老价格的结束时间小于新价格的结束时间
                else if (newStart < oldStart && newEnd > oldEnd){
                    //没有相同的星期
                    if (isNoWeekCommon(rule,price)){
                        //新价格插入
                        ShopItemNetPriceRule priceRule = new ShopItemNetPriceRule();
                        BeanUtils.copyProperties(price,priceRule);
                        priceRule.setStartDate(oldStartDate);
                        priceRule.setEndDate(oldEndDate);
                        priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                        priceRule = insertPrice(priceRule);
                        if (priceRule != null){
                            result.add(priceRule);
                        }
                        //左边价格
                        List<ShopItemNetPriceRule> list = Lists.newLinkedList();
                        ShopItemNetPriceRule leftPrice = new ShopItemNetPriceRule();
                        BeanUtils.copyProperties(price,leftPrice);
                        calendar.setTime(oldStartDate);
                        calendar.add(Calendar.DAY_OF_MONTH,-1);
                        leftPrice.setStartDate(newStartDate);
                        leftPrice.setEndDate(calendar.getTime());
                        list.add(leftPrice);
                        //右边价格
                        ShopItemNetPriceRule rightPrice = new ShopItemNetPriceRule();
                        BeanUtils.copyProperties(price,rightPrice);
                        calendar.setTime(oldEndDate);
                        calendar.add(Calendar.DAY_OF_MONTH,1);
                        rightPrice.setStartDate(calendar.getTime());
                        rightPrice.setEndDate(newEndDate);
                        list.add(rightPrice);
                        remainList.add(list);
                    }else {
                        //老价格所有的星期都在新价格中存在
                        if (isBothWeekCommon(rule,price)){
                            //老价格删除
                            rule.setDelFlag(DelFlagEnums.DELETE.getCode());
                            shopItemNetPriceRuleMapper.updateById(rule);
                            //新价格插入
                            ShopItemNetPriceRule priceRule = new ShopItemNetPriceRule();
                            BeanUtils.copyProperties(price,priceRule);
                            priceRule.setStartDate(oldStartDate);
                            priceRule.setEndDate(oldEndDate);
                            priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            priceRule = insertPrice(priceRule);
                            if (priceRule != null){
                                result.add(priceRule);
                            }
                            //左边价格
                            List<ShopItemNetPriceRule> list = Lists.newLinkedList();
                            ShopItemNetPriceRule leftPrice = new ShopItemNetPriceRule();
                            BeanUtils.copyProperties(price,leftPrice);
                            calendar.setTime(oldStartDate);
                            calendar.add(Calendar.DAY_OF_MONTH,-1);
                            leftPrice.setStartDate(newStartDate);
                            leftPrice.setEndDate(calendar.getTime());
                            list.add(leftPrice);
                            //右边价格
                            ShopItemNetPriceRule rightPrice = new ShopItemNetPriceRule();
                            BeanUtils.copyProperties(price,rightPrice);
                            calendar.setTime(oldEndDate);
                            calendar.add(Calendar.DAY_OF_MONTH,1);
                            rightPrice.setStartDate(calendar.getTime());
                            rightPrice.setEndDate(newEndDate);
                            list.add(rightPrice);
                            remainList.add(list);
                        }else {//老价格部分星期在新价格中存在
                            List<Integer> weeks = findCommonWeek(rule,price);
                            //老价格更新
                            if (weeks.contains(1)){
                                rule.setMonday(1);
                            }else {
                                rule.setMonday(0);
                            }
                            if (weeks.contains(2)){
                                rule.setTuesday(1);
                            }else {
                                rule.setTuesday(0);
                            }
                            if (weeks.contains(3)){
                                rule.setWednesday(1);
                            }else {
                                rule.setWednesday(0);
                            }
                            if (weeks.contains(4)){
                                rule.setThursday(1);
                            }else {
                                rule.setThursday(0);
                            }
                            if (weeks.contains(5)){
                                rule.setFriday(1);
                            }else {
                                rule.setFriday(0);
                            }
                            if (weeks.contains(6)){
                                rule.setSaturday(1);
                            }else {
                                rule.setSaturday(0);
                            }
                            if (weeks.contains(7)){
                                rule.setSunday(1);
                            }else {
                                rule.setSunday(0);
                            }
                            shopItemNetPriceRuleMapper.updateById(rule);
                            //新价格插入
                            ShopItemNetPriceRule priceRule = new ShopItemNetPriceRule();
                            BeanUtils.copyProperties(price,priceRule);
                            priceRule.setStartDate(oldStartDate);
                            priceRule.setEndDate(oldEndDate);
                            priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            priceRule = insertPrice(priceRule);
                            if (priceRule != null){
                                result.add(priceRule);
                            }
                            //左边价格
                            List<ShopItemNetPriceRule> list = Lists.newLinkedList();
                            ShopItemNetPriceRule leftPrice = new ShopItemNetPriceRule();
                            BeanUtils.copyProperties(price,leftPrice);
                            calendar.setTime(oldStartDate);
                            calendar.add(Calendar.DAY_OF_MONTH,-1);
                            leftPrice.setStartDate(newStartDate);
                            leftPrice.setEndDate(calendar.getTime());
                            list.add(leftPrice);
                            //右边价格
                            ShopItemNetPriceRule rightPrice = new ShopItemNetPriceRule();
                            BeanUtils.copyProperties(price,rightPrice);
                            calendar.setTime(oldEndDate);
                            calendar.add(Calendar.DAY_OF_MONTH,1);
                            rightPrice.setStartDate(calendar.getTime());
                            rightPrice.setEndDate(newEndDate);
                            list.add(rightPrice);
                            remainList.add(list);
                        }
                    }
                }else {
                    List<ShopItemNetPriceRule> list = Lists.newLinkedList();
                    ShopItemNetPriceRule remainRule = new ShopItemNetPriceRule();
                    BeanUtils.copyProperties(price,remainRule);
                    remainRule.setStartDate(newStartDate);
                    remainRule.setEndDate(newEndDate);
                    list.add(remainRule);
                    remainList.add(list);
                }
                if (i == rules.size()){
                    if (flag){
                        remainList = null;
                    }
                    if (!CollectionUtils.isEmpty(remainList)){
                        List<ShopItemNetPriceRule> intersectList = Lists.newLinkedList();
                        intersectList = remainList.get(0);
                        int j = 0;
                        for (List<ShopItemNetPriceRule> shopItemNetPriceRules : remainList) {
                            j++;
                            if (!CollectionUtils.isEmpty(intersectList)){
                                intersectList = unionPriceDate(intersectList,shopItemNetPriceRules);
                            }
                            if (j == remainList.size()){
                                if (!CollectionUtils.isEmpty(intersectList)){
                                    for (ShopItemNetPriceRule shopItemNetPriceRule : intersectList) {
                                        ShopItemNetPriceRule temp = new ShopItemNetPriceRule();
                                        shopItemNetPriceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                                        temp = insertPrice(shopItemNetPriceRule);
                                        if (temp != null){
                                            result.add(temp);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }else {
            //新价格插入
            ShopItemNetPriceRule priceRule = new ShopItemNetPriceRule();
            BeanUtils.copyProperties(price,priceRule);
            priceRule.setStartDate(newStartDate);
            priceRule.setEndDate(newEndDate);
            priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
            priceRule = insertPrice(priceRule);
            if (priceRule != null){
                result.add(priceRule);
            }
        }
        return result;
    }

    /**
     * 价格入库，重复入库判断
     * @param shopItemNetPriceRule
     * @return
     */
    private ShopItemNetPriceRule insertPrice(ShopItemNetPriceRule price){
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and shop_id ="+price.getShopId() +" and shop_item_id ="+price.getShopItemId()+" and start_date = '"+price.getStartDate()+
                        "' and end_date = '"+price.getEndDate()+"' and monday ="+price.getMonday()+" and tuesday ="+price.getTuesday()+" and wednesday ="+price.getWednesday()+
                        " and thursday ="+price.getThursday()+" and friday ="+price.getFriday()+" and saturday ="+price.getSaturday()+" and sunday ="+price.getSunday()+
                        " and net_price ="+price.getNetPrice()+" and service_rate ="+price.getServiceRate()+" and tax_rate ="+price.getTaxRate();
            }
        };
        List<ShopItemNetPriceRule> priceRules = shopItemNetPriceRuleMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(priceRules)){
            shopItemNetPriceRuleMapper.insert(price);
            return price;
        }
        return null;
    }

    /**
     * 得到2个列表中价格的交集(按相同的年月日区分)
     * @param unionList
     * @param remainList
     * @return
     */
    private List<ShopItemNetPriceRule> unionPriceDate(List<ShopItemNetPriceRule> intersectList,List<ShopItemNetPriceRule> remainList){
        List<ShopItemNetPriceRule> result = Lists.newLinkedList();
        for (ShopItemNetPriceRule intersect : intersectList) {
            Date intersectStartDate = intersect.getStartDate();
            Date intersectEndDate = intersect.getEndDate();
            long intersectStart = intersectStartDate.getTime();
            long intersectEnd = intersectEndDate.getTime();
            for (ShopItemNetPriceRule remain : remainList) {
                Date remainStartDate = remain.getStartDate();
                Date remainEndDate = remain.getEndDate();
                long remainStart = remainStartDate.getTime();
                long remainEnd = remainEndDate.getTime();
                if (remainStart >= intersectStart && remainStart <= intersectEnd && remainEnd > intersectEnd){
                    ShopItemNetPriceRule price = new ShopItemNetPriceRule();
                    BeanUtils.copyProperties(intersect,price);
                    price.setStartDate(remainStartDate);
                    price.setEndDate(intersectEndDate);
                    result.add(price);
                }else if (remainEnd >= intersectStart && remainEnd <= intersectEnd && remainStart < intersectStart){
                    ShopItemNetPriceRule price = new ShopItemNetPriceRule();
                    BeanUtils.copyProperties(intersect,price);
                    price.setStartDate(intersectStartDate);
                    price.setEndDate(remainEndDate);
                    result.add(price);
                }else if (remainStart >= intersectStart && remainEnd <= intersectEnd){
                    ShopItemNetPriceRule price = new ShopItemNetPriceRule();
                    BeanUtils.copyProperties(intersect,price);
                    price.setStartDate(remainStartDate);
                    price.setEndDate(remainEndDate);
                    result.add(price);
                }else if (remainStart < intersectStart && remainEnd > intersectEnd){
                    ShopItemNetPriceRule price = new ShopItemNetPriceRule();
                    BeanUtils.copyProperties(intersect,price);
                    price.setStartDate(intersectStartDate);
                    price.setEndDate(intersectEndDate);
                    result.add(price);
                }
            }
        }
        return result;
    }

    /**
     * 判断两个价格是否没有相同的星期
     * @param rule1
     * @param rule2
     * @return
     */
    private boolean isNoWeekCommon(ShopItemNetPriceRule rule1,ShopItemNetPriceRule rule2){
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
     * 判断第一个价格是否全部在第二个价格中存在相同的星期
     * @param rule1
     * @param rule2
     * @return
     */
    private boolean isBothWeekCommon(ShopItemNetPriceRule rule1,ShopItemNetPriceRule rule2){
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
     * 查找第一个价格存在而第二个价格不存在的星期列表
     * @param rule1
     * @param rule2
     * @return
     */
    private List<Integer> findCommonWeek(ShopItemNetPriceRule rule1,ShopItemNetPriceRule rule2){
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
     * 将ShopItemNetPriceRuleOptReq转换为ShopItemNetPriceRule
     * @param req
     * @return
     */
    private ShopItemNetPriceRule convert(ShopItemNetPriceRuleOptReq req){
        ShopItemNetPriceRule shopItemNetPriceRule = new ShopItemNetPriceRule();
        BeanUtils.copyProperties(req,shopItemNetPriceRule);
        shopItemNetPriceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
        if (req.getNetPrice() == null){
            shopItemNetPriceRule.setNetPrice(new BigDecimal(0));
        }
        if (req.getServiceRate() == null){
            shopItemNetPriceRule.setServiceRate(new BigDecimal(0));
        }
        if (req.getTaxRate() == null){
            shopItemNetPriceRule.setTaxRate(new BigDecimal(0));
        }
        if (!CollectionUtils.isEmpty(req.getWeeks())){
            if (req.getWeeks().contains(1)){
                shopItemNetPriceRule.setMonday(1);
            }else {
                shopItemNetPriceRule.setMonday(0);
            }
            if (req.getWeeks().contains(2)){
                shopItemNetPriceRule.setTuesday(1);
            }else {
                shopItemNetPriceRule.setTuesday(0);
            }
            if (req.getWeeks().contains(3)){
                shopItemNetPriceRule.setWednesday(1);
            }else {
                shopItemNetPriceRule.setWednesday(0);
            }
            if (req.getWeeks().contains(4)){
                shopItemNetPriceRule.setThursday(1);
            }else {
                shopItemNetPriceRule.setThursday(0);
            }
            if (req.getWeeks().contains(5)){
                shopItemNetPriceRule.setFriday(1);
            }else {
                shopItemNetPriceRule.setFriday(0);
            }
            if (req.getWeeks().contains(6)){
                shopItemNetPriceRule.setSaturday(1);
            }else {
                shopItemNetPriceRule.setSaturday(0);
            }
            if (req.getWeeks().contains(7)){
                shopItemNetPriceRule.setSunday(1);
            }else {
                shopItemNetPriceRule.setSunday(0);
            }
        }
        return shopItemNetPriceRule;
    }
}
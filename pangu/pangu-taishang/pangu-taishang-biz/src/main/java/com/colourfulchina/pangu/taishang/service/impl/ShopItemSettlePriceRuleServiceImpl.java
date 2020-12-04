package com.colourfulchina.pangu.taishang.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.api.vo.SettleExpressBriefVo;
import com.colourfulchina.pangu.taishang.api.vo.SettleExpressTranslateVo;
import com.colourfulchina.pangu.taishang.api.vo.SettleExpressVo;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopItemSettleRuleOptReq;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopItemSettleRulesListRes;
import com.colourfulchina.pangu.taishang.mapper.ShopItemSettlePriceRuleMapper;
import com.colourfulchina.pangu.taishang.service.GiftService;
import com.colourfulchina.pangu.taishang.service.ShopItemSettleExpressService;
import com.colourfulchina.pangu.taishang.service.ShopItemSettlePriceRuleService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import rx.annotations.Experimental;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ShopItemSettlePriceRuleServiceImpl extends ServiceImpl<ShopItemSettlePriceRuleMapper, ShopItemSettlePriceRule> implements ShopItemSettlePriceRuleService {
    @Autowired
    private ShopItemSettlePriceRuleMapper shopItemSettlePriceRuleMapper;
    @Autowired
    private ShopItemSettleExpressService shopItemSettleExpressService;
    @Autowired
    private GiftService giftService;

    /**
     * 查询规格结算规格信息
     * @param shopItemSettlePriceRule
     * @return
     */
    @Override
    public List<ShopItemSettleRulesListRes> selectSettleRuleList(ShopItemSettlePriceRule shopItemSettlePriceRule) throws Exception{
        List<ShopItemSettleRulesListRes> resList = Lists.newLinkedList();
        Wrapper settlePriceRuleWrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and status = 0 and shop_id ="+shopItemSettlePriceRule.getShopId()+" and shop_item_id ="+shopItemSettlePriceRule.getShopItemId();
            }
        };
        List<ShopItemSettlePriceRule> settlePriceRules = shopItemSettlePriceRuleMapper.selectList(settlePriceRuleWrapper);
        if (!CollectionUtils.isEmpty(settlePriceRules)){
            for (ShopItemSettlePriceRule settlePriceRule : settlePriceRules) {
                ShopItemSettleRulesListRes shopItemSettleRulesListRes = new ShopItemSettleRulesListRes();
                BeanUtils.copyProperties(settlePriceRule,shopItemSettleRulesListRes);
                ShopItemSettleExpress shopItemSettleExpress = new ShopItemSettleExpress();
                if (settlePriceRule.getSettleExpressId() != null){
                    shopItemSettleExpress = shopItemSettleExpressService.selectById(settlePriceRule.getSettleExpressId());
                    shopItemSettleRulesListRes.setSettleExpress(shopItemSettleExpress);
                }
                //翻译权益结算规则，回显各个权益的规则用
                SettleExpressTranslateVo settleExpressTranslateVo = new SettleExpressTranslateVo();
                SettleExpressBriefVo expressBriefVo = new SettleExpressBriefVo();
                BeanUtils.copyProperties(shopItemSettleExpress,expressBriefVo);
                settleExpressTranslateVo.setExpressVo(expressBriefVo);
                settleExpressTranslateVo = translateSettleExpress(settleExpressTranslateVo);
                if (StringUtils.isNotBlank(settleExpressTranslateVo.getExpressVo().getSettleExpress())){
                    shopItemSettleRulesListRes.setExpress(settleExpressTranslateVo.getExpressVo().getSettleExpress());
                }
                if (StringUtils.isNotBlank(settleExpressTranslateVo.getExpressVo().getCustomTaxExpress())){
                    shopItemSettleRulesListRes.setCustomTaxExpress(settleExpressTranslateVo.getExpressVo().getCustomTaxExpress());
                }
                //翻译结算规则，列表展示用
                shopItemSettleRulesListRes = translate(shopItemSettleRulesListRes);
                resList.add(shopItemSettleRulesListRes);
            }
        }
        return resList;
    }

    /**
     * 添加规格结算规则
     * @param shopItemSettleRuleOptReq
     * @return
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public List<ShopItemSettlePriceRule> add(ShopItemSettleRuleOptReq shopItemSettleRuleOptReq) throws Exception{
        List<ShopItemSettlePriceRule> list = Lists.newLinkedList();
        //如果是修改。则删除后添加
        if (shopItemSettleRuleOptReq.getId() != null){
            //获取原结算规则
            ShopItemSettlePriceRule rule = shopItemSettlePriceRuleMapper.selectById(shopItemSettleRuleOptReq.getId());
            rule.setDelFlag(DelFlagEnums.DELETE.getCode());
            shopItemSettlePriceRuleMapper.updateById(rule);
            shopItemSettleRuleOptReq.setId(null);
        }
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and shop_item_id ="+shopItemSettleRuleOptReq.getShopItemId();
            }
        };
        List<ShopItemSettlePriceRule> settleRules = shopItemSettlePriceRuleMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(shopItemSettleRuleOptReq.getSettleExpressVoList())){
            for (SettleExpressVo settleExpressVo : shopItemSettleRuleOptReq.getSettleExpressVoList()) {
                if (!settleExpressVo.isOnlyGift()){
                    ShopItemSettleExpress shopItemSettleExpress = new ShopItemSettleExpress();
                    BeanUtils.copyProperties(settleExpressVo,shopItemSettleExpress);
                    shopItemSettleExpress.setDelFlag(DelFlagEnums.NORMAL.getCode());
                    shopItemSettleExpress.setShopId(shopItemSettleRuleOptReq.getShopId());
                    shopItemSettleExpress.setShopItemId(shopItemSettleRuleOptReq.getShopItemId());
                    shopItemSettleExpress = generateExpress(shopItemSettleExpress);
                    shopItemSettleExpress.setCreateUser(SecurityUtils.getLoginName());
                    shopItemSettleExpressService.insert(shopItemSettleExpress);

                    ShopItemSettlePriceRule shopItemSettlePriceRule = convert(shopItemSettleRuleOptReq);
                    shopItemSettlePriceRule.setGift(StringUtils.isBlank(settleExpressVo.getGift())?null:settleExpressVo.getGift());
                    shopItemSettlePriceRule.setSettleExpressId(shopItemSettleExpress.getId());
                    shopItemSettlePriceRule.setCreateUser(SecurityUtils.getLoginName());
                    List<ShopItemSettlePriceRule> priceRules = optSettleRule(shopItemSettlePriceRule,settleRules);
                    list.addAll(priceRules);
                }else {
                    ShopItemSettlePriceRule shopItemSettlePriceRule = convert(shopItemSettleRuleOptReq);
                    shopItemSettlePriceRule.setGift(StringUtils.isBlank(settleExpressVo.getGift())?null:settleExpressVo.getGift());
                    shopItemSettlePriceRule.setCreateUser(SecurityUtils.getLoginName());
                    List<ShopItemSettlePriceRule> priceRules = optSettleRule(shopItemSettlePriceRule,settleRules);
                    list.addAll(priceRules);
                }
            }
        }
        return list;
    }

    /**
     * 结算规则公式中文翻译
     * @param shopItemSettleExpress
     * @return
     */
    @Override
    public SettleExpressTranslateVo translateSettleExpress(SettleExpressTranslateVo settleExpressTranslateVo) throws Exception{
        ShopItemSettleExpress shopItemSettleExpress = new ShopItemSettleExpress();
        BeanUtils.copyProperties(settleExpressTranslateVo.getExpressVo(),shopItemSettleExpress);
        shopItemSettleExpress = generateExpress(shopItemSettleExpress);
        String rule = shopItemSettleExpress.getSettleExpress();
        String custom = shopItemSettleExpress.getCustomTaxExpress();
        if (StringUtils.isNotBlank(custom)){
            if (StringUtils.isNotBlank(rule)){
                if (bigDecimalIsExist(shopItemSettleExpress.getNetPricePer())){
                    rule = rule.replaceAll("net_price_per",shopItemSettleExpress.getNetPricePer().multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString()+"%");
                }
                if (bigDecimalIsExist(shopItemSettleExpress.getServiceFeePer())){
                    rule = rule.replaceAll("service_fee_per",shopItemSettleExpress.getServiceFeePer().multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString()+"%");
                }
                if (bigDecimalIsExist(shopItemSettleExpress.getCustomTaxFeePer())){
                    rule = rule.replaceAll("custom_tax_fee_per",shopItemSettleExpress.getCustomTaxFeePer().multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString()+"%");
                }
                rule = rule.replaceAll("net_price","净价");
                rule = rule.replaceAll("service_fee","服务费");
                rule = rule.replaceAll("custom_tax_fee","自定义增值税");
                if (bigDecimalIsExist(shopItemSettleExpress.getDiscount())){
                    rule = rule.replaceAll("discount",shopItemSettleExpress.getDiscount().setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                }
            }

            if (bigDecimalIsExist(shopItemSettleExpress.getTaxNetPricePer())){
                custom = custom.replaceAll("tax_net_price_per",shopItemSettleExpress.getTaxNetPricePer().multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString()+"%");
            }
            if (bigDecimalIsExist(shopItemSettleExpress.getTaxServiceFeePer())){
                custom = custom.replaceAll("tax_service_fee_per",shopItemSettleExpress.getTaxServiceFeePer().multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).toString()+"%");
            }
            custom = custom.replaceAll("net_price","净价");
            custom = custom.replaceAll("service_fee","服务费");
            if (bigDecimalIsExist(shopItemSettleExpress.getAdjust())){
                custom = custom.replaceAll("adjust",shopItemSettleExpress.getAdjust().setScale(2,BigDecimal.ROUND_HALF_UP).toString());
            }
            custom = "(自定义增值税:" + custom + ")";
        }else {
            if (StringUtils.isNotBlank(rule)){
                if (bigDecimalIsExist(shopItemSettleExpress.getNetPricePer())){
                    rule = rule.replaceAll("net_price_per",shopItemSettleExpress.getNetPricePer().multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString()+"%");
                }
                if (bigDecimalIsExist(shopItemSettleExpress.getServiceFeePer())){
                    rule = rule.replaceAll("service_fee_per",shopItemSettleExpress.getServiceFeePer().multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString()+"%");
                }
                if (bigDecimalIsExist(shopItemSettleExpress.getTaxFeePer())){
                    rule = rule.replaceAll("tax_fee_per",shopItemSettleExpress.getTaxFeePer().multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString()+"%");
                }
                rule = rule.replaceAll("net_price","净价");
                rule = rule.replaceAll("service_fee","服务费");
                rule = rule.replaceAll("tax_fee","增值税");
                if (bigDecimalIsExist(shopItemSettleExpress.getDiscount())){
                    rule = rule.replaceAll("discount",shopItemSettleExpress.getDiscount().setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                }
            }
        }
        if (StringUtils.isBlank(rule)){
            rule = "无";
        }
        shopItemSettleExpress.setSettleExpress(rule);
        shopItemSettleExpress.setCustomTaxExpress(custom);
        BeanUtils.copyProperties(shopItemSettleExpress,settleExpressTranslateVo.getExpressVo());
        return settleExpressTranslateVo;
    }

    /**
     * 删除结算公式
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public ShopItemSettlePriceRule delSettle(Integer id) throws Exception {
        ShopItemSettlePriceRule shopItemSettlePriceRule = shopItemSettlePriceRuleMapper.selectById(id);
        shopItemSettlePriceRule.setDelFlag(DelFlagEnums.DELETE.getCode());
        shopItemSettlePriceRuleMapper.updateById(shopItemSettlePriceRule);
        return shopItemSettlePriceRule;
    }

    /**
     * 根据结算公式和价格计算协议价
     * @param shopItemNetPriceRule
     * @param shopItemSettleExpress
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal calProtocolPrice(ShopItemNetPriceRule price, ShopItemSettleExpress express) throws Exception {
        BigDecimal res = null;
        //净价*比例
        BigDecimal netPrice = new BigDecimal(0);
        //服务费*比例
        BigDecimal servicePrice = new BigDecimal(0);
        //税费*比例
        BigDecimal taxPrice = new BigDecimal(0);
        //固定贴现
        BigDecimal discount = new BigDecimal(0);
        //增值税中的净价*比例
        BigDecimal netTaxPrice = new BigDecimal(0);
        //增值税中的服务费*比例
        BigDecimal serviceTaxPrice = new BigDecimal(0);
        //调整金额
        BigDecimal adjust = new BigDecimal(0);
        if (price != null && express != null){
            //计算净价*比例
            if (bigDecimalIsExist(express.getNetPricePer()) && bigDecimalIsExist(price.getNetPrice())){
                netPrice = price.getNetPrice().multiply(express.getNetPricePer());
            }
            //计算服务费*比例
            if (bigDecimalIsExist(express.getServiceFeePer()) && bigDecimalIsExist(price.getServiceRate())){
                servicePrice = price.getNetPrice().multiply(price.getServiceRate()).multiply(express.getServiceFeePer());
            }
            //计算税费*比例非自定义增值税
            if (bigDecimalIsExist(express.getTaxFeePer()) && bigDecimalIsExist(price.getTaxRate())){
                BigDecimal serviceTemp = price.getNetPrice().multiply(price.getServiceRate());
                taxPrice = (price.getNetPrice().add(serviceTemp)).multiply(price.getTaxRate()).multiply(express.getTaxFeePer());
            }
            //计算税费*比例自定义增值税
            if (bigDecimalIsExist(express.getCustomTaxFeePer()) && bigDecimalIsExist(price.getTaxRate())){
                //计算增值税中的净价*比例
                if (bigDecimalIsExist(express.getTaxNetPricePer()) && bigDecimalIsExist(price.getNetPrice())){
                    netTaxPrice = price.getNetPrice().multiply(express.getTaxNetPricePer());
                }
                //计算增值税中的服务费*比例
                if (bigDecimalIsExist(express.getTaxServiceFeePer()) && bigDecimalIsExist(price.getServiceRate())){
                    serviceTaxPrice = price.getNetPrice().multiply(price.getServiceRate()).multiply(express.getTaxServiceFeePer());
                }
                //计算调整金额
                if (bigDecimalIsExist(express.getAdjust())){
                    adjust = express.getAdjust();
                }
                BigDecimal base = netTaxPrice.add(serviceTaxPrice).add(adjust);
                taxPrice = base.multiply(price.getTaxRate()).multiply(express.getCustomTaxFeePer());
            }
            //计算固定贴现
            if (bigDecimalIsExist(express.getDiscount())){
                discount = express.getDiscount();
            }
            res = netPrice.add(servicePrice).add(taxPrice).add(discount);
        }else if (price == null && express != null){
            //仅存在固定贴现的情况
            if (!bigDecimalIsExist(express.getNetPricePer()) &&
                    !bigDecimalIsExist(express.getTaxNetPricePer()) &&
                    !bigDecimalIsExist(express.getServiceFeePer()) &&
                    !bigDecimalIsExist(express.getTaxServiceFeePer()) &&
                    !bigDecimalIsExist(express.getTaxFeePer()) &&
                    !bigDecimalIsExist(express.getCustomTaxFeePer()) &&
                    !bigDecimalIsExist(express.getAdjust())){
                if (bigDecimalIsExist(express.getDiscount())){
                    res = express.getDiscount();
                }
            }
        }
        return res;
    }

    /**
     * 跟新结算价规则是否停用启用
     *
     * @param params
     */
    @Override
    public void updateShopItemSettlePriceRule(Product params) {
        shopItemSettlePriceRuleMapper.updateShopItemSettlePriceRule( params);
    }

    /**
     * 生成结算规则公式
     * @param shopItemSettleExpress
     * @return
     */
    private ShopItemSettleExpress generateExpress(ShopItemSettleExpress shopItemSettleExpress) throws Exception{
        List<String> settleExpressList = Lists.newLinkedList();
        List<String> customTaxexpressList = Lists.newLinkedList();
        if (bigDecimalIsExist(shopItemSettleExpress.getNetPricePer())){
            settleExpressList.add("net_price*net_price_per");
        }
        if (bigDecimalIsExist(shopItemSettleExpress.getServiceFeePer())){
            settleExpressList.add("service_fee*service_fee_per");
        }
        if (bigDecimalIsExist(shopItemSettleExpress.getTaxFeePer())){
            settleExpressList.add("tax_fee*tax_fee_per");
        }
        if (bigDecimalIsExist(shopItemSettleExpress.getCustomTaxFeePer())){
            settleExpressList.add("custom_tax_fee*custom_tax_fee_per");
        }
        if (bigDecimalIsExist(shopItemSettleExpress.getDiscount())){
            settleExpressList.add("discount");
        }

        if (bigDecimalIsExist(shopItemSettleExpress.getTaxNetPricePer())){
            customTaxexpressList.add("net_price*tax_net_price_per");
        }
        if (bigDecimalIsExist(shopItemSettleExpress.getTaxServiceFeePer())){
            customTaxexpressList.add("service_fee*tax_service_fee_per");
        }
        if (bigDecimalIsExist(shopItemSettleExpress.getAdjust())){
            customTaxexpressList.add("adjust");
        }

        String settleExpress = StringUtils.join(settleExpressList,"+");
        String customTaxexpress = StringUtils.join(customTaxexpressList,"+");
        shopItemSettleExpress.setSettleExpress(settleExpress);
        shopItemSettleExpress.setCustomTaxExpress(customTaxexpress);
        return shopItemSettleExpress;
    }

    /**
     * 判断BigDecimal类型的数据是否存在
     * @param bigDecimal
     * @return
     */
    private Boolean bigDecimalIsExist(BigDecimal bigDecimal){
        if (bigDecimal != null && bigDecimal.compareTo(new BigDecimal(0)) != 0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * ShopItemSettleRuleOptReq转换为ShopItemSettlePriceRule
     * @param req
     * @return
     */
    private ShopItemSettlePriceRule convert(ShopItemSettleRuleOptReq req) throws Exception{
        ShopItemSettlePriceRule shopItemSettlePriceRule = new ShopItemSettlePriceRule();
        BeanUtils.copyProperties(req,shopItemSettlePriceRule);
        shopItemSettlePriceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
        if (!CollectionUtils.isEmpty(req.getWeeks())){
            if (req.getWeeks().contains(1)){
                shopItemSettlePriceRule.setMonday(1);
            }else {
                shopItemSettlePriceRule.setMonday(0);
            }
            if (req.getWeeks().contains(2)){
                shopItemSettlePriceRule.setTuesday(1);
            }else {
                shopItemSettlePriceRule.setTuesday(0);
            }
            if (req.getWeeks().contains(3)){
                shopItemSettlePriceRule.setWednesday(1);
            }else {
                shopItemSettlePriceRule.setWednesday(0);
            }
            if (req.getWeeks().contains(4)){
                shopItemSettlePriceRule.setThursday(1);
            }else {
                shopItemSettlePriceRule.setThursday(0);
            }
            if (req.getWeeks().contains(5)){
                shopItemSettlePriceRule.setFriday(1);
            }else {
                shopItemSettlePriceRule.setFriday(0);
            }
            if (req.getWeeks().contains(6)){
                shopItemSettlePriceRule.setSaturday(1);
            }else {
                shopItemSettlePriceRule.setSaturday(0);
            }
            if (req.getWeeks().contains(7)){
                shopItemSettlePriceRule.setSunday(1);
            }else {
                shopItemSettlePriceRule.setSunday(0);
            }
        }
        return shopItemSettlePriceRule;
    }

    /**
     * 结算规则翻译
     * @param shopItemSettleRulesListRes
     * @return
     */
    private ShopItemSettleRulesListRes translate(ShopItemSettleRulesListRes shopItemSettleRulesListRes) throws Exception{
        List<String> weeks = Lists.newLinkedList();
        StringBuffer timeStr = new StringBuffer();
        StringBuffer ruleStr = new StringBuffer();
        timeStr.append(DateUtil.format(shopItemSettleRulesListRes.getStartDate(),"yyyy-MM-dd"));
        timeStr.append("~");
        timeStr.append(DateUtil.format(shopItemSettleRulesListRes.getEndDate(),"yyyy-MM-dd"));
        timeStr.append("：");
        if (shopItemSettleRulesListRes.getMonday()==1){
            weeks.add("周一");
        }
        if (shopItemSettleRulesListRes.getTuesday()==1){
            weeks.add("周二");
        }
        if (shopItemSettleRulesListRes.getWednesday()==1){
            weeks.add("周三");
        }
        if (shopItemSettleRulesListRes.getThursday()==1){
            weeks.add("周四");
        }
        if (shopItemSettleRulesListRes.getFriday()==1){
            weeks.add("周五");
        }
        if (shopItemSettleRulesListRes.getSaturday()==1){
            weeks.add("周六");
        }
        if (shopItemSettleRulesListRes.getSunday()==1){
            weeks.add("周日");
        }
        if (!CollectionUtils.isEmpty(weeks)){
            timeStr.append(StringUtils.join(weeks,"、"));
        }
        shopItemSettleRulesListRes.setTimeStr(timeStr.toString());
        if (StringUtils.isNotBlank(shopItemSettleRulesListRes.getGift())){
            ruleStr.append("【");
            ruleStr.append(giftService.selectById(shopItemSettleRulesListRes.getGift()).getShortName());
            ruleStr.append("】 ");
        }
        ruleStr.append("【");
        if (shopItemSettleRulesListRes.getSettleExpressId() == null){
            ruleStr.append("无】");
        }else {
            String rule = shopItemSettleRulesListRes.getSettleExpress().getSettleExpress();
            String custom = shopItemSettleRulesListRes.getSettleExpress().getCustomTaxExpress();
            if (StringUtils.isNotBlank(custom)){
                if (bigDecimalIsExist(shopItemSettleRulesListRes.getSettleExpress().getNetPricePer())){
                    rule = rule.replaceAll("net_price_per",shopItemSettleRulesListRes.getSettleExpress().getNetPricePer().multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString()+"%");
                }
                if (bigDecimalIsExist(shopItemSettleRulesListRes.getSettleExpress().getServiceFeePer())){
                    rule = rule.replaceAll("service_fee_per",shopItemSettleRulesListRes.getSettleExpress().getServiceFeePer().multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString()+"%");
                }
                if (bigDecimalIsExist(shopItemSettleRulesListRes.getSettleExpress().getCustomTaxFeePer())){
                    rule = rule.replaceAll("custom_tax_fee_per",shopItemSettleRulesListRes.getSettleExpress().getCustomTaxFeePer().multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString()+"%");
                }
                rule = rule.replaceAll("net_price","净价");
                rule = rule.replaceAll("service_fee","服务费");
                rule = rule.replaceAll("custom_tax_fee","自定义增值税");
                if (bigDecimalIsExist(shopItemSettleRulesListRes.getSettleExpress().getDiscount())){
                    rule = rule.replaceAll("discount",shopItemSettleRulesListRes.getSettleExpress().getDiscount().setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                }

                if (bigDecimalIsExist(shopItemSettleRulesListRes.getSettleExpress().getTaxNetPricePer())){
                    custom = custom.replaceAll("tax_net_price_per",shopItemSettleRulesListRes.getSettleExpress().getTaxNetPricePer().multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString()+"%");
                }
                if (bigDecimalIsExist(shopItemSettleRulesListRes.getSettleExpress().getTaxServiceFeePer())){
                    custom = custom.replaceAll("tax_service_fee_per",shopItemSettleRulesListRes.getSettleExpress().getTaxServiceFeePer().multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).toString()+"%");
                }
                custom = custom.replaceAll("net_price","净价");
                custom = custom.replaceAll("service_fee","服务费");
                if (bigDecimalIsExist(shopItemSettleRulesListRes.getSettleExpress().getAdjust())){
                    custom = custom.replaceAll("adjust",shopItemSettleRulesListRes.getSettleExpress().getAdjust().setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                }

                ruleStr.append(rule + " (自定义增值税:" + custom + ") 】");
            }else {
                if (bigDecimalIsExist(shopItemSettleRulesListRes.getSettleExpress().getNetPricePer())){
                    rule = rule.replaceAll("net_price_per",shopItemSettleRulesListRes.getSettleExpress().getNetPricePer().multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString()+"%");
                }
                if (bigDecimalIsExist(shopItemSettleRulesListRes.getSettleExpress().getServiceFeePer())){
                    rule = rule.replaceAll("service_fee_per",shopItemSettleRulesListRes.getSettleExpress().getServiceFeePer().multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString()+"%");
                }
                if (bigDecimalIsExist(shopItemSettleRulesListRes.getSettleExpress().getTaxFeePer())){
                    rule = rule.replaceAll("tax_fee_per",shopItemSettleRulesListRes.getSettleExpress().getTaxFeePer().multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString()+"%");
                }
                rule = rule.replaceAll("net_price","净价");
                rule = rule.replaceAll("service_fee","服务费");
                rule = rule.replaceAll("tax_fee","增值税");
                if (bigDecimalIsExist(shopItemSettleRulesListRes.getSettleExpress().getDiscount())){
                    rule = rule.replaceAll("discount",shopItemSettleRulesListRes.getSettleExpress().getDiscount().setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                }

                ruleStr.append(rule+"】");
            }
        }
        shopItemSettleRulesListRes.setSettleRuleStr(ruleStr.toString());
        return shopItemSettleRulesListRes;
    }

    /**
     * 结算规则新增.修改操作(按相同的时间段和星期、权益类型覆盖替换)
     * @param settle
     * @param settleRules
     */
    private List<ShopItemSettlePriceRule> optSettleRule(ShopItemSettlePriceRule settle,List<ShopItemSettlePriceRule> settleRules){
        List<ShopItemSettlePriceRule> result = Lists.newLinkedList();
        Date newStartDate = DateUtil.parse(DateUtil.format(settle.getStartDate(),"yyyy-MM-dd"),"yyyy-MM-dd");
        Date newEndDate = DateUtil.parse(DateUtil.format(settle.getEndDate(),"yyyy-MM-dd"),"yyyy-MM-dd");
        List<List<ShopItemSettlePriceRule>> remainList = Lists.newLinkedList();
        int i = 0;
        boolean flag = false;
        if (!CollectionUtils.isEmpty(settleRules)){
            for (ShopItemSettlePriceRule settleRule : settleRules) {
                i++;
                long newStart = newStartDate.getTime();
                long newEnd = newEndDate.getTime();
                Date oldStartDate = settleRule.getStartDate();
                Date oldEndDate = settleRule.getEndDate();
                long oldStart = oldStartDate.getTime();
                long oldEnd = oldEndDate.getTime();
                Calendar calendar = Calendar.getInstance();
                //新规则的起始时间在老规则起止时间之间,新规则结束时间在老规则起止时间之外
                if (newStart >= oldStart && newStart <= oldEnd && newEnd > oldEnd){
                    //没有相同的星期或者权益类型
                    if (isNoWeekCommon(settleRule,settle) || !isGiftCommon(settleRule,settle)){
                        //新规则插入
                        ShopItemSettlePriceRule priceRule = new ShopItemSettlePriceRule();
                        BeanUtils.copyProperties(settle,priceRule);
                        priceRule.setStartDate(newStartDate);
                        priceRule.setEndDate(oldEndDate);
                        priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                        priceRule = insertSettleRule(priceRule);
                        if (priceRule != null){
                            result.add(priceRule);
                        }
                        //剩下的区间塞进列表中
                        List<ShopItemSettlePriceRule> list = Lists.newLinkedList();
                        calendar.setTime(oldEndDate);
                        calendar.add(Calendar.DAY_OF_MONTH,1);
                        ShopItemSettlePriceRule remainRule = new ShopItemSettlePriceRule();
                        BeanUtils.copyProperties(settle,remainRule);
                        remainRule.setStartDate(calendar.getTime());
                        remainRule.setEndDate(newEndDate);
                        list.add(remainRule);
                        remainList.add(list);
                    }else {
                        //老规则所有的星期都在新规则中存在
                        if (isBothWeekCommon(settleRule,settle)){
                            if (oldStart == newStart){
                                settleRule.setDelFlag(DelFlagEnums.DELETE.getCode());
                            }else {
                                //老规则结束时间更新为新规则开始时间-1
                                calendar.setTime(newStartDate);
                                calendar.add(Calendar.DAY_OF_MONTH,-1);
                                settleRule.setEndDate(calendar.getTime());
                            }
                            shopItemSettlePriceRuleMapper.updateById(settleRule);
                            //新规则插入
                            ShopItemSettlePriceRule priceRule = new ShopItemSettlePriceRule();
                            BeanUtils.copyProperties(settle,priceRule);
                            priceRule.setStartDate(newStartDate);
                            priceRule.setEndDate(oldEndDate);
                            priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            priceRule = insertSettleRule(priceRule);
                            if (priceRule != null){
                                result.add(priceRule);
                            }
                            //剩下的区间塞进列表中
                            List<ShopItemSettlePriceRule> list = Lists.newLinkedList();
                            calendar.setTime(oldEndDate);
                            calendar.add(Calendar.DAY_OF_MONTH,1);
                            ShopItemSettlePriceRule remainRule = new ShopItemSettlePriceRule();
                            BeanUtils.copyProperties(settle,remainRule);
                            remainRule.setStartDate(calendar.getTime());
                            remainRule.setEndDate(newEndDate);
                            list.add(remainRule);
                            remainList.add(list);
                        }else {//老规则部分星期在新规则中存在
                            List<Integer> weeks = findCommonWeek(settleRule,settle);
                            if (oldStart == newStart){
                                settleRule.setDelFlag(DelFlagEnums.DELETE.getCode());
                            }else {
                                //老规则结束时间更新为新规则开始时间-1
                                calendar.setTime(newStartDate);
                                calendar.add(Calendar.DAY_OF_MONTH,-1);
                                settle.setEndDate(calendar.getTime());
                            }
                            shopItemSettlePriceRuleMapper.updateById(settleRule);
                            //老规则存在而新规则不存在的星期规则插入
                            ShopItemSettlePriceRule oldPrice = new ShopItemSettlePriceRule();
                            BeanUtils.copyProperties(settleRule,oldPrice);
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
                            oldPrice = insertSettleRule(oldPrice);
                            if (oldPrice != null){
                                result.add(oldPrice);
                            }
                            //新规则插入
                            ShopItemSettlePriceRule priceRule = new ShopItemSettlePriceRule();
                            BeanUtils.copyProperties(settle,priceRule);
                            priceRule.setStartDate(newStartDate);
                            priceRule.setEndDate(oldEndDate);
                            priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            priceRule = insertSettleRule(priceRule);
                            if (priceRule != null){
                                result.add(priceRule);
                            }
                            //剩下的区间塞进列表中
                            List<ShopItemSettlePriceRule> list = Lists.newLinkedList();
                            calendar.setTime(oldEndDate);
                            calendar.add(Calendar.DAY_OF_MONTH,1);
                            ShopItemSettlePriceRule remainRule = new ShopItemSettlePriceRule();
                            BeanUtils.copyProperties(settle,remainRule);
                            remainRule.setStartDate(calendar.getTime());
                            remainRule.setEndDate(newEndDate);
                            list.add(remainRule);
                            remainList.add(list);
                        }
                    }
                }
                //新规则的结束时间在老规则的起止时间之间，新规则的起始时间在老规则的起止时间之外
                else if (newEnd >= oldStart && newEnd <= oldEnd && newStart < oldStart){
                    //没有相同的星期或者相同的权益类型
                    if (isNoWeekCommon(settleRule,settle) || !isGiftCommon(settleRule,settle)){
                        //新规则插入
                        ShopItemSettlePriceRule priceRule = new ShopItemSettlePriceRule();
                        BeanUtils.copyProperties(settle,priceRule);
                        priceRule.setStartDate(oldStartDate);
                        priceRule.setEndDate(newEndDate);
                        priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                        priceRule = insertSettleRule(priceRule);
                        if (priceRule != null){
                            result.add(priceRule);
                        }
                        //剩下的区间塞进列表中
                        List<ShopItemSettlePriceRule> list = Lists.newLinkedList();
                        calendar.setTime(oldStartDate);
                        calendar.add(Calendar.DAY_OF_MONTH,-1);
                        ShopItemSettlePriceRule remainRule = new ShopItemSettlePriceRule();
                        BeanUtils.copyProperties(settle,remainRule);
                        remainRule.setStartDate(newStartDate);
                        remainRule.setEndDate(calendar.getTime());
                        list.add(remainRule);
                        remainList.add(list);
                    }else {
                        //老规则所有的星期都在新规则中存在
                        if (isBothWeekCommon(settleRule,settle)){
                            if (oldEnd == newEnd){
                                settleRule.setDelFlag(DelFlagEnums.DELETE.getCode());
                            }else {
                                //老规则开始时间更新为新规则结束时间+1
                                calendar.setTime(newEndDate);
                                calendar.add(Calendar.DAY_OF_MONTH,1);
                                settleRule.setStartDate(calendar.getTime());
                            }
                            shopItemSettlePriceRuleMapper.updateById(settleRule);
                            //新规则插入
                            ShopItemSettlePriceRule priceRule = new ShopItemSettlePriceRule();
                            BeanUtils.copyProperties(settle,priceRule);
                            priceRule.setStartDate(oldStartDate);
                            priceRule.setEndDate(newEndDate);
                            priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            priceRule = insertSettleRule(priceRule);
                            if (priceRule != null){
                                result.add(priceRule);
                            }
                            //剩下的区间塞进列表中
                            List<ShopItemSettlePriceRule> list = Lists.newLinkedList();
                            calendar.setTime(oldStartDate);
                            calendar.add(Calendar.DAY_OF_MONTH,-1);
                            ShopItemSettlePriceRule remainRule = new ShopItemSettlePriceRule();
                            BeanUtils.copyProperties(settle,remainRule);
                            remainRule.setStartDate(newStartDate);
                            remainRule.setEndDate(calendar.getTime());
                            list.add(remainRule);
                            remainList.add(list);
                        }else {//老规则部分星期在新规则中存在
                            List<Integer> weeks = findCommonWeek(settleRule,settle);
                            if (oldEnd == newEnd){
                                settleRule.setDelFlag(DelFlagEnums.DELETE.getCode());
                            }else {
                                //老规则开始时间更新为新规则结束时间+1
                                calendar.setTime(newEndDate);
                                calendar.add(Calendar.DAY_OF_MONTH,1);
                                settleRule.setStartDate(calendar.getTime());
                            }
                            shopItemSettlePriceRuleMapper.updateById(settleRule);
                            //老规则存在而新规则不存在的星期规则插入
                            ShopItemSettlePriceRule oldPrice = new ShopItemSettlePriceRule();
                            BeanUtils.copyProperties(settleRule,oldPrice);
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
                            oldPrice = insertSettleRule(oldPrice);
                            if (oldPrice != null){
                                result.add(oldPrice);
                            }
                            //新规则插入
                            ShopItemSettlePriceRule priceRule = new ShopItemSettlePriceRule();
                            BeanUtils.copyProperties(settle,priceRule);
                            priceRule.setStartDate(oldStartDate);
                            priceRule.setEndDate(newEndDate);
                            priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            priceRule = insertSettleRule(priceRule);
                            if (priceRule != null){
                                result.add(priceRule);
                            }
                            //剩下的区间塞进列表中
                            List<ShopItemSettlePriceRule> list = Lists.newLinkedList();
                            calendar.setTime(oldStartDate);
                            calendar.add(Calendar.DAY_OF_MONTH,-1);
                            ShopItemSettlePriceRule remainRule = new ShopItemSettlePriceRule();
                            BeanUtils.copyProperties(settle,remainRule);
                            remainRule.setStartDate(newStartDate);
                            remainRule.setEndDate(calendar.getTime());
                            list.add(remainRule);
                            remainList.add(list);
                        }
                    }
                }
                //新规则起止时间都在老规则起止时间之间
                else if (newStart >= oldStart && newEnd <= oldEnd){
                    //没有相同的星期或者相同的权益类型
                    if (isNoWeekCommon(settleRule,settle) || !isGiftCommon(settleRule,settle)){
                        //新规则插入
                        ShopItemSettlePriceRule priceRule = new ShopItemSettlePriceRule();
                        BeanUtils.copyProperties(settle,priceRule);
                        priceRule.setStartDate(newStartDate);
                        priceRule.setEndDate(newEndDate);
                        priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                        priceRule = insertSettleRule(priceRule);
                        if (priceRule != null){
                            result.add(priceRule);
                        }
                        flag = true;
                    }else {
                        //老规则所有的星期都在新规则中存在
                        if (isBothWeekCommon(settleRule,settle)){
                            if (newStart == oldStart && newEnd < oldEnd){
                                //老规则开始时间更新为新规则结束时间+1
                                calendar.setTime(newEndDate);
                                calendar.add(Calendar.DAY_OF_MONTH,1);
                                settleRule.setStartDate(calendar.getTime());
                                shopItemSettlePriceRuleMapper.updateById(settleRule);
                            }else if (newStart > oldStart && newEnd == oldEnd){
                                //老规则结束时间更新为新规则开始时间-1
                                calendar.setTime(newStartDate);
                                calendar.add(Calendar.DAY_OF_MONTH,-1);
                                settleRule.setEndDate(calendar.getTime());
                                shopItemSettlePriceRuleMapper.updateById(settleRule);
                            }else if (newStart == oldStart && newEnd == oldEnd){
                                settleRule.setDelFlag(DelFlagEnums.DELETE.getCode());
                                shopItemSettlePriceRuleMapper.updateById(settleRule);
                            }else if (newStart > oldStart && newEnd < oldEnd){
                                ShopItemSettlePriceRule leftPrice = new ShopItemSettlePriceRule();
                                ShopItemSettlePriceRule rightPrice = new ShopItemSettlePriceRule();
                                BeanUtils.copyProperties(settleRule,leftPrice);
                                BeanUtils.copyProperties(settleRule,rightPrice);
                                //左边规则插入
                                calendar.setTime(newStartDate);
                                calendar.add(Calendar.DAY_OF_MONTH,-1);
                                leftPrice.setId(null);
                                leftPrice.setStartDate(oldStartDate);
                                leftPrice.setEndDate(calendar.getTime());
                                //右边规则插入
                                calendar.setTime(newEndDate);
                                calendar.add(Calendar.DAY_OF_MONTH,1);
                                rightPrice.setId(null);
                                rightPrice.setStartDate(calendar.getTime());
                                rightPrice.setEndDate(oldEndDate);
                                //老价格删除
                                settleRule.setDelFlag(DelFlagEnums.DELETE.getCode());
                                leftPrice = insertSettleRule(leftPrice);
                                rightPrice = insertSettleRule(rightPrice);
                                if (leftPrice != null){
                                    result.add(leftPrice);
                                }
                                if (rightPrice != null){
                                    result.add(rightPrice);
                                }
                                shopItemSettlePriceRuleMapper.updateById(settleRule);
                            }
                            //新规则插入
                            ShopItemSettlePriceRule priceRule = new ShopItemSettlePriceRule();
                            BeanUtils.copyProperties(settle,priceRule);
                            priceRule.setStartDate(newStartDate);
                            priceRule.setEndDate(newEndDate);
                            priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            priceRule = insertSettleRule(priceRule);
                            if (priceRule != null){
                                result.add(priceRule);
                            }
                            flag = true;
                        }else {//老规则部分星期在新规则中存在
                            List<Integer> weeks = findCommonWeek(settleRule,settle);
                            if (newStart == oldStart && newEnd < oldEnd){
                                //老规则开始时间更新为新规则结束时间+1
                                calendar.setTime(newEndDate);
                                calendar.add(Calendar.DAY_OF_MONTH,1);
                                settleRule.setStartDate(calendar.getTime());
                                shopItemSettlePriceRuleMapper.updateById(settleRule);
                            }else if (newStart > oldStart && newEnd == oldEnd){
                                //老规则结束时间更新为新规则开始时间-1
                                calendar.setTime(newStartDate);
                                calendar.add(Calendar.DAY_OF_MONTH,-1);
                                settleRule.setEndDate(calendar.getTime());
                                shopItemSettlePriceRuleMapper.updateById(settleRule);
                            }else if (newStart == oldStart && newEnd == oldEnd){
                                settleRule.setDelFlag(DelFlagEnums.DELETE.getCode());
                                shopItemSettlePriceRuleMapper.updateById(settleRule);
                            }else if (newStart > oldStart && newEnd < oldEnd){
                                ShopItemSettlePriceRule leftPrice = new ShopItemSettlePriceRule();
                                ShopItemSettlePriceRule rightPrice = new ShopItemSettlePriceRule();
                                BeanUtils.copyProperties(settleRule,leftPrice);
                                BeanUtils.copyProperties(settleRule,rightPrice);
                                //左边规则插入
                                calendar.setTime(newStartDate);
                                calendar.add(Calendar.DAY_OF_MONTH,-1);
                                leftPrice.setId(null);
                                leftPrice.setStartDate(oldStartDate);
                                leftPrice.setEndDate(calendar.getTime());
                                //右边规则插入
                                calendar.setTime(newEndDate);
                                calendar.add(Calendar.DAY_OF_MONTH,1);
                                rightPrice.setId(null);
                                rightPrice.setStartDate(calendar.getTime());
                                rightPrice.setEndDate(oldEndDate);
                                //老规则删除
                                settleRule.setDelFlag(DelFlagEnums.DELETE.getCode());
                                leftPrice = insertSettleRule(leftPrice);
                                rightPrice = insertSettleRule(rightPrice);
                                if (leftPrice != null){
                                    result.add(leftPrice);
                                }
                                if (rightPrice != null){
                                    result.add(rightPrice);
                                }
                                shopItemSettlePriceRuleMapper.updateById(settleRule);
                            }
                            //老规则存在而新规则不存在的星期规则插入
                            ShopItemSettlePriceRule oldPrice = new ShopItemSettlePriceRule();
                            BeanUtils.copyProperties(settleRule,oldPrice);
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
                            oldPrice = insertSettleRule(oldPrice);
                            if (oldPrice != null){
                                result.add(oldPrice);
                            }
                            //新规则插入
                            ShopItemSettlePriceRule priceRule = new ShopItemSettlePriceRule();
                            BeanUtils.copyProperties(settle,priceRule);
                            priceRule.setStartDate(newStartDate);
                            priceRule.setEndDate(newEndDate);
                            priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            priceRule = insertSettleRule(priceRule);
                            if (priceRule != null){
                                result.add(priceRule);
                            }
                            flag = true;
                        }
                    }
                }
                //老规则开始时间大于新规则的起始时间，老规则的结束时间小于新规则的结束时间
                else if (newStart < oldStart && newEnd > oldEnd){
                    //没有相同的星期或者相同的权益类型
                    if (isNoWeekCommon(settleRule,settle) || !isGiftCommon(settleRule,settle)){
                        //新规则插入
                        ShopItemSettlePriceRule priceRule = new ShopItemSettlePriceRule();
                        BeanUtils.copyProperties(settle,priceRule);
                        priceRule.setStartDate(oldStartDate);
                        priceRule.setEndDate(oldEndDate);
                        priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                        priceRule = insertSettleRule(priceRule);
                        if (priceRule != null){
                            result.add(priceRule);
                        }
                        //左边规则
                        List<ShopItemSettlePriceRule> list = Lists.newLinkedList();
                        ShopItemSettlePriceRule leftPrice = new ShopItemSettlePriceRule();
                        BeanUtils.copyProperties(settle,leftPrice);
                        calendar.setTime(oldStartDate);
                        calendar.add(Calendar.DAY_OF_MONTH,-1);
                        leftPrice.setStartDate(newStartDate);
                        leftPrice.setEndDate(calendar.getTime());
                        list.add(leftPrice);
                        //右边规则
                        ShopItemSettlePriceRule rightPrice = new ShopItemSettlePriceRule();
                        BeanUtils.copyProperties(settle,rightPrice);
                        calendar.setTime(oldEndDate);
                        calendar.add(Calendar.DAY_OF_MONTH,1);
                        rightPrice.setStartDate(calendar.getTime());
                        rightPrice.setEndDate(newEndDate);
                        list.add(rightPrice);
                        remainList.add(list);
                    }else {
                        //老规则所有的星期都在新规则中存在
                        if (isBothWeekCommon(settleRule,settle)){
                            //老规则删除
                            settleRule.setDelFlag(DelFlagEnums.DELETE.getCode());
                            shopItemSettlePriceRuleMapper.updateById(settleRule);
                            //新规则插入
                            ShopItemSettlePriceRule priceRule = new ShopItemSettlePriceRule();
                            BeanUtils.copyProperties(settle,priceRule);
                            priceRule.setStartDate(oldStartDate);
                            priceRule.setEndDate(oldEndDate);
                            priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            priceRule = insertSettleRule(priceRule);
                            if (priceRule != null){
                                result.add(priceRule);
                            }
                            //左边规则
                            List<ShopItemSettlePriceRule> list = Lists.newLinkedList();
                            ShopItemSettlePriceRule leftPrice = new ShopItemSettlePriceRule();
                            BeanUtils.copyProperties(settle,leftPrice);
                            calendar.setTime(oldStartDate);
                            calendar.add(Calendar.DAY_OF_MONTH,-1);
                            leftPrice.setStartDate(newStartDate);
                            leftPrice.setEndDate(calendar.getTime());
                            list.add(leftPrice);
                            //右边规则
                            ShopItemSettlePriceRule rightPrice = new ShopItemSettlePriceRule();
                            BeanUtils.copyProperties(settle,rightPrice);
                            calendar.setTime(oldEndDate);
                            calendar.add(Calendar.DAY_OF_MONTH,1);
                            rightPrice.setStartDate(calendar.getTime());
                            rightPrice.setEndDate(newEndDate);
                            list.add(rightPrice);
                            remainList.add(list);
                        }else {//老规则部分星期在新规则中存在
                            List<Integer> weeks = findCommonWeek(settleRule,settle);
                            //老规则更新
                            if (weeks.contains(1)){
                                settleRule.setMonday(1);
                            }else {
                                settleRule.setMonday(0);
                            }
                            if (weeks.contains(2)){
                                settleRule.setTuesday(1);
                            }else {
                                settleRule.setTuesday(0);
                            }
                            if (weeks.contains(3)){
                                settleRule.setWednesday(1);
                            }else {
                                settleRule.setWednesday(0);
                            }
                            if (weeks.contains(4)){
                                settleRule.setThursday(1);
                            }else {
                                settleRule.setThursday(0);
                            }
                            if (weeks.contains(5)){
                                settleRule.setFriday(1);
                            }else {
                                settleRule.setFriday(0);
                            }
                            if (weeks.contains(6)){
                                settleRule.setSaturday(1);
                            }else {
                                settleRule.setSaturday(0);
                            }
                            if (weeks.contains(7)){
                                settleRule.setSunday(1);
                            }else {
                                settleRule.setSunday(0);
                            }
                            shopItemSettlePriceRuleMapper.updateById(settleRule);
                            //新规则插入
                            ShopItemSettlePriceRule priceRule = new ShopItemSettlePriceRule();
                            BeanUtils.copyProperties(settle,priceRule);
                            priceRule.setStartDate(oldStartDate);
                            priceRule.setEndDate(oldEndDate);
                            priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            priceRule = insertSettleRule(priceRule);
                            if (priceRule != null){
                                result.add(priceRule);
                            }
                            //左边规则
                            List<ShopItemSettlePriceRule> list = Lists.newLinkedList();
                            ShopItemSettlePriceRule leftPrice = new ShopItemSettlePriceRule();
                            BeanUtils.copyProperties(settle,leftPrice);
                            calendar.setTime(oldStartDate);
                            calendar.add(Calendar.DAY_OF_MONTH,-1);
                            leftPrice.setStartDate(newStartDate);
                            leftPrice.setEndDate(calendar.getTime());
                            list.add(leftPrice);
                            //右边规则
                            ShopItemSettlePriceRule rightPrice = new ShopItemSettlePriceRule();
                            BeanUtils.copyProperties(settle,rightPrice);
                            calendar.setTime(oldEndDate);
                            calendar.add(Calendar.DAY_OF_MONTH,1);
                            rightPrice.setStartDate(calendar.getTime());
                            rightPrice.setEndDate(newEndDate);
                            list.add(rightPrice);
                            remainList.add(list);
                        }
                    }
                }else {
                    List<ShopItemSettlePriceRule> list = Lists.newLinkedList();
                    ShopItemSettlePriceRule remainRule = new ShopItemSettlePriceRule();
                    BeanUtils.copyProperties(settle,remainRule);
                    remainRule.setStartDate(newStartDate);
                    remainRule.setEndDate(newEndDate);
                    list.add(remainRule);
                    remainList.add(list);
                }
                if (i == settleRules.size()){
                    if (flag){
                        remainList = null;
                    }
                    if (!CollectionUtils.isEmpty(remainList)){
                        List<ShopItemSettlePriceRule> intersectList = Lists.newLinkedList();
                        intersectList = remainList.get(0);
                        int j = 0;
                        for (List<ShopItemSettlePriceRule> shopItemSettlePriceRules : remainList) {
                            j++;
                            if (!CollectionUtils.isEmpty(intersectList)){
                                intersectList = unionSettleRuleDate(intersectList,shopItemSettlePriceRules);
                            }
                            if (j == remainList.size()){
                                if (!CollectionUtils.isEmpty(intersectList)){
                                    for (ShopItemSettlePriceRule shopItemSettlePriceRule : intersectList) {
                                        ShopItemSettlePriceRule temp = new ShopItemSettlePriceRule();
                                        shopItemSettlePriceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                                        temp = insertSettleRule(shopItemSettlePriceRule);
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
            //新规则插入
            ShopItemSettlePriceRule priceRule = new ShopItemSettlePriceRule();
            BeanUtils.copyProperties(settle,priceRule);
            priceRule.setStartDate(newStartDate);
            priceRule.setEndDate(newEndDate);
            priceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
            priceRule = insertSettleRule(priceRule);
            if (priceRule != null){
                result.add(priceRule);
            }
        }
        return result;
    }

    /**
     * 结算规则入库，重复入库判断
     * @param shopItemNetPriceRule
     * @return
     */
    private ShopItemSettlePriceRule insertSettleRule(ShopItemSettlePriceRule settleRule){
        Date startDate = DateUtil.parse(DateUtil.format(settleRule.getStartDate(),"yyyy-MM-dd"),"yyyy-MM-dd");
        Date endDate = DateUtil.parse(DateUtil.format(settleRule.getEndDate(),"yyyy-MM-dd"),"yyyy-MM-dd");
        settleRule.setStartDate(startDate);
        settleRule.setEndDate(endDate);
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("where del_flag = 0 and shop_id ="+settleRule.getShopId() +" and shop_item_id ="+settleRule.getShopItemId()+" and start_date = '"+settleRule.getStartDate()+
                        "' and end_date = '"+settleRule.getEndDate()+"' and monday ="+settleRule.getMonday()+" and tuesday ="+settleRule.getTuesday()+" and wednesday ="+settleRule.getWednesday()+
                        " and thursday ="+settleRule.getThursday()+" and friday ="+settleRule.getFriday()+" and saturday ="+settleRule.getSaturday()+" and sunday ="+settleRule.getSunday());
                if (StringUtils.isNotBlank(settleRule.getGift())){
                    stringBuffer.append(" and gift ='"+settleRule.getGift()+"'");
                }else {
                    stringBuffer.append(" and gift IS NULL");
                }
                if (settleRule.getSettleExpressId() != null){
                    stringBuffer.append(" and settle_express_id ="+settleRule.getSettleExpressId());
                }else {
                    stringBuffer.append(" and settle_express_id IS NULL");
                }
                return stringBuffer.toString();
            }
        };
        List<ShopItemSettlePriceRule> priceRules = shopItemSettlePriceRuleMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(priceRules)){
            shopItemSettlePriceRuleMapper.insert(settleRule);
            return settleRule;
        }
        return null;
    }

    /**
     * 得到2个列表中规则的交集(按相同的年月日区分)
     * @param unionList
     * @param remainList
     * @return
     */
    private List<ShopItemSettlePriceRule> unionSettleRuleDate(List<ShopItemSettlePriceRule> intersectList,List<ShopItemSettlePriceRule> remainList){
        List<ShopItemSettlePriceRule> result = Lists.newLinkedList();
        for (ShopItemSettlePriceRule intersect : intersectList) {
            Date intersectStartDate = intersect.getStartDate();
            Date intersectEndDate = intersect.getEndDate();
            long intersectStart = intersectStartDate.getTime();
            long intersectEnd = intersectEndDate.getTime();
            for (ShopItemSettlePriceRule remain : remainList) {
                Date remainStartDate = remain.getStartDate();
                Date remainEndDate = remain.getEndDate();
                long remainStart = remainStartDate.getTime();
                long remainEnd = remainEndDate.getTime();
                if (remainStart >= intersectStart && remainStart <= intersectEnd && remainEnd > intersectEnd){
                    ShopItemSettlePriceRule price = new ShopItemSettlePriceRule();
                    BeanUtils.copyProperties(intersect,price);
                    price.setStartDate(remainStartDate);
                    price.setEndDate(intersectEndDate);
                    result.add(price);
                }else if (remainEnd >= intersectStart && remainEnd <= intersectEnd && remainStart < intersectStart){
                    ShopItemSettlePriceRule price = new ShopItemSettlePriceRule();
                    BeanUtils.copyProperties(intersect,price);
                    price.setStartDate(intersectStartDate);
                    price.setEndDate(remainEndDate);
                    result.add(price);
                }else if (remainStart >= intersectStart && remainEnd <= intersectEnd){
                    ShopItemSettlePriceRule price = new ShopItemSettlePriceRule();
                    BeanUtils.copyProperties(intersect,price);
                    price.setStartDate(remainStartDate);
                    price.setEndDate(remainEndDate);
                    result.add(price);
                }else if (remainStart < intersectStart && remainEnd > intersectEnd){
                    ShopItemSettlePriceRule price = new ShopItemSettlePriceRule();
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
     * 判断两个结算规则的权益类型是否相同
     * @param rule1
     * @param rule2
     * @return
     */
    private boolean isGiftCommon(ShopItemSettlePriceRule rule1,ShopItemSettlePriceRule rule2){
        boolean flag = false;
        if (StringUtils.isBlank(rule1.getGift()) && StringUtils.isBlank(rule2.getGift())){
            flag = true;
        }else if (StringUtils.isNotBlank(rule1.getGift()) && StringUtils.isNotBlank(rule2.getGift())){
            if (rule1.getGift().equals(rule2.getGift())){
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 判断两个结算规则是否没有相同的星期
     * @param rule1
     * @param rule2
     * @return
     */
    private boolean isNoWeekCommon(ShopItemSettlePriceRule rule1,ShopItemSettlePriceRule rule2){
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
     * 判断第一个规则是否全部在第二个规则中存在相同的星期
     * @param rule1
     * @param rule2
     * @return
     */
    private boolean isBothWeekCommon(ShopItemSettlePriceRule rule1,ShopItemSettlePriceRule rule2){
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
     * 查找第一个规则存在而第二个规则不存在的星期列表
     * @param rule1
     * @param rule2
     * @return
     */
    private List<Integer> findCommonWeek(ShopItemSettlePriceRule rule1,ShopItemSettlePriceRule rule2){
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
}
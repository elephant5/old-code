package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemNetPriceRule;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopItemNetPriceRuleOptReq;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopItemNetPriceRuleQueryRes;

import java.util.Date;
import java.util.List;

public interface ShopItemNetPriceRuleService extends IService<ShopItemNetPriceRule> {

    /**
     * 规格价格添加、修改
     * @param shopItemNetPriceRuleReq
     * @return
     * @throws Exception
     */
    List<ShopItemNetPriceRule> add(ShopItemNetPriceRuleOptReq shopItemNetPriceRuleReq)throws Exception;

    /**
     * 规格价格修改
     * @param shopItemNetPriceRuleOptReq
     * @return
     * @throws Exception
     */
    List<ShopItemNetPriceRule> upd(ShopItemNetPriceRuleOptReq shopItemNetPriceRuleOptReq)throws Exception;

    /**
     * 翻译转换价格信息
     * @param priceRules
     * @return
     * @throws Exception
     */
    List<ShopItemNetPriceRuleQueryRes> translatePrice(List<ShopItemNetPriceRule> priceRules)throws Exception;

    /**
     * 预约时间对应的价格
     * @param bookDate
     * @param priceList
     * @return
     * @throws Exception
     */
    ShopItemNetPriceRule foundPriceByTime(Date bookDate, List<ShopItemNetPriceRule> priceList) throws Exception;
}
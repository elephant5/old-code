package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.Product;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemNetPriceRule;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemSettleExpress;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemSettlePriceRule;
import com.colourfulchina.pangu.taishang.api.vo.SettleExpressTranslateVo;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopItemSettleRuleOptReq;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopItemSettleRulesListRes;

import java.math.BigDecimal;
import java.util.List;

public interface ShopItemSettlePriceRuleService extends IService<ShopItemSettlePriceRule> {

    /**
     * 查询规格结算规格信息
     * @param shopItemSettlePriceRule
     * @return
     */
    List<ShopItemSettleRulesListRes> selectSettleRuleList(ShopItemSettlePriceRule shopItemSettlePriceRule) throws Exception;

    /**
     * 添加规格结算规则
     * @param shopItemSettleRuleOptReq
     * @return
     */
    List<ShopItemSettlePriceRule> add(ShopItemSettleRuleOptReq shopItemSettleRuleOptReq) throws Exception;

    /**
     * 结算规则公式中文翻译
     * @param settleExpressTranslateVo
     * @return
     */
    SettleExpressTranslateVo translateSettleExpress(SettleExpressTranslateVo settleExpressTranslateVo) throws Exception;

    /**
     * 删除结算规则
     * @param id
     * @return
     * @throws Exception
     */
    ShopItemSettlePriceRule delSettle(Integer id) throws Exception;

    /**
     * 根据结算公式和价格计算协议价
     * @param shopItemNetPriceRule
     * @param shopItemSettleExpress
     * @return
     * @throws Exception
     */
    BigDecimal calProtocolPrice(ShopItemNetPriceRule shopItemNetPriceRule, ShopItemSettleExpress shopItemSettleExpress)throws Exception;

    /**
     * 跟新结算价规则是否停用启用
     * @param params
     */
    void updateShopItemSettlePriceRule(Product params);
}
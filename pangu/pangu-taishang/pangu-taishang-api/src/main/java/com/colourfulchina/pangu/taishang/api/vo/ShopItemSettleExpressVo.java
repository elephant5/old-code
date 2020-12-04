package com.colourfulchina.pangu.taishang.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 老系统shopItem中的price字段中的结算规则解析实体
 */
@Data
public class ShopItemSettleExpressVo implements Serializable {
    private static final long serialVersionUID = -3086747882182737098L;

    /**
     * 星期一
     */
    private Integer monday;
    /**
     * 星期二
     */
    private Integer tuesday;
    /**
     * 星期三
     */
    private Integer wednesday;
    /**
     * 星期四
     */
    private Integer thursday;
    /**
     * 星期五
     */
    private Integer friday;
    /**
     * 星期六
     */
    private Integer saturday;
    /**
     * 星期日
     */
    private Integer sunday;
    /**
     * 净价百分比
     */
    private BigDecimal netPricePer;
    /**
     * 自定义税费净价百分比
     */
    private BigDecimal taxNetPricePer;
    /**
     * 服务费百分比
     */
    private BigDecimal serviceFeePer;
    /**
     * 自定义税费服务费百分比
     */
    private BigDecimal taxServiceFeePer;
    /**
     * 增值税百分比
     */
    private BigDecimal taxFeePer;
    /**
     * 自定义增值税百分比
     */
    private BigDecimal customTaxFeePer;
    /**
     * 固定贴现
     */
    private BigDecimal discount;
    /**
     * 调整金额
     */
    private BigDecimal adjust;
    /**
     * 结算公式
     */
    private String settleExpress;
    /**
     * 自定义增值税公式
     */
    private String customTaxExpress;

    @Override
    public String toString() {
        return "ShopItemSettleExpressVo{" +
                "monday=" + monday +
                ", tuesday=" + tuesday +
                ", wednesday=" + wednesday +
                ", thursday=" + thursday +
                ", friday=" + friday +
                ", saturday=" + saturday +
                ", sunday=" + sunday +
                ", netPricePer=" + netPricePer +
                ", taxNetPricePer=" + taxNetPricePer +
                ", serviceFeePer=" + serviceFeePer +
                ", taxServiceFeePer=" + taxServiceFeePer +
                ", taxFeePer=" + taxFeePer +
                ", customTaxFeePer=" + customTaxFeePer +
                ", discount=" + discount +
                ", adjust=" + adjust +
                ", settleExpress='" + settleExpress + '\'' +
                ", customTaxExpress='" + customTaxExpress + '\'' +
                '}';
    }
}

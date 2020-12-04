package com.colourfulchina.pangu.taishang.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ShopItemSettleExpress简略Vo
 */
@Data
public class SettleExpressBriefVo implements Serializable {
    private static final long serialVersionUID = 8727875341575572350L;

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
}

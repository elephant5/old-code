package com.colourfulchina.pangu.taishang.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 结算公式中的参数倍数对象
 */
@Data
public class SettleVo implements Serializable {
    private static final long serialVersionUID = -646822537853948925L;

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
     * 净价倍数
     */
    private BigDecimal netNum;
    /**
     * 服务费倍数
     */
    private BigDecimal rateNum;
    /**
     * 税费倍数
     */
    private BigDecimal taxNum;
    /**
     * 固定金额
     */
    private BigDecimal fixedAmount;
}

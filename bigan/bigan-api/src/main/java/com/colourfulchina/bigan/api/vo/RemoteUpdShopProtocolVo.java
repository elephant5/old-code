package com.colourfulchina.bigan.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class RemoteUpdShopProtocolVo implements Serializable {
    private static final long serialVersionUID = 946579845253130517L;
    /**
     * 商户id
     */
    private Integer shopId;
    /**
     * 渠道id
     */
    private Integer channelId;
    /**
     * 币种
     */
    private String currency;
    /**
     * 结算方式
     */
    private String settleMethod;
    /**
     * 结算精度
     */
    private Integer decimal;
    /**
     * 负责人
     */
    private String principal;
    /**
     * 预付金
     */
    private BigDecimal imprest;
    /**
     * 押金
     */
    private BigDecimal deposit;
    /**
     * 合同开始日期
     */
    private Date contractStart;
    /**
     * 合同结束日期
     */
    private Date contractExpiry;
    /**
     * block规则
     */
    private String blockRule;
    /**
     * 停车策略
     */
    private String parking;
    /**
     * 儿童政策
     */
    private String children;
    /**
     * 重要通知
     */
    private String notice;
}

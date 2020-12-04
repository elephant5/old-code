package com.colourfulchina.pangu.taishang.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 老系统shopItem中的price字段解析实体
 */
@Data
public class ShopItemPriceVo implements Serializable {
    private static final long serialVersionUID = -468514392069422307L;

    /**
     * 净价和星期对应关系列表
     */
    private List<Map<String,String>> netList;
    /**
     * 服务费
     */
    private BigDecimal serviceFee;
    /**
     * 税费
     */
    private BigDecimal taxation;
    /**
     * 二免一
     */
    private String gift2F1;
    /**
     * 三免一
     */
    private String gift3F1;
    /**
     * 五折
     */
    private String giftD5;
    /**
     * 单免
     */
    private String giftF1;
    /**
     * 双免
     */
    private String giftF2;

    @Override
    public String toString() {
        return "ShopItemPriceVo{" +
                "netList=" + netList +
                ", serviceFee=" + serviceFee +
                ", taxation=" + taxation +
                ", gift2F1='" + gift2F1 + '\'' +
                ", gift3F1='" + gift3F1 + '\'' +
                ", giftD5='" + giftD5 + '\'' +
                ", giftF1='" + giftF1 + '\'' +
                ", giftF2='" + giftF2 + '\'' +
                '}';
    }
}

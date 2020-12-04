package com.colourfulchina.pangu.taishang.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ShopItemSettleExpress操作的Vo
 */
@Data
public class SettleExpressVo implements Serializable {
    private static final long serialVersionUID = -2418256224722000614L;

    /**
     * 权益类型
     */
    private String gift;
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
     * 判断是不是除了gift其他字段都为空
     * @return
     */
    public Boolean isOnlyGift(){
        boolean flag = true;
        if (this.netPricePer != null){
            flag = false;
        }
        if (this.taxNetPricePer != null){
            flag = false;
        }
        if (this.serviceFeePer != null){
            flag = false;
        }
        if (this.taxServiceFeePer != null){
            flag = false;
        }
        if (this.taxFeePer != null){
            flag = false;
        }
        if (this.customTaxFeePer != null){
            flag = false;
        }
        if (this.discount != null){
            flag = false;
        }
        if (this.adjust != null){
            flag = false;
        }
        return flag;
    }
}

package com.colourfulchina.pangu.taishang.api.vo.res.bigan;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class GoodsPriceVo implements Serializable {
    private static final SimpleDateFormat YYYYMMDD_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private Date date;
    private String dateStr;
    private BigDecimal net;
    private String serviceFeeRateStr;
    private BigDecimal serviceFeeRate;
    private String incrTaxRateStr;
    private BigDecimal incrTaxRate;
    private BigDecimal discount;
    private String discountRateStr;
    private BigDecimal discountRate;
    private Integer weekDay;
    private BigDecimal giftF2;
    private BigDecimal gift2F1;
    private BigDecimal gift3F1;
    private BigDecimal giftB1F1;
    private BigDecimal giftD5;
    private BigDecimal giftF1;
    private BigDecimal giftN1;
    private BigDecimal giftN2;
    private BigDecimal giftN3;
    private BigDecimal giftN4;
    private BigDecimal giftNX;

    public BigDecimal getNet() {
        if (this.net == null){
            return new BigDecimal(0);
        }
        return net;
    }

    public BigDecimal getServiceFeeRate() {
        if (this.serviceFeeRate == null){
            return new BigDecimal(0);
        }
        return serviceFeeRate;
    }

    public String getServiceFeeRateStr() {
        if (this.serviceFeeRateStr == null &&  getServiceFeeRate() != null){
            serviceFeeRateStr=getServiceFeeRate().multiply(new BigDecimal(100)).doubleValue()+"%";
        }
        return serviceFeeRateStr;
    }

    public BigDecimal getIncrTaxRate() {
        if (this.incrTaxRate == null){
            return new BigDecimal(0);
        }
        return incrTaxRate;
    }

    public String getIncrTaxRateStr() {
        if (this.incrTaxRateStr == null && getIncrTaxRate() != null){
            incrTaxRateStr=getIncrTaxRate().multiply(new BigDecimal(100)).doubleValue()+"%";
        }
        return incrTaxRateStr;
    }

    public BigDecimal getDiscount() {
        if (this.discount == null){
            return new BigDecimal(0);
        }
        return discount;
    }

    public BigDecimal getDiscountRate() {
        if (this.discountRate == null){
            return new BigDecimal(0);
        }
        return discountRate;
    }

    public String getDateStr() {
        if (this.getDate() != null){
            return YYYYMMDD_FORMAT.format(this.getDate());
        }
        return dateStr;
    }
}

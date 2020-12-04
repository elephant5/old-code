package com.colourfulchina.pangu.taishang.api.vo.res;

import com.colourfulchina.pangu.taishang.api.entity.GoodsPrice;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsPriceRes extends GoodsPrice {
    private BigDecimal discount;//折扣价
    private BigDecimal price;//零售价

    public BigDecimal getDiscount() {
        if (this.getPrice() != null && this.getDisRate() != null){
            return this.getPrice().multiply(this.getDisRate());
        }
        return discount;
    }
}

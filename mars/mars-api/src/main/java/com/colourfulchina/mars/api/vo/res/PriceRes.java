package com.colourfulchina.mars.api.vo.res;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/7/22 16:04
 */
@Data
public class PriceRes {
    /*
    * 服务费
    * */
    private BigDecimal serviceRate;
    /*
    * 税费
    * */
    private BigDecimal taxRate;
    /*
    *
    * 净价
    * */
    private BigDecimal netPrice;
    /*
    * 应付
    * */
    private BigDecimal payPrice;
}

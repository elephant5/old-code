package com.colourfulchina.pangu.taishang.api.vo.res.bigan;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class AlipayBookPriceVo implements Serializable {

    private Map<Integer, List<GoodsPriceVo>> priceMap;
    private String bookStartTime;
    private String bookEndTime;
    private String nowDate;
    private BigDecimal refPrice;
}

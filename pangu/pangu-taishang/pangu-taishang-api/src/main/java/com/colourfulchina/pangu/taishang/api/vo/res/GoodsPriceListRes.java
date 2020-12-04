package com.colourfulchina.pangu.taishang.api.vo.res;

import com.colourfulchina.pangu.taishang.api.entity.GoodsPrice;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class GoodsPriceListRes implements Serializable {
    List<GoodsPriceRes> goodsPriceResList;
    private Integer goodsId;
    private BigDecimal price;//零售价
}

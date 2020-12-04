package com.colourfulchina.pangu.taishang.api.vo.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShopItemSettlePriceReq implements Serializable {
    private static final long serialVersionUID = 5013412339964914480L;

    private Integer shopId;
    private Integer shopItemId;
    private Integer year;
    private Integer month;
}

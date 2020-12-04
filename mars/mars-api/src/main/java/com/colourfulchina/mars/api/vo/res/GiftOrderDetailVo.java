package com.colourfulchina.mars.api.vo.res;

import com.colourfulchina.mars.api.entity.GiftOrderCapital;
import lombok.Data;

@Data
public class GiftOrderDetailVo extends GiftOrderCapital {

    private Integer goodsId;
    private String itemOrderId;

}

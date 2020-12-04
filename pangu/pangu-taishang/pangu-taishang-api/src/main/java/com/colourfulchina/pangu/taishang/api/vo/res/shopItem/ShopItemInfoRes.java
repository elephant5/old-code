package com.colourfulchina.pangu.taishang.api.vo.res.shopItem;

import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShopItemInfoRes {

    private String name ;

    private String needs;

    private String addon;

    private int monday;

    private int tuesday;

    private int wednesday;

    private int thursday;

    private int friday;

    private int saturday;

    private int sunday;

    private BigDecimal netPrice;

    private BigDecimal serviceRate;

    private BigDecimal taxRate;

}

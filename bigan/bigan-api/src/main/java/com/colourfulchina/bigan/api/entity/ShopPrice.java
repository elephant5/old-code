package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("shop_price")
public class ShopPrice extends Model<ShopPrice> {
    private static final long serialVersionUID = -8707184254407711749L;
    @TableId(value = "id")
    private Integer id;
    @TableId(value = "shop_id")
    private Integer shopId;
    @TableId(value = "item_id")
    private Integer itemId;
    @TableId(value = "price_rule")
    private String priceRule;
    @TableId(value = "date")
    private Date date;
    @TableId(value = "uid")
    private Integer uid;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
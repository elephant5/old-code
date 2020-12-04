package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("shop_item")
public class ShopItem  extends Model<ShopItem> {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type=IdType.INPUT)
    private Long id;
    @TableField("type")
    private String type;
    @TableField("shop_id")
    private Long shopId;
    @TableField("name")
    private String name;
    @TableField("needs")
    private String needs;
    @TableField("addon")
    private String addon;
    @TableField("opentime")
    private String opentime;
    @TableField("block")
    private String block;
    @TableField("price")
    private String price;
    @TableField("more")
    private String more;

    @TableField(exist = false)
    private String bookTime; //预约时段:适用单杯茶饮 下午茶 自助餐 定制套餐

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
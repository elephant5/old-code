package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("shop_account")
public class ShopAccount extends Model<ShopAccount> {
    private static final long serialVersionUID = 7609006799748080629L;

    @TableId(value = "shop_id",type = IdType.INPUT)
    private Long shopId;
    @TableField(value = "account")
    private String account;
    @TableField("pwd")
    private String pwd;

    @Override
    protected Serializable pkVal() {
        return this.shopId;
    }

    @Override
    public String toString() {
        return "ShopAccount{" +
                "shop_id=" + shopId +
                ", account='" + account + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}

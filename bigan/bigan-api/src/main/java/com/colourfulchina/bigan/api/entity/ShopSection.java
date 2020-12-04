package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
@Data
@TableName("shop_section")
public class ShopSection  extends Model<ShopSection> {
    private static final long serialVersionUID = -8707184254407711749L;
    @TableId(value = "shop_id",type = IdType.INPUT)
    private Long shopId;
    @TableField(value = "title")
    private String title;
    @TableField(value = "content")
    private String content;


    @Override
    protected Serializable pkVal() {
        return null;
    }
}
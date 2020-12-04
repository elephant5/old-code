package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("book_channel")
public class BookChannel extends Model<BookChannel> {

    @TableId(value = "id",type = IdType.INPUT)
    private Integer id;
    @TableField(value = "name")
    private String name;
    @TableField(value = "internal")
    private Integer internal;
    @TableField(value = "settle_method")
    private String settleMethod;
    @TableField(value = "currency")
    private String currency;
    @TableField(value = "phone")
    private String phone;
    @TableField(value = "consignee")
    private String consignee;
    @TableField(value = "notes")
    private String notes;
    @TableField(value = "create_time")
    private Date createTime;
    @TableField(value = "target")
    private String target;
    @TableField(value = "active")
    private Integer active;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

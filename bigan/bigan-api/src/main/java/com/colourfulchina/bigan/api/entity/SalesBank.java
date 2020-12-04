package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sales_bank")
public class SalesBank extends Model<SalesBank> {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;
    @TableField(value = "name")
    private String name;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SalesBank{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}

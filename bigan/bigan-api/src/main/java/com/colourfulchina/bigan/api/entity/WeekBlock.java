package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sys_block_week")
public class WeekBlock extends Model<WeekBlock> {
    private static final long serialVersionUID = 1L;

    @TableField("name")
    private String name;
    @TableField("code")
    private String code;


    @Override
    protected Serializable pkVal() {
        return null;
    }
}
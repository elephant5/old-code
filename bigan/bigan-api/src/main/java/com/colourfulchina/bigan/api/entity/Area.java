package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sys_country_area")
public class Area extends Model<Area> {
    private static final long serialVersionUID = 1L;

    @TableId(value = "code",type = IdType.INPUT)
    private String code;
    @TableField(value = "name")
    private String name;
    @TableField("search")
    private String search;
    @TableField("sort")
    private Integer sort;

    @Override
    protected Serializable pkVal() {
        return this.code;
    }

}

package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 区域
 */
@Data
@TableName("sys_country_area")
public class SysCountryArea extends Model<SysCountryArea> {

    private static final long serialVersionUID = 6610791429587857988L;

    @TableId(value = "code",type = IdType.INPUT)
    private String code;
    @TableField("name")
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

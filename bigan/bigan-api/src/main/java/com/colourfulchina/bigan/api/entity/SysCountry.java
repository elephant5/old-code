package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 国家
 */
@Data
@TableName("sys_country")
public class SysCountry extends Model<SysCountry> {

    private static final long serialVersionUID = -5217601001885427549L;

    @TableId(value = "code",type = IdType.INPUT)
    private String code;
    @TableField("name_cn")
    private String nameCn;
    @TableField("name_en")
    private String nameEn;
    @TableField("name_py")
    private String namePy;
    @TableField("flag")
    private String flag;
    @TableField("alias")
    private String alias;
    @TableField("area")
    private String area;

    @Override
    protected Serializable pkVal() {
        return this.code;
    }
}

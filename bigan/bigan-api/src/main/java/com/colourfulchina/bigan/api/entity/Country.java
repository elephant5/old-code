package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sys_country")
public class Country extends Model<Country> {
    private static final long serialVersionUID = 1L;

    @TableId(value = "code",type = IdType.INPUT)
    private String code;
    @TableField(value = "name_cn")
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

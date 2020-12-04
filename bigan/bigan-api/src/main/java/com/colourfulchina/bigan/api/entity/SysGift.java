package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sys_gift")
public class SysGift extends Model<SysGift> {
    private static final long serialVersionUID = 1466979961941878276L;

    @TableId(value = "code",type = IdType.INPUT)
    private String code;
    @TableField("name")
    private String name;
    @TableField("short")
    private String shortName;
    @TableField("target")
    private String target;
    @TableField("sort")
    private Integer sort;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}

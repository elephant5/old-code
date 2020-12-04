package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sys_service")
public class SysService extends Model<SysService> {
    private static final long serialVersionUID = -8970617004597631543L;

    @TableField("define")
    private String define;
    @TableField("stamp")
    private String stamp;
    @TableField("sort")
    private Integer sort;
    @TableField("classic")
    private Integer classic;
    @TableField("name")
    private String name;
    @TableField("code")
    private String code;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}

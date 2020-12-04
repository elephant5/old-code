package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sys_option")
public class SysOption extends Model<SysOption> {
    @TableId(value = "name",type = IdType.INPUT)
    private String name;
    private String value;
    private String notes;

    @Override
    protected Serializable pkVal() {
        return this.name;
    }
}

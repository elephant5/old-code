package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sys_file_quote")
public class SysFileQuote extends Model<SysFileQuote> {
    @TableField(value = "type")
    private String type;
    @TableField(value = "obj_id")
    private Integer objId;
    @TableField(value = "file_id")
    private Integer fileId;
    @TableField(value = "sort")
    private Integer sort;

    @Override
    protected Serializable pkVal() {
        return this.fileId;
    }
}

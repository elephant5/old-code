package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
@TableName("sys_file")
public class SysFile extends Model<SysFile> {
    private static final long serialVersionUID = -2769001845655720927L;
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "guid")
    private String guid;
    @TableField(value = "path")
    private String path;
    @TableField(value = "code")
    private String code;
    @TableField(value = "ext")
    private String ext;
    @TableField(value = "name")
    private String name;
    @TableField(value = "size")
    private Long size;
    @TableField(value = "creator_id")
    private Integer creatorId;
    @TableField(value = "date")
    private Date date;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

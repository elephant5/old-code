package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统全局block
 */
@Data
@TableName("sys_block")
public class SysBlock extends Model<SysBlock> {
    private static final long serialVersionUID = 5814436903921709274L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "code")
    private String code;
    @TableField(value = "target")
    private String target;
    @TableField("notes")
    private String notes;
    @TableField("create_time")
    private Date createTime;
    @TableField("creator_id")
    private Integer creatorId;
    @TableField("more")
    private String more;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("prj_group")
public class PrjGroup extends Model<PrjGroup> {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;
    @TableField(value = "project_id")
    private Long projectId;
    @TableField(value = "dynamic")
    private Long dynamic;
    @TableField(value = "filter")
    private String filter;
    @TableField(value = "limit_type")
    private String limitType;
    @TableField(value = "title")
    private String title;
    @TableField(value = "times")
    private Long times;
    @TableField(value = "define")
    private String define;
    @TableField(value = "more")
    private String more;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

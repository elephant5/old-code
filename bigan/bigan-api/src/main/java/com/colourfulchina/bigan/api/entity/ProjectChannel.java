package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("project_channel")
public class ProjectChannel implements Serializable {
    private static final long serialVersionUID = 2958887527612149066L;
    @TableField(value = "project_id")
    private Integer ProjectId;
    @TableField(value = "old_id")
    private Integer oldId;
    @TableField(value = "new_id")
    private Integer newId;
    @TableField(value = "note")
    private String note;
}

package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

@Data
public class ProjectMeta extends Model<ProjectMeta> {
    @TableId(value = "project_id",type = IdType.INPUT)
    private Integer projectId;
    @TableId(value = "name",type = IdType.INPUT)
    private String name;
    private String type;
    private String value;
    @Override
    protected Serializable pkVal() {
        return this.projectId;
    }
}

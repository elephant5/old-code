package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
@TableName("prj_package")
public class PrjPackage extends Model<PrjPackage> {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id")
    private Integer id;
    @TableField("project_id")
    private Integer projectId;
    @TableField("name")
    private String name;
    @TableField("summary")
    private String summary;
    @TableField("create_time")
    private Date createTime;
    @TableField("more")
    private String more;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
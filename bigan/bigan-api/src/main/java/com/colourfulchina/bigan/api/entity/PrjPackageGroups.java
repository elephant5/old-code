package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("prj_package_groups")
public class PrjPackageGroups extends Model<PrjPackageGroups> {
    private static final long serialVersionUID = 1L;
    @TableField("package_id")
    private Integer packageId;
    @TableField("group_id")
    private Integer groupId;

    @Override
    protected Serializable pkVal() {
        return this.packageId;
    }
}
package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("project_goods")
public class ProjectGoods extends Model<ProjectGoods> {
    private static final long serialVersionUID = 1L;

    @TableField(value = "goods_id")
    private Long goodsId;
    @TableField(value = "project_id")
    private Long projectId;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}

package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("prj_group_goods")
public class PrjGroupGoods extends Model<PrjGroupGoods> {
    private static final long serialVersionUID = 1L;

    @TableField(value = "group_id")
    private Long groupId;
    @TableField(value = "goods_id")
    private Long goodsId;
    @TableField(value = "weight")
    private BigDecimal weight;
    @TableField(value = "project_id")
    private Long projectId;
    @TableField(value = "online")
    private Integer online;

    @TableField(value = "sort")
    private Integer sort; //prj_group_goods表新增的排序值字段

    @Override
    protected Serializable pkVal() {
        return null;
    }
}

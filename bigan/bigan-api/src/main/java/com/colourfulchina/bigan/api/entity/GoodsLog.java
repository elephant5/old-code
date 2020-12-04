package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("goods_log")
public class GoodsLog  extends Model<GoodsLog> {
    private static final long serialVersionUID = 1L;
    @TableId(value = "logId")
    private Integer logId;
    @TableField("action")
    private String action;
    @TableField("goods_id")
    private Integer goodsId;
    @TableField("project_id")
    private Integer projectId;
    @TableField("who")
    private Integer who;
    @TableField("date")
    private Date date;
    @TableField("wid")
    private Integer wid;

    @Override
    protected Serializable pkVal() {
        return this.logId;
    }

}
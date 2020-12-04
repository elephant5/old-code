package com.colourfulchina.pangu.taishang.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品使用条款
 */
@Data
@TableName(value = "goods_clause")
public class GoodsClause extends Model<GoodsClause> {

    @TableId(value = "id" ,type = IdType.AUTO)
    private Integer id ;// 商品ID
    @TableField(value = "goods_id")
    private Integer goodsId ;// 商品ID
    private String clauseType;
    private String clause;
    @TableField(value = "del_flag")
    private Integer delFlag;//'删除标识（0-正常，1-删除）',
    @TableField(value = "create_time")
    private Date createTime ;//'创建时间',
    @TableField(value = "create_user")
    private String createUser;// '创建人',
    @TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
    private Date updateTime;// '更新时间',
    @TableField(value = "update_user")
    private String updateUser;// '更新人',

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

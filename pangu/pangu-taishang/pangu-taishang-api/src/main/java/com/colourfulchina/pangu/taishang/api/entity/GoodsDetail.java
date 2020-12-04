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
 * 商品详情
 */
@Data
@TableName(value = "goods_detail")
public class GoodsDetail extends Model<GoodsDetail> {

    private static final long serialVersionUID = 4039871917773859200L;
    @TableId(value = "goods_id",type = IdType.INPUT)
    private Integer goodsId ;// 商品ID
    private String detail;
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
        return this.goodsId;
    }
}

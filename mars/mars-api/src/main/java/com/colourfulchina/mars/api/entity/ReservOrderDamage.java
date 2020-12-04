package com.colourfulchina.mars.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/5/15 10:25
 */
@TableName("reserv_order_damage")
@Data
public class ReservOrderDamage extends Model<ReservOrderDamage> {
    @TableId(value = "order_id",type = IdType.AUTO)
    private Integer orderId;
    @TableField(value = "amount")
    private String amount;
    @TableField(value = "notes")
    private String notes;
    @TableField(value = "del_flag")
    private Integer delFlag;
    @TableField(value = "create_time")
    private Date createTime;
    @TableField(value = "create_user")
    private String createUser;
    @TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
    private Date updateTime;
    @TableField(value = "update_user")
    private String updateUser;

    @Override
    protected Serializable pkVal() {
        return this.orderId;
    }
}

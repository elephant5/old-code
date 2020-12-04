package com.colourfulchina.mars.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@TableName("reserv_tag")
@Data
public class ReservTag extends Model<ReservTag> {
    @TableId(value = "order_id")
    private Integer orderId;
    @TableField(value = "tag_id")
    private Integer tagId;
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

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
 * 预约单日志
 *
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/7/31 16:52
 */
@Data
@TableName(value = "reserv_order_log")
public class ReservOrderLog extends Model<ReservOrderLog> {
    @TableId(value = "id" ,type = IdType.AUTO)
    private Integer id;
    @TableField(value = "order_id")
    private Integer orderId;
    @TableField(value = "old_prose_status")
    private String oldProseStatus;
    @TableField(value = "now_prose_status")
    private String nowProseStatus;
    @TableField(value = "oper_type")
    private String operType;
    @TableField(value = "content")
    private String content;
    @TableField(value = "create_time")
    private Date createTime;
    @TableField(value = "create_user")
    private String createUser;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

package com.colourfulchina.mars.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/5/15 10:21
 */
@TableName("reserv_price")
@Data
public class ReservPrice extends Model<ReservPrice> {
    @TableId(value = "order_id",type = IdType.AUTO)
    private Integer orderId;
    @TableField(value = "price")
    private BigDecimal price;
    @TableField(value = "contract")
    private String contract;
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

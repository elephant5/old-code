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
 * @Date: 2019/5/15 10:13
 */
@TableName("reserv_settle")
@Data
public class ReservSettle extends Model<ReservSettle> {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "order_id")
    private Integer orderId;
    @TableField(value = "product_type")
    private String productType;
    @TableField(value = "product_id")
    private Integer productId;
    @TableField(value = "net_price")
    private BigDecimal netPrice;
    @TableField(value = "fee_price")
    private String fee_price;
    @TableField(value = "add_rate")
    private String add_rate;
    @TableField(value = "rule")
    private String rule;
    @TableField(value = "agree_price")
    private String agreePrice;
    @TableField(value = "real_price")
    private String realPrice;
    @TableField(value = "currency")
    private String currency;
    @TableField(value = "reserv_date")
    private Date reservDate;
    @TableField(value = "number")
    private String number;
    @TableField(value = "payMethod")
    private String payMethod;
    @TableField(value = "del_flag")
    private Integer delFlag;
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time")
    private Date createTime;
    @ApiModelProperty("创建人")
    @TableField(value = "create_user")
    private String createUser;
    @ApiModelProperty("更新时间")
    @TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
    private Date updateTime;
    @ApiModelProperty("更新人")
    @TableField(value = "update_user")
    private String updateUser;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

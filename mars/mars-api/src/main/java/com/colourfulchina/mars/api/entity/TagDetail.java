package com.colourfulchina.mars.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Nickal
 * @Description:
 * @Date: 2019/5/15 10:37
 */
@Data
@TableName (value = "tag_detail")
public class TagDetail extends Model<TagDetail>
{
    @TableId(value = "id",type = IdType.INPUT)
    private Integer id;
    @TableId(value = "order_id")
    private Integer orderId;
    @TableField(value = "sales_id")
    private Integer salesId;
    @TableField(value = "order_code")
    private String orderCode;
    @TableField(value = "callin")
    private String callin;
    @TableField(value = "adjust")
    private String adjust;
    @TableField(value = "pay")
    private String pay;
    @TableField(value = "discount")
    private String discount;
    @TableField(value = "back")
    private String back;
    @TableField(value = "test")
    private String test;
    @TableField(value = "vip")
    private Integer vip;
    @TableField(value = "post")
    private Integer post;
    @TableField(value = "refill")
    private String refill;
    @TableField(value = "delay")
    private String delay;
    @TableField(value = "send")
    private String send;
    @TableField(value = "tag_ext_1")
    private String tag_ext_1;
    @TableField(value = "tag_ext_2")
    private String tag_ext_2;
    @TableField(value = "tag_ext_3")
    private String tag_ext_3;
    @TableField(value = "tag_ext_4")
    private String tag_ext_4;
    @TableField(value = "tag_ext_5")
    private Integer tag_ext_5;
    @TableField(value = "tag_ext_6")
    private Integer tag_ext_6;
    @TableField(value = "order_type")
    private String order_type;
    @ApiModelProperty("删除标识（0-正常，1-删除）")
    @TableField(value = "del_flag")
    private Integer delFlag;
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time")
    private java.util.Date createTime;
    @ApiModelProperty("创建人")
    @TableField(value = "create_user")
    private String createUser;
    @ApiModelProperty("更新时间")
    @TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
    private java.util.Date updateTime;
    @ApiModelProperty("更新人")
    @TableField(value = "update_user")
    private String updateUser;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

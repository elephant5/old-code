package com.colourfulchina.pangu.taishang.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Copyright Colourful
 * @Author SunnyWang
 * @Date 2019年9月6日10:19:10
 */
@Data
@TableName(value = "activity")
public class Activity extends Model<Activity> {
    private static final long serialVersionUID = -728116610306540573L;
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty("活动名称")
    @TableField(value = "act_name")
    private String actName;
    @ApiModelProperty("销售渠道")
    @TableField(value = "sales_channel")
    private String salesChannel;
    @ApiModelProperty("全部商品 0否；1是；默认0")
    @TableField(value = "goods_tag")
    private Integer goodsTag;
    @ApiModelProperty("发放方式 1 购买成功后 2 注册成功后 3其他")
    @TableField(value = "grant_mode")
    private Integer grantMode;
    @ApiModelProperty("活动开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "start_date")
    private Date startDate;
    @ApiModelProperty("活动结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "end_date")
    private Date endDate;
    @ApiModelProperty("永久有效：0否 1是 默认0")
    @TableField(value = "forever_tag")
    private Integer foreverTag;
    @ApiModelProperty("活动状态 0草稿 1待审核 2审核通过 3待上线 4上线 5审核未通过 6待下线 7下线")
    @TableField(value = "act_status")
    private Integer actStatus;
    @ApiModelProperty("创建人")
    @TableField(value = "create_user")
    private String createUser;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "create_time")
    private Date createTime;
    @ApiModelProperty("更新人")
    @TableField(value = "update_user")
    private String updateUser;
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "update_time")
    private Date updateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

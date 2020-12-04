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
@TableName(value = "activity_coupon")
public class ActivityCoupon extends Model<ActivityCoupon> {
    private static final long serialVersionUID = -8881360998035344343L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty("活动Id")
    @TableField(value = "act_id")
    private Integer actId;
    @ApiModelProperty("礼券类型：1抵用券 2折扣券")
    @TableField(value = "coupon_type")
    private Integer couponType;
    @ApiModelProperty("礼券批次")
    @TableField(value = "batch_id")
    private Long batchId;
    @ApiModelProperty("优惠券名称")
    @TableField(value = "batch_name")
    private String batchName;
    @ApiModelProperty("发放规则：-1 无限制 ，其他为固定次数")
    @TableField(value = "grant_limit")
    private Integer grantLimit;
    @ApiModelProperty("使用限制：-1 无限制 其他为固定次数")
    @TableField(value = "use_limit")
    private Integer useLimit;
    @ApiModelProperty("使用规则频率数量：几 年，几 季度，几 月，几 周，几 天")
    @TableField(value = "use_limit_rate_num")
    private Integer useLimitRateNum;
    @ApiModelProperty("使用规则频率单位：Y 年，S 季度，M 月，W 周，D 天")
    @TableField(value = "use_limit_rate")
    private String useLimitRate;
    @ApiModelProperty("状态：0 正常 1删除 默认0")
    @TableField(value = "del_flag")
    private Integer delFlag;
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
